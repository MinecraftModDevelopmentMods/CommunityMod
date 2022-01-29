package com.mcmoddev.communitymod.davidm.extrarandomness.core.attribute;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface AltarItem {

	public abstract int getCooldown();
	public abstract void onAltarAction(World world, BlockPos pos);
}
