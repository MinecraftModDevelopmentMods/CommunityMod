package com.mcmoddev.communitymod.traverse.core;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.traverse.TraverseCommon;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import java.util.ArrayList;
import java.util.List;

@SubMod(modid = "traverse", name = "Traverse", attribution = "prospectus dude")
public class TraverseMod implements ISubMod {

    @SidedProxy(clientSide = TraverseConstants.CLIENT_PROXY_CLASS, serverSide = TraverseConstants.SERVER_PROXY_CLASS)
    public static TraverseCommon proxy;
    public static List<Item> itemModelsToRegister = new ArrayList<>();
    public static List<Block> blockModelsToRegister = new ArrayList<>();

    @Override
    public void onPreInit(final FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Override
    public void onInit(final FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Override
    public void onPostInit(final FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Override
    public void onServerStarting(final FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        proxy.serverAboutToStart(event);
    }


    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        proxy.serverStopping(event);
    }

}
