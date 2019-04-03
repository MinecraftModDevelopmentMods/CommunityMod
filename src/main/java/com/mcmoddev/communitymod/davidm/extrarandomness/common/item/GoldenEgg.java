package com.mcmoddev.communitymod.davidm.extrarandomness.common.item;

import com.mcmoddev.communitymod.davidm.extrarandomness.core.AltarItem;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GoldenEgg extends AltarItem {

	@Override
	public int getCooldown() {
		return 2;
	}
	
	@Override
	public String getExtraInfo() {
		return I18n.format("tooltip.community_mod.golden_egg_extra");
	}

	@Override
	public void onAltarAction(World world, BlockPos pos) {
		EntityEgg egg = new EntityEgg(world, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
		world.spawnEntity(egg);
		egg.addVelocity(world.rand.nextFloat() * 2 - 1, world.rand.nextFloat(), world.rand.nextFloat() * 2 - 1);
		egg.velocityChanged = true;
	}
}
