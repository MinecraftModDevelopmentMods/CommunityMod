package com.mcmoddev.communitymod.willsAssortedThings;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SubMod(
        name = "Will's Assorted Things",
        description = "Assorted things from Willbl3pic",
        attribution = "Willbl3pic"

)
public class WillsAssortedThingsSubMod implements ISubMod {

    @Override
    public void onPreInit (FMLPreInitializationEvent event) {
        FMLLog.log.info("Will's Assorted things active, deleting MC...");
    }
}
