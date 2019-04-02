package com.mcmoddev.communitymod.routiduct.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RoutiductRegistry {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        RoutiductItems.init(event);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        RoutiductBlocks.init(event);
    }

}
