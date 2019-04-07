package com.mcmoddev.communitymod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = CommunityGlobals.MOD_ID, name = CommunityGlobals.MOD_NAME, version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@", guiFactory = "com.mcmoddev.communitymod.client.gui.CommunityGuiFactory", dependencies = "required:forge@[14.23.5.2824,);")
public class CommunityMod {
    
    public static final Logger LOGGER = LogManager.getLogger(CommunityGlobals.MOD_NAME);

    private SubModContainer activeSubMod;
    private CommunityConfig config;
    
    @Mod.Instance(CommunityGlobals.MOD_ID)
    public static CommunityMod INSTANCE;

    public static CommunityConfig getConfig()
    {
        return INSTANCE.config;
    }

    public static SubModContainer getActiveSubMod()
    {
        return INSTANCE.activeSubMod;
    }

    @EventHandler
    public void onConstruction (FMLConstructionEvent event) {

        config = new CommunityConfig(new File("config/" + CommunityGlobals.MOD_ID + ".cfg"));
        SubModLoader.INSTANCE.onConstruction(event);
        config.syncConfigData();
    }
    
    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {
    
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        
        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().onPreInit(event);
        }

        activeSubMod = null;
    }

    @EventHandler
    public void onInit (FMLInitializationEvent event) {

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().onInit(event);
        }

        activeSubMod = null;
    }

    @EventHandler
    public void onPostInit (FMLPostInitializationEvent event) {

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().onPostInit(event);
        }

        activeSubMod = null;
    }

    @EventHandler
    public void onLoadComplete (FMLLoadCompleteEvent event) {

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().onLoadComplete(event);
        }

        activeSubMod = null;
    }

    @EventHandler
    public void onServerStarting (FMLServerStartingEvent event) {

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().onServerStarting(event);
        }

        activeSubMod = null;
    }

    @EventHandler
    public void onServerStopped (FMLServerStoppedEvent event) {

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().onServerStopped(event);
        }

        activeSubMod = null;
    }

    @SubscribeEvent
    public void blocks (RegistryEvent.Register<Block> event) {

        IForgeRegistry<Block> reg = SubModLoader.INSTANCE.trackRegistry(event.getRegistry());

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().registerBlocks(reg);
        }

        activeSubMod = null;
    }

    @SubscribeEvent
    public void items (RegistryEvent.Register<Item> event) {

        IForgeRegistry<Item> reg = SubModLoader.INSTANCE.trackRegistry(event.getRegistry());

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().registerItems(reg);
        }

        activeSubMod = null;
    }

    @SubscribeEvent
    public void entities (RegistryEvent.Register<EntityEntry> event) {

        IForgeRegistry<EntityEntry> reg = SubModLoader.INSTANCE.trackRegistry(event.getRegistry());

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            activeSubMod = container;
            container.getSubMod().registerEntities(reg);
        }

        activeSubMod = null;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void models (ModelRegistryEvent event) {

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

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
            SubModContainer subMod = SubModLoader.getSubModOrigin(stack.getItem());

            if (subMod != null)
            {
                event.getToolTip().add(TextFormatting.DARK_GRAY + "(" + subMod.getName() + " - " + subMod.getAttribution() + ")");
            }
        }
    }
}
