package com.mcmoddev.communitymod.davidm.extrarandomness.core.helper;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.ExtraRandomness;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketUpdateTileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class NetworkHelper {

	public static void sendTileEntityToNearby(TileEntity tileEntity, int range) {
		int dimension = tileEntity.getWorld().provider.getDimension();
		BlockPos pos = tileEntity.getPos();
		
		ExtraRandomness.network.sendToAllAround(
				new PacketUpdateTileEntity(tileEntity), 
				new NetworkRegistry.TargetPoint(dimension, pos.getX(), pos.getY(), pos.getZ(), 64)
		);
	}
}
