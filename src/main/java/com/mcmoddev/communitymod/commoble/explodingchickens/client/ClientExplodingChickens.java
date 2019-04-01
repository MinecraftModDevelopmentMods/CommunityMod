package com.mcmoddev.communitymod.commoble.explodingchickens.client;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.commoble.explodingchickens.EntityExplodingChicken;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SubMod(name = "Exploding Chickens Client", description = "Like chickens but louder and on the client", attribution = "Commoble", clientSideOnly = true)
@Mod.EventBusSubscriber(modid = "community_mod")
public class ClientExplodingChickens implements ISubMod
{
	@Override
	public void onPreInit (FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityExplodingChicken.class, RenderExplodingChicken::new);
	}
}
