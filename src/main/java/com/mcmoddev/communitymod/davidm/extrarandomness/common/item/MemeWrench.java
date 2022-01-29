package com.mcmoddev.communitymod.davidm.extrarandomness.common.item;

import java.util.List;

import com.mcmoddev.communitymod.davidm.extrarandomness.core.attribute.IWrenchable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class MemeWrench extends Item {

	public MemeWrench() {
		super();
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof IWrenchable) {
				if (!world.isRemote) {
					((IWrenchable) tileEntity).onWrench(player);
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format("lore.community_mod.wrench"));
		tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.community_mod.wrench_1"));
		tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.community_mod.wrench_2"));
	}
}
