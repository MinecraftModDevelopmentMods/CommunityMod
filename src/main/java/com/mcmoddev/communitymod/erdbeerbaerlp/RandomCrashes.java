package com.mcmoddev.communitymod.erdbeerbaerlp;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.mcmoddev.communitymod.SubMod;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@SubMod(name = "Random Crashes", description = "Gives zou a 0.1% chance to crash per minute", attribution = "ErdbeerbaerLP")
@Mod.EventBusSubscriber(modid = "community_mod")
public class RandomCrashes {
	int i = 0;
	int attempts = 0;
	final Random r = new Random();
	@SubscribeEvent
	public void onIngameTick(TickEvent.PlayerTickEvent ev) {
		if(i >= TimeUnit.MINUTES.toSeconds(1)*20) { //Make sure to execute it only every minute
			i=0;
			attempts++;
			int number = r.nextInt(1000); 
			if(number == 1) { 
				someCrashingMethod();
			}else {
				ev.player.sendMessage(new TextComponentString(TextFormatting.RED+"Failed to crash the Game!"+TextFormatting.GOLD+" It did not crash "+attempts+"times"));
			}
		}
		i++;
	}
	
	private void someCrashingMethod() {
		Minecraft.getMinecraft().crashed(new CrashReport("April Fools :P", new Throwable("april.fools.exception")));
	}
}
