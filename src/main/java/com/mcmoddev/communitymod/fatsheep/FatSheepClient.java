package com.mcmoddev.communitymod.fatsheep;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.fatsheep.model.RenderOvergrownSheep;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.Random;

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