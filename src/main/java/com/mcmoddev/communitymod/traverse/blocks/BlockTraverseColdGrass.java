package com.mcmoddev.communitymod.traverse.blocks;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;

import static com.mcmoddev.communitymod.traverse.util.TUtils.getBlock;

public class BlockTraverseColdGrass extends BlockTallGrass {

    public BlockTraverseColdGrass() {
        super();
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, "cold_grass"));
        setTranslationKey(getRegistryName().toString());
        setCreativeTab(TraverseTab.TAB);
        setSoundType(SoundType.PLANT);
        useNeighborBrightness = true;
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "plant", TYPE));
    }

    public boolean canSustainBush(IBlockState state) {
        return state.getBlock() == getBlock("blue_rock") || state.getBlock() == Blocks.ICE || state.getBlock() == Blocks.FROSTED_ICE || state.getBlock() == Blocks.PACKED_ICE;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
    }

    public int damageDropped(IBlockState state) {
        return 0;
    }

    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}
