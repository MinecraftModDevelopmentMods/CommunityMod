package com.mcmoddev.communitymod.commoble.gnomes.ai.job;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnomeWood;
import com.mcmoddev.communitymod.commoble.gnomes.SubmodGnomes;
import com.mcmoddev.communitymod.commoble.gnomes.TileEntityGnomeCache;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class JobChestReturn extends Job
{
	public JobChestReturn(EntityGnomeWood ent, BlockPos loc)
	{
		super(ent, loc);
	}

	@SuppressWarnings("static-access")
	@Override
	public void finishJob(boolean near)
	{
		EntityGnomeWood gnomewood = (EntityGnomeWood)(this.gnome);
		TileEntityGnomeCache cache = (TileEntityGnomeCache) gnomewood.gnode;
		
		if (near && cache != null && cache.chestReady.size() > 0)
		{
			// mark chest spot as occupied
			BlockPos loc = cache.chestReady.poll();
			cache.chestOccupied.add(loc);
			// if this is still an air block, set as chest and move inventory
			if (gnomewood.world.isAirBlock(loc))
			{
				gnomewood.world.setBlockState(loc, SubmodGnomes.gnome_proof_chest.getDefaultState());
				TileEntityChest te = (TileEntityChest)gnomewood.world.getTileEntity(loc);
				for (int i = 0; i<27; i++)
				{
					te.setInventorySlotContents(i, gnomewood.inventory[i]);
					gnomewood.inventory[i] = ItemStack.EMPTY;
				}
			}
			else
			{
				Vec3d dumpVec = this.getRandomSurfaceVec(gnomewood, 3, 3);
				dumpVec = this.groundify(gnomewood.world, dumpVec);
				BlockPos pos = new BlockPos(dumpVec);
				gnomewood.world.setBlockState(pos, Blocks.CHEST.getDefaultState());
				TileEntityChest te = (TileEntityChest)gnomewood.world.getTileEntity(pos);
				for (int i = 0; i<27; i++)
				{
					te.setInventorySlotContents(i, gnomewood.inventory[i]);
					gnomewood.inventory[i] = ItemStack.EMPTY;
				}
			}

			gnomewood.setCarriedToAir();
		}
	}
	
}
