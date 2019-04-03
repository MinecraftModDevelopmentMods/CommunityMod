package com.mcmoddev.communitymod.traverse.blocks.base;

import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;

public class BlockTraverseWoodFence extends BlockFence {

    public BlockTraverseWoodFence(String name) {
        super(Material.WOOD, MapColor.WOOD);
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_fence"));
        setTranslationKey(getRegistryName().toString());
        setCreativeTab(TraverseTab.TAB);
        setSoundType(SoundType.WOOD);
        setHardness(2.0F);
        setResistance(5.0F);
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "fence"));
    }
}
