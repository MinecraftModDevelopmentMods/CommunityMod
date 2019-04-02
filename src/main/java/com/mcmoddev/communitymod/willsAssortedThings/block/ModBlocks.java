package com.mcmoddev.communitymod.willsAssortedThings.block;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
public class ModBlocks {

    @GameRegistry.ObjectHolder("e_block")
    public static BlockE E_BLOCK = null;

    public static Block[] blocks = new Block[]{new BlockE()};

}
