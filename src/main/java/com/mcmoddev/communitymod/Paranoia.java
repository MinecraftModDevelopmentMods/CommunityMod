package com.mcmoddev.communitymod;

import java.util.Random;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@EventBusSubscriber
public class Paranoia {
	
	static Random rand = new Random();
	static String[] messages = new String[] { "Error", "Something is behind you", "Beginning Invasion", "Inventory Cleared", "I hunger" };

	public static void playerTick(PlayerTickEvent e) {
		if(rand.nextInt(80000) < 1) {
			e.player.sendStatusMessage(new TextComponentString(messages[rand.nextInt(messages.length)]), true);
		}
	}
	
}
