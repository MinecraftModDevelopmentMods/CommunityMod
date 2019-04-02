package com.mcmoddev.communitymod.traverse.blocks.base;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;

public class BlockTraverseStairs extends BlockStairs {

    public BlockTraverseStairs(IBlockState modelState, String name) {
        super(modelState);
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_stairs"));
        setCreativeTab(TraverseTab.TAB);
        setTranslationKey(getRegistryName().toString());
        useNeighborBrightness=true;
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "stairs"));
    }
}