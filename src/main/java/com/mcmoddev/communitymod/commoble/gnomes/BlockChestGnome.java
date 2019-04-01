package com.mcmoddev.communitymod.commoble.gnomes;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;



/** Identical to a regular chest but it isn't one
 * Used by Wood Gnomes to store their stuff in so they don't
 * steal each other's chests.
 * @author Commoble
 *
 */
public class BlockChestGnome extends BlockChest
{

	public BlockChestGnome()
	{
		super(BlockChest.Type.BASIC);
		this.setCreativeTab(null);
		this.setHardness(2.5F);
		this.setSoundType(SoundType.WOOD);
	}

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Blocks.CHEST.getItemDropped(state, rand, fortune);
    }
}
