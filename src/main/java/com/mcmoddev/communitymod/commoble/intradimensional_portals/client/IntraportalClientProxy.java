package com.mcmoddev.communitymod.commoble.intradimensional_portals.client;

import com.mcmoddev.communitymod.commoble.intradimensional_portals.IIntraportalProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class IntraportalClientProxy implements IIntraportalProxy
{
		// helper functions for spawning particles from code that runs on both client and server
		// these do nothing on the server proxy but are overridden here in the client
		
		// parameters are xyz position and velocity
		@Override
		public void spawnPortalParticle(World world, double x, double y, double z, double vx, double vy, double vz, float red, float green, float blue)
		{
			ParticlePortal part = new ParticlePortal(world, x, y, z, vx, vy, vz, red, green, blue);
			Minecraft.getMinecraft().effectRenderer.addEffect(part);
		}
}
