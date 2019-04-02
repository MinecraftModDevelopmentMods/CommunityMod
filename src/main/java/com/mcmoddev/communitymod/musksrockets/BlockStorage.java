package com.mcmoddev.communitymod.musksrockets;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class BlockStorage {
	
	public IBlockState blockstate;
	public TileEntity tileentity;
	public int light;
	
	public BlockStorage() {}
	
	public BlockStorage(IBlockState b, TileEntity t, int l) {
		blockstate = b;
		tileentity = t;
		light = l;
	}
	
	public void toBuf(ByteBuf buf) {
		buf.writeInt(Block.getStateId(blockstate));
		buf.writeInt(light);
		ByteBufUtils.writeTag(buf, tileentity == null ? null : tileentity.serializeNBT());
	}
	
	public void fromBuf(ByteBuf buf, World world) {
		blockstate = Block.getStateById(buf.readInt());
		light = buf.readInt();
		NBTTagCompound tag = ByteBufUtils.readTag(buf); 
		if(tag != null) {
			tileentity = TileEntity.create(world, tag);
		}
	}
}
