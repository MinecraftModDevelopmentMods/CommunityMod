package com.mcmoddev.communitymod.lemons.neatnether;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.lemons.neatnether.block.BlockSmoulderingAsh;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
public class NeatNetherBlocks
{
	public static final Block SMOULDERING_ASH = Blocks.AIR;
	public static final Block SOUL_GLASS = Blocks.AIR;

	public static void registerBlocks(IForgeRegistry<Block> reg)
	{
		registerBlock(setAttribues(new BlockSmoulderingAsh(), 0.5F, 0.5F), "smouldering_ash", reg);
		registerBlock(setAttribues(new BlockSoulGlass(), 0.25F, 1F), "soul_glass", reg);
	}

	public static void registerItems(IForgeRegistry<Item> reg)
	{
		registerItemBlock(SMOULDERING_ASH, reg);
		registerItemBlock(SOUL_GLASS, reg);
	}

	public static void registerModels(ModelRegistryEvent event)
	{
		registerBlockItemModel(SMOULDERING_ASH);
		registerBlockItemModel(SOUL_GLASS);
	}

	public static void registerBlockItemModel(Block block)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}

	public static ItemBlock registerItemBlock(Block block, IForgeRegistry<Item> r)
	{
		ItemBlock ib = new ItemBlock(block);
		ib.setRegistryName(block.getRegistryName());

		r.register(ib);

		return ib;
	}

	public static Block registerBlock(Block block, String name, IForgeRegistry<Block> r)
	{
		block.setTranslationKey(CommunityGlobals.MOD_ID + "." + name);
		block.setRegistryName(new ResourceLocation(CommunityGlobals.MOD_ID, name));
		block.setCreativeTab(CommunityGlobals.TAB);

		r.register(block);

		return block;
	}

	public static Block setAttribues(Block block, float hardness, float resistence)
	{
		block.setResistance(resistence);
		block.setHardness(hardness);
		return block;
	}
}
