package com.mcmoddev.communitymod.traverse.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;

public class BlockTraverseWall extends BlockWall {

    public BlockTraverseWall(Block modelBlock, String name) {
        super(modelBlock);
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_wall"));
        setCreativeTab(TraverseTab.TAB);
        setTranslationKey(getRegistryName().toString());
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "wall", VARIANT));
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