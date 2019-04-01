package com.mcmoddev.communitymod.commoble.gnomes;

import java.util.LinkedList;
import java.util.Random;

import com.mcmoddev.communitymod.commoble.gnomes.util.BlockPosAndBlock;
import com.mcmoddev.communitymod.commoble.gnomes.util.GnomeAssignment;
import com.mcmoddev.communitymod.commoble.gnomes.util.GnomeSorter;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public abstract class TileEntityGnode extends TileEntity implements ITickable
{
	/**
	 * The Blueprint is a list of locations and block IDs at those locations.
	 * The Gnode allows a gnome to build and maintain a structure around this tile
	 * by using this Blueprint.
	 * 
	 * On creation, the Gnode builds the blueprint. It periodically scans the
	 * blueprint and compares it with the actual blocks at those locations.
	 * If a mismatch is found, it adds that location to a set of locations that need
	 * updating. It periodically looks for nearby gnomes to assign that location
	 * to.
	 * 
	 */
	protected LinkedList<BlockPosAndBlock> blueprint;
	protected LinkedList<GnomeAssignment> assignmentQueue = new LinkedList<GnomeAssignment>();
	protected long buildseed;
	protected Random buildrand;
	protected final GnomeSorter sorter;
	
	public TileEntityGnode()
	{
		this.buildseed = 0;
		this.sorter = new GnomeSorter(this);
	}
	
	/**
     * Reads a tile entity from NBT.
     */
	@Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.buildseed = nbt.getLong("buildseed");
        //this.initialize(this.worldObj);
    }

    /**
     * Writes a tile entity to NBT.
     * @return 
     */
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagCompound compound = super.writeToNBT(nbt);
        nbt.setLong("buildseed", this.buildseed);
        return compound;
    }
    
    public void initialize(World world)
    {
    	// reads from NBT default to 0 if they haven't been set yet.
    	// buildseed = 0 -> very fresh gnode
    	if (this.buildseed == 0)
    	{
    		// buildseed is based on the world's seed and the tile's position
    		long seed1 = world.getSeed();
    		long seed2 = (long)(this.getPos().hashCode());
    		this.buildseed = seed1*seed2;
    		if (this.buildseed == 0)
    		{
    			this.buildseed = 1;
    		}
    	}
    	this.buildrand = new Random(buildseed);
		this.blueprint = new LinkedList<BlockPosAndBlock>();
    	this.buildBlueprint();
    }
    
    // called every tick for update stuff
    @Override
    public void update()
    {
    	if (!this.world.isRemote)
    	{
    		if (this.blueprint == null)
    		{
    			this.initialize(this.world);
    		}
        	if (this.blueprint.size() > 0)
        	{
        		BlockPosAndBlock bpb = this.blueprint.poll();
            	Block oldblock = this.getWorld().getBlockState(bpb.pos).getBlock();
            	this.processBlueprintLoc(bpb, oldblock);
        	}
        	if (this.assignmentQueue.size() > 0)
        	{
        		// scan assignments for errors
        		GnomeAssignment assignment = this.assignmentQueue.poll();
        		if (assignment.newblock == assignment.oldblock)
        		{
        			// if block assigned for breaking has already been broken, return it to the blueprint
        			this.blueprint.add(new BlockPosAndBlock(assignment.pos, assignment.newblock));
        		}
        		else
        		{
        			this.assignmentQueue.add(assignment);
        		}
        	}
    	}
    }


    /**
     * Attempts to assign a BlockLocator to the closest appropriate gnome
     * Returns TRUE if a gnome was found and the locator was passed to it
     * Returns FALSE if no gnome accepted the assignment
     * @param loc the BlockLocator (x,y,z,id) for this assignment
     */
	/*protected boolean attemptToAssignLocator(GnomeAssignment assign)
	{
		int radius = this.getRadius();
		List gnomelist = this.worldObj.getEntitiesWithinAABB(this.getGnomeType(), AxisAlignedBB.getAABBPool().getAABB(this.xCoord-radius, this.yCoord-radius, this.zCoord-radius, this.xCoord+radius, this.yCoord + radius, this.zCoord + radius));
        Collections.sort(gnomelist, this.sorter); // sort the list in order of closeness
		Iterator iter = gnomelist.iterator();
		
		while (iter.hasNext())
		{
			EntityGnome gnome = (EntityGnome)iter.next();
			if (gnome.attemptToAcceptAssignment(assign))
			{
				return true;
			}
		}
		return false;
	}*/
	
	/**
	 * Called by a gnome when the gnome end an assignment.
	 * Returns the BlockLocator to the blueprint list.
	 * (regardless of whether the gnome successfully completed it)
	 * @param finished = set to true to return to blueprint, false to return to assign queue
	 */
	public void endAssignment(GnomeAssignment assign, boolean finished, boolean mismatch)
	{
		if (finished)
		{
			this.blueprint.add(assign.getBlockPosAndNewBlock());
		}
		else
		{
			this.assignmentQueue.add(assign);
		}
	}
	
	/**
	 * Attempt to delegate a Job to the closest appropriate gnome
	 * Returns TRUE if a job was delegated
	 * returns FALSE if no gnomes were available
	 */
	//public abstract boolean attemptToDelegateJob();
    
    /**
     * Generate the gnode's blueprint. It should use the entity's
     * buildrand Random to perform this task, which gets initialized
     * with the appropriate seed just before this function is called.
     */
    protected abstract void buildBlueprint();
    
    /**
     * Returns a Class for the type of gnome this gnode
	 * is associated with
     * @return some sort of gnome class
     */
    //@SuppressWarnings("rawtypes")
	//protected abstract Class getGnomeType();
    
    /**
     * Generates a Job for a gnome.
     * Can return null if no job needs to be done.
     * @return Job
     */
    //public abstract Job generateJob(EntityGnome gnome);
    
    /**
     * Processes a blueprint location
     * Adds it to the assignment queue if necessary,
     * otherwise returns it to the blueprint
     * @loc The blocklocator from the blueprint
     * @oldid The existing block ID at that location
     */
    protected abstract void processBlueprintLoc(BlockPosAndBlock loc, Block oldblock);
    
    /**
     * Called when the block is broken
     */
    public abstract void onDestroy();
    
    /**
     * Called to remove this tileentity and turn the block into a normal block
     */
    public abstract void selfDestruct();
    
    /**
     * Called when a resident gnome dies
     */
    public abstract void onGnomeDeath();
    
    /**
     * Adds a gnome to this gnode's known resident gnomes
     */
    public abstract void addGnome(EntityGnome gnome);
    
    
    public int getRadius()
    {
    	return 16;
    }
    
    
}
