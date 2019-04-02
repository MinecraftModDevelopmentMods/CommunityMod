package com.mcmoddev.communitymod.musksrockets.packets;

import com.mcmoddev.communitymod.musksrockets.BaseVehicleEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SeatSyncPacket implements IMessage {

	long pos;
	int ID;
	int index;
	
	public SeatSyncPacket(long p, int id, int i) {
		pos = p;
		ID = id;
		index = i;
	}
	
	public SeatSyncPacket() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		ID = buf.readInt();
		pos = buf.readLong();
		index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(ID);
		buf.writeLong(pos);
		buf.writeInt(index);
	}

	public static class Handler implements IMessageHandler<SeatSyncPacket, IMessage> {
		@Override
		public IMessage onMessage(SeatSyncPacket message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(SeatSyncPacket message, MessageContext ctx) {
			Entity e = Minecraft.getMinecraft().world.getEntityByID(message.ID);
			if(e instanceof BaseVehicleEntity) {
				//TODO ((BaseVehicleEntity)e).addSeatOffset(BlockPos.fromLong(message.pos), message.index);;
			}
		}
	}
	
}
