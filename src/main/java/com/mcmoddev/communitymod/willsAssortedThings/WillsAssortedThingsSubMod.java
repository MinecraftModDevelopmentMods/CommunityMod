package com.mcmoddev.communitymod.willsAssortedThings;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.willsAssortedThings.block.ModBlocks;
import com.mcmoddev.communitymod.willsAssortedThings.client.render.RenderChickenArrow;
import com.mcmoddev.communitymod.willsAssortedThings.entity.EntityChickenArrow;
import com.mcmoddev.communitymod.willsAssortedThings.entity.ModEntities;
import com.mcmoddev.communitymod.willsAssortedThings.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.UUID;

import static com.mcmoddev.communitymod.shared.ClientUtil.simpleItemModel;

@SubMod(
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
    public void registerEntities(IForgeRegistry<EntityEntry> reg) {
        reg.registerAll(EntityEntryBuilder.create()
                .entity(EntityChickenArrow.class)
                .factory(EntityChickenArrow::new)
                .id(new ResourceLocation(CommunityGlobals.MOD_ID, "chicken_arrow"), CommunityGlobals.entity_id++)
                .name("chicken_arrow")
                .tracker(64, 3, true)
                .build());
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
       for (Block block : ModBlocks.blocks) {
            simpleItemModel(Item.getItemFromBlock(block));
        }
       for (Item item : ModItems.items) {
           simpleItemModel(item);
       }
        RenderingRegistry.registerEntityRenderingHandler(EntityChickenArrow.class, RenderChickenArrow::new);
    }
}
