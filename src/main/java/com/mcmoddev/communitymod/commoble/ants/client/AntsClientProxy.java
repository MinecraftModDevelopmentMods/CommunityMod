package com.mcmoddev.communitymod.commoble.ants.client;

import com.mcmoddev.communitymod.commoble.ants.IAntsProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class AntsClientProxy implements IAntsProxy
{
	public void spawnAntParticle(World world, double x, double y, double z, double xVel, double yVel, double zVel, float scale, EnumFacing face)
	{
		ParticleAnt part = new ParticleAnt(world, x, y, z, xVel, yVel, zVel, scale, face);
		Minecraft.getMinecraft().effectRenderer.addEffect(part);
	}
}
