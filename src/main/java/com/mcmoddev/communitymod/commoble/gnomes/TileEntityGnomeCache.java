package com.mcmoddev.communitymod.commoble.gnomes;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;

import com.mcmoddev.communitymod.commoble.gnomes.ai.job.Job;
import com.mcmoddev.communitymod.commoble.gnomes.ai.job.JobChestReturn;
import com.mcmoddev.communitymod.commoble.gnomes.ai.job.JobChestSteal;
import com.mcmoddev.communitymod.commoble.gnomes.ai.job.JobPanicTo;
import com.mcmoddev.communitymod.commoble.gnomes.ai.job.JobSetBlock;
import com.mcmoddev.communitymod.commoble.gnomes.util.BlockPosAndBlock;
import com.mcmoddev.communitymod.commoble.gnomes.util.EnumAssignType;
import com.mcmoddev.communitymod.commoble.gnomes.util.GnomeAssignment;
import com.mcmoddev.communitymod.commoble.gnomes.util.MobbableBlock;
import com.mcmoddev.communitymod.commoble.gnomes.util.WorldHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

//@SuppressWarnings("rawtypes")
public class TileEntityGnomeCache extends TileEntityGnode
{
	// the gnome that lives here
	public EntityGnomeWood denizen;
	public int target_chest_iter;
	private int timeout;
	
	// holds 4 locations for stashing chests
	public ArrayDeque<BlockPos> chestReady = new ArrayDeque<BlockPos>(4);
	public ArrayDeque<BlockPos> chestOccupied = new ArrayDeque<BlockPos>(4);
	private BlockPos nearestChest;
	private double nearestChestDist;
	
	public TileEntityGnomeCache()
	{
		super();
		this.denizen = null;
		this.target_chest_iter = 0;
		this.timeout = 0;
		this.nearestChest = null;
		this.resetChestTracker();
	}
	
	public void resetChestTracker()
	{
		this.setChestTracker(null, Double.POSITIVE_INFINITY);
	}
	
	private void setChestTracker(BlockPos loc, double dist)
	{
		this.nearestChest = loc;
		this.nearestChestDist = dist;
	}
	
	//public Class getGnomeType()
	{
		//return EntityGnomeWood.class;
	}
	
	@Override
	public void update()
	{
		// if this tile is associated with a gnome
		if (this.denizen != null)
		{
			super.update();
			if (this.chestReady.size() > 0)
			{
				BlockPos loc = this.chestReady.poll();
				if (this.world.getBlockState(loc).getBlock() != Blocks.AIR)
				{
					this.chestOccupied.add(loc);
				}
				else
				{
					this.chestReady.add(loc);
				}
			}
			if (this.chestOccupied.size() > 0)
			{
				BlockPos loc = this.chestOccupied.poll();
				if (this.world.getBlockState(loc).getBlock() != Blocks.AIR)
				{
					this.chestOccupied.add(loc);
				}
				else
				{
					this.chestReady.add(loc);
				}
			}
			BlockPos loc = this.findTargetChest();
			if (loc != null)
			{
				// if a chest was found, check the distance
				// if the distance is closer than the closest known chest,
				// 		but not too close, mark it
				double dist = MathHelper.sqrt(this.getDistanceSq(loc.getX(), loc.getY(), loc.getZ()));
				if (dist+1.0D < this.nearestChestDist && dist > 5.0D)
				{
					this.setChestTracker(loc, dist);
				}
			}
		}
		else	// if no gnome, wait a while and am become dirt
		{
			this.timeout++;
			if (this.timeout > 500)
			{
				this.selfDestruct();
			}
		}
	}
	
	/**
	 * Called when the corresponding block is broken
	 */
	public void onDestroy()
	{
		if (this.denizen != null)
		{
			this.denizen.onHomeDestroyed();
		}
	}
	
