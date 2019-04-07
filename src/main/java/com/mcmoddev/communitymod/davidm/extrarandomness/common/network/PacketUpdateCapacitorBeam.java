package com.mcmoddev.communitymod.davidm.extrarandomness.common.network;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityCapacitor;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketUpdateCapacitorBeam implements IMessage {

	private BlockPos pos;
	private BlockPos[] beams;
	
	public PacketUpdateCapacitorBeam() {
		this.beams = new BlockPos[6];
	}
	
	public PacketUpdateCapacitorBeam(TileEntityCapacitor capacitor) {
		this(capacitor.getPos(), capacitor.getBeams());
	}
	
	public PacketUpdateCapacitorBeam(BlockPos pos, BlockPos[] beams) {
		this.pos = pos;
		this.beams = beams;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		for (int i = 0; i < this.beams.length; i++) {
			this.beams[i] = BlockPos.fromLong(buf.readLong());
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		for (BlockPos i: this.beams) {
			buf.writeLong(i.toLong());
		}
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateCapacitorBeam, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateCapacitorBeam message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					
					@Override
					public void run() {
						TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(message.pos);
						if (tileEntity instanceof TileEntityCapacitor) {
							((TileEntityCapacitor) tileEntity).setBeams(message.beams);
						}
					}
				});
			} else {
				System.out.println("Bad packet (wrong side: SERVER)!");
			}
			return null;
		}
	}
}
