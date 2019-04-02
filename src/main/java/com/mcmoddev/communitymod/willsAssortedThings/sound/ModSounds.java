package com.mcmoddev.communitymod.willsAssortedThings.sound;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
public class ModSounds {

    static ResourceLocation EResourceLocation = new ResourceLocation(CommunityGlobals.MOD_ID, "e_sound");

    @GameRegistry.ObjectHolder("e_sound")
    public static SoundEvent E_SOUND = new SoundEvent(EResourceLocation).setRegistryName(EResourceLocation);

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(E_SOUND);
    }
}
