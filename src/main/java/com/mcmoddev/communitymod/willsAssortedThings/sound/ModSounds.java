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
    static ResourceLocation BRUH2ResourceLocation = new ResourceLocation(CommunityGlobals.MOD_ID, "bruh_2_sound");

    @GameRegistry.ObjectHolder("e_sound")
    public static SoundEvent E_SOUND = new SoundEvent(EResourceLocation).setRegistryName(EResourceLocation);

    @GameRegistry.ObjectHolder("bruh_2_sound")
    public static SoundEvent BRUH_2_SOUND = new SoundEvent(BRUH2ResourceLocation).setRegistryName(BRUH2ResourceLocation);

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(E_SOUND, BRUH_2_SOUND);
    }
}
