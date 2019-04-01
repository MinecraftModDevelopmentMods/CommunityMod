package com.mcmoddev.communitymod.fiskfille.sentientbread;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SubMod(name = "Sentient Bread", description = "Bread will plead for its life if you try to eat it.", attribution = "FiskFille")
@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID, value = Side.CLIENT)
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

    @SideOnly(Side.CLIENT)
    private static final Narrator NARRATOR = Narrator.getNarrator();

    private static ItemStack prevActiveItem = ItemStack.EMPTY;

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();

        if (!stack.isEmpty() && stack.getItem() == Items.BREAD && player.canEat(false))
        {
            prevActiveItem = stack;
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if (!prevActiveItem.isEmpty())
            {
                Random rand = event.player.world.rand;

                if (event.player.getItemInUseCount() % 7 == 6 || rand.nextFloat() < 0.05)
                {
                    say(event.player, MESSAGES_STOP[rand.nextInt(MESSAGES_STOP.length)]);
                }

                prevActiveItem = ItemStack.EMPTY;
            }
        }
    }

    private static void say(EntityPlayer player, String msg)
    {
        int i = Minecraft.getMinecraft().gameSettings.narrator;

        if (NARRATOR.active() && (i == 0 || i == 3)) // Don't narrate if the setting is already turned on
        {
            NARRATOR.clear();
            NARRATOR.say(msg);
        }

        player.sendMessage(prevActiveItem.getTextComponent().appendSibling(new TextComponentString(": " + msg)));
    }
}
