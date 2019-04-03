package com.mcmoddev.communitymod.davidm;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketUpdateAltar implements IMessage {
	
	private BlockPos pos;
	private ItemStack stack;
	
	public PacketUpdateAltar() {
		
	}
	
	public PacketUpdateAltar(TileEntityAltar tileEntityAltar) {
		this(tileEntityAltar.getPos(), tileEntityAltar.getStack());
	}
	
	public PacketUpdateAltar(BlockPos pos, ItemStack stack) {
		this.pos = pos;
		this.stack = stack;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.stack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		ByteBufUtils.writeItemStack(buf, this.stack);
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateAltar, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateAltar message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					
					@Override
					public void run() {
						TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(message.pos);
						if (tileEntity instanceof TileEntityAltar) {
							((TileEntityAltar) tileEntity).setStack(message.stack);
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
