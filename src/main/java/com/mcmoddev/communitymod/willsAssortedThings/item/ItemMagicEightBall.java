package com.mcmoddev.communitymod.willsAssortedThings.item;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.willsAssortedThings.SpecialPlayerHandler;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ItemMagicEightBall extends Item {

    public static Map<SpecialPlayerHandler.SpecialPlayerEnum, String[]> messages = new HashMap<>();

    private static final Narrator NARRATOR = Narrator.getNarrator();

    static {
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.NONE, new String[]{
                "Life is to you an adventure",
                "You will not survive this week",
                "The blocks, they listen",
                "You can hear them, can't you?",
                "The answer is yes",
                "Really, the creepers are the protagonists.",
                "Are you proud?",
                "Do not allow your aspirations to get in the way of your current success",
                "Please pay €5 to continue.",
                "PC LOAD LETTER",
                "Segmentation fault: Core dumped",
                "Answer hazy, try again later",
                "NullPointerException",
                "Invalid",
                "They all know it",
                "Stop trying to hide it.",
                "Deleting /*",
                "I know",
                "You should do it.",
                "Don't be stupid.",
                "Of course the answer you're looking for is §kUNREADABLE §r, you just won't accept it.",
                "§kPLEASE HELP ME",
                "I'm trapped in a Magic 8 Ball Factory!",
                "Of course, the question is: does this ball actually have any wisdom?",
                "You should probably download Essential Features.",
                "The last thing said was a lie.",
                "You are a horrible person",
                "Of course not.",
                "Haha, sure."
        });
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.Poke1650, new String[]{
                "Are you spooked yet?",
                "DAB ONE MORE TIME, I DARE YOU!",
                "But what if we're all turtles, and you're the only human?",
                "\"Gib good or get banned\": Poke 2019. I guess I'm getting banned then :dab:"
        });
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.mojotimmy2, new String[]{
                "My Name is LegendaryGeek and i have a well established knowledge base of minecraft commands and mods.",
                "Why are the legends orange, and why are you the geek of them?"
        });
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.snakefangox, new String[]{
                "Stay intrigued, and you will find out many things.",
                "Curiosity killed the cat."
        });
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.FiskFille, new String[]{
                "Let's get this bread.",
                "We've had nothing but maggoty bread for three stinking days!",
                "I've done nothing but teleport bread for three days...",
                "Please don't eat me!",
                "You have brought life to the bread, and so you shall be forced to become bread after death.",
                "YOU HAVE GIVEN ME A VOICE!"
        });
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.sokratis12GR, new String[]{
                "As Socrates (oh, sorry, _sokratis_) said: \"Minecraft mods are the greatest form of art.\"",
                "Modder, Artist, Streamer/Youtuber, Modpack Maker, Translator... Is there anything you don't do?",
                "WHAT ARE THOSE?"
        });
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.calweyland, new String[]{
                "E",
                "EEEE",
                "EEEEEEEEEEEEEEEEEEEEEEEE"
        });
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.Willbl3pic, new String[]{
                "If you're reading this, this works."
        });
    }

    public ItemMagicEightBall() {
        super();
        this.setRegistryName("magic_eight_ball");
        this.setCreativeTab(CommunityGlobals.TAB);
        this.setTranslationKey(getRegistryName().toString());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);

        if (worldIn.rand.nextBoolean())
            giveSpecialPlayerMessage(playerIn, worldIn.rand, SpecialPlayerHandler.getSpecialPlayer(playerIn));
        else
            giveSpecialPlayerMessage(playerIn, worldIn.rand, SpecialPlayerHandler.SpecialPlayerEnum.NONE);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void giveMessage(EntityPlayer player, String message) {
        player.sendMessage(new TextComponentString(message));

        int i = Minecraft.getMinecraft().gameSettings.narrator;
        if (NARRATOR.active() && (i == 0 || i == 3)) // Don't narrate if the setting is already turned on
        {
            NARRATOR.clear();
            NARRATOR.say(message);
        } // Thank you FiskFille, very cool
    }

    private void giveSpecialPlayerMessage(EntityPlayer player, Random rand, SpecialPlayerHandler.SpecialPlayerEnum playerEnum) {
        String[] messageArray = messages.get(playerEnum);
        giveMessage(player, messageArray[rand.nextInt(messageArray.length)]);
    }
}
