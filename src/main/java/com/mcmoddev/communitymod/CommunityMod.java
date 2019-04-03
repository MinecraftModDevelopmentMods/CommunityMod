package com.mcmoddev.communitymod;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(modid = CommunityGlobals.MOD_ID, name = "Community Mod", version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class CommunityMod {
    
    public static final Logger LOGGER = LogManager.getLogger("Community Mod");
    private final Map<String, SubModContainer> subMods = new HashMap<>();

    private Map<IForgeRegistryEntry, SubModContainer> regEntryOrigin = new HashMap<>();
    private SubModContainer activeSubMod;
    
    @Mod.Instance(CommunityGlobals.MOD_ID)
    public static CommunityMod INSTANCE;
    
    public List<SubModContainer> getSubMods() {
    	ArrayList<SubModContainer> list = new ArrayList<SubModContainer>();
    	list.addAll(subMods.values());
    	return Collections.unmodifiableList(list);
    }

    public SubModContainer getActiveSubMod()
    {
        return activeSubMod;
    }

    public SubModContainer getSubModOrigin(IForgeRegistryEntry entry)
    {
        return regEntryOrigin.get(entry);
    }
    
    public boolean isSubModLoaded(String submodid) 
    {
    	return subMods.containsKey(submodid);
    }
    
    public boolean isSubModLoaded(SubModContainer container) 
    {
    	return subMods.containsValue(container);
    }

    @EventHandler
    public void onConstruction (FMLConstructionEvent event) throws Exception {
        
    	CommunityConfig config = new CommunityConfig(new File("config/" + CommunityGlobals.MOD_ID + ".cfg"));
        Set<SubModContainer> eventSubscriberWarns = new HashSet<>();

    	final long startTime = System.currentTimeMillis();
        final Set<ASMData> datas = event.getASMHarvestedData().getAll(SubMod.class.getCanonicalName());

        for (final ASMData data : datas) {
            
            final String modName = (String) data.getAnnotationInfo().get("name");
            final String modid = (String) data.getAnnotationInfo().get("modid");
            
            if(!modid.toLowerCase().equals(modid)) {
            	LOGGER.fatal("Submod with modid {} has invalid uppercase characters! It will not be loaded!", modid);
            	throw new RuntimeException("Invalid modid!");
            }
            if(modid.contains(" ")) {
            	LOGGER.fatal("Submod with modid {} has a space! It will not be loaded!", modid);
            	throw new RuntimeException("Invalid modid!");
            }
            
            try {

                if (!data.getAnnotationInfo().containsKey("clientSideOnly") || (boolean) data.getAnnotationInfo().get("clientSideOnly") && event.getSide() == Side.CLIENT) {
                    
                    final ISubMod subMod = (ISubMod) Class.forName(data.getClassName()).newInstance();
                    final String description = (String) data.getAnnotationInfo().getOrDefault("description", "No description is provided for this submod.");
                    final String attribution = (String) data.getAnnotationInfo().getOrDefault("attribution", "No attribution is provided for this submod.");
                    final SubModContainer container = new SubModContainer(modName, description, attribution, subMod, modid);

                    if (subMod.getClass().isAnnotationPresent(Mod.EventBusSubscriber.class))
                    {
                        MinecraftForge.EVENT_BUS.unregister(subMod.getClass());
                        eventSubscriberWarns.add(container);
                    }

                	if (config.isSubModEnabled(container)) {
                		if(!this.subMods.containsKey(modid)) {
                			this.subMods.put(modid, container);
                		} else {
                			LOGGER.fatal("Duplicate submodid {} between {} ({}) and {} ({})", modid, subMod.getClass().getName(), modName, subMods.get(modid).getSubMod().getClass().getName(), subMods.get(modid).getName());
                			throw new RuntimeException("Duplicate submodid, see above.");
                		}
                        MinecraftForge.EVENT_BUS.register(subMod.getClass());

                        LOGGER.info("Loaded submod {} from class {}.", modName, data.getClassName());
                	}
                }
            }
            
            catch (final Exception e) {
                if(e.getMessage().equals("Duplicate submodid, see above.")) {
                	throw e;
                }
                LOGGER.error("Failed to load submod {}.", modName);
                LOGGER.catching(e);
                if(this.subMods.containsKey(modid)) {
                	this.subMods.remove(modid);
                }
            }
        }

        for (SubModContainer container : eventSubscriberWarns)
        {
            LOGGER.warn("{} from {} uses an @EventBusSubscriber annotation, which is normally done automagically. This is highly discouraged! - Proceeding anyway.", container.getSubMod().getClass(), container.getName());
        }
        
        LOGGER.info("Loaded {} submods in {}ms.", this.subMods.size(), System.currentTimeMillis() - startTime);
        boolean dependencyFail = false;
        
        for (final SubModContainer container : this.subMods.values()) {
            if(container.getSubMod().getClass().isAnnotationPresent(DependsOn.class)) {
            	DependsOn data = container.getSubMod().getClass().getAnnotation(DependsOn.class);
            	String[] dependencies = data.depend();
            	String modid = data.modid();
            	for(String dependency : dependencies) {
            		if(!this.isSubModLoaded(dependency)) {
            			LOGGER.fatal("Submod {} depends on {} which is not loaded! Either disable {} or enable {}", modid, dependency, modid, dependency);
            			dependencyFail = true;
            		}
            	}
            }
        	
        	if(!dependencyFail) {
        		container.getSubMod().onConstruction(event);
        	}
        }
        
        if(dependencyFail) {
        	throw new RuntimeException("One or more missing dependencies! See log above.");
        }
        
        config.syncConfigData();
    }
    
    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {
    
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        
        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().onPreInit(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onInit (FMLInitializationEvent event) {
        
        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().onInit(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onPostInit (FMLPostInitializationEvent event) {
        
        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().onPostInit(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onLoadComplete (FMLLoadCompleteEvent event) {
        
        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().onLoadComplete(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onServerStarting (FMLServerStartingEvent event) {
        
        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().onServerStarting(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onServerStopped (FMLServerStoppedEvent event) {
        
        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().onServerStopped(event);
        }

        activeSubMod = null;
    }
    
    @SubscribeEvent
    public void blocks (RegistryEvent.Register<Block> event) {
        
        IForgeRegistry<Block> reg = new SubModRegistryTracker(event.getRegistry());
    
        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().registerBlocks(reg);
        }

        activeSubMod = null;
    }

    @SubscribeEvent
    public void items (RegistryEvent.Register<Item> event) {

        IForgeRegistry<Item> reg = new SubModRegistryTracker(event.getRegistry());

        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().registerItems(reg);
        }

        activeSubMod = null;
    }

    @SubscribeEvent
    public void entities (RegistryEvent.Register<EntityEntry> event) {

        IForgeRegistry<EntityEntry> reg = new SubModRegistryTracker(event.getRegistry());

        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().registerEntities(reg);
        }

        activeSubMod = null;
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void models (ModelRegistryEvent event) {
        
        for (final SubModContainer container : this.subMods.values()) {

            activeSubMod = container;
            container.getSubMod().registerModels(event);
        }

        activeSubMod = null;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        if (!stack.isEmpty() && CommunityGlobals.MOD_ID.equals(stack.getItem().getRegistryName().getNamespace()))
        {
            SubModContainer subMod = getSubModOrigin(stack.getItem());

            if (subMod != null)
            {
                event.getToolTip().add(TextFormatting.DARK_GRAY + "(" + subMod.getName() + " - " + subMod.getAttribution() + ")");
            }
        }
    }

    private class SubModRegistryTracker<V extends IForgeRegistryEntry<V>> implements IForgeRegistry<V>
    {
        private final IForgeRegistry<V> fallbackRegistry;

        private SubModRegistryTracker(IForgeRegistry<V> reg)
        {
            fallbackRegistry = reg;
        }

        @Override
        public Class<V> getRegistrySuperType() { return fallbackRegistry.getRegistrySuperType(); }

        @Override
        public void register(V value)
        {
            track(value);
            fallbackRegistry.register(value);
        }

        @Override
        public void registerAll(V... values)
        {
            track(values);
            fallbackRegistry.registerAll(values);
        }

        private void track(IForgeRegistryEntry... values)
        {
            for (IForgeRegistryEntry e : values)
            {
                regEntryOrigin.put(e, getActiveSubMod());
            }
        }

        @Override
        public boolean containsKey(ResourceLocation key) { return fallbackRegistry.containsKey(key); }

        @Override
        public boolean containsValue(V value) { return fallbackRegistry.containsValue(value); }

        @Nullable
        @Override
        public V getValue(ResourceLocation key) { return fallbackRegistry.getValue(key); }

        @Nullable
        @Override
        public ResourceLocation getKey(V value) { return fallbackRegistry.getKey(value); }

        @Nonnull
        @Override
        public Set<ResourceLocation> getKeys() { return fallbackRegistry.getKeys(); }

        @Nonnull
        @Override
        public List<V> getValues() { return fallbackRegistry.getValues(); }

        @Nonnull
        @Override
        public Set<Map.Entry<ResourceLocation, V>> getEntries() { return fallbackRegistry.getEntries(); }

        @Override
        public <T1> T1 getSlaveMap(ResourceLocation slaveMapName, Class<T1> type) { return fallbackRegistry.getSlaveMap(slaveMapName, type); }

        @Override
        public Iterator<V> iterator() { return fallbackRegistry.iterator(); }
    }
}
