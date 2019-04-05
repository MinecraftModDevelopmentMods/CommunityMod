package com.mcmoddev.communitymod.davidm.extrarandomness.common.item;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.ExtraRandomness;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketAltarAnimation;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.AltarItem;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumAltarAnimation;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.WorldHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

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
		ExtraRandomness.network.sendToAllAround(
				new PacketAltarAnimation(pos, EnumAltarAnimation.SHOCKWAVE),
				new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 80)
		);
	}

	@Override
	public EnumAltarAnimation animationType() {
		return EnumAltarAnimation.SHOCKWAVE;
	}
}
