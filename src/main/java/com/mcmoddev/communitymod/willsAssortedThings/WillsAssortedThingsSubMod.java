package com.mcmoddev.communitymod.willsAssortedThings;

import static com.mcmoddev.communitymod.shared.ClientUtil.simpleItemModel;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.willsAssortedThings.block.ModBlocks;
import com.mcmoddev.communitymod.willsAssortedThings.item.ModItems;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(
		modid = "willsthings",
        name = "Will's Assorted Things",
        description = "Assorted things from Willbl3pic",
        attribution = "Willbl3pic"

)
public class WillsAssortedThingsSubMod implements ISubMod {

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

        for (Item item : ModItems.items) {
            reg.register(item);
        }
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
       for (Block block : ModBlocks.blocks) {
            simpleItemModel(Item.getItemFromBlock(block));
        }
       for (Item item : ModItems.items) {
           simpleItemModel(item);
       }
    }
}
