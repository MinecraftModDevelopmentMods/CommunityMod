package com.mcmoddev.communitymod.routiduct;

import com.mcmoddev.communitymod.CommunityMod;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.routiduct.block.tiles.TileRoutiduct;
import com.mcmoddev.communitymod.routiduct.gui.RoutiductGuiHandler;
import com.mcmoddev.communitymod.routiduct.proxy.RoutiductServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SubMod(modid = "routiduct", name = "Routiduct", attribution = "pippity poppity prospector")
public class Routiduct implements ISubMod {

    @SidedProxy(clientSide = RoutiductConstants.CLIENT_PROXY_CLASS, serverSide = RoutiductConstants.SERVER_PROXY_CLASS)
    public static RoutiductServer proxy;

    @Override
    public void onPreInit(final FMLPreInitializationEvent event) {
        GameRegistry.registerTileEntity(TileRoutiduct.class, "TileRoutiduct");
        proxy.preInit();
    }


    @Override
    public void onInit(final FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(CommunityMod.INSTANCE, new RoutiductGuiHandler());
        proxy.init();
    }

    @Override
    public void onPostInit(final FMLPostInitializationEvent event) {
        proxy.postInit();
    }

}