package com.mcmoddev.communitymod.test;

import java.util.Random;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SubMod(name = "Example SubMod", attribution = "MMD Team")
public class ModTest implements ISubMod {
    
    private final String[] messages = { "Loading herobrine", "Deleting save files", "Subscribing to Vazkii on Patreon", "Teleporting behind you", "Preparing to *nuzzles*" };
    
    @Override
    public void onPreInit (FMLPreInitializationEvent event) {
        
        FMLLog.log.info(this.messages[new Random().nextInt(this.messages.length)] + "...");
    }
}