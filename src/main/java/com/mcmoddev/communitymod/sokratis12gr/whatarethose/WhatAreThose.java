package com.mcmoddev.communitymod.jamieswhiteshirt.modbyvazkii;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

@SubMod(
    name = "What are Those",
    description = "WHAT ARE THOOOOOSE!?",
    attribution = "sokratis12GR"
)
@Mod.EventBusSubscriber
public class WhatAreThose implements ISubMod {
    private static Pattern neatPattern = Pattern.compile("hi", Pattern.CASE_INSENSITIVE);
    
    public static Random rand = new Random();
    
    @SubscribeEvent()
    public static void onClientChat(ClientChatEvent e) {
   		if(rand.nextInt(1000) < 1) {
           e.setMessage(neatPattern.matcher(e.getMessage()).replaceAll("What are THOOOOOSE!?"));
      }
    }
}
