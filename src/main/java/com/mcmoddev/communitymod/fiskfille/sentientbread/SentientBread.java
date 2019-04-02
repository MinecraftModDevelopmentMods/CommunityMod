package com.mcmoddev.communitymod.fiskfille.sentientbread;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@SubMod(name = "Sentient Bread", description = "Bread will plead for its life if you try to eat it.", attribution = "FiskFille", clientSideOnly = true)
public class SentientBread implements ISubMod
{
    private static final String[] MESSAGES_STOP = {
            "NO!!",
            "Please stop that",
            "Please don't eat me!",
            "Please stop",
            "I have a wife and family!",
            "THE PAIN! IT'S UNBEARABLE!",
            "I beg of you!",
            "I won't tell anyone, just spare me!",
            "Think of my children!",
            "WHY WOULD YOU DO THIS?",
            "AHHHH!",
            "WHY",
            "You're a MONSTER!",
            "Show me mercy!",
            "Spare me!",
            "Show mercy!",
            "Don't kill me, please!",
            "PLEASE!",
            "I'll do whatever you want!",
            "Don't kill me, please!",
            "Please don't kill me!",
            "I'm just a poor dough, I need no sympathy",
            "End my suffering"
    };

    private static final Narrator NARRATOR = Narrator.getNarrator();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.phase == TickEvent.Phase.END && mc.world != null && !mc.isGamePaused())
        {
            ItemStack stack = mc.player.getActiveItemStack();

            if (!stack.isEmpty() && stack.getItem() == Items.BREAD)
            {
                Random rand = mc.player.world.rand;

                if (rand.nextFloat() < 0.04)
                {
                    String msg = MESSAGES_STOP[rand.nextInt(MESSAGES_STOP.length)];
                    int i = mc.gameSettings.narrator;

                    if (NARRATOR.active() && (i == 0 || i == 3)) // Don't narrate if the setting is already turned on
                    {
                        NARRATOR.clear();
                        NARRATOR.say(msg);
                    }

                    mc.player.sendMessage(stack.getTextComponent().appendSibling(new TextComponentString(": " + msg)));
                }
            }
        }
    }
}
