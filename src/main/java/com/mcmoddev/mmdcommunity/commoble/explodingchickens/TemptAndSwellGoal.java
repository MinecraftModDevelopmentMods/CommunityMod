package com.mcmoddev.mmdcommunity.commoble.explodingchickens;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class TemptAndSwellGoal extends TemptGoal
{
	protected final ExplodingChicken chicken;

	public TemptAndSwellGoal(ExplodingChicken chicken, double pSpeedModifier, Ingredient pItems, boolean pCanScare)
	{
		super(chicken, pSpeedModifier, pItems, pCanScare);
		this.chicken = chicken;
	}

	@Override
	public boolean requiresUpdateEveryTick()
	{
		return true;
	}

	@Override
	public void tick()
	{
		super.tick();
		if (this.player == null
			|| this.chicken.distanceToSqr(this.player) > 49.0D
			|| !this.chicken.getSensing().hasLineOfSight(this.player))
		{
			this.chicken.setSwellDir(-1);
		}
		else
		{
			this.chicken.setSwellDir(1);
		}
	}

	@Override
	public void stop()
	{
		this.chicken.setSwellDir(-1);
		super.stop();
	}
}
