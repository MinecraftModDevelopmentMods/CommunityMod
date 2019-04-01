package com.mcmoddev.communitymod.lemons.neatnether;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@SubMod(name = "Neat Nether", description = "Adds some neat stuff to the Nether", attribution = "Lemons")
public class NeatNether implements ISubMod
{
	@Override
	public void onPostInit (FMLPostInitializationEvent event)
	{
		FurnaceRecipes.instance().addSmeltingRecipeForBlock(Blocks.SOUL_SAND, new ItemStack(NeatNetherBlocks.SOUL_GLASS), 0.2F);
	}
}
