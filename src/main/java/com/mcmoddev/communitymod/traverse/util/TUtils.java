package com.mcmoddev.communitymod.traverse.util;

import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import net.minecraft.block.Block;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;

public class TUtils {
    public static Block getBlock(String name) {
        return ShootingStar.getBlock(TraverseConstants.MOD_ID, name);
    }
}
