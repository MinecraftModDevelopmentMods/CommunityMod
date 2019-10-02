package com.mcmoddev.communitymod.commoble.gnomes.client;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.commoble.gnomes.EntityGnomeWood;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SubMod(name = "Gnomes Client", description = "Client side submod for the gnomes", attribution = "Commoble", clientSideOnly = true)
public class ClientGnomes implements ISubMod
{
	@Override
	public void onPreInit (FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityGnomeWood.class, RenderGnomeWood::new);
	}

}
