package com.mcmoddev.communitymod.neatnether.gen;

import com.mcmoddev.communitymod.neatnether.NeatNetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenSmoulderingAsh extends WorldGenerator
{
	private final Block block = NeatNetherBlocks.SMOULDERING_ASH;
	private final int width;

	public WorldGenSmoulderingAsh(int width)
	{
		this.width = width;
	}

	public boolean generate(World world, Random rand, BlockPos position)
	{
		while (world.isAirBlock(position) && position.getY() > 2)
		{
			position = position.down();
		}

		int i = rand.nextInt(1 +(this.width - 2)) + 2;
		int offset = -2 + rand.nextInt(4);
		if(world.getBlockState(position.down()).getBlock() == Blocks.LAVA)
			position = position.down();

		for (int x = position.getX() - i; x <= position.getX() + i; ++x)
		{
			for (int z = position.getZ() - i; z <= position.getZ() + i; ++z)
			{
				int xx = x - position.getX();
				int zz = z - position.getZ();

				boolean doPlace = xx * xx + zz * zz <= i * i || rand.nextInt(3) == 0;

				if (doPlace)
				{
					for (int yy = position.getY() - 1; yy <= position.getY() + offset; ++yy)
					{
						BlockPos blockpos = new BlockPos(x, yy, z);
						Block block = world.getBlockState(blockpos).getBlock();

						if (block == Blocks.LAVA || block == Blocks.NETHERRACK || block == Blocks.AIR || block == Blocks.SOUL_SAND)
						{
							world.setBlockState(blockpos, this.block.getDefaultState(), 2);
						}
					}
				}
			}
		}

		return true;
	}

	protected boolean canSilkHarvest()
	{
		return true;
	}
}