package com.mcmoddev.communitymod.commoble.gnomes.ai.job;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnome;
import com.mcmoddev.communitymod.commoble.gnomes.util.WorldHelper;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * A Job is a special AI class used by gnomes
 * to perform tasks more lengthy and complicated than altering
 * a block at a specific location (which is done by Assignments).
 * 
 * They are not true AI classes (which extend EntityAIBase), but
 * instead are handled by a wrapper (EntityAIPerformJob) that calls
 * shouldExecute, start, continue, etc.
 * 
 * Assignments have higher priority than Jobs.
 * @author Joe
 *
 */
public abstract class Job
{
	// integer position of target
	protected BlockPos pos;
	
	// the Vec3 position of target
	protected Vec3d targetVec;
	
	// the intermediate path vector between current location and target
	protected Vec3d pathVec;
	
	
	protected EntityGnome gnome;
	protected int totalJobTime;
	
	protected double speed;
	
	/**
	 * This sets this Job's target location.
	 * @param loc An IntLoc containing the coordinates for the
	 * 		target of this Job.
	 */
	public Job(EntityGnome ent, BlockPos pos)
	{
		this.pos = pos;
		this.targetVec = new Vec3d(pos);
		this.gnome = ent;
		this.totalJobTime = 0;
		this.speed = 1.0D;
	}
	
	public Job(EntityGnome ent, Vec3d vec)
	{
		this.targetVec = vec;
		this.pos = new BlockPos(vec);
		this.gnome = ent;
		this.totalJobTime = 0;
		this.speed = 1.0D;
	}
	
	public boolean shouldStart()
	{
		Vec3d path = this.getPathVec();
		if (path == null)
		{
			return false;
		}
		else
		{
			this.pathVec = path;
			return true;
		}
	}
	
	public void start()
	{
		this.gnome.getNavigator().tryMoveToXYZ(this.pathVec.x, this.pathVec.y+1, this.pathVec.z, this.speed);
	}
	
	public boolean shouldContinue()
	{
		this.totalJobTime++;
		// if close to final target, finish
		if (this.gnome.getDistance(this.targetVec.x, this.targetVec.y, this.targetVec.z) < 3.0D)
		{
			this.gnome.job = null;
			this.finishJob(true);
			return false;
		}
		// if not close to final target but close to intermediate path target, reset
		else if (this.gnome.getDistance(this.pathVec.x, this.pathVec.y, this.pathVec.z) < 3.0D)
		{
			return false;
		}
		// if close to nothing and navigator can't path, or job timeout, cancel
		else if (this.gnome.getNavigator().noPath()  || this.totalJobTime > 10000)
		{
			this.gnome.job = null;
			this.finishJob(false);
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//public abstract void update();
	
	/**
	 * Perform the task at the job's location.
	 * The gnome's Job is set to Null just before this.
	 * This function is allowed to grant the Gnome a new job.
	 * @param near True if the gnome is near the target and may fully complete the job,
	 * 		false if it is ended prematurely.
	 */
	public abstract void finishJob(boolean near);
	
	public float getBlockPathWeight(BlockPos pos)
	{
		double distSQ = this.pos.distanceSq(pos);
		return (float)(10000D - distSQ);	// essentially 100-dist
	}
	
	/**
	 * Returns a location toward the job target
	 * If the distance between the specified entity and the target is
	 * 		less than 12 blocks, returns that target.
	 * If the distance is greater than 12 blocks, returns a random block toward
	 * 		that target within 12 blocks horizontal / 4 vertical.
	 * @param ent the entity
	 * @return	a Vec3 containing the path location, or null.
	 */
	protected Vec3d getPathVec()
	{
		// if within 12 blocks, try to path there directly
		if (this.gnome.getDistanceSq(this.targetVec.x, this.targetVec.y, this.targetVec.z) < 144)
		{
			return this.targetVec;
		}
		else	// if far away, wander in its general direction
		{
			return this.getSurfaceVecToward(this.gnome, this.targetVec, 12, 4);
		}
	}
	
	public Vec3d getRandomSurfaceVec(EntityCreature ent, int xrad, int yrad)
	{
		Vec3d vecRand = RandomPositionGenerator.findRandomTarget(ent, xrad, yrad);
		return Job.groundify(ent.world, vecRand);
	}
	
	public Vec3d getSurfaceVecToward(EntityCreature ent, Vec3d vec, int xrad, int yrad)
	{
		Vec3d vecRand = RandomPositionGenerator.findRandomTargetBlockTowards(ent, xrad, yrad, vec);
		return Job.groundify(ent.world, vecRand);
	}
	
	/**
	 * Alters the vector's y-coordinate to be on the next air block touching a non-air block below it,
	 * using the same x- and z-coordinates
	 * searches up first, then down
	 */
	public static Vec3d groundify(World world, Vec3d vec)
	{
		if (vec == null)
		{
			return null;
		}

		int ytemp = (int)vec.y;
		
		if (ytemp < 2)
		{
			ytemp = 2;
		}
		if (ytemp > 255)
		{
			ytemp = 255;
		}
		
		BlockPos tempLoc = new BlockPos((int)vec.x, ytemp, (int)vec.z);

		while (world.isAirBlock(tempLoc) && tempLoc.getY() > 2)
		{
			tempLoc = tempLoc.down();
		}
		while (world.isAirBlock(tempLoc) && tempLoc.getY() < 255)
		{
			tempLoc = tempLoc.up();
		}
		
		return new Vec3d(tempLoc);
	}
	
	/**
	 * Alters the location's y-coordinate to be on the next air block touching a non-air block below it,
	 * using the same x- and z-coordinates
	 * searches up first, then down
	 * ALTERS THE PASSED INTLOC. Also returns it.
	 */
	public static BlockPos groundify(World world, BlockPos loc)
	{
		if (loc == null)
		{
			return null;
		}

		int ytemp = loc.getY();
		
		if (ytemp < 2)
		{
			ytemp = 2;
		}
		if (ytemp > 255)
		{
			ytemp = 255;
		}
		
		BlockPos tempLoc = new BlockPos(loc.getX(), ytemp, loc.getZ());
		
		//System.out.println("Groundifying " + tempLoc);

		// go down until found ground
		while (WorldHelper.isAirLikeBlock(world, tempLoc) && tempLoc.getY() > 2)
		{
			//System.out.println("Block at " + tempLoc + " is airblock, moving down");
			tempLoc = tempLoc.down();
		}
		// go up until found air
		while (!WorldHelper.isAirLikeBlock(world, tempLoc) && tempLoc.getY() < 255)
		{
			//System.out.println("Block at " + tempLoc + " is NOT airblock, moving up");
			tempLoc = tempLoc.up();
		}
		
		//System.out.println("Grounded to " + tempLoc);
		
		return tempLoc;
	}
}
