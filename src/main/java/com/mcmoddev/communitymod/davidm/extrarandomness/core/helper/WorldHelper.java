package com.mcmoddev.communitymod.davidm.extrarandomness.core.helper;

import java.util.List;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.LexWand;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldHelper {

	public static void repelNearbyEntities(World world, BlockPos pos, double radius, double strength) {
		AxisAlignedBB aoe = new AxisAlignedBB(0, 0, 0, 1, 1, 1).grow(radius);
		Vec3d centerPos = (new Vec3d(pos)).add(0.5, 0, 0.5);
		getEntitiesInBox(world, Entity.class, aoe.offset(pos)).forEach(entity -> {
			double distance = centerPos.distanceTo(entity.getPositionVector());
			double power = Math.max(0, strength * ((radius - distance) / radius));
			
			Vec3d direction = entity.getPositionVector().subtract(centerPos).normalize().scale(power);
			entity.addVelocity(direction.x, direction.y, direction.z);
			entity.velocityChanged = true;
		});
	}
	
	public static <T extends Entity> List<T> getEntitiesInBox(World world, Class<? extends T> entityClass, AxisAlignedBB box) {
		List<T> entities = world.<T>getEntitiesWithinAABB(entityClass, box);
		entities.removeIf(
				entity -> entity instanceof EntityPlayer && ((EntityPlayer) entity).inventory.getCurrentItem().getItem() instanceof LexWand
		);
		return entities;
	}
}
