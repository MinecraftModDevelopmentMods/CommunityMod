package com.mcmoddev.communitymod.commoble.gnomes;

import com.mcmoddev.communitymod.commoble.gnomes.ai.EntityAICreateGnomeCache;
import com.mcmoddev.communitymod.commoble.gnomes.ai.EntityAIPerformJob;
import com.mcmoddev.communitymod.commoble.gnomes.ai.job.Job;
import com.mcmoddev.communitymod.commoble.gnomes.ai.job.JobPanicTo;
import com.mcmoddev.communitymod.commoble.gnomes.util.GnomeAssignment;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class EntityGnomeWood extends EntityGnome
{
	public TileEntityGnomeCache gnode;
	public BlockPos homeloc = null;
	public boolean panic;
	public static final int INVENTORY_MAX = 27;
	public ItemStack[] inventory = new ItemStack[INVENTORY_MAX];
	//public boolean hasChest;

	public EntityGnomeWood(World par1World)
	{
		super(par1World);

        this.setSize(0.6F, 0.8F);
        //this.getNavigator().setAvoidsWater(true);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        //this.tasks.addTask(1, new EntityAIPanicToHome(this, 1.5D));
        //this.tasks.addTask(1, new EntityAIFleeFrom(this, 1.25D));
        //this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.5D));
        this.tasks.addTask(3, new EntityAICreateGnomeCache(this, 1.0));
        //this.tasks.addTask(4, new EntityAIPerformGnomeAssignment(this, 1.0));
        this.tasks.addTask(5, new EntityAIPerformJob(this));
        this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
        //this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
       // this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        //this.tasks.addTask(9, new EntityAILookIdle(this));
        
        this.gnode = null;
        this.panic = false;
        //this.hasChest = false;
	}
	
	public void finishSetBlock(GnomeAssignment assign, boolean finished, boolean mismatch, boolean isGnodeAssignment)
	{
		if (this.gnode != null && isGnodeAssignment)
		{
			this.gnode.endAssignment(assign, finished, mismatch);
		}
	}

	@Override
	public Job getNewJob()
	{
		if (this.gnode != null)
		{
			return this.gnode.generateJob(this);
		}
		else if (this.panic)	// no home, panicking
		{
			Vec3d vec = null;
			while (vec == null)
			{
				vec = RandomPositionGenerator.findRandomTarget(this, 32,8);
			}
			return new JobPanicTo(this, vec, 1.5D);
		}
		else	// no home, not panicking
		{
			// return new JobCreateGnomeCache
			// TODO replace hovel creation with a Job class
			return null;
		}
	}
	
	@Override
	// gnome jobs have their own path weight, prefer milling around the hovel if no job
	public float getBlockPathWeight(BlockPos pos)
	{
		if (this.job != null)
		{
			return this.job.getBlockPathWeight(pos);
		}
		if (this.gnode != null)
		{
			double distSQ = this.gnode.getPos().distanceSq(pos); // square of distance between gnode and gnome
			float weight = (float)(10000D - distSQ);	// equivalent to 100 - dist
			return (weight > 0F) ? weight : 0F;
		}
		return 0;
	}
	
	@Override
    public void onLivingUpdate()
    {
		if (this.world.getBlockState(new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ)).getBlock() == Blocks.DIRT)
        {
        	this.heal(0.1F);
        }

        super.onLivingUpdate();
    }

	/*@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		
		//this.assignedGnode = new TileEntityGnomeCache();
		
		return data;
	}*/
	
	/**
	 * Called when another entity attacks this gnome
	 */
	@Override
	public boolean attackEntityFrom(DamageSource damage, float f)
	{
		boolean superflag = super.attackEntityFrom(damage, f);
		if (superflag && !this.isDead && damage.getTrueSource() != null)
		{
			if (this.job != null)
			{
				this.job.finishJob(false);
				this.job = null;
			}
			if (this.gnode != null)
			{	// if home is closer than 4 tiles, gtfo
				if (this.getDistanceSq(this.gnode.getPos().down(2)) < 16.0D) // if distance < 4
				{
					this.gnode.selfDestruct();
				}
			}
			this.panic = true;
		}
		return superflag;
	}
	
	@Override
	public void onDeath(DamageSource damage)
	{
		if (this.getCarried().getBlock() == Blocks.CHEST)
		{
			this.dropChest();
		}
		if (this.gnode != null)
		{
			//this.gnode.denizen = null;
			this.gnode.onGnomeDeath();
			this.gnode.selfDestruct();
		}
	}
	
	/**
	 * Find a nearby open surface and drop the carried items in a chest there
	 */
	private void dropChest()
	{
		int x = (int) Math.floor(this.posX);
		int y = (int) Math.floor(this.posY);
		int z = (int) Math.floor(this.posZ);
		
		BlockPos tempPos = new BlockPos(x,y,z);
		
		// TODO rework this to use Groundify, and just dump items on the ground if too many fails
		
		while (this.world.getBlockState(tempPos).getBlock() != Blocks.AIR)
		{
			if (tempPos.getY() < this.world.getActualHeight()-1)
			{
				tempPos = tempPos.up();
			}
			else
			{
				tempPos = new BlockPos(
						tempPos.getX() + (this.rand.nextInt(5) - 10),
						y,
						tempPos.getZ() + (this.rand.nextInt(5) - 10));
			}
		}
		y = tempPos.getY();
		while(this.world.getBlockState(tempPos).getBlock() == Blocks.AIR)
		{
			if (tempPos.getY() > 0)
			{
				tempPos = tempPos.down();
			}
			else
			{
				tempPos = new BlockPos(
						tempPos.getX() + (this.rand.nextInt(5) - 10),
						y,
						tempPos.getZ() + (this.rand.nextInt(5) - 10));
			}
		} 
		IBlockState state = Blocks.CHEST.getDefaultState();
		this.world.setBlockState(tempPos, Blocks.CHEST.getDefaultState());
		ItemStack fakeStack = new ItemStack(Blocks.AIR);
		// in chest.onBlockPlacedBy, the itemstack is only used for checking the custom name
		// but it can't be null
		Blocks.CHEST.onBlockPlacedBy(this.world, tempPos, state, this, fakeStack);
		TileEntityChest te = (TileEntityChest)this.world.getTileEntity(tempPos);
		for (int i = 0; i<27; i++)
		{
			te.setInventorySlotContents(i, this.inventory[i]);
			this.inventory[i] = ItemStack.EMPTY;
		}
		this.setCarriedToAir();
	}
	
	public void onHomeDestroyed()
	{
		this.homeloc = null;
		this.gnode = null;
		this.job = null;	// TODO will need to handle placing chests if in mid-job
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		
		// if gnome has a home
		if (nbt.getBoolean("hasHome"))
		{
			int x = nbt.getInteger("homeX");
			int y = nbt.getInteger("homeY");
			int z = nbt.getInteger("homeZ");
			//this.home = (TileEntityGnomeCache)this.worldObj.getBlockTileEntity(x, y, z);
			this.homeloc = new BlockPos(x,y,z);
		}
		else
		{
			this.gnode = null;
		}
		

		// read inventory data
        NBTTagList nbttaglist = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        this.inventory = new ItemStack[INVENTORY_MAX];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < INVENTORY_MAX)
            {
            	this.inventory[j] = new ItemStack(nbttagcompound);
            }
        }
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		if (this.gnode != null)
		{
			nbt.setBoolean("hasHome", true);
			BlockPos pos = this.gnode.getPos();
			nbt.setInteger("homeX", pos.getX());
			nbt.setInteger("homeY", pos.getY());
			nbt.setInteger("homeZ", pos.getZ());
		}
		else
		{
			nbt.setBoolean("hasHome", false);
		}
		

		// write inventory data
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
	}
	
	@Override
	public boolean canDespawn()
	{
		return false;
	}	
	


    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        return this.world.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
    }

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance diff, IEntityLivingData data)
	{
		data = super.onInitialSpawn(diff, data);
		//this.setCarried(Blocks.CHEST);
		//this.assignedGnode = new TileEntityGnomeCache();
		
		return data;
	}
}
