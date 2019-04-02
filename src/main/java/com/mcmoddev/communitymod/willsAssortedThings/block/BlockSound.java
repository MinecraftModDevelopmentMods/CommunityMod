package com.mcmoddev.communitymod.willsAssortedThings.block;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.willsAssortedThings.sound.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.SoundEvent;

public class BlockSound extends Block {

    public BlockSound(String name, SoundEvent sound) {
        super(Material.ROCK);
        this.setCreativeTab(CommunityGlobals.TAB);
        this.setSoundType(new SoundType(2.0F, 1.0F, sound, sound, sound, sound, sound));
        this.setRegistryName(name);
        this.setTranslationKey(getRegistryName().toString());
    }

}
