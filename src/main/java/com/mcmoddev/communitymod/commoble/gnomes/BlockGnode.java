package com.mcmoddev.communitymod.commoble.gnomes;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockGnode extends BlockContainer
{
	protected BlockGnode(Material mat)
	{
		super(mat);
	}

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
	@Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
		TileEntity ent = world.getTileEntity(pos);
		if (ent != null && ent instanceof TileEntityGnode)
		{
			((TileEntityGnode)ent).onDestroy();
		}
		super.breakBlock(world, pos, state);
    }

}
