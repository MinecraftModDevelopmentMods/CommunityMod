package com.mcmoddev.communitymod.badcommand;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SubMod(name = "Massive security hole", attribution = "Not me")
public class BadCommandMod implements ISubMod {

	@Override
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new BadCommand());
	}
}
