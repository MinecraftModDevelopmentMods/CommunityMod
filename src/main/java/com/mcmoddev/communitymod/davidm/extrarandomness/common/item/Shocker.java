package com.mcmoddev.communitymod.davidm.extrarandomness.common.item;

import java.util.List;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.ExtraRandomness;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketAltarAnimation;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumAltarAnimation;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.attribute.AltarItem;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.WorldHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class Shocker extends Item implements AltarItem, IAltarAnimation {

	public Shocker() {
		super();
		this.setMaxStackSize(1);
	}
	
	@Override
	public int getCooldown() {
		return 40;
	}

	@Override
	public void onAltarAction(World world, BlockPos pos) {
		WorldHelper.repelNearbyEntities(world, pos.offset(EnumFacing.DOWN), 20, 10);
		ExtraRandomness.network.sendToAllAround(
				new PacketAltarAnimation(pos, EnumAltarAnimation.SHOCKWAVE),
				new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 80)
		);
	}

	@Override
	public EnumAltarAnimation animationType() {
		return EnumAltarAnimation.SHOCKWAVE;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.community_mod.altar_item"));
		tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.community_mod.shocker_extra"));
	}
}
