package com.mcmoddev.communitymod.commoble.ants;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class AntsServerProxy implements IAntsProxy
{
	public void spawnAntParticle(World world, double x, double y, double z, double xVel, double yVel, double zVel, float scale, EnumFacing face)
	{
		return; // client only
	}
}
