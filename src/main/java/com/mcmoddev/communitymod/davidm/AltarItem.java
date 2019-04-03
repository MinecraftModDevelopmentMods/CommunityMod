package com.mcmoddev.communitymod.davidm;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

// Lets just create a useless class for this because INHERITANCE OVER COMPOSITION!!! YAY!
// (insert 12 year old making modding tutorials on youtube)
public abstract class AltarItem extends Item {
	
	public AltarItem() {
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.community_mod.altar_item"));
	}
	
	public abstract int getCooldown();
	public abstract void onAltarAction(World world, BlockPos pos);
}
