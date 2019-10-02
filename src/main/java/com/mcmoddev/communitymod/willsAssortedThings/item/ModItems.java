package com.mcmoddev.communitymod.willsAssortedThings.item;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
public class ModItems {

    @GameRegistry.ObjectHolder("magic_eight_ball")
    public static ItemMagicEightBall MAGIC_EIGHT_BALL = null;

    @GameRegistry.ObjectHolder("chicken_arrow")
    public static ItemChickenArrow CHICKEN_ARROW = null;

    public static Item[] items = new Item[]{
            new ItemMagicEightBall(),
            new ItemChickenArrow().setRegistryName("chicken_arrow").setTranslationKey(CommunityGlobals.MOD_ID+".chicken_arrow")
    };

}