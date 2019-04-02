package com.mcmoddev.communitymod.traverse.blocks.base;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;

public class BlockTraverseWoodLog extends BlockLog {

    public BlockTraverseWoodLog(String name) {
        super();
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_log"));
        setCreativeTab(TraverseTab.TAB);
        setTranslationKey(getRegistryName().toString());
        setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "log"));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{LOG_AXIS});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LOG_AXIS).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        for (EnumAxis axis : EnumAxis.values()) {
            if (axis.ordinal() == meta) {
                return getDefaultState().withProperty(LOG_AXIS, axis);
            }
        }
        return getDefaultState();
    }

    public int damageDropped(IBlockState state) {
        return 0;
    }
}
