package com.mcmoddev.communitymod.commoble.gnomes.ai.job;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnome;
import com.mcmoddev.communitymod.commoble.gnomes.NetworkHandler;
import com.mcmoddev.communitymod.commoble.gnomes.util.EnumAssignType;
import com.mcmoddev.communitymod.commoble.gnomes.util.GnomeAssignment;
import com.mcmoddev.communitymod.commoble.gnomes.util.WorldHelper;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class JobSetBlock extends Job
{
	private GnomeAssignment assign;	// remembered to facilitate returning it
	
	public JobSetBlock(EntityGnome gnome, GnomeAssignment assign)
	{
		super(gnome, assign.pos);
		this.assign = assign;
	}

	@Override
	public void finishJob(boolean near)
	{
		if (near)
		{
			IBlockState oldstate_actual = this.gnome.world.getBlockState(this.pos);
			Block oldblock_actual = oldstate_actual.getBlock();
			if (oldblock_actual == this.assign.oldblock)	// no mismatch, continue
			{
				if (!this.gnome.world.isRemote)
				{	
					// if destroying a block, need line of sight
					if (this.assign.type == EnumAssignType.DESTROY || this.assign.type == EnumAssignType.HARVEST)
					{
						// first, check if there's another block in the way
						BlockPos hitpos = WorldHelper.getTargetBlockTowards(this.gnome.world, this.gnome, this.pos);
						
						if (hitpos != null && !hitpos.equals(this.pos) && !this.gnome.world.isAirBlock(hitpos)) // if there's another block in the way, potentially break it
						{
							Block hitblock = WorldHelper.getBlock(this.gnome.world, hitpos);
							if (this.gnome.canDigBlock(hitpos))	// if can dig, break it
							{
								GnomeAssignment newAssign = new GnomeAssignment(hitpos, hitblock, Blocks.AIR, EnumAssignType.DESTROY);
								this.gnome.finishSetBlock(newAssign, true, false, false);
								this.setBlock(newAssign);
							}
							// finish the original setblock job to return it to the gnode if necessary
							this.gnome.finishSetBlock(this.assign, true, true, true);
						}
						else
						{
							// otherwise, continue
							this.gnome.finishSetBlock(this.assign, true, false, true);	
							this.setBlock(this.assign);
						}
					}
					else
					{
						this.gnome.finishSetBlock(this.assign, true, false, true);	
						this.setBlock(this.assign);
					}
				}
			}
			else	// mismatch, invalid job, return to blueprint
			{
				this.gnome.finishSetBlock(this.assign, true, true, true);
			}
		}
		else	// failed and not near yet
		{// do gnome-specific end-of-job stuff
			this.gnome.finishSetBlock(this.assign, false, false, true);
		}
	}
	
	private void setBlock(GnomeAssignment assignment)
	{
		
		// play block-placing sound effect if placing or changing a block
		Block newblock = assignment.newblock;
		IBlockState state = newblock.getDefaultState();
		if (assignment.type == EnumAssignType.CREATE || assignment.type == EnumAssignType.ALTER)
		{
			SoundType blockSoundType = assignment.newblock.getSoundType(newblock.getDefaultState(), this.gnome.world, assignment.pos, this.gnome);
			SoundEvent sound = blockSoundType.getPlaceSound();
			this.gnome.world.playSound(null, assignment.pos, sound, SoundCategory.BLOCKS, (blockSoundType.getVolume() + 1.0F) / 2.0F, blockSoundType.getPitch() * 0.8F);
		}	// play block-breaking sound effect if breaking a block
		else if (assignment.type == EnumAssignType.DESTROY || assignment.type == EnumAssignType.HARVEST)
		{
			SoundType blockSoundType = assignment.oldblock.getSoundType(newblock.getDefaultState(), this.gnome.world, assignment.pos, this.gnome);
			SoundEvent sound = blockSoundType.getBreakSound();
			this.gnome.world.playSound(null, assignment.pos, sound, SoundCategory.BLOCKS, (blockSoundType.getVolume() + 1.0F) / 2.0F, blockSoundType.getPitch() * 0.8F);

			// send particle event packet to clients
			NetworkHandler.broadcastBreakParticles(this.gnome.world, assignment.oldblock, assignment.pos);
		}
		
		
		this.gnome.world.setBlockState(assignment.pos, state);
		this.assign.newblock.onBlockPlacedBy(this.gnome.world, assignment.pos, state, this.gnome, new ItemStack(assignment.newblock, 1));
	}
	
}
