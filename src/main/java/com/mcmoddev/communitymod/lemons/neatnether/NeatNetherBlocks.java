package com.mcmoddev.communitymod.lemons.neatnether;

import com.mcmoddev.communitymod.lemons.neatnether.block.BlockSmoulderingAsh;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = "community_mod")
@GameRegistry.ObjectHolder("community_mod")
public class NeatNetherBlocks
{
	public static final Block SMOULDERING_ASH = Blocks.AIR;
	public static final Block SOUL_GLASS = Blocks.AIR;

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> r = event.getRegistry();

		registerBlock(setAttribues(new BlockSmoulderingAsh(), 0.5F, 0.5F), "smouldering_ash", r);
		registerBlock(setAttribues(new BlockSoulGlass(), 0.25F, 1F), "soul_glass", r);
	}

	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> r = event.getRegistry();

		registerItemBlock(SMOULDERING_ASH, r);
		registerItemBlock(SOUL_GLASS, r);
	}


	@SubscribeEvent
	public static void onRegisterModel(ModelRegistryEvent event)
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
		block.setTranslationKey("community_mod." + name);
		block.setRegistryName(new ResourceLocation("community_mod", name));
		block.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

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
