package com.mcmoddev.communitymod.willsAssortedThings.item;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
public class ModItems {

    @GameRegistry.ObjectHolder("magic_eight_ball")
    public static ItemMagicEightBall MAGIC_EIGHT_BALL = null;

    public static Item[] items = new Item[]{new ItemMagicEightBall()};

}