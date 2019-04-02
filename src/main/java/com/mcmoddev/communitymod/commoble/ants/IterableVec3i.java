package com.mcmoddev.communitymod.commoble.ants;

import net.minecraft.util.math.Vec3i;

/**
 * Vec3i with enumerated fields so that x,y,z can be iterated over
 * @author Joseph
 *
 */
public class IterableVec3i extends Vec3i
{
	private final int[] xyz;	// array holding the x, y, and z-value

	public IterableVec3i(int xIn, int yIn, int zIn)
	{
		super(xIn, yIn, zIn);
		int[] values = {xIn, yIn, zIn};
		this.xyz = values;
		// TODO Auto-generated constructor stub
	}
	
	/** 
	 * construct an IV3i from a regular V3i
	 */
	public IterableVec3i(Vec3i vec)
	{
		this(vec.getX(), vec.getY(), vec.getZ());
	}
	
	public int getValueOnAxis(int index)
	{
		return this.xyz[index];
	}
	
}
