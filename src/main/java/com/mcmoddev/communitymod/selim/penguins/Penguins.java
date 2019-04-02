package com.mcmoddev.communitymod.selim.penguins;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Adds penguins (light version) from the offical
 * <a href="https://minecraft.curseforge.com/projects/penguins">Selim's
 * Penguins</a> mod
 */
@EventBusSubscriber(modid = "community_mod")
@SubMod(name = "selim_penguins", attribution = "Selim")
public class Penguins implements ISubMod {

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		List<Biome> penguinBiomes = new LinkedList<Biome>();
		for (Entry<ResourceLocation, Biome> e : ForgeRegistries.BIOMES.getEntries())
			if (e.getValue().getTempCategory() == TempCategory.COLD)
				penguinBiomes.add(e.getValue());
		event.getRegistry()
				.register(EntityEntryBuilder.create().entity(EntityPenguin.class).egg(0x000000, 0xFFFFFF)
						.tracker(32, 4, true).name("community_mod.penguin")
						.spawn(EnumCreatureType.CREATURE, 7, 7, 9, penguinBiomes)
						.id(new ResourceLocation("community_mod", "penguin"), CommunityGlobals.entity_id++).build());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityPenguin.class, PenguinRenderer::new);
	}

}
