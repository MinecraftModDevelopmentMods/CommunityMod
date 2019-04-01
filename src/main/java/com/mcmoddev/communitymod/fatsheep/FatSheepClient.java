package com.mcmoddev.communitymod.fatsheep;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.fatsheep.model.RenderOvergrownSheep;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SubMod(name = "Fat Sheep Client", description = "Fixes sheep not getting fat", attribution = "Lemons", clientSideOnly = true)
@Mod.EventBusSubscriber(modid = "community_mod")
public class FatSheepClient implements ISubMod
{
	/*
			Making a whole submod for this because i couldn't be bothered making proxies
			#yolo
	 */

	@Override
	public void onPreInit (FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityOvergrownSheep.class, RenderOvergrownSheep::new);
	}
}