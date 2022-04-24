package com.mcmoddev.mmdcommunity.commoble.explodingchickens.client;

import com.mcmoddev.mmdcommunity.commoble.explodingchickens.ExplodingChickens;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ExplodingChickensClientProxy
{
	public static void init(IEventBus modBus, IEventBus forgeBus)
	{
		ExplodingChickensClientProxy proxy = new ExplodingChickensClientProxy();
		modBus.addListener(proxy::onRegisterEntityRenderers);
	}
	
	private void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(ExplodingChickens.get().explodingChicken.get(), ExplodingChickenRenderer::new);
	}
}
