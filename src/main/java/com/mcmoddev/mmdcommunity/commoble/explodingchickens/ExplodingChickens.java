package com.mcmoddev.mmdcommunity.commoble.explodingchickens;

import com.mcmoddev.mmdcommunity.MMDCommunity;
import com.mcmoddev.mmdcommunity.commoble.explodingchickens.client.ExplodingChickensClientProxy;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid=MMDCommunity.MODID, bus=Bus.MOD)
public class ExplodingChickens
{
	private static ExplodingChickens instance;
	public static ExplodingChickens get() { return instance; }
	
	@SubscribeEvent
	public static void onConstructMod(FMLConstructModEvent event)
	{
		event.enqueueWork(ExplodingChickens::afterConstructMod);
	}
	
	private static void afterConstructMod()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		
		ExplodingChickens mod = new ExplodingChickens();
		instance = mod;
		
		// low => add exploding chickens to any biome that has vanilla chickens
		// (allows other mods to add/remove chickens first)
		forgeBus.addListener(EventPriority.LOW, mod::onLowPriorityBiomeLoad);
		
		if (FMLEnvironment.dist == Dist.CLIENT)
		{
			ExplodingChickensClientProxy.init(modBus, forgeBus);
		}
	}
	
	public static final String EXPLODING_CHICKEN = "exploding_chicken";
	public static final String EXPLODING_CHICKEN_SPAWN_EGG = EXPLODING_CHICKEN + "_spawn_egg";
	
	public final RegistryObject<EntityType<ExplodingChicken>> explodingChicken;
	public final RegistryObject<ForgeSpawnEggItem> explodingChickenSpawnEgg;
	
	private ExplodingChickens()
	{
		this.explodingChicken = MMDCommunity.ENTITIES.register(EXPLODING_CHICKEN,
			() -> EntityType.Builder.of(ExplodingChicken::new, MobCategory.CREATURE)
				.sized(0.4F, 0.7F)
				.clientTrackingRange(10)
				.build(EXPLODING_CHICKEN));
		this.explodingChickenSpawnEgg = MMDCommunity.ITEMS.register(EXPLODING_CHICKEN_SPAWN_EGG,
			() -> new ForgeSpawnEggItem(this.explodingChicken, 16711680, 10592673,
				new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	}
	
	private void onLowPriorityBiomeLoad(BiomeLoadingEvent event)
	{
		MobSpawnSettingsBuilder spawns = event.getSpawns();
		if (spawns.getEntityTypes().contains(EntityType.CHICKEN))
		{
			spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(this.explodingChicken.get(), 10,1,4));
		}
	}
}
