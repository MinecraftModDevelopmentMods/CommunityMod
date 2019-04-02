package com.mcmoddev.communitymod.musksrockets.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mcmoddev.communitymod.musksrockets.BaseVehicleEntity;
import com.mcmoddev.communitymod.musksrockets.BlockStorage;
import com.mcmoddev.communitymod.musksrockets.StorageDimReg;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class VehicleUpdatePacket implements IMessage {

	int ID;
	Map<BlockPos, BlockStorage> blockMap;

	public VehicleUpdatePacket(int id, Map<BlockPos, BlockStorage> map) {
		ID = id;
		blockMap = map;
	}

	public VehicleUpdatePacket() {}


	@Override
	public void toBytes(ByteBuf buf) {
		World w = DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId());
		buf.writeInt(ID);
		buf.writeInt(blockMap.size());
		for (Entry<BlockPos, BlockStorage> e : blockMap.entrySet()) {
			buf.writeLong(e.getKey().toLong());
			e.getValue().toBuf(buf);
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		ID = buf.readInt();
		int s = buf.readInt();
		blockMap = new HashMap<BlockPos, BlockStorage>();
		for (int i = 0; i < s; ++i) {
			BlockPos bp = BlockPos.fromLong(buf.readLong());
			BlockStorage bs = new BlockStorage();
			bs.fromBuf(buf, Minecraft.getMinecraft().world);
			blockMap.put(bp, bs);
		}
	}

	public static class Handler implements IMessageHandler<VehicleUpdatePacket, IMessage> {
		@Override
		public IMessage onMessage(VehicleUpdatePacket message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(VehicleUpdatePacket message, MessageContext ctx) {
			if (ctx.side.isClient()
					&& Minecraft.getMinecraft().world.getEntityByID(message.ID) instanceof BaseVehicleEntity) {
				BaseVehicleEntity entity = (BaseVehicleEntity) Minecraft.getMinecraft().world.getEntityByID(message.ID);
				for (Entry<BlockPos, BlockStorage> entry : message.blockMap.entrySet()) {
					entity.getStorage().blockMap.get(entry.getKey()).blockstate = entry.getValue().blockstate;
					entity.getStorage().blockMap.get(entry.getKey()).light = entry.getValue().light;
					entity.getStorage().blockMap.get(entry.getKey()).tileentity = entry.getValue().tileentity;
					if(entity.getStorage().blockMap.get(entry.getKey()).tileentity != null)
						entity.getStorage().blockMap.get(entry.getKey()).tileentity.setWorld(entity.getStorage());
				}
				entity.setEntityBoundingBox(entity.getEncompassingBoundingBox());
				entity.getStorage().setTESRs();
				entity.getStorage().updateRequired = true;
			}
		}
	}
}
