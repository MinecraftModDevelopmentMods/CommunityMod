package com.mcmoddev.communitymod.commoble.gnomes.ai.job;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class JobMoveTo extends Job
{
	public JobMoveTo(EntityGnome ent, BlockPos loc, double speed)
	{
		super(ent, loc);
		this.speed = speed;
	}
	
	public JobMoveTo(EntityGnome ent, Vec3d vec, double speed)
	{
		super(ent, vec);
		this.speed = speed;
	}

	@Override
	public void finishJob(boolean near)
	{
		return;
	}
	
}
