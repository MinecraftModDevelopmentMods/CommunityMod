package com.mcmoddev.communitymod.commoble.gnomes.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Utility functions for entities
 * @author Commoble
 *
 */
public class EntityHelper
{
	/**
	 * 
	 * @param world The world object
	 * @param pos The BlockPos to spawn the entity at
	 * @param newEnt A new EntityLiving, e.g. pass (new EntityPig(world)) into function
	 * @return Returns result of spawnEntityInWorld (true if spawn was successful?)
	 */
	public static boolean spawnEntityAtBlockPos(World world, BlockPos pos, EntityLiving newEnt)
	{
		// mostly copied from spawner eggs
		newEnt.setLocationAndAngles(pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D, world.rand.nextFloat()*360F, 0F);
		newEnt.rotationYawHead = newEnt.rotationYaw;
		newEnt.renderYawOffset = newEnt.rotationYaw;
		newEnt.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
		boolean flag = world.spawnEntity(newEnt);
		newEnt.playLivingSound();
		
        return flag;
	}
}
