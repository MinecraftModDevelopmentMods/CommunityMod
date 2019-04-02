package com.mcmoddev.communitymod.traverse.blocks.base;

import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockTraverseLeaves extends BlockLeaves {

    public Item sapling;
    public int saplingDropChance;

    public BlockTraverseLeaves(String name, Item sapling, int saplingDropChance) {
        this.sapling = sapling;
        this.saplingDropChance = saplingDropChance;
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_leaves"));
        setTranslationKey(getRegistryName().toString());
        setCreativeTab(TraverseTab.TAB);
        this.setDefaultState(this.getDefaultState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
        Blocks.FIRE.setFireInfo(this, 30, 60);
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "leaves", CHECK_DECAY, DECAYABLE));
    }

    public int quantityDropped(Random random) {
        return random.nextInt(saplingDropChance) == 0 ? 1 : 0;
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        List<ItemStack> list = new java.util.ArrayList<>();
        list.add(new ItemStack(this, 1, 0));
        return list;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return Blocks.LEAVES.getDefaultState().isOpaqueCube();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return Blocks.LEAVES.getRenderLayer();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DECAYABLE, (meta & 1) == 0)
                .withProperty(CHECK_DECAY, (meta & 2) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (!state.getValue(DECAYABLE)) {
            meta |= 1;
        }
        if (state.getValue(CHECK_DECAY)) {
            meta |= 2;
        }
        return meta;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return sapling;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (!Minecraft.getMinecraft().gameSettings.fancyGraphics) {
            if (!(blockAccess.getBlockState(pos.offset(side)).getBlock() instanceof BlockLeaves)) {
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(DECAYABLE, false);
    }

}
