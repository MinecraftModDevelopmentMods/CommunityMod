package com.mcmoddev.communitymod.gegy.transiconherobrinebutwithbetterpants;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class TransIconHerobrineButWithBetterPantsEntity extends EntityCreature implements IRangedAttackMob {
    public static final int DAB_LENGTH = 5;

    private static final int DEATH_ANIMATION_LENGTH = 90;
    private static final int DEATH_PREPARE_LENGTH = 20;

    private static final byte DAB_LEFT_ID = 69;
    private static final byte DAB_RIGHT_ID = 70;

    private int deathAnimation;

    private int dominancePose;
    private int prevDominancePose;

    private double deathOriginY;

    private int dabAnimation;
    private int prevDabAnimation;

    private DabDirection dabDirection;

    public TransIconHerobrineButWithBetterPantsEntity(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWander(this, 0.6));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 0.6, 20, 6.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 32.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, NotchButWithWorsererPantsEntity.class, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.rand.nextInt(1000) == 0 && this.dabAnimation == 0) {
            this.dab(this.rand.nextBoolean() ? DabDirection.LEFT : DabDirection.RIGHT);
        }

        this.prevDabAnimation = this.dabAnimation;

        if (this.dabDirection != null) {
            this.dabAnimation += this.dabDirection.step;
            if (Math.abs(this.dabAnimation) > DAB_LENGTH) {
                this.dabDirection = null;
            }
        } else {
            this.dabAnimation -= Math.signum(this.dabAnimation);
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        EntityLightningBolt lightning = new EntityLightningBolt(this.world, target.posX, target.posY, target.posZ, true);
        this.world.addWeatherEffect(lightning);

        target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 5.0F);

        this.dab(this.rand.nextBoolean() ? DabDirection.LEFT : DabDirection.RIGHT);

        this.navigator.clearPath();
    }

    @Override
    public void onLivingUpdate() {
        if (this.isEntityAlive()) {
            super.onLivingUpdate();
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        this.noClip = true;
        this.deathOriginY = this.posY;
    }

    @Override
    protected void onDeathUpdate() {
        this.hurtTime = 0;
        this.limbSwing = 0.0F;
        this.limbSwingAmount = this.prevLimbSwingAmount = 0.0F;

        this.prevDominancePose = this.dominancePose;
        if (this.dominancePose < DEATH_PREPARE_LENGTH) {
            this.dominancePose++;
        }

        if (this.deathAnimation > DEATH_PREPARE_LENGTH) {
            int riseTime = this.deathAnimation - DEATH_PREPARE_LENGTH;
            int riseLength = DEATH_ANIMATION_LENGTH - DEATH_PREPARE_LENGTH;

            float intermediate = (float) riseTime / riseLength;
            intermediate = (float) Math.pow(intermediate, 4.0);

            this.posY = this.deathOriginY + (255 - this.deathOriginY) * intermediate;
        }

        if (++this.deathAnimation >= DEATH_ANIMATION_LENGTH) {
            this.setDead();
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    public void dab(DabDirection direction) {
        this.dabDirection = direction;

        if (!this.world.isRemote) {
            this.world.setEntityState(this, direction == DabDirection.LEFT ? DAB_LEFT_ID : DAB_RIGHT_ID);
        }
    }

    public float getDabAnimation(float partialTicks) {
        float animation = this.prevDabAnimation + (this.dabAnimation - this.prevDabAnimation) * partialTicks;
        return animation / DAB_LENGTH;
    }

    public float getDominanceAnimation(float partialTicks) {
        float animation = this.prevDominancePose + (this.dominancePose - this.prevDominancePose) * partialTicks;
        return animation / DEATH_PREPARE_LENGTH;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == DAB_LEFT_ID) {
            this.dab(DabDirection.LEFT);
        } else if (id == DAB_RIGHT_ID) {
            this.dab(DabDirection.RIGHT);
        } else {
            super.handleStatusUpdate(id);
        }
    }
}
