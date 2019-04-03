package com.mcmoddev.communitymod.davidm;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GoldenEgg extends AltarItem {

	@Override
	public int getCooldown() {
		return 1;
	}

	@Override
	public void onAltarAction(World world, BlockPos pos) {
		
	}
}
