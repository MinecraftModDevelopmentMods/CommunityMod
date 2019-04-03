package com.mcmoddev.communitymod.commoble.intradimensional_portals;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenManagerForIntraportals implements IWorldGenerator
{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider)
	{
		int x = random.nextInt(16) + 16*chunkX;
		int z = random.nextInt(16) + 16*chunkZ;
		int y = random.nextInt(world.getActualHeight()-10) + 5;
		BlockPos checkPos = new BlockPos(x,y,z);
		if (random.nextInt(100) == 0
				&& world.getBlockState(checkPos).getBlock() == Blocks.AIR
				&& world.getBlockState(checkPos.up()).getBlock() == Blocks.AIR
				&& world.getBlockState(checkPos.down()).getBlock() == Blocks.AIR)
		{
			world.setBlockState(checkPos, SubmodIntradimensionalPortals.intradimensional_portal_base.getDefaultState());
			//world.setBlockState(checkPos.up(), SubmodIntradimensionalPortals.intradimensional_portal_glowy_air.getDefaultState());
			//world.setBlockState(checkPos.down(), SubmodIntradimensionalPortals.intradimensional_portal_glowy_air.getDefaultState());
		}
	}

}
