package com.mcmoddev.communitymod.commoble.gnomes;

import java.util.LinkedList;

import com.google.common.base.Optional;
import com.mcmoddev.communitymod.commoble.gnomes.ai.job.Job;
import com.mcmoddev.communitymod.commoble.gnomes.util.GnomeAssignment;
import com.mcmoddev.communitymod.commoble.gnomes.util.MobbableBlock;
import com.mcmoddev.communitymod.commoble.gnomes.util.WorldHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class EntityGnome extends EntityCreature
{
	//public EntityGnode assignedGnode;
	//public boolean needsFreshGnode;	// whether a new Gnode needs to be created
	//public Vec3 gnodeVec;	// location of assigned Gnode
	//public GnomeAssignment assignment;	// the (x,y,z,id) assigned to this gnome
	public Job job;	// the gnome's current Job
	public boolean canMineFreely;
	private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK = EntityDataManager.<Optional<IBlockState>>createKey(EntityGnome.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	
	// job priority is as follows:
	// highest: jobStack -- used for sub-jobs of other jobs
	// medium: jobQueue	-- used for jobs assigned to the gnome from elsewhere, do this if nothing in the stack
	// lowest: request job from Gnode if no job is in the stack or the queue

	public LinkedList<Job> jobStack;	// use push() and poll()
	public LinkedList<Job> jobQueue;	// use add() and poll()
		// (null if no outstanding request)

	public EntityGnome(World par1World)
	{
		super(par1World);
        this.experienceValue = 5;
        //this.assignedGnode = null;
        //this.needsFreshGnode = false;
        //this.gnodeVec = null;
        //this.assignment = null;
        this.job = null;
        this.canMineFreely = false;
        
        this.jobStack = new LinkedList<Job>();
        this.jobQueue = new LinkedList<Job>();
	}
	
	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(CARRIED_BLOCK, Optional.<IBlockState>absent());
        //this.dataWatcher.addObject(carryDataId, new Byte((byte)Block.getIdFromBlock(Blocks.air)));
    }
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
    }
	
	
	
	/**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();

        super.onLivingUpdate();
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
    }
    
    /**
     * Attempt to accept a BlockLocator assignment from a Gnode.
     * 
     */
    /*public boolean attemptToAcceptAssignment(GnomeAssignment assign)
    {
    	if (this.assignment == null)
    	{
    		this.assignment = assign;
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }*/
    
    /*public boolean attemptToAcceptJob(Job job)
    {
    	if (this.job == null)
    	{
    		this.job = job;
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }*/
    
    /**
     * Checks the gnome's current job,
     * and generates a new one if no current job
     */
    public void updateJob()
    {
    	// if gnome has existing job, do nothing
    	if (this.job != null)
    	{
    		return;
    	}
    	else	// if gnome has no job, get new one
    	{
    		this.job = this.getNewJob();
    	}
    }
    
    /**
     * Returns a Job object, used by updateJob
     * to find a Job when the gnome has no existing job
     * Priority of job source is:
     * 	-jobStack (highest)
     * 	-jobQueue
     * 	-Request Job from Gnode, if Gnode exists
     * 	-Null (allow for subclass to determine job)
     * @return Job
     */
    protected Job getNewJob()
    {
    	if (this.jobStack.size() > 0)
    	{
    		return this.jobStack.poll();
    	}
    	else if (this.jobQueue.size() > 0)
    	{
    		return this.jobQueue.poll();
    	}
    	else
    	{
    		return null;
    	}
    }
    
    /**
     * Called when a Job ends, regardless of whether it was successfully completed
     * @param assign The job's assignment data
     * @param finished Whether the job site was reached
     * @param mismatch if the job site was reached but failed due to block mismatch, this will be true, false in any other case
     * @param isGnodeAssignment true if the job was originally assigned by a Gnode
     */
    public abstract void finishSetBlock(GnomeAssignment assign, boolean finished, boolean mismatch, boolean isGnodeAssignment);
    
    /**
     * This function is called when the gnome successfully finishes a JobChestFill. Coordinates are the
     * coordinates of the chest, stack is the ItemStack in the chest that was added to 
     * If chest is full, success is FALSE and contents do not change
     */
    public void onPlaceItemInChest(BlockPos pos, ItemStack stack, boolean success)
    {
    	return;
    }
    
    //public abstract boolean canAlterBlock(int id);

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    /*public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return 0.5F - this.worldObj.getLightBrightness(par1, par2, par3);
    }/*

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SubmodGnomes.GNOME_SPEAK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return SubmodGnomes.GNOME_SPEAK;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SubmodGnomes.GNOME_DEATH;
    }
    
    /*@Override
    public void playLivingSound()
    {
        String s = this.getLivingSound();

        if (s != null)
        {
            this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
        }
    }*/

    /**
     * Plays step sound at given position for the entity
     */
    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }
    
    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        
    }

    protected int getExperiencePoints(EntityPlayer par1EntityPlayer)
    {
        return 1 + this.world.rand.nextInt(3);
    }

    /**
     * Sets this gnome's held block state
     */
    
    public void setCarriedToAir()
    {
    	this.setCarriedState(Blocks.AIR.getDefaultState());
    }
	
	public void setCarriedState(IBlockState state)
	{
        this.dataManager.set(CARRIED_BLOCK, Optional.fromNullable(state));
		//this.dataWatcher.updateObject(carryDataId, Byte.valueOf((byte)(Block.getIdFromBlock(block) & 0xFF)));
	}
	
	public IBlockState getCarried()
    {
		IBlockState state = (IBlockState)((Optional)this.dataManager.get(CARRIED_BLOCK)).orNull();
		if (state == null)
		{
			this.setCarriedToAir();
			return Blocks.AIR.getDefaultState();
		}
        return state;
        //return (Block.getBlockById(this.dataWatcher.getWatchableObjectByte(carryDataId)));
    }
	
	@Override
	public boolean canDespawn()
	{
		return false;
	}
	
	@Override
	public EntityLivingBase getAttackTarget()
	{
		// this is mostly used for entityaifleefrom
		if (this.isBurning())
		{
			return this;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);

		// Check NBT for block id+data for carried block
		
        IBlockState state;

        if (nbt.hasKey("carried", 8))
        {
            state = Block.getBlockFromName(nbt.getString("carried")).getStateFromMeta(nbt.getShort("carriedData") & 65535);
        }
        else
        {
            state = Block.getBlockById(nbt.getShort("carried")).getStateFromMeta(nbt.getShort("carriedData") & 65535);
        }

        if (state == null || state.getBlock() == null || state.getMaterial() == Material.AIR)
        {
            state = null;
        }

        this.setCarriedState(state);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		
		// Write id+data of carried block to NBT

        IBlockState state = this.getCarried();

        if (state != null)
        {
            nbt.setShort("carried", (short)Block.getIdFromBlock(state.getBlock()));
            nbt.setShort("carriedData", (short)state.getBlock().getMetaFromState(state));
        }
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance diff, IEntityLivingData data)
	{
		data = super.onInitialSpawn(diff, data);
		
		this.setCarriedToAir();
		
		return data;
	}
	
	public boolean canDigBlock(BlockPos pos)
	{
		return (WorldHelper.isAirLikeBlock(this.world, pos) ||
				MobbableBlock.isSoftBlock(WorldHelper.getBlock(this.world, pos), this.world));
	}
}
