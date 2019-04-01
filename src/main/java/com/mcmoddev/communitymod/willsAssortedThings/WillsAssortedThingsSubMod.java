package com.mcmoddev.communitymod.willsAssortedThings;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.willsAssortedThings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.UUID;

import static com.mcmoddev.communitymod.shared.ClientUtil.simpleItemModel;

@SubMod(
        name = "Will's Assorted Things",
        description = "Assorted things from Willbl3pic",
        attribution = "Willbl3pic"

)
public class WillsAssortedThingsSubMod implements ISubMod {

    public static final UUID[] goodIDs = new UUID[]{
            UUID.fromString("9b035372-0d8d-4513-8bd5-9808d7f4a9b3"),
            UUID.fromString("330eacd1-1117-4e89-b664-c88f7af73de5"),
            UUID.fromString("8bb30ce6-63f0-4f52-9170-053c646fe86f"),
            UUID.fromString("3db6e5b5-7534-47e4-8640-a32601c4cd01")
    };

    public static final UUID[] badIDS = new UUID[]{
            UUID.fromString("4b8da266-1b9b-4cc3-8697-9afb07178bc2"),
            UUID.fromString("cc015425-b43f-4cd1-8efe-a791693a50ff"),
            UUID.fromString("52d1e4a0-062a-4623-8ac9-4f9ee790f40d"),
            UUID.fromString("9fbf118d-d607-4bf9-8a0d-98b616dee368")
    };

    @Override
    public void onPreInit (FMLPreInitializationEvent event) {
        FMLLog.log.info("Will's Assorted things active, deleting MC...");
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> reg) {
        for (Block block : ModBlocks.blocks) {
            reg.register(block);
        }
    }

    @Override
    public void registerItems(IForgeRegistry<Item> reg) {
        for (Block block : ModBlocks.blocks) {
            reg.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
       for (Block block : ModBlocks.blocks) {
            simpleItemModel(Item.getItemFromBlock(block));
        }
    }
}
