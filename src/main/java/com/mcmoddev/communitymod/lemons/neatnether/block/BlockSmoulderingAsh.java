package com.mcmoddev.communitymod.lemons.neatnether.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSmoulderingAsh extends BlockFalling
{
	public BlockSmoulderingAsh()
	{
		blockSoundType = SoundType.SAND;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		//Dont update
	}

	@Override
	public int quantityDropped(Random random)
	{
		//Drop 1 - 4
		return random.nextInt(((4 - 1) + 1)) + 1;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		return random.nextInt(((4 + fortune - 2) + 1)) + 2;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.FIRE_CHARGE;
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		if(worldIn.rand.nextInt(10) == 0)
		{
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		}

		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@SideOnly(Side.CLIENT)
	public int getDustColor(IBlockState state)
	{
		return 0x3f271e;
	}

}
