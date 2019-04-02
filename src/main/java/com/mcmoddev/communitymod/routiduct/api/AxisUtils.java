package com.mcmoddev.communitymod.routiduct.api;

import com.mcmoddev.communitymod.routiduct.block.BlockRoutiduct;
import net.minecraft.util.EnumFacing;

/**
 * Created by Prospector
 */
public class AxisUtils {

	public static BlockRoutiduct.EnumAxis getAxis(EnumFacing facing) {
		if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
			return BlockRoutiduct.EnumAxis.X;
		} else if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
			return BlockRoutiduct.EnumAxis.Z;
		} else {
			return BlockRoutiduct.EnumAxis.Y;
		}
	}
}
