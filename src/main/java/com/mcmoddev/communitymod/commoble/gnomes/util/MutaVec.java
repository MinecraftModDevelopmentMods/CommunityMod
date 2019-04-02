package com.mcmoddev.communitymod.commoble.gnomes.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Vector3 that can be modified in place
 * because vector3s that can't be modified in place annoy me
 * @author Commoble
 *
 */
public class MutaVec
{
    public static final MutaVec ZERO = new MutaVec(0.0D, 0.0D, 0.0D);
    
	public double x;
	public double y;
	public double z;
	
	public MutaVec(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public MutaVec(Vec3d vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public Vec3d asVec3d()
	{
		return new Vec3d(this.x, this.y, this.z);
	}

    /**
     * Normalizes the vector to a length of 1 (except if it is the zero vector)
     */
    public MutaVec getNormalized()
    {
        double length = MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return length < 1.0E-4D ? ZERO : new MutaVec(this.x / length, this.y / length, this.z / length);
    }
    
    /**
     * Normalizes this vector in place, altering its x/y/z values (except if it is the zero vector)
     */
    public void normalizeInPlace()
    {
        double length = MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (length < 1.0E-4D)
        {
        	this.x = 0D;
        	this.y = 0D;
        	this.z = 0D;
        }
        else
        {
        	this.x = this.x / length;
        	this.y = this.y / length;
        	this.z = this.z / length;
        }
    }
}
