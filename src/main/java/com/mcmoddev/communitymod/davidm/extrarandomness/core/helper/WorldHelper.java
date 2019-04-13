package com.mcmoddev.communitymod.davidm.extrarandomness.core.helper;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldHelper {

	public static void repelNearbyEntities(World world, BlockPos pos, double radius, double strength) {
		AxisAlignedBB aoe = new AxisAlignedBB(0, 0, 0, 1, 1, 1).grow(radius);
		Vec3d centerPos = (new Vec3d(pos)).add(0.5, 0, 0.5);
		world.getEntitiesWithinAABB(Entity.class, aoe.offset(pos)).forEach(entity -> {
			double distance = centerPos.distanceTo(entity.getPositionVector());
			double power = Math.max(0, strength * ((radius - distance) / radius));
			
			Vec3d direction = entity.getPositionVector().subtract(centerPos).normalize().scale(power);
			entity.addVelocity(direction.x, direction.y, direction.z);
			entity.velocityChanged = true;
		});
	}
}
