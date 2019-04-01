package com.mcmoddev.communitymod.its_meow.chickenificator;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID, value = Side.CLIENT)
@SubMod(name = "Chickenificator", description = "Replaces things with chickens", attribution = "its_meow", clientSideOnly = true)
public class Chickenificator implements ISubMod {

	@SuppressWarnings("deprecation")
	@Override
	public void onInit(FMLInitializationEvent event) {
		FMLLog.log.info("Chickenification is beginning...");
		RenderingRegistry.registerEntityRenderingHandler(EntitySheep.class, new RenderStupidChicken());
		RenderingRegistry.registerEntityRenderingHandler(EntityCow.class, new RenderStupidChicken());
		RenderingRegistry.registerEntityRenderingHandler(EntityPig.class, new RenderStupidChicken());
		RenderingRegistry.registerEntityRenderingHandler(EntitySquid.class, new RenderStupidChicken());
		RenderingRegistry.registerEntityRenderingHandler(EntityZombie.class, new RenderStupidChicken());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpider.class, new RenderStupidChicken());
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeleton.class, new RenderStupidChicken());
		FMLLog.log.info("Chickenificated all the bois!");
	} 

}
