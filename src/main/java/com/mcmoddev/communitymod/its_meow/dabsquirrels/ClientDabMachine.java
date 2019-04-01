package com.mcmoddev.communitymod.its_meow.dabsquirrels;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID, value = Side.CLIENT)
public class ClientDabMachine {

	@SubscribeEvent
	public static void textureStitchEventPre(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(DabSquirrels.dab);
	}

	@SubscribeEvent
	public static void registerRender(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityDabSquirrel.class, RenderDabSquirrel::new);
	}

}