package com.mcmoddev.communitymod.explodingchickens;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityExplodingChicken extends EntityAnimal
{
	private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
	private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityExplodingChicken.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> POWERED = EntityDataManager.<Boolean>createKey(EntityExplodingChicken.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.<Boolean>createKey(EntityExplodingChicken.class, DataSerializers.BOOLEAN);
    
    private int lastActiveTime;
    /** The amount of time since the creeper was close enough to the player to ignite */
    private int timeSinceIgnited;
    private int fuseTime = 30;
    /** Explosion radius for this creeper. */
    private int explosionRadius = 2;
    


    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    /** The time until the next egg is spawned. */
    public int timeUntilNextEgg;
    public boolean chickenJockey;
    
	public EntityExplodingChicken(World worldIn)
	{
		super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	@Override
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITemptAndExplode(this, 1.0D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(STATE, Integer.valueOf(-1));
        this.dataManager.register(POWERED, Boolean.valueOf(false));
        this.dataManager.register(IGNITED, Boolean.valueOf(false));
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getCreeperState()
    {
        return ((Integer)this.dataManager.get(STATE)).intValue();
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setCreeperState(int state)
    {
        this.dataManager.set(STATE, Integer.valueOf(state));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.isEntityAlive())
        {
            this.lastActiveTime = this.timeSinceIgnited;

            if (this.hasIgnited())
            {
                this.setCreeperState(1);
            }

            int i = this.getCreeperState();

            if (i > 0 && this.timeSinceIgnited == 0)
            {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;

            if (this.timeSinceIgnited < 0)
            {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime)
            {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }

        super.onUpdate();
    }

    public boolean hasIgnited()
    {
        return ((Boolean)this.dataManager.get(IGNITED)).booleanValue();
    }

    public void ignite()
    {
        this.dataManager.set(IGNITED, Boolean.valueOf(true));
    }

    /**
     * Creates an explosion as determined by this creeper's power and explosion radius.
     */
    private void explode()
    {
        if (!this.world.isRemote)
        {
            this.playSound(SoundEvents.ENTITY_CHICKEN_DEATH, 2.0F, 1F);
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this);

        	for (int i=0; i<30; i++)
        	{
                EntityEgg entityegg = new EntityEgg(world, this);
                entityegg.motionY = this.world.rand.nextFloat() * .5F;
                entityegg.motionX = (this.world.rand.nextFloat() - 0.5F) * 1F;
                entityegg.motionZ = (this.world.rand.nextFloat() - 0.5F) * 1F;
                world.spawnEntity(entityegg);
        	}
        	this.dead = true;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, flag);
            this.setDead();
        }
    }

    /**
     * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
     */
    @SideOnly(Side.CLIENT)
    public float getCreeperFlashIntensity(float p_70831_1_)
    {
        return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (float)(this.fuseTime - 2);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (((Boolean)this.dataManager.get(POWERED)).booleanValue())
        {
            compound.setBoolean("powered", true);
        }

        compound.setShort("Fuse", (short)this.fuseTime);
        compound.setByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.setBoolean("ignited", this.hasIgnited());
        compound.setBoolean("IsChickenJockey", this.chickenJockey);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.dataManager.set(POWERED, Boolean.valueOf(compound.getBoolean("powered")));

        if (compound.hasKey("Fuse", 99))
        {
            this.fuseTime = compound.getShort("Fuse");
        }

        if (compound.hasKey("ExplosionRadius", 99))
        {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }

        if (compound.getBoolean("ignited"))
        {
            this.ignite();
        }
        this.chickenJockey = compound.getBoolean("IsChickenJockey");

        if (compound.hasKey("EggLayTime"))
        {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
    }

    public float getEyeHeight()
    {
        return this.height;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);

        if (!this.onGround && this.wingRotDelta < 1.0F)
        {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.wingRotation += this.wingRotDelta * 2.0F;

        if (!this.world.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0)
        {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Items.EGG, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_CHICKEN;
    }

    public EntityChicken createChild(EntityAgeable ageable)
    {
        return new EntityChicken(this.world);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return TEMPTATION_ITEMS.contains(stack.getItem());
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
        return this.isChickenJockey() ? 10 : super.getExperiencePoints(player);
    }

    public static void registerFixesChicken(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityChicken.class);
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return this.isChickenJockey() && !this.isBeingRidden();
    }

    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);
        float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
        float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
        passenger.setPosition(this.posX + (double)(0.1F * f), this.posY + (double)(this.height * 0.5F) + passenger.getYOffset() + 0.0D, this.posZ - (double)(0.1F * f1));

        if (passenger instanceof EntityLivingBase)
        {
            ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
        }
    }

    /**
     * Determines if this chicken is a jokey with a zombie riding it.
     */
    public boolean isChickenJockey()
    {
        return this.chickenJockey;
    }

    /**
     * Sets whether this chicken is a jockey or not.
     */
    public void setChickenJockey(boolean jockey)
    {
        this.chickenJockey = jockey;
    }
}
