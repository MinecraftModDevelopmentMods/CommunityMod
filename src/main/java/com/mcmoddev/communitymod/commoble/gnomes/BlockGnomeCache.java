package com.mcmoddev.communitymod.commoble.gnomes;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockGnomeCache extends BlockGnode
{

	public BlockGnomeCache()
	{
		super(Material.GROUND);
		this.setHardness(0.5F);
        this.blockSoundType = SoundType.GROUND;
	}

	@Override	// this is createNewTileEntity, I think
	public TileEntity createNewTileEntity(World world, int num)
	{
		return new TileEntityGnomeCache();
	}

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Blocks.DIRT.getItemDropped(state, rand, fortune);
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
    	return EnumBlockRenderType.MODEL;
    }
}
