package com.mcmoddev.communitymod.explodingchickens;

import java.util.HashSet;
import java.util.Set;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@SubMod(name = "Exploding Chickens", description = "Like chickens but louder", attribution = "Commoble")
@Mod.EventBusSubscriber(modid = "community_mod")
public class SubmodExplodingChickens implements ISubMod
{
	public static Set<Biome> most_biomes = new HashSet<Biome>();
	static
	{
		most_biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.HOT));
		most_biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD));
		most_biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.BEACH));
		most_biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
		most_biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MOUNTAIN));
		most_biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
		most_biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.WET));
		most_biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
	}
	
	@SubscribeEvent
	public static void onRegisterEntity(RegistryEvent.Register<EntityEntry> event)
	{
		event.getRegistry().register(EntityEntryBuilder.create()
				.name("explodingchicken")
				.entity(EntityExplodingChicken.class)
				.id(new ResourceLocation(CommunityGlobals.MOD_ID, "exploding_chicken"), CommunityGlobals.entity_id++)
				.tracker(80, 3, true)
				.spawn(EnumCreatureType.CREATURE, 3, 1, 4, most_biomes)
				.egg(16711680, 10592673)
				.build());
	}
}
