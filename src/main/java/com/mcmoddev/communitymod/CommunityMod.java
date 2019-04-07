package com.mcmoddev.communitymod;

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
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

@Mod(modid = CommunityGlobals.MOD_ID, name = "Community Mod", version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@", dependencies = "required:forge@[14.23.5.2824,);")
public class CommunityMod {
    
    public static final Logger LOGGER = LogManager.getLogger("Community Mod");
    private final List<SubModContainer> subMods = new ArrayList<>();

    private Map<IForgeRegistryEntry, SubModContainer> regEntryOrigin = new HashMap<>();
    private SubModContainer activeSubMod;
    
    @Mod.Instance(CommunityGlobals.MOD_ID)
    public static CommunityMod INSTANCE;
    
    public List<SubModContainer> getSubMods() {
    	
    	return Collections.unmodifiableList(subMods);
    }

    public SubModContainer getActiveSubMod()
    {
        return activeSubMod;
    }

    public SubModContainer getSubModOrigin(IForgeRegistryEntry entry)
    {
        return regEntryOrigin.get(entry);
    }

    @EventHandler
    public void onConstruction (FMLConstructionEvent event) {
        
    	CommunityConfig config = new CommunityConfig(new File("config/" + CommunityGlobals.MOD_ID + ".cfg"));
        Set<SubModContainer> eventSubscriberWarns = new HashSet<>();

    	final long startTime = System.currentTimeMillis();
        final Set<ASMData> datas = event.getASMHarvestedData().getAll(SubMod.class.getCanonicalName());

        for (final ASMData data : datas) {
            
            final String modName = (String) data.getAnnotationInfo().get("name");
            
            try {

                if (!data.getAnnotationInfo().containsKey("clientSideOnly") || (boolean) data.getAnnotationInfo().get("clientSideOnly") && event.getSide() == Side.CLIENT) {
                    
                    final ISubMod subMod = (ISubMod) Class.forName(data.getClassName()).newInstance();
                    final String description = (String) data.getAnnotationInfo().getOrDefault("description", "No description is provided for this submod.");
                    final String attribution = (String) data.getAnnotationInfo().getOrDefault("attribution", "No attribution is provided for this submod.");
                    final SubModContainer container = new SubModContainer(modName, description, attribution, subMod);

                    if (subMod.getClass().isAnnotationPresent(Mod.EventBusSubscriber.class))
                    {
                        MinecraftForge.EVENT_BUS.unregister(subMod.getClass());
                        eventSubscriberWarns.add(container);
                    }

                	if (config.isSubModEnabled(container)) {
                		
                        this.subMods.add(container);
                        MinecraftForge.EVENT_BUS.register(subMod.getClass());

                        LOGGER.info("Loaded submod {} from class {}.", modName, data.getClassName());
                	}
                }
            }
            
            catch (final Exception e) {
                
                LOGGER.error("Failed to load submod {}.", modName);
                LOGGER.catching(e);
            }
        }

        for (SubModContainer container : eventSubscriberWarns)
        {
            LOGGER.warn("{} from {} uses an @EventBusSubscriber annotation, which is normally done automagically. This is highly discouraged! - Proceeding anyway.", container.getSubMod().getClass(), container.getName());
        }
        
        LOGGER.info("Loaded {} submods in {}ms.", this.subMods.size(), System.currentTimeMillis() - startTime);
        for (final SubModContainer container : this.subMods) {
            
            container.getSubMod().onConstruction(event);
        }
        
        config.syncConfigData();
    }
    
    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {
    
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        
        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().onPreInit(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onInit (FMLInitializationEvent event) {
        
        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().onInit(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onPostInit (FMLPostInitializationEvent event) {
        
        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().onPostInit(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onLoadComplete (FMLLoadCompleteEvent event) {
        
        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().onLoadComplete(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onServerStarting (FMLServerStartingEvent event) {
        
        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().onServerStarting(event);
        }

        activeSubMod = null;
    }
    
    @EventHandler
    public void onServerStopped (FMLServerStoppedEvent event) {
        
        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().onServerStopped(event);
        }

        activeSubMod = null;
    }
    
    @SubscribeEvent
    public void blocks (RegistryEvent.Register<Block> event) {
        
        IForgeRegistry<Block> reg = new SubModRegistryTracker(event.getRegistry());
    
        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().registerBlocks(reg);
        }

        activeSubMod = null;
    }

    @SubscribeEvent
    public void items (RegistryEvent.Register<Item> event) {

        IForgeRegistry<Item> reg = new SubModRegistryTracker(event.getRegistry());

        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().registerItems(reg);
        }

        activeSubMod = null;
    }

    @SubscribeEvent
    public void entities (RegistryEvent.Register<EntityEntry> event) {

        IForgeRegistry<EntityEntry> reg = new SubModRegistryTracker(event.getRegistry());

        for (final SubModContainer container : this.subMods) {

            activeSubMod = container;
            container.getSubMod().registerEntities(reg);
        }

        activeSubMod = null;
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void models (ModelRegistryEvent event) {
        
        for (final SubModContainer container : this.subMods) {

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
