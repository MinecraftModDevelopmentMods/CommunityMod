package com.mcmoddev.communitymod.willsAssortedThings.block;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.willsAssortedThings.sound.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockE extends Block {

    public BlockE() {
        super(Material.ROCK);
        this.setCreativeTab(CommunityGlobals.TAB);
        this.setSoundType(new SoundType(2.0F, 1.0F, ModSounds.E_SOUND, ModSounds.E_SOUND, ModSounds.E_SOUND, ModSounds.E_SOUND, ModSounds.E_SOUND));
        this.setRegistryName("e_block");
        this.setTranslationKey(CommunityGlobals.MOD_ID + "." + getRegistryName());
    }

}
