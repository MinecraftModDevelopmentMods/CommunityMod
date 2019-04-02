package com.mcmoddev.communitymod.commoble.gnomes.ai.job;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnomeWood;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class JobPanicTo extends JobMoveTo
{

	public JobPanicTo(EntityGnomeWood ent, Vec3d vec, double speed)
	{
		super(ent, vec, speed);
	}
	
	public JobPanicTo(EntityGnomeWood ent, BlockPos loc, double speed)
	{
		super(ent, loc, speed);
	}

	@Override
	public void finishJob(boolean near)
	{
		if (near)
		{
			((EntityGnomeWood)this.gnome).panic = false;
		}
	}
}
