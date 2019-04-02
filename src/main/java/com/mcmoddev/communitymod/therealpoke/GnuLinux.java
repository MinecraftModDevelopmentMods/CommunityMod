package com.mcmoddev.communitymod.therealpoke;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.RegUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.regex.Pattern;

@SubMod(
        name = "GNULinux",
        description = "it's GNU/Linux",
        attribution = "Poke"
)
@Mod.EventBusSubscriber
public class GnuLinux implements ISubMod {
    private static Pattern linuxPatern = Pattern.compile("linux", Pattern.CASE_INSENSITIVE);

    public static SoundEvent GNU_LINUX;

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
        GNU_LINUX = RegUtil.registerSound(event.getRegistry(), new ResourceLocation(CommunityGlobals.MOD_ID, "gnu_linux"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent()
    public static void onClientChat(ClientChatEvent e) {
        String msg = e.getMessage();

        if (linuxPatern.matcher(msg).find()) {
            Minecraft.getMinecraft().player.playSound(GNU_LINUX, 10f, 1f);
            e.setMessage(linuxPatern.matcher(e.getMessage()).replaceAll("GNU/Linux"));
        }
    }
}
