package com.mcmoddev.communitymod.commoble.gnomes;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler
{
	public static final String NETWORK_ID = "gnomes";
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(NETWORK_ID);
	public static int id = 0;
	
	static
	{
		// first param is the packet handler class
		// second param is the packet class itself
		// third param is an ID
		// fourth param is the side the packet is being received on
		
		INSTANCE.registerMessage(PacketBreakParticle.PacketBreakParticleReceiver.class,
				PacketBreakParticle.class,
				id++,
				Side.CLIENT);
	}
	
	public static void broadcastBreakParticles(World world, Block block, BlockPos pos)
	{
		PacketBreakParticle packet = new PacketBreakParticle(block, pos);
		TargetPoint point = new TargetPoint(world.provider.getDimension(),
				pos.getX(), pos.getY(), pos.getZ(), 15);
		INSTANCE.sendToAllAround(packet, point);
	}
	
}
