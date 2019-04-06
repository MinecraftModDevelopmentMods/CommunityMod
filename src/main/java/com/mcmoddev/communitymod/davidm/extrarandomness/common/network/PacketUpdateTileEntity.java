package com.mcmoddev.communitymod.davidm.extrarandomness.common.network;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityAltar;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketUpdateTileEntity implements IMessage {
	
	private BlockPos pos;
	private NBTTagCompound compound;
	
	public PacketUpdateTileEntity() {
		
	}
	
	public PacketUpdateTileEntity(TileEntity tileEntity) {
		this(tileEntity.getPos(), tileEntity.writeToNBT(new NBTTagCompound()));
	}
	
	public PacketUpdateTileEntity(BlockPos pos, NBTTagCompound compound) {
		this.pos = pos;
		this.compound = compound;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		ByteBufUtils.writeTag(buf, this.compound);
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateTileEntity, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateTileEntity message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					
					@Override
					public void run() {
						TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(message.pos);
						if (tileEntity != null) {
							tileEntity.readFromNBT(message.compound);
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
