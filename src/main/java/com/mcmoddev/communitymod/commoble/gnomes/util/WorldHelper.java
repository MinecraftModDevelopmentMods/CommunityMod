package com.mcmoddev.communitymod.commoble.gnomes.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Utility functions for interacting with the World
 * @author Commoble
 *
 */
public class WorldHelper
{
	/**
	 * Returns the Block at the specified position
	 */
	public static Block getBlock(IBlockAccess access, BlockPos pos)
	{
		return access.getBlockState(pos).getBlock();
	}
	
	/**
	 * Returns the Block at the specified coordinates
	 */
	public static Block getBlock(IBlockAccess access, int x, int y, int z)
	{
		return WorldHelper.getBlock(access, new BlockPos(x,y,z));
	}
	
	public static boolean isAirBlock(IBlockAccess access, int x, int y, int z)
	{
		return access.isAirBlock(new BlockPos(x,y,z));
	}
	
	public static void setBlock(World world, int x, int y, int z, Block block)
	{
		world.setBlockState(new BlockPos(x,y,z), block.getDefaultState());
	}
	
	public static void setAirBlock(World world, int x, int y, int z)
	{
		world.setBlockToAir(new BlockPos(x,y,z));
	}
	
	/**
	 * Returns true if block is replaceable and has no collider
	 */
	public static boolean isAirLikeBlock(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		boolean replaceable = block.isReplaceable(world, pos);
		boolean noCollider = (state.getCollisionBoundingBox(world, pos) == Block.NULL_AABB);
		return (replaceable || noCollider);
	}
	
	/**
	 * Returns the first hittable blockpos along an entity's line of sight
	 */
	public static BlockPos getLineOfSightTargetBlock(World world, Entity ent, double dist)
	{
		Vec3d start = ent.getPositionVector().add(0D, ent.getEyeHeight(), 0D);
		Vec3d lookVec = ent.getLookVec().scale(dist);	// look along a distance of 16
		Vec3d end = start.add(lookVec);
		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, true);
		return (result == null) ? null : result.getBlockPos();
	}
	
	/**
	 * Returns the first hittable blockpos along a random vector in a cone based
	 * on an entity's line of sight
	 */
	public static BlockPos getConeOfSightTargetBlock(World world, Entity ent, double dist)
	{
		Vec3d start = ent.getPositionVector().add(0D, ent.getEyeHeight(), 0D);
		
		Vec3d LOSvec = ent.getLookVec();
		LOSvec = LOSvec.rotatePitch((world.rand.nextFloat()-0.5F)*MathExtender.PI_HALVED);
		LOSvec = LOSvec.rotateYaw((world.rand.nextFloat()-0.5F)*MathExtender.PI_QUARTERED);
		LOSvec = LOSvec.scale(dist);
		Vec3d end = start.add(LOSvec);
		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, true);
		return (result == null) ? null : result.getBlockPos();
	}
	
	/**
	 * Returns the first hittable blockpos between an entity and a target position
	 */
	public static BlockPos getTargetBlockTowards(World world, Entity ent, BlockPos target)
	{
		Vec3d start = ent.getPositionVector().add(0D, ent.getEyeHeight(), 0D);
		Vec3d end = new Vec3d(target);
		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, true);
		return (result == null) ? null : result.getBlockPos();
	}
}
