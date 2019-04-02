package com.mcmoddev.communitymod.traverse.blocks.base;

import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockTraverseSlab extends BlockSlab {

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.<BlockTraverseSlab.Variant>create("variant", BlockTraverseSlab.Variant.class);
    public final String name;
    public Block halfslab;

    public BlockTraverseSlab(String name, Material material, SoundType soundType) {
        super(material, material.getMaterialMapColor());
        this.name = name;
        IBlockState iblockstate = this.blockState.getBaseState();

        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, EnumBlockHalf.BOTTOM);
            setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_slab"));
            halfslab = this;
        }
        else {
            setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_double_slab"));
        }
        setCreativeTab(TraverseTab.TAB);
        setTranslationKey(getRegistryName().toString());
        setHarvestLevel("pickaxe", 0);
        setHardness(2.0F);
        setResistance(10);
        setSoundType(soundType);
        this.setDefaultState(iblockstate.withProperty(VARIANT, BlockTraverseSlab.Variant.DEFAULT));
        useNeighborBrightness = true;
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "slab"));
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(halfslab);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(halfslab);
    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockTraverseSlab.Variant.DEFAULT);

        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }

        return iblockstate;
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, new IProperty[]{VARIANT}) : new BlockStateContainer(this, new IProperty[]{HALF, VARIANT});
    }

    public String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }

    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    public Comparable<?> getTypeForItem(ItemStack stack) {
        return BlockTraverseSlab.Variant.DEFAULT;
    }

    public static enum Variant implements IStringSerializable {
        DEFAULT;

        public String getName() {
            return "default";
        }
    }

    public static class Double extends BlockTraverseSlab {

        public Double(String name, Material material, SoundType soundType, Block half) {
            super(name, material, soundType);
            this.halfslab = half;
        }

        public boolean isDouble() {
            return true;
        }

    }

    public static class Half extends BlockTraverseSlab {

        public Half(String name, Material material, SoundType soundType) {
            super(name, material, soundType);
        }

        public boolean isDouble() {
            return false;
        }

    }

}