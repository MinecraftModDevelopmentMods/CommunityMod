package com.mcmoddev.communitymod.davidm.extrarandomness.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRequestUpdateTileEntity implements IMessage {

	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateTileEntity() {
	}
	
	public PacketRequestUpdateTileEntity(TileEntity tileEntityAltar) {
		this(tileEntityAltar.getPos(), tileEntityAltar.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateTileEntity(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.dimension = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.dimension);
	}
	
	public static class Handler implements IMessageHandler<PacketRequestUpdateTileEntity, IMessage> {

		@Override
		public IMessage onMessage(PacketRequestUpdateTileEntity message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
				TileEntity tileEntity = world.getTileEntity(message.pos);
				if (tileEntity != null) return new PacketUpdateTileEntity(tileEntity);
			} else {
				System.out.println("Bad packet (wrong side: CLIENT)!");
			}
			return null;
		}
	}
}
