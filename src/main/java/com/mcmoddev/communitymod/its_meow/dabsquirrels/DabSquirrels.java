package com.mcmoddev.communitymod.its_meow.dabsquirrels;

import java.util.Random;
import java.util.Set;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber
@SubMod(name = "Dab Squirrels", attribution = "its_meow")
public class DabSquirrels implements ISubMod {
    
	private final String[] messages = { "Increasing dabaliciousness of squirrels", "Creating dab engine", "Deleting Vazkii from existence", "Increasing dab version", "Enjoying some dabalicious nuts with some squirrel" };
	public static final ResourceLocation dab = new ResourceLocation(CommunityGlobals.MOD_ID, "particles/dab");
	
	@Override
    public void onPreInit(FMLPreInitializationEvent event) {
        FMLLog.log.info(this.messages[new Random().nextInt(this.messages.length)] + "...");
    }
	
	@Override
    public void onInit(FMLInitializationEvent event) {
        
    }
    
	@Override
    public void onPostInit(FMLPostInitializationEvent event) {
        
    }
	
	@SubscribeEvent
	public static void registerEntity(RegistryEvent.Register<EntityEntry> event) {
		Set<Biome> biomeset = BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST);
		Biome[] biomes = biomeset.toArray(new Biome[0]);
		event.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityDabSquirrel.class).egg(0x89806f, 0xb2a489)
				.name(CommunityGlobals.MOD_ID + ".dabsquirrel")
				.id(new ResourceLocation(CommunityGlobals.MOD_ID, "dabsquirrel"), CommunityGlobals.entity_id++)
				.tracker(128, 1, true)
				.spawn(EnumCreatureType.CREATURE, 12, 1, 3, biomes)
				.build());
	}
	
	
}