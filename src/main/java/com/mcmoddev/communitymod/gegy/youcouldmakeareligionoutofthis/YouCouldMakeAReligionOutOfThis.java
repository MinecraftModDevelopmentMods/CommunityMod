package com.mcmoddev.communitymod.gegy.youcouldmakeareligionoutofthis;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.RegUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SubMod(
		modid = "youcouldmakeareligionoutofthis",
        name = "youcouldmakeareligionoutofthis",
        description = "You know, you really could make a religion out of this",
        attribution = "Bill Wurtz (& gegy1000)"
)
@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
public class YouCouldMakeAReligionOutOfThis {
    private static final long M = 181783497276652981L;
    private static final int CHANCE = 10000;

    private static final Random RANDOM = new Random();

    public static final SoundEvent YOU_COULD_MAKE_A_RELIGION_OUT_OF_THIS = SoundEvents.ENTITY_VILLAGER_AMBIENT;

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
        RegUtil.registerSound(event.getRegistry(), new ResourceLocation(CommunityGlobals.MOD_ID, "you_could_make_a_religion_out_of_this"));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft client = Minecraft.getMinecraft();
            if (client.world != null && !client.isGamePaused()) {
                RANDOM.setSeed((client.world.getTotalWorldTime() * M) ^ client.world.getSeed());
                if (RANDOM.nextInt(CHANCE) == 0) {
                    ISound sound = PositionedSoundRecord.getMasterRecord(YOU_COULD_MAKE_A_RELIGION_OUT_OF_THIS, 1.0F);
                    client.getSoundHandler().playSound(sound);
                }
            }
        }
    }
}
