package com.mcmoddev.communitymod.commoble.gnomes;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import com.mcmoddev.communitymod.commoble.gnomes.client.RenderGnomeWood;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "Gnomes", description = "Implemegnts gnomes", attribution = "Commoble")
public class SubmodGnomes implements ISubMod
{
	@ObjectHolder(CommunityGlobals.MOD_ID + ":" + "gnome_cache")
	public static final Block gnome_cache = null;
	
	@ObjectHolder(CommunityGlobals.MOD_ID + ":" + "gnome_proof_chest")
	public static Block gnome_proof_chest;
	
	public static SoundEvent GNOME_SPEAK;
	public static SoundEvent GNOME_DEATH;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e)
	{
		GameRegistry.registerTileEntity(TileEntityGnomeCache.class, new ResourceLocation(CommunityGlobals.MOD_ID, "te_gnomecache"));

		if (e.getSide().isClient())
		{
			RenderingRegistry.registerEntityRenderingHandler(EntityGnomeWood.class, RenderGnomeWood::new);
		}
	}
  
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry)
	{
		registerBlock(registry, new BlockGnomeCache(), "gnome_cache");
		registerBlock(registry, new BlockChestGnome(), "gnome_proof_chest");
	}

	@Override
	public void registerEntities(IForgeRegistry<EntityEntry> reg)
	{
		reg.register(EntityEntryBuilder.create()
				.name(CommunityGlobals.MOD_ID + "." + "wood_gnome")
				.entity(EntityGnomeWood.class)
				.id(new ResourceLocation(CommunityGlobals.MOD_ID, "wood_gnome"), CommunityGlobals.entity_id++)
				.tracker(80, 3, false)
				.spawn(EnumCreatureType.CREATURE, 1, 1, 4, BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST))
				.egg(0xd3753f, 0x774725)
				.build());
	}
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
	{
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		
		GNOME_SPEAK = registerSoundEvent(registry, "mob.gnome.say");
		GNOME_DEATH = registerSoundEvent(registry, "mob.gnome.death");
	}
	
	private static Block registerBlock(IForgeRegistry<Block> registry, Block newBlock, String name)
	{
		name = appendPrefix(name);
		newBlock.setTranslationKey(name);
		newBlock.setRegistryName(name);
		registry.register(newBlock);
		return newBlock;
	}
	
	private static String appendPrefix(String unPrefixedString)
	{
		return CommunityGlobals.MOD_ID + ":" + unPrefixedString;
	}
	
	//location is the local location of the sound, e.g. "mob.gnome.say"
	private static SoundEvent registerSoundEvent(IForgeRegistry<SoundEvent> registry, String location)
	{
		ResourceLocation rl = new ResourceLocation(CommunityGlobals.MOD_ID, location);
		SoundEvent sound = new SoundEvent(rl);
		sound.setRegistryName(rl);
		registry.register(sound);
		return sound;
	}
}
