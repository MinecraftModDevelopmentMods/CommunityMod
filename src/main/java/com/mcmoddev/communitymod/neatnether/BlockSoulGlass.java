package com.mcmoddev.communitymod.neatnether;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSoulGlass extends BlockBreakable
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	private static final AxisAlignedBB EMPTY_BB = new AxisAlignedBB(0,0,0,0,0,0);

	public BlockSoulGlass()
	{
		super(Material.GLASS, false);
		blockSoundType = SoundType.GLASS;

		setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return blockState.getValue(POWERED) ? EMPTY_BB : super.getCollisionBoundingBox(blockState, worldIn, pos);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return FULL_BLOCK_AABB.offset(pos);
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean powered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());

		if (powered)
		{
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.withProperty(POWERED, true));
		}
		else
		{
			if(state.getValue(POWERED))
			{
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
				worldIn.setBlockState(pos, state.withProperty(POWERED, false));
			}
		}
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if(worldIn.isBlockPowered(pos))
		{
			worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.withProperty(POWERED, true));
		}
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, POWERED);
	}

	public int getMetaFromState(IBlockState state)
	{
		if(state.getValue(POWERED))
			return 1;

		return 0;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(POWERED, meta == 1);
	}

	public int quantityDropped(Random random)
	{
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	protected boolean canSilkHarvest()
	{
		return true;
	}

}
