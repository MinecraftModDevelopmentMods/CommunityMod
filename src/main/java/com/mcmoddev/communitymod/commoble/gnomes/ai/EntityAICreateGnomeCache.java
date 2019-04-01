package com.mcmoddev.communitymod.commoble.gnomes.ai;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnomeWood;
import com.mcmoddev.communitymod.commoble.gnomes.SubmodGnomes;
import com.mcmoddev.communitymod.commoble.gnomes.TileEntityGnomeCache;
import com.mcmoddev.communitymod.commoble.gnomes.ai.job.Job;
import com.mcmoddev.communitymod.commoble.gnomes.util.WorldHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAICreateGnomeCache extends EntityAIBase
{
	private EntityGnomeWood gnome;
	private BlockPos pos;
	private double speed;
	private int assigntimer;
	
	public EntityAICreateGnomeCache(EntityGnomeWood gnome, double speed)
	{
		this.gnome = gnome;
		this.speed = speed;
		this.setMutexBits(1);
		this.assigntimer = 0;
	}

	@Override
	public boolean shouldExecute()
	{
		if ((this.gnome.gnode == null) && this.assigntimer <= 0)
		{
			this.assigntimer = 5;
			if (this.gnome.homeloc != null)
			{
				BlockPos loc = this.gnome.homeloc;
				this.gnome.gnode = (TileEntityGnomeCache)this.gnome.world.getTileEntity(loc);
				this.gnome.homeloc = null;
				if (this.gnome.gnode != null)
				{
					this.gnome.gnode.addGnome(this.gnome);
				}
				return false;
			}
			// needs new home
			Vec3d vec = RandomPositionGenerator.findRandomTarget(this.gnome, 16, 7);
			if (vec == null)
			{
				return false;
			}

			BlockPos tempPos = new BlockPos(vec);
			
			if (tempPos.getY() < 2)
			{
				tempPos = new BlockPos(tempPos.getX(), 2, tempPos.getY());
			}
			
			World world = this.gnome.world;

			tempPos = Job.groundify(world, tempPos);	// gets an air-like block above ground
			
			Block block = world.getBlockState(tempPos.down()).getBlock();
			
			if (block != Blocks.DIRT && block != Blocks.GRASS)
			{
				//System.out.println("Block just below position " + tempPos.getX() + "," + tempPos.getY() + "," + tempPos.getZ()
				//	+ " is not dirt/grass");
				return false;
			}

			this.pos = tempPos;
			
			this.assigntimer = 10 + this.gnome.getRNG().nextInt(10);
			return true;
		}
		else
		{
			if (this.assigntimer > 0)
				this.assigntimer--;
			return false;
		}
	}

	/**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
	@Override
    public boolean shouldContinueExecuting()
    {
		if (this.gnome.getDistanceSq(this.pos) < 4.0D) // equiv. to dist < 2D
    	{
    		TileEntityGnomeCache cache = this.placeGnomeCache();
    		this.gnome.gnode = cache;
    		return false;
    	}
        return !this.gnome.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.gnome.getNavigator().tryMoveToXYZ(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.speed);
    }
    
    
	protected TileEntityGnomeCache placeGnomeCache()
    {
    	if (!this.gnome.world.isRemote && WorldHelper.isAirLikeBlock(this.gnome.world, this.pos))
    	{
    		this.assigntimer = 5;
    		this.gnome.world.setBlockState(this.pos, SubmodGnomes.gnome_cache.getDefaultState());
    		TileEntity ent = this.gnome.world.getTileEntity(this.pos);
    		if (ent != null && ent instanceof TileEntityGnomeCache)
    		{
    			((TileEntityGnomeCache)ent).denizen = this.gnome;
    			return (TileEntityGnomeCache)ent;
    		}
    		return null;
    	}
    	else
    	{
        	return null;
    	}
    }
}
