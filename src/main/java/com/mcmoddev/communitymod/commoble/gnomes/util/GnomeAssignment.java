package com.mcmoddev.communitymod.commoble.gnomes.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class GnomeAssignment
{
	public final BlockPos pos;
	public final Block oldblock;
	public final Block newblock;
	public final EnumAssignType type;
	
	/**
	 * Contains a BlockLocator
	 * -which contains a position and a block type to change a position to
	 * And the expected existing block type of that location.
	 * The existing type is used to cancel an assignment if the block changes
	 * between the assignment being assigned and the gnome reaching its target.
	 * @param pos BlockPos of the assignment
	 * @param oldblock Existing block at that location
	 * @param newblock Block to set to at that location
	 */
	public GnomeAssignment(BlockPos pos, Block oldblock, Block newblock, EnumAssignType type)
	{
		this.pos = pos;
		this.oldblock = oldblock;
		this.newblock = newblock;
		this.type = type;
	}
	
	public GnomeAssignment(BlockPosAndBlock bpb, Block oldblock, EnumAssignType type)
	{
		this(bpb.pos, oldblock, bpb.block, type);
	}
	
	public BlockPosAndBlock getBlockPosAndNewBlock()
	{
		return new BlockPosAndBlock(this.pos, this.newblock);
	}
	
	public BlockPosAndBlock getBlockPosAndOldBlock()
	{
		return new BlockPosAndBlock(this.pos, this.oldblock);
	}
}
