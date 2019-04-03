package com.mcmoddev.communitymod.test;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.SubModLoader;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

@SubMod(name = "Example SubMod", attribution = "MMD Team", requiresMcRestart = false)
public class ModTest implements ISubMod {
    
    private final String[] messages = { "Loading Herobrine", "Deleting save files", "Subscribing to Vazkii on Patreon", "Teleporting behind you", "Preparing to *nuzzles*", "Fisking the Files" };
    private static boolean isLoaded;

    @Override
    public void onConstruction(FMLConstructionEvent event)
    {
        isLoaded = SubModLoader.isSubModLoaded(this);
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event)
    {
        if (isLoaded)
        {
            FMLLog.log.info(messages[new Random().nextInt(messages.length)] + "...");
        }
    }

    @SubscribeEvent
    public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent e)
    {
        if (isLoaded && e.getGui() instanceof GuiModList)
        {
            e.getGui().drawString(e.getGui().mc.fontRenderer, "ExampleMod loaded! Neat, eh?", e.getMouseX(), e.getMouseY(), -1);
        }
    }

    @Override
    public boolean enabledByDefault()
    {
        return false; // Only enabled if the user actively wants it to be
    }

    @Override
    public void onLoadStateChanged(boolean unload)
    {
        isLoaded = !unload;
    }
}