	/**
	 * Wood gnomes are singular, not communal
	 * Don't get the nearest gnome, just get this gnode's denizen
	 */
	/*@Override
	public boolean attemptToAssignLocator(GnomeAssignment assign)
	{
		if (this.denizen != null)
		{
			return this.denizen.attemptToAcceptAssignment(assign);
		}
		else
		{
			return false;
		}
	}*/
	
	/*@Override
	public boolean attemptToDelegateJob()
	{
		IntLoc loc = this.chestlocs.poll();
		this.chestlocs.add(loc);
		// check if gnome can place a chest here
		if (MobbableBlock.isSoftBlock(this.worldObj.getBlockId(loc.x, loc.y, loc.z), this.worldObj))
		{
			if (this.denizen != null)
			{
				IntLoc targetloc = this.findTargetChest();
				if (targetloc != null && this.denizen.attemptToAcceptJob(new JobChestSteal(this.denizen, targetloc)))
				{
					return true;
				}
			}
		}
		return false;
	}*/
	
	public Job generateJob(EntityGnome gnome)
	{
		if (this.denizen.panic)
		{
			return new JobPanicTo(this.denizen, this.pos.down(2), 1.5D);
		}
		if (this.assignmentQueue.size() > 0)
		{
			if (this.denizen.getRNG().nextInt(5) == 0)
			{
				return new JobSetBlock(this.denizen, this.assignmentQueue.poll());
			}
			else
			{
				return null;
			}
		}
		else if (this.chestReady.size() > 0)
		{
			if (this.denizen.getCarried().getBlock() == Blocks.AIR) // no chest held
			{
				if (this.denizen.getRNG().nextInt(5) == 0)
				{
					BlockPos targetloc = this.nearestChest;
					if (targetloc != null)
					{
						return new JobChestSteal(this.denizen, targetloc);
					}
					else
					{
						return null;
					}
				}
				else
				{
					return null;
				}
			}
			else if (this.denizen.getCarried().getBlock() == Blocks.CHEST)
			{
				if (this.denizen.getRNG().nextInt(5) == 0)
				{
					BlockPos targetloc = this.pos.down(2);
					return new JobChestReturn(this.denizen, targetloc);
				}
				else
				{
					return null;
				}
			}
			else	// carrying something other than chest
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Iterates over the list of loaded tile entities once per tick to find a chest
	 * Returns a position of the chest if a chest is found with at least one item in it
	 * @return
	 */
	public BlockPos findTargetChest()
	{
		List<TileEntity> list = this.world.loadedTileEntityList;
		if (this.target_chest_iter >= list.size())
		{
			this.target_chest_iter = 0;
		}
		TileEntity potential_entity = (TileEntity) list.get(this.target_chest_iter);
		BlockPos tepos = potential_entity.getPos();
		this.target_chest_iter++;
		if (potential_entity != null && this.world.getBlockState(tepos).getBlock() == Blocks.CHEST)
		{
			// TODO maybe put a try/catch here? (life should go on)
			TileEntityChest chestent = (TileEntityChest)potential_entity;
			for (int i = 0; i < chestent.getSizeInventory(); i++)
			{
				if (chestent.getStackInSlot(i) != null)
				{
					return tepos;
				}
			}
		}
		return null;
	}
	
	/**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        
    }

    /**
     * Writes a tile entity to NBT.
     */
    public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        return super.writeToNBT(par1NBTTagCompound);
    }
    
    @Override
    public void selfDestruct()
    {
    	this.world.setBlockState(this.pos, Blocks.DIRT.getDefaultState());
    }
    
    protected void processBlueprintLoc(BlockPosAndBlock loc, Block oldblock)
    {
    	Block normalblock = oldblock;
    	// normalize blocks
    	if (normalblock == Blocks.GRASS)
    	{
    		normalblock = Blocks.DIRT;
    	}
    	if (normalblock != loc.block)
    	{	// block mismatch
    		if (isBlockUnsafe(normalblock))
    		{	// avoid conflicts with nearby hovels by selfdestructing
    			this.selfDestruct();
    		}
    		else if (MobbableBlock.isSoftBlock(oldblock, this.world))
    		{
        		if (loc.block == Blocks.AIR) // if old block is diggable and new block is air, destroy
        		{
        			this.assignmentQueue.add(new GnomeAssignment(loc, oldblock, EnumAssignType.DESTROY));
        		}
        		else	// if old block is diggable and new block is solid, alter
        		{
    				this.assignmentQueue.add(new GnomeAssignment(loc, oldblock, EnumAssignType.ALTER));
        		}
    		}
    		else if (WorldHelper.isAirLikeBlock(this.world, loc.pos))
			{	// if old block is air-like, create
				this.assignmentQueue.add(new GnomeAssignment(loc, oldblock, EnumAssignType.CREATE));
			}
    	}
    	else
    	{	// no mismatch
    		this.blueprint.add(loc);
    	}
    	
    }
    
	public static boolean isBlockUnsafe(Block block)
    {
    	if (MobbableBlock.isUnsafeBlock(block))
    	{
    		return true;
    	}
    	else if (block == SubmodGnomes.gnome_cache)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    /**
     * Creates a blueprint of dirt and air blocks to shape into a gnome hovel
     */
	@Override
	protected void buildBlueprint()
	{
		if (this.world.isRemote)
		{
			return;
		}
		Random rand = this.buildrand;
		int xstart = this.pos.getX();
		int ystart = this.pos.getY();
		int zstart = this.pos.getZ();
		int orientation = rand.nextInt(4);
		int doorhole_x = ((orientation%2) == 0) ? 0 : (orientation-2)*2;
		int doorhole_z = ((orientation%2) == 1) ? 0 : 2 - (orientation*2);
		
		for (int i = 0; i < 4; i++)
		{
			int xoff = ((i/2) * 2) - 1;
			int zoff = ((i%2) * 2) - 1;
			this.chestReady.add(new BlockPos(xstart + xoff, ystart-2, zstart + zoff));
		}

		
		
		// for each side, add decorator dirts
		for (int side = 0; side < 3; side++)
		{
			int xoff = ((side%2) == 0) ? 0 : (side-2)*3;
			int zoff = ((side%2) == 1) ? 0 : 3 - (side*3);
			
			if (side%2 == 0)	// x = 0, z = 3 or -3
			{
				for (int iter = -2; iter <= 2; iter++)
				{
					this.blueprint.add(new BlockPosAndBlock(xstart + xoff + iter, ystart -2, zstart + zoff, Blocks.DIRT));
					if (!(xoff + iter == doorhole_x && zoff + (zoff < 0 ? 1 : -1) == doorhole_z))
					{
						if (this.buildrand.nextInt(1+(iter*iter)) == 0)
						{
							this.blueprint.add(new BlockPosAndBlock(xstart + xoff + iter, ystart-1, zstart + zoff, Blocks.DIRT));
							if (this.buildrand.nextInt(2) == 0)
							{
								this.blueprint.add(new BlockPosAndBlock(xstart + xoff + iter, ystart, zstart + zoff, Blocks.DIRT));
							}
						}
					}
					else
					{
						int zplus = (zoff > 0 ? 1 : -1);	// z/3, 1 or -1
						int z_incr = 0;
						for (int i = 0; i < 3; i++)	// Make room for an entrance stairway
						{	// TODO maybe make the entrance indefinitely long if necessary
							this.blueprint.add(new BlockPosAndBlock(xstart+xoff+iter, ystart+i-1, zstart + zoff+z_incr, Blocks.AIR));
							this.blueprint.add(new BlockPosAndBlock(xstart+xoff+iter, ystart+i, zstart + zoff+z_incr, Blocks.AIR));
							z_incr += zplus;
						}
						
					}
				}
			}
			else	// z = 0, x = 3 or -3
			{
				for (int iter = -2; iter <= 2; iter++)
				{
					this.blueprint.add(new BlockPosAndBlock(xstart + xoff, ystart -2, zstart + zoff + iter, Blocks.DIRT));
					if(!(xoff + (xoff < 0 ? 1 : -1) == doorhole_x && (zoff + iter == doorhole_z)))
					{
						if (this.buildrand.nextInt(1+(iter*iter)) == 0)
						{
							this.blueprint.add(new BlockPosAndBlock(xstart + xoff, ystart-1, zstart + zoff + iter, Blocks.DIRT));
							if (this.buildrand.nextInt(2) == 0)
							{
								this.blueprint.add(new BlockPosAndBlock(xstart + xoff, ystart, zstart + zoff + iter, Blocks.DIRT));
							}
						}
					}
					else	// doorhole place, dig stairs
					{
						int xplus = (xoff > 0 ? 1 : -1);	// x/3. 1 or -1
						int x_incr = 0;
						for (int i = 0; i < 3; i++)
						{
							this.blueprint.add(new BlockPosAndBlock(xstart+xoff+x_incr, ystart+i-1, zstart + zoff+iter, Blocks.AIR));
							this.blueprint.add(new BlockPosAndBlock(xstart+xoff+x_incr, ystart+i, zstart + zoff+iter, Blocks.AIR));
							x_incr += xplus;
						}
					}
				}
			}
		}
		
		// walls
		for (int yoff = -2; yoff < 0; yoff++)
		{
			for (int xoff = -2; xoff <= 2; xoff++)
			{
				for (int zoff = -2; zoff <= 2; zoff++)
				{
					//this.blueprint.add(new BlockLocator(xstart + xoff, ystart + yoff, zstart + zoff, Block.dirt.blockID));
					// if (exterior AND not door) OR (door AND -2), make dirt, else make air
					boolean exterior = (((xoff<0) ? -xoff : xoff) == 2 || ((zoff<0) ? -zoff : zoff) == 2);
					boolean door = (xoff == doorhole_x && zoff == doorhole_z);
					if ((exterior && !door) || (door && yoff == -2))
					{	// walls = more dirt
						this.blueprint.add(new BlockPosAndBlock(xstart + xoff, ystart + yoff, zstart + zoff, Blocks.DIRT));
					}
					else
					{
						this.blueprint.add(new BlockPosAndBlock(xstart+xoff, ystart+yoff, zstart+zoff, Blocks.AIR));
					}
				}
			}
		}
		
		// ceiling
		for (int xoff = -2; xoff <= 2; xoff++)
		{
			for (int zoff = -2; zoff <= 2; zoff++)
			{
				// give the house a hat
				if (xoff*xoff < 4 && zoff*zoff < 4)
				{
					this.blueprint.add(new BlockPosAndBlock(xstart + xoff, ystart + 1, zstart + zoff, Blocks.DIRT));
				}
				// DON'T put a blueprint loc at the tile entity
					// or it'll erase itself
				if (zoff == 0 && xoff == 0)
				{
					continue;
				}
				if (zoff != doorhole_z || xoff != doorhole_x)
				{
					this.blueprint.add(new BlockPosAndBlock(xstart + xoff, ystart, zstart + zoff, Blocks.DIRT));
				}
				else
				{
					this.blueprint.add(new BlockPosAndBlock(xstart + xoff, ystart, zstart + zoff, Blocks.AIR));
				}
				if (xoff*xoff < 4 && zoff*zoff < 4)
				{
					this.blueprint.add(new BlockPosAndBlock(xstart + xoff, ystart + 1, zstart + zoff, Blocks.DIRT));
				}
			}
		}
	}

	@Override
	public void onGnomeDeath()
	{
		this.denizen = null;
	}

	@Override
	public void addGnome(EntityGnome gnome)
	{
		this.denizen = (EntityGnomeWood)gnome;
	}
}
