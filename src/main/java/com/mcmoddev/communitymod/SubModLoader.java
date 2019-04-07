package com.mcmoddev.communitymod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public enum SubModLoader
{
    INSTANCE;

    private static final Logger LOGGER = LogManager.getLogger("SubModLoader");

    private final Map<IForgeRegistryEntry, SubModContainer> regEntryOrigin = new HashMap<>();
    private final Map<ISubMod, SubModContainer> subMods = new HashMap<>();
    private final List<SubModContainer> loadedSubMods = new ArrayList<>();

    public void onConstruction(FMLConstructionEvent event)
    {
        long startTime = System.currentTimeMillis();
        Set<SubModContainer> eventSubscriberWarns = new HashSet<>();
        Set<ASMDataTable.ASMData> dataSet = event.getASMHarvestedData().getAll(SubMod.class.getCanonicalName());

        for (ASMDataTable.ASMData data : dataSet)
        {
            String modName = (String) data.getAnnotationInfo().get("name");

            try
            {
                if (!data.getAnnotationInfo().containsKey("clientSideOnly") || (boolean) data.getAnnotationInfo().get("clientSideOnly") && event.getSide().isClient())
                {
                    ISubMod subMod = (ISubMod) Class.forName(data.getClassName()).newInstance();
                    String description = (String) data.getAnnotationInfo().getOrDefault("description", "No description is provided for this submod.");
                    String attribution = (String) data.getAnnotationInfo().getOrDefault("attribution", "No attribution is provided for this submod.");
                    Boolean requiresMcRestart = (Boolean) data.getAnnotationInfo().getOrDefault("requiresMcRestart", true);
                    SubModContainer container = new SubModContainer(modName, description, attribution, subMod, requiresMcRestart);

                    if (subMod.getClass().isAnnotationPresent(Mod.EventBusSubscriber.class))
                    {
                        eventSubscriberWarns.add(container);
                        MinecraftForge.EVENT_BUS.unregister(subMod.getClass());
                    }

                    if (CommunityMod.getConfig().isSubModEnabled(container))
                    {
                        loadedSubMods.add(container);
                        MinecraftForge.EVENT_BUS.register(subMod.getClass());

                        LOGGER.info("Loaded submod {} from class {}.", modName, data.getClassName());
                    }
                    else if (!requiresMcRestart)
                    {
                        MinecraftForge.EVENT_BUS.register(subMod.getClass());
                    }

                    subMods.put(subMod, container);
                }
            }
            catch (Exception e)
            {
                LOGGER.error("Failed to load submod {}.", modName);
                LOGGER.catching(e);
            }
        }

        for (SubModContainer container : eventSubscriberWarns)
        {
            LOGGER.warn("{} from {} uses an @EventBusSubscriber annotation, which is normally done automagically. Proceeding anyway.", container.getSubMod().getClass(), container.getName());
        }

        LOGGER.info("Loaded {} submods in {}ms.", loadedSubMods.size(), System.currentTimeMillis() - startTime);

        for (SubModContainer container : loadedSubMods)
        {
            container.getSubMod().onConstruction(event);
        }
    }

    public static List<SubModContainer> getLoadedSubMods()
    {
        return Collections.unmodifiableList(INSTANCE.loadedSubMods);
    }

    public static Collection<SubModContainer> getSubMods()
    {
        return Collections.unmodifiableCollection(INSTANCE.subMods.values());
    }

    public static boolean isSubModLoaded(ISubMod subMod)
    {
        return INSTANCE.loadedSubMods.contains(INSTANCE.subMods.get(subMod));
    }

    public static boolean unload(ISubMod subMod)
    {
        SubModContainer container = INSTANCE.subMods.get(subMod);

        if (container != null)
        {
            CommunityMod.getConfig().getSubModEnabled(container).set(false);

            if (!container.requiresMcRestart() && isSubModLoaded(subMod))
            {
                INSTANCE.loadedSubMods.remove(container);
                subMod.onLoadStateChanged(true);
                return true;
            }
        }

        return false;
    }

    public static boolean load(ISubMod subMod)
    {
        SubModContainer container = INSTANCE.subMods.get(subMod);

        if (container != null)
        {
            CommunityMod.getConfig().getSubModEnabled(container).set(true);

            if (!container.requiresMcRestart() && !isSubModLoaded(subMod))
            {
                INSTANCE.loadedSubMods.add(container);
                subMod.onLoadStateChanged(false);
                return true;
            }
        }

        return false;
    }

    public static SubModContainer getSubModOrigin(IForgeRegistryEntry entry)
    {
        return INSTANCE.regEntryOrigin.get(entry);
    }

    public <V extends IForgeRegistryEntry<V>> IForgeRegistry<V> trackRegistry(IForgeRegistry<V> reg)
    {
        return new SubModRegistryTracker<>(reg);
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
                regEntryOrigin.put(e, CommunityMod.getActiveSubMod());
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
