package com.mcmoddev.communitymod.willsAssortedThings.block;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.willsAssortedThings.sound.ModSounds;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
public class ModBlocks {

    @GameRegistry.ObjectHolder("e_block")
    public static BlockSound E_BLOCK = null;

    public static Block[] blocks = new Block[]{
            new BlockSound("e_block", ModSounds.E_SOUND),
            new BlockSound("bruh_2_block", ModSounds.BRUH_2_SOUND)
    };

}
