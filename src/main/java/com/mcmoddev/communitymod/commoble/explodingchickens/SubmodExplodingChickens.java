package com.mcmoddev.communitymod.commoble.explodingchickens;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.commoble.explodingchickens.client.RenderExplodingChicken;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

@SubMod(name = "Exploding Chickens", description = "Like chickens but louder", attribution = "Commoble")
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

	@Override
	public void registerEntities(IForgeRegistry<EntityEntry> reg)
	{
		reg.register(EntityEntryBuilder.create()
				.name(CommunityGlobals.MOD_ID + "." + "exploding_chicken")
				.entity(EntityExplodingChicken.class)
				.id(new ResourceLocation(CommunityGlobals.MOD_ID, "exploding_chicken"), CommunityGlobals.entity_id++)
				.tracker(80, 3, true)
				.spawn(EnumCreatureType.CREATURE, 3, 1, 4, most_biomes)
				.egg(16711680, 10592673)
				.build());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPreInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityExplodingChicken.class, RenderExplodingChicken::new);
	}
}
