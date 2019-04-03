package com.mcmoddev.communitymod.davidm.extrarandomness.common.item;

import com.mcmoddev.communitymod.davidm.extrarandomness.core.AltarItem;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumAltarAnimation;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.WorldHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Shocker extends AltarItem implements IAltarAnimation {

	@Override
	public int getCooldown() {
		return 40;
	}
	
	@Override
	public String getExtraInfo() {
		return I18n.format("tooltip.community_mod.shocker_extra");
	}

	@Override
	public void onAltarAction(World world, BlockPos pos) {
		WorldHelper.repelNearbyEntities(world, pos.offset(EnumFacing.DOWN), 20, 10);
	}

	@Override
	public EnumAltarAnimation animationType() {
		return EnumAltarAnimation.SHOCKWAVE;
	}

	@Override
	public int animationLength() {
		return 40;
	}
}
