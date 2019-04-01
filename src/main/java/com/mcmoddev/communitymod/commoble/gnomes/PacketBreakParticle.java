package com.mcmoddev.communitymod.commoble.gnomes;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketBreakParticle implements IMessage
{
	private Block block;
	private BlockPos pos;
	
	/**
	 * Order of bytebuffer data is:
	 * block ID
	 * posx
	 * posy
	 * posz
	 */
	
	public PacketBreakParticle()
	{
		
	}
	
	public PacketBreakParticle(Block block, BlockPos pos)
	{
		this.block = block;
		this.pos = pos;
	}
	
	// read data from incoming bytes
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.block = Block.getBlockById(buf.readInt());
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	// write data to outgoing bytes
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(Block.getIdFromBlock(this.block));
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
	}
	
	// format is <REQ, REPLY>
	// <message received, message sent in response>
	
	public static class PacketBreakParticleReceiver implements IMessageHandler<PacketBreakParticle, IMessage>
	{

		@Override
		public IMessage onMessage(PacketBreakParticle message,
				MessageContext ctx)
		{
			// when packet is received, create block-breakage particle at location
			Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(message.pos, message.block.getDefaultState());
			return null;
		}
		
	}
}
