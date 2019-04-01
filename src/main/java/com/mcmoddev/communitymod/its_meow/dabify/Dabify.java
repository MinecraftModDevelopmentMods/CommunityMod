package com.mcmoddev.communitymod.its_meow.dabify;

import java.util.Random;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.its_meow.dabsquirrels.DabSquirrels;
import com.mcmoddev.communitymod.its_meow.dabsquirrels.ParticleBasic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID, value = Side.CLIENT)
@SubMod(name = "Dabify", description = "Get dabbed on every tick", attribution = "its_meow", clientSideOnly = true)
public class Dabify implements ISubMod {

	private final String[] messages = { "Dabbing on em", "Supercharging dab engine", "Banishing Vazkii to hell", "Dabification Initializationification", "Overusing the dab joke" };
	public static final ResourceLocation dab = new ResourceLocation(CommunityGlobals.MOD_ID, "textures/particles/dab");

	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		FMLLog.log.info(this.messages[new Random().nextInt(this.messages.length)] + "...");
	} 
	
	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		EntityPlayer e = Minecraft.getMinecraft().player;
		if(e != null && e.world != null && !Minecraft.getMinecraft().isGamePaused()) {
			Particle particle = new ParticleBasic(e.world, e.posX + (Math.random() - 0.5F), e.posY + 3, e.posZ + (Math.random() - 0.5F), 0.0F, 1000.0F, 0.0F, DabSquirrels.dab, 0x1.0p0f);
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

}
