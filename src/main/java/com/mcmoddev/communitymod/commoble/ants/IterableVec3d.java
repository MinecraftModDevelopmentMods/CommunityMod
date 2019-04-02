package com.mcmoddev.communitymod.commoble.ants;

import net.minecraft.util.math.Vec3d;

/**
 * Vec3d with iterable array of its xyz values
 * @author Joseph
 *
 */
public class IterableVec3d extends Vec3d
{
	private final double[] xyz;
	
	public IterableVec3d(double xIn, double yIn, double zIn)
	{
		super(xIn, yIn, zIn);
		double[] values = {xIn, yIn, zIn};
		this.xyz = values;
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * A double array of length 3 can be used to construct an IV3D
	 * Using a smaller array will throw an error! don't do that
	 * If the array is longer than 3, the extra values will be ignored
	 */
	public IterableVec3d(double[] inputs)
	{
		this(inputs[0], inputs[1], inputs[2]);
	}
	
	// return the corresponding x,y, or z value
	// index should be 0, 1, or 2, respectively
	public double getValueOnAxis(int index)
	{
		return this.xyz[index];
	}

	/**
	 * Returns the dotproduct of two vectors a and b (in this case an iterable vec3d and an iterable vec3i
	 * The dot product of the vectors is defined as a.x*b.x + a.y*b.y + a.z*b.z
	 * @param a an IterableVec3d
	 * @param b an IterableVec3i
	 * @return The dot product returns a scalar number (in this case a double)
	 */
	public static double dotProduct(IterableVec3d a, IterableVec3i b)
	{
		double outvalue = 0;
		for (int i=0; i<3; i++)
		{
			outvalue += (a.getValueOnAxis(i) * b.getValueOnAxis(i));
		}
		return outvalue;
	}
}
