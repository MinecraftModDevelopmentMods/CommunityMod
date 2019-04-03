package com.mcmoddev.communitymod.commoble.intradimensional_portals;

import net.minecraft.world.World;

public class IntraportalServerProxy implements IIntraportalProxy
{
	public void spawnPortalParticle(World world, double x, double y, double z, double vx, double vy, double vz, float red, float green, float blue)
	{
		return;	// client only
	}
}
