package com.mcmoddev.communitymod.commoble.gnomes.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class BlockPosAndBlock
{
	public final BlockPos pos;
	public final Block block;
	
	public BlockPosAndBlock(BlockPos pos, Block block)
	{
		this.pos = pos;
		this.block = block;
	}
	
	public BlockPosAndBlock(int x, int y, int z, Block block)
	{
		this(new BlockPos(x,y,z), block);
	}
	
	public BlockPosAndBlock(BlockPos pos, IBlockState state)
	{
		this(pos, state.getBlock());
	}
	
	public BlockPosAndBlock(int x, int y, int z, IBlockState state)
	{
		this(new BlockPos(x,y,z), state.getBlock());
	}
}
