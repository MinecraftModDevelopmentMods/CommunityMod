package com.mcmoddev.communitymod.davidm.extrarandomness.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class MemeWrench extends Item {

	public MemeWrench() {
		super();
		this.setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.community_mod.wrench"));
	}
}
