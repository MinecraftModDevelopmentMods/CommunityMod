package com.mcmoddev.communitymod.commoble.gnomes.ai.job;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnomeWood;
import com.mcmoddev.communitymod.commoble.gnomes.TileEntityGnomeCache;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;

public class JobChestSteal extends Job
{
	//private Vec3 vecToChest;

	public JobChestSteal(EntityGnomeWood ent, BlockPos loc)
	{
		super(ent, loc);
	}
	
	public void finishJob(boolean near)
	{
		EntityGnomeWood gnomewood = (EntityGnomeWood)this.gnome;
		TileEntityGnomeCache cache = (TileEntityGnomeCache) gnomewood.gnode;
		if (near && gnomewood.gnode != null)
		{
			// if this is still a chest
			IBlockState state = gnomewood.world.getBlockState(this.pos);
			if (state.getBlock() == Blocks.CHEST)
			{
				TileEntityChest te = (TileEntityChest)gnomewood.world.getTileEntity(this.pos);
				if (te.numPlayersUsing == 0)
				{
					// move contents of chest from chest to gnome
					for (int i = 0; i < 27; i++)
					{
						gnomewood.inventory[i] = te.getStackInSlot(i);
						te.setInventorySlotContents(i, ItemStack.EMPTY);
					}
					if (!gnomewood.world.isRemote)
					{
						gnomewood.world.setBlockToAir(this.pos);
						gnomewood.setCarriedState(state);
					}
				}
			}
			cache.resetChestTracker();
		}
	}
	
}
