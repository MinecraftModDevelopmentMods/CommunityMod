package com.mcmoddev.communitymod.commoble.ants.client;

import java.util.EnumSet;
import java.util.List;

import com.mcmoddev.communitymod.commoble.ants.IterableVec3d;
import com.mcmoddev.communitymod.commoble.ants.IterableVec3i;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleAnt extends Particle
{
    protected EnumFacing face; // the face of the block this ant is traversing
    private int cornerbuffer;	// ticks after rounding a convex edge before ant starts checking edges again
    public static final int CORNER_BUFFER_RESET_TICKS = 2; // ticks to set cornerbuffer to
    
    // the faces on the x, y, and z-axes, for the positive or the negative direction of each axis, respectively
    public static final EnumFacing[] POSITIVE_FACES = {EnumFacing.EAST, EnumFacing.UP, EnumFacing.SOUTH};
    public static final EnumFacing[] NEGATIVE_FACES = {EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.NORTH};

	public ParticleAnt(World world, double x, double y, double z, double xVel, double yVel, double zVel, float scale, EnumFacing face)
	{
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = xVel;
        this.motionY = yVel;
        this.motionZ = zVel;
        float f = (float)(Math.random() * 0.4F + 0.05F);
        this.particleRed = f;
        this.particleGreen = f;
        this.particleBlue = f;
        this.particleScale = scale;
        this.face = face;
        this.cornerbuffer = 0;
        
        this.particleMaxAge = world.rand.nextInt(40) + 20; // about 1 to 3 seconds
        //this.particleMaxAge = (int)((float)this.particleMaxAge * scale);
        this.setParticleTextureIndex(0);
        this.onUpdate();
	}

    public void onUpdate()
    {

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
        
        if (this.cornerbuffer > 0)
        {
        	this.cornerbuffer--;
        }

        //this.motionY -= 0.04D * (double)this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        //this.motionX *= 0.9800000190734863D;
        //this.motionY *= 0.9800000190734863D;
        //this.motionZ *= 0.9800000190734863D;

        /*if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }*/
        
        // set these after move because they're used during move
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }
 // copied from base Particle class so it could be edited
    @Override
    public void move(double x, double y, double z)
    {
        double origY = y;
        double origX = x;
        double origZ = z;

        if (this.canCollide)
        {
            List<AxisAlignedBB> list = this.world.getCollisionBoxes((Entity)null, this.getBoundingBox().expand(x, y, z));

            for (AxisAlignedBB axisalignedbb : list)
            {
                y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
            }

            this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
            }

            this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb2 : list)
            {
                z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
            }

            this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
        }
        else
        {
            this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        }

        this.resetPositionToBB();
        
        // if it hits a wall, change direction
        if (origX != x)	// hit on x-axis
        {
        	this.setVelocityBecauseHitWall(0);
        }
        else if (origY != y)	// hit on y-axis
        {
        	this.setVelocityBecauseHitWall(1);
        }
        else if (origZ != z)	// hit on z-axis
        {
        	this.setVelocityBecauseHitWall(2);
        }
        else
        {
        	if (this.cornerbuffer <= 0 && this.wentAroundCorner(this.world))
        	{
        		// TODO going around corners doesn't really work yet
        		this.setVelocityBecauseWentAroundCorner(this.world);
        	}
        }
        
        this.motionX = this.motionX * ((this.world.rand.nextDouble() * 0.2D) + 0.9D); //random between 0.9 and 1.1
        this.motionY = this.motionY * ((this.world.rand.nextDouble() * 0.2D) + 0.9D);	// won't affect vel if vel==0
        this.motionZ = this.motionZ * ((this.world.rand.nextDouble() * 0.2D) + 0.9D);
    }
    
    /**
     * 
     * @param xyzAxis 0, 1, or 2, respectively
     */
    private void setVelocityBecauseHitWall(int xyzAxis)
    {
    	IterableVec3d oldVel = new IterableVec3d(this.motionX, this.motionY, this.motionZ);
    	IterableVec3i oldNorm = new IterableVec3i(this.face.getDirectionVec());
    	// set new traversal face
    	this.face = oldVel.getValueOnAxis(xyzAxis) > 0 ? ParticleAnt.NEGATIVE_FACES[xyzAxis] : ParticleAnt.POSITIVE_FACES[xyzAxis];
    	IterableVec3i newNorm = new IterableVec3i(this.face.getDirectionVec());
    	IterableVec3d newVel = ParticleAnt.getRotatedVelocity(oldVel, oldNorm, newNorm, false);
    	this.motionX = newVel.x;
    	this.motionY = newVel.y;
    	this.motionZ = newVel.z;
    }
    
    private void setVelocityBecauseWentAroundCorner(World world)
    {
    	IterableVec3d oldVel = new IterableVec3d(this.motionX, this.motionY, this.motionZ);
    	IterableVec3i oldNorm = new IterableVec3i(this.face.getDirectionVec());
    	double checkX = this.posX - 0.2D*(this.face.getXOffset());
    	double checkY = this.posY - 0.2D*(this.face.getYOffset());
    	double checkZ = this.posZ - 0.2D*(this.face.getZOffset());
    	Vec3d checkVec1 = new Vec3d(checkX, checkY, checkZ);	// TODO: this has already been calculated in wentAroundCorner, find a way to reuse that
    	// figure out what the new face is
    	// start by checking the four faces perpendicular to the current one
    	EnumSet<EnumFacing> faceSet = EnumSet.allOf(EnumFacing.class); 
    	faceSet.remove(this.face.getOpposite());
    	faceSet.remove(this.face);
    	for (EnumFacing face : faceSet)
    	{
    		// for each of these four faces, calculate a second offset and check if that is blocked
    		// TODO some code is reused here, consolidate that
    		Vec3d checkVec2 = checkVec1.add(-0.2D*face.getXOffset(), -0.2D*face.getYOffset(), -0.2D*face.getZOffset());
        	
        	// get the BlockPos for that position
        	BlockPos checkPos = new BlockPos(Math.floor(checkVec2.x), Math.floor(checkVec2.y), Math.floor(checkVec2.z));
        	
        	// check if the point is inside that block's collision AABB
        	AxisAlignedBB aabb = world.getBlockState(checkPos).getCollisionBoundingBox(world, checkPos);
        	if (aabb != null && aabb.offset(checkPos).contains(checkVec2))
        	{
            	IterableVec3d newVel = ParticleAnt.getRotatedVelocity(oldVel, oldNorm, new IterableVec3i(face.getDirectionVec()), true);
            	this.face = face;
            	this.motionX = newVel.x;
            	this.motionY = newVel.y;
            	this.motionZ = newVel.z;
            	this.cornerbuffer = ParticleAnt.CORNER_BUFFER_RESET_TICKS;
        	}
        	// if all four faces fail just ignore it
    	}
    }
    
    
    /**
     * Ants walk on walls. Ant particles need to know how to handle walking around a block's corner.
     * This problem is broken down into old velocity, old "floor" face's normal vector, new velocity, new floor's normal vector
     * @param oldVel Velocity before turning
     * @param oldNorm Normal vector of current floor before turning (elements should be -1, 0, or 1)
     * @param newNorm Surface normal of new floor after turning
     * @param convexEdge True if going around the outside of an edge (i.e. onto the same block), false if traveling to a different block
     * @return IterableVec3D containing new x,y,z velocity after turning
     */
    public static IterableVec3d getRotatedVelocity(IterableVec3d oldVel, IterableVec3i oldNorm, IterableVec3i newNorm, boolean convexEdge)
    {
    	double[] outValues = new double[3];
    	int edgeFactor = convexEdge ? -1 : 1;
    	
    	//for the x, y, and z axes of the velocity
    	for (int i=0; i<3; i++)
    	{
    		// if the new surface's normal is along this axis, velocity along that axis is 0
    		if (newNorm.getValueOnAxis(i) != 0)
    		{
    			outValues[i] = 0;
    		}
    		else
    		{	
    			//otherwise, old normal's value on this axis determines the velocity
    			int oldNormSubValue = oldNorm.getValueOnAxis(i);
    			if (oldNormSubValue == 0)
    			{	// if that value is 0, velocity on this axis is preserved
    				outValues[i] = oldVel.getValueOnAxis(i);
    			}
    			else
    			{
    				outValues[i] = oldNormSubValue * edgeFactor * Math.abs(IterableVec3d.dotProduct(oldVel, newNorm));
    			}
    		}
    	}
    	
    	return new IterableVec3d(outValues);
    }
    
    // returns true if the ant went around a convex edge and needs to change direction
    private boolean wentAroundCorner(World world)
    {
    	//check the position 0.1 units inside the "floor" the ant is traversing
    	double checkX = this.posX - 0.1D*(this.face.getXOffset());
    	double checkY = this.posY - 0.1D*(this.face.getYOffset());
    	double checkZ = this.posZ - 0.1D*(this.face.getZOffset());
    	
    	// get the BlockPos for that position
    	BlockPos checkPos = new BlockPos(Math.floor(checkX), Math.floor(checkY), Math.floor(checkZ));
    	
    	// check if the point is inside that block's collision AABB; if not (or AABB is null), return true
    	AxisAlignedBB aabb = world.getBlockState(checkPos).getCollisionBoundingBox(world, checkPos);
    	return (aabb == null) || (!aabb.offset(checkPos).contains(new Vec3d(checkX, checkY, checkZ)));
    }
}
