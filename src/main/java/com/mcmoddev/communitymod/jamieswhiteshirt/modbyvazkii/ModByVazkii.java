package com.mcmoddev.communitymod.jamieswhiteshirt.modbyvazkii;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

@SubMod(
	modid = "vazkii",
    name = "mod by vazkii",
    description = "mod by vazkii",
    attribution = "JamiesWhiteShirt",
    clientSideOnly = true
)
public class ModByVazkii implements ISubMod {
    private static Pattern neatPattern = Pattern.compile("neat", Pattern.CASE_INSENSITIVE);

    @SubscribeEvent
    public static void onClientChat(ClientChatEvent e) {
        e.setMessage(neatPattern.matcher(e.getMessage()).replaceAll("mod by vazkii"));
    }
}
