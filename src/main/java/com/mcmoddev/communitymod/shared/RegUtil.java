package com.mcmoddev.communitymod.shared;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class RegUtil {
	public static <T extends Block> T registerBlock(IForgeRegistry<Block> reg, T block, String name) {
		block.setRegistryName(new ResourceLocation(CommunityGlobals.MOD_ID, name));
		block.setTranslationKey(CommunityGlobals.MOD_ID + '.' + name);
		block.setCreativeTab(CommunityGlobals.TAB);
		
		reg.register(block);
		
		return block;
	}
	
	public static <T extends ItemBlock> T registerItemBlock(IForgeRegistry<Item> reg, T item) {
		item.setRegistryName(Objects.requireNonNull(item.getBlock().getRegistryName()));
		item.setCreativeTab(CommunityGlobals.TAB);
		
		reg.register(item);
		
		return item;
	}
	
	public static <T extends Item> T registerItem(IForgeRegistry<Item> reg, T item, String name) {
		item.setRegistryName(new ResourceLocation(CommunityGlobals.MOD_ID, name));
		item.setTranslationKey(CommunityGlobals.MOD_ID + '.' + name);
		item.setCreativeTab(CommunityGlobals.TAB);
		
		reg.register(item);
		
		return item;
	}
}
