package com.mcmoddev.communitymod.its_meow.dabsquirrels;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDabSquirrel extends EntityAnimalWithTypes {

	private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntityDabSquirrel.class, DataSerializers.BYTE);

	private int climbTimeWithoutLog = 0;
	private float prevRearingAmount;
	private int gallopTime;

	public EntityDabSquirrel(World worldIn) {
		super(worldIn);
		this.setSize(5F, 5F);
		this.stepHeight = 4.0F;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIPanic(this, 0.72D));
		this.tasks.addTask(3, new EntityAIMate(this, 0.5D));
		this.tasks.addTask(4, new EntityAITempt(this, 0.5D, Items.WHEAT_SEEDS, false));
		this.tasks.addTask(5, new EntityAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 10F, 0.5D, 0.7D));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.5D));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CLIMBING, Byte.valueOf((byte) 0));
	}

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getTrueSource();
        return this.isBeingRidden() && entity != null && this.isRidingOrBeingRiddenBy(entity) ? false : super.attackEntityFrom(source, amount);
    }

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(this.world.isRemote && this.getRNG().nextInt(100) == 0) {
			this.spawnParticle();
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void spawnParticle() {
		Particle particle = new ParticleBasic(this.world, this.posX + Math.random(), this.posY + 4.5, this.posZ + Math.random(), 0.0F, 0.0F, 0.0F, DabSquirrels.dab, 10F);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		boolean flag = !itemstack.isEmpty();

		if (flag && itemstack.getItem() == Items.SPAWN_EGG) {
			return super.processInteract(player, hand);
		} else {
			if (!this.isChild()) {
				if (player.isSneaking()) {
					return false;
				}

				if (this.isBeingRidden()) {
					return super.processInteract(player, hand);
				}
			}

			if (this.isChild()) {
				return super.processInteract(player, hand);
			} else {
				this.mountTo(player);
				return true;
			}
		}
	}

	@Override
	public boolean canBePushed() {
		return !this.isBeingRidden();
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		if (distance > 1.0F) {
			this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.0F);
		}

		int i = MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);

		if (i > 0) {
			this.attackEntityFrom(DamageSource.FALL, i);

			if (this.isBeingRidden()) {
				for (Entity entity : this.getRecursivePassengers()) {
					entity.attackEntityFrom(DamageSource.FALL, i);
				}
			}
			BlockPos pos = new BlockPos(this.posX, this.posY - 0.2D - this.prevRotationYaw, this.posZ);
			IBlockState iblockstate = this.world.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (iblockstate.getMaterial() != Material.AIR && !this.isSilent()) {
				SoundType soundtype = block.getSoundType(block.getDefaultState(), this.world, pos, this);
				this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), this.getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
			}
		}
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		if (!blockIn.getDefaultState().getMaterial().isLiquid()) {
			SoundType soundtype = blockIn.getSoundType(blockIn.getDefaultState(), this.world, pos, this);

			if (this.world.getBlockState(pos.up()).getBlock() == Blocks.SNOW_LAYER) {
				soundtype = Blocks.SNOW_LAYER.getSoundType(Blocks.SNOW_LAYER.getDefaultState(), this.world, pos, this);
			}

			if (this.isBeingRidden()) {
				++this.gallopTime;

				if (this.gallopTime > 5 && this.gallopTime % 3 == 0) {
					this.playGallopSound(soundtype);
				} else if (this.gallopTime <= 5) {
					this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
				}
			} else if (soundtype == SoundType.WOOD) {
				this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
			} else {
				this.playSound(SoundEvents.ENTITY_HORSE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
			}
		}
	}

	protected void mountTo(EntityPlayer player) {
		player.rotationYaw = this.rotationYaw;
		player.rotationPitch = this.rotationPitch;

		if (!this.world.isRemote) {
			player.startRiding(this);
		}
	}

	@Override
	public void travel(float strafe, float vertical, float forward) {
		if (this.isBeingRidden() && this.canBeSteered()) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
			this.rotationYaw = entitylivingbase.rotationYaw;
			this.prevRotationYaw = this.rotationYaw;
			this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
			this.setRotation(this.rotationYaw, this.rotationPitch);
			this.renderYawOffset = this.rotationYaw;
			this.rotationYawHead = this.renderYawOffset;
			strafe = entitylivingbase.moveStrafing * 0.5F;
			forward = entitylivingbase.moveForward;

			if (forward <= 0.0F) {
				forward *= 0.25F;
				this.gallopTime = 0;
			}

			if (this.canPassengerSteer()) {
				this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
				super.travel(strafe, vertical, forward);
			} else if (entitylivingbase instanceof EntityPlayer) {
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}

			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d1 = this.posX - this.prevPosX;
			double d0 = this.posZ - this.prevPosZ;
			float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

			if (f2 > 1.0F) {
				f2 = 1.0F;
			}

			this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
			this.limbSwing += this.limbSwingAmount;
		} else {
			this.jumpMovementFactor = 0.02F;
			super.travel(strafe, vertical, forward);
		}
	}

	@Override
	public boolean canBeSteered() {
		return this.getControllingPassenger() instanceof EntityLivingBase;
	}


	@Override
	protected boolean isMovementBlocked() {
		return super.isMovementBlocked() && this.isBeingRidden() && true;
	}

	protected void playGallopSound(SoundType p_190680_1_) {
		this.playSound(SoundEvents.ENTITY_HORSE_GALLOP, p_190680_1_.getVolume() * 0.15F, p_190680_1_.getPitch());
	}
	
    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);

        if (passenger instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) passenger;
            this.renderYawOffset = entityliving.renderYawOffset;
        }

        if (this.prevRearingAmount > 0.0F) {
            float f3 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float f1 = 0.7F * this.prevRearingAmount;
            float f2 = 0.15F * this.prevRearingAmount;
            passenger.setPosition(this.posX + f1 * f3, this.posY + this.getMountedYOffset() + passenger.getYOffset() + f2 - 2F, this.posZ - f1 * f);

            if (passenger instanceof EntityLivingBase) {
                ((EntityLivingBase) passenger).renderYawOffset = this.renderYawOffset;
            }
        }
        this.stepHeight = 4F;
    }
    
    @Override
	public double getMountedYOffset() {
		return this.height * 0.5F;
	}

	@Override
    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		if (!this.isChild()) {
			int i = this.rand.nextInt(this.getVariantMax()) + 1; // Values 1 to 3
			if (i == 3 && this.rand.nextInt(4) != 0) { // 1/4 chance it remains white (overall 1/12 chance of white)
				i = this.rand.nextInt(2) + 1; // 1 - 2
			}
			if (livingdata instanceof TypeData) {
				i = ((TypeData) livingdata).typeData;
			} else {
				livingdata = new TypeData(i);
			}

			this.setType(i);
		}
		return livingdata;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote) {
			boolean nearLog = false;
			for (EnumFacing facing : EnumFacing.values()) {
				BlockPos pos = this.getPosition().offset(facing);
				Block block = this.world.getBlockState(pos).getBlock();
				if (block == Blocks.LOG || block == Blocks.LOG2) {
					nearLog = true;
				}
			}
			this.setBesideClimbableBlock((this.collidedHorizontally && nearLog) || (this.collidedHorizontally && this.climbTimeWithoutLog < 15));
			if (this.collidedHorizontally && !nearLog) {
				this.climbTimeWithoutLog++;
			} else if (this.climbTimeWithoutLog > 0 || (this.collidedHorizontally && nearLog)) {
				this.climbTimeWithoutLog = 0;
			}
		}
	}

	@Override
	public boolean isOnLadder() {
		return this.isBesideClimbableBlock();
	}

	/**
	 * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false.
	 * The WatchableObject is updated using
	 * setBesideClimableBlock.
	 */
	public boolean isBesideClimbableBlock() {
		return (this.dataManager.get(CLIMBING).byteValue() & 1) != 0;
	}

	/**
	 * Updates the WatchableObject (Byte) created in entityInit(), setting it to
	 * 0x01 if par1 is true or 0x00 if it is
	 * false.
	 */
	public void setBesideClimbableBlock(boolean climbing) {
		byte b0 = this.dataManager.get(CLIMBING).byteValue();

		if (climbing) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}

		this.dataManager.set(CLIMBING, Byte.valueOf(b0));
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateClimber(this, worldIn);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		EntityDabSquirrel squirrel = new EntityDabSquirrel(this.world);
		if (ageable instanceof EntityDabSquirrel) {
			EntityDabSquirrel other = (EntityDabSquirrel) ageable;
			if ((this.getTypeNumber() == 3 || other.getTypeNumber() == 3) && this.getTypeNumber() != other.getTypeNumber()) {
				squirrel.setType(this.getTypeNumber() == 3 ? other.getTypeNumber() : this.getTypeNumber());
			} else {
				squirrel.setType(this.rand.nextBoolean() ? this.getTypeNumber() : other.getTypeNumber());
			}
		}
		return squirrel;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.WHEAT_SEEDS || stack.getItem() == Items.BEETROOT_SEEDS || stack.getItem() == Items.MELON_SEEDS || stack.getItem() == Items.PUMPKIN_SEEDS;
	}

	@Override
	public int getVariantMax() {
		return 3;
	}

	@Override
	protected IVariantTypes getBaseChild() {
		return null; // This is not used, createChild is overriden
	}

}