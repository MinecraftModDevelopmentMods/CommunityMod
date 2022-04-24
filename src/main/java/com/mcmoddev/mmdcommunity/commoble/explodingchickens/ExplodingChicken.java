package com.mcmoddev.mmdcommunity.commoble.explodingchickens;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import net.minecraft.Util;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ExplodingChicken extends Chicken
{
	private static final Supplier<Ingredient> FOOD = Util.make(() ->
	{
		Supplier<Field> field = Suppliers.memoize(() -> ObfuscationReflectionHelper.findField(Chicken.class, "f_28233_"));
		return () -> {
			try
			{
				return (Ingredient)(field.get().get(null));
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				throw new RuntimeException("Reflective chicken food lookup failed: ", e);
			}
		};
	});
	

	private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(ExplodingChicken.class, EntityDataSerializers.INT);
	
	public static final int EXPLOSION_RADIUS = 3;
	public static final int MAX_SWELL = 30;
	
	private int swell = 0;
	private int oldSwell = 0;
	
	public ExplodingChicken(EntityType<? extends Chicken> type, Level level)
	{
		super(type, level);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		List<Goal> temptGoals = this.goalSelector.getAvailableGoals()
			.stream()
			.map(WrappedGoal::getGoal)
			.filter(TemptGoal.class::isInstance)
			.toList();
		temptGoals.forEach(this.goalSelector::removeGoal);
		this.goalSelector.addGoal(3, new TemptAndSwellGoal(this, 1.0D, FOOD.get(), false));
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(DATA_SWELL_DIR, -1);
	}

	@Override
	public void tick()
	{
		if (this.isAlive())
		{
			this.oldSwell = this.swell;
			int i = this.getSwellDir();
			if (i > 0 && this.swell == 0)
			{
				this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
				this.gameEvent(GameEvent.PRIME_FUSE);
			}

			this.swell += i;
			if (this.swell < 0)
			{
				this.swell = 0;
			}

			if (this.swell >= MAX_SWELL)
			{
				this.swell = MAX_SWELL;
				this.explode();
			}
		}

		super.tick();
	}

	public float getSwelling(float pPartialTicks)
	{
		return Mth.lerp(pPartialTicks, (float) this.oldSwell, (float) this.swell) / (float) (MAX_SWELL - 2);
	}

	/**
	 * -1 is idle (disarming), 1 is 'in fuse' (swelling)
	 */
	public int getSwellDir()
	{
		return this.entityData.get(DATA_SWELL_DIR);
	}

	/**
	 * -1 to idle and 1 to be 'in fuse'
	 */
	public void setSwellDir(int pState)
	{
		this.entityData.set(DATA_SWELL_DIR, pState);
	}

	/**
	 * Creates an explosion as determined by this creeper's power and explosion radius.
	 */
	private void explode()
	{
		if (!this.level.isClientSide)
		{
			this.playSound(SoundEvents.CHICKEN_DEATH, 2F, 1F);
			for (int i=0; i<30; i++)
			{
				ThrownEgg egg = new ThrownEgg(this.level, this);
				float xVel = this.level.random.nextFloat() - 0.5F;
				float yVel = this.level.random.nextFloat() * 0.5F;
				float zVel = this.level.random.nextFloat() - 0.5F;
				egg.setDeltaMovement(new Vec3(xVel, yVel, zVel));
			}
			Explosion.BlockInteraction interaction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)
				? Explosion.BlockInteraction.DESTROY
				: Explosion.BlockInteraction.NONE;
			this.dead = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_RADIUS, interaction);
			this.discard();
			this.spawnLingeringCloud();
		}

	}

	private void spawnLingeringCloud()
	{
		Collection<MobEffectInstance> collection = this.getActiveEffects();
		if (!collection.isEmpty())
		{
			AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
			areaeffectcloud.setRadius(2.5F);
			areaeffectcloud.setRadiusOnUse(-0.5F);
			areaeffectcloud.setWaitTime(10);
			areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
			areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

			for (MobEffectInstance mobeffectinstance : collection)
			{
				areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
			}

			this.level.addFreshEntity(areaeffectcloud);
		}
	}
}
