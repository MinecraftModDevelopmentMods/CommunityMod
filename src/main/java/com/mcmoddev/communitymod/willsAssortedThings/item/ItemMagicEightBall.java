package com.mcmoddev.communitymod.willsAssortedThings.item;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.willsAssortedThings.SpecialPlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
                "Deleting /*"});
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
                "You have brought life to the bread, and so you shall be forced to become bread after death."
        });
        messages.put(SpecialPlayerHandler.SpecialPlayerEnum.sokratis12GR, new String[]{
                "As Socrates (oh, sorry, _sokratis_) said: \"Minecraft mods are the greatest form of art.\"",
                "Modder, Artist, Streamer/Youtuber, Modpack Maker, Translator... Is there anything you don't do?",
                "WHAT ARE THOSE?"
        });
    }

    public ItemMagicEightBall() {
        super();
        this.setRegistryName("magic_eight_ball");
        this.setCreativeTab(CommunityGlobals.TAB);
        this.setTranslationKey(getRegistryName().toString());
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.rand.nextBoolean())
            giveSpecialPlayerMessage(player, worldIn.rand, SpecialPlayerHandler.getSpecialPlayer(player));
        else
            giveSpecialPlayerMessage(player, worldIn.rand, SpecialPlayerHandler.SpecialPlayerEnum.NONE);
        return EnumActionResult.SUCCESS;
    }

    private void giveMessage(EntityPlayer player, String message) {
        player.sendMessage(new TextComponentString(message));
    }

    private void giveSpecialPlayerMessage(EntityPlayer player, Random rand, SpecialPlayerHandler.SpecialPlayerEnum playerEnum) {
        String[] messageArray = messages.get(playerEnum);
        giveMessage(player, messageArray[rand.nextInt(messageArray.length)]);
    }
}
