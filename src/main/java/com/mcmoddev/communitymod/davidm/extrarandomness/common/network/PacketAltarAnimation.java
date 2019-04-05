package com.mcmoddev.communitymod.davidm.extrarandomness.common.network;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumAltarAnimation;

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

public class PacketAltarAnimation implements IMessage {
	
	private BlockPos pos;
	private EnumAltarAnimation altarAnimation;
	
	public PacketAltarAnimation() {
		
	}
	
	public PacketAltarAnimation(BlockPos pos, EnumAltarAnimation altarAnimation) {
		this.pos = pos;
		this.altarAnimation = altarAnimation;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.altarAnimation = EnumAltarAnimation.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.altarAnimation.ordinal());
	}
	
	public static class Handler implements IMessageHandler<PacketAltarAnimation, IMessage> {

		@Override
		public IMessage onMessage(PacketAltarAnimation message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					
					@Override
					public void run() {
						TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(message.pos);
						if (tileEntity instanceof TileEntityAltar) {
							((TileEntityAltar) tileEntity).altarAnimation = message.altarAnimation;
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
