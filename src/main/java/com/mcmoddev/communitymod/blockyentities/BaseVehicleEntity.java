package com.mcmoddev.communitymod.blockyentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mcmoddev.communitymod.CommunityMod;
import com.mcmoddev.communitymod.musksrockets.packets.VehicleUpdatePacket;
import com.mcmoddev.communitymod.space.SpaceWorldProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class BaseVehicleEntity extends Entity implements IEntityAdditionalSpawnData {

	private static final DataParameter<Area> QUBE = EntityDataManager.createKey(BaseVehicleEntity.class,
			HDataSerializers.AREA);
	private static final DataParameter<BlockPos> OFFSET = EntityDataManager.createKey(BaseVehicleEntity.class,
			DataSerializers.BLOCK_POS);

	List<BlockPos> seatOffsets = new ArrayList<BlockPos>();

	ShipStorage storage;
	Ticket loadTicket;

	public BaseVehicleEntity(World worldIn, Area a, BlockPos os) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
		storage = new ShipStorage(os, worldIn);
		dataManager.set(QUBE, a);
		dataManager.set(OFFSET, os);
		getArea().loadShipIntoStorage(DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()), this);
	}

	public BaseVehicleEntity(World worldIn) {
		super(worldIn);
		storage = new ShipStorage(getOffset(), worldIn);
		Ticket tic = ForgeChunkManager.requestTicket(CommunityMod.INSTANCE,
				DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()), Type.NORMAL);
		loadTicket = tic;
	}

	@Override
	protected void entityInit() {
		dataManager.register(QUBE, new Area());
		dataManager.register(OFFSET, new BlockPos(0, 0, 0));
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		World w = DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId());
		List<BlockPos> bpl = getArea().scanArea(w);
		buffer.writeInt(bpl.size());
		for (Entry<BlockPos, BlockStorage> e : storage.blockMap.entrySet()) {
			buffer.writeLong(e.getKey().toLong());
			e.getValue().toBuf(buffer);
		}
		setEntityBoundingBox(getEncompassingBoundingBox());
		getChunksToLoad();
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		int s = additionalData.readInt();
		for (int i = 0; i < s; ++i) {
			BlockPos bp = BlockPos.fromLong(additionalData.readLong());
			BlockStorage bs = new BlockStorage();
			bs.fromBuf(additionalData, storage);
			storage.blockMap.put(bp, bs);
		}
		storage.setTESRs();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		Area a = new Area();
		a.deserialize(compound.getIntArray("qube"));
		dataManager.set(QUBE, a);
		int[] o = compound.getIntArray("offset");
		BlockPos offset = new BlockPos(o[0], o[1], o[2]);
		dataManager.set(OFFSET, offset);
		getArea().loadShipIntoStorage(DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()), this);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setIntArray("qube", dataManager.get(QUBE).serialize());
		BlockPos b = dataManager.get(OFFSET);
		compound.setIntArray("offset", new int[] { b.getX(), b.getY(), b.getZ() });
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.isServerWorld()) {
			BlockPos pos = new BlockPos(this);
			if (motionY < 0 && (!world
					.isAirBlock(pos.down((int) ((getEntityBoundingBox().maxY - getEntityBoundingBox().minY) / 2)))
					|| world.provider instanceof SpaceWorldProvider))
				return;
			if (!this.world.isRemote
					|| this.world.isBlockLoaded(pos) && this.world.getChunk(pos).isLoaded()) {
				if (!this.hasNoGravity()) {
					this.motionY -= 0.08D;
				}
			} else if (this.posY > 0.0D) {
				this.motionY = -0.1D;
			} else {
				this.motionY = 0.0D;
			}
			this.motionY *= 0.98D;
			this.motionX *= 0.91D;
			this.motionZ *= 0.91D;
		}
	}

	private boolean isServerWorld() {
		return !world.isRemote;
	}

	public Area getArea() {
		return dataManager.get(QUBE);
	}

	public BlockPos getOffset() {
		return dataManager.get(OFFSET);
	}

	public Map<BlockPos, BlockStorage> getBlocks() {
		return storage.blockMap;
	}

	public void changeArea(List<BlockPos> li) {
		dataManager.set(QUBE, getArea().update(li));
	}

	public void setBlockStates(Map<BlockPos, BlockStorage> s) {
		for (BlockPos bp : s.keySet()) {
			if (bp != null) {
				storage.blockMap.put(bp, s.get(bp));
			}
		}
	}

	public void addBlockState(BlockPos b, BlockStorage s) {
		storage.blockMap.put(b, s);
	}

	public ShipStorage getStorage() {
		return storage;
	}

	public BlockPos globalBlockPosToLocal(BlockPos b) {
		return b.subtract(getPosition()).add(getOffset());
	}

	public BlockPos localBlockPosToGlobal(BlockPos b) {
		return b.add(getPosition()).subtract(getOffset());
	}

	public Vec3d globalVecToLocal(Vec3d v) {
		return v.subtract(posX, posY, posZ).add(new Vec3d(getOffset().getX(), getOffset().getY(), getOffset().getZ()));
	}

	public Vec3d localVecToGlobal(Vec3d v) {
		return v.add(new Vec3d(getPosition().getX(), getPosition().getY(), getPosition().getZ()))
				.subtract(getOffset().getX(), getOffset().getY(), getOffset().getZ());
	}

	public AxisAlignedBB getEncompassingBoundingBox() {
		Area a = getArea();
		return new AxisAlignedBB(
				new Vec3d(a.startX, a.startY, a.startZ)
						.subtract(getOffset().getX(), getOffset().getY(), getOffset().getZ()).add(getPositionVector()),
				new Vec3d(a.endX + 1, a.endY + 1, a.endZ + 1)
						.subtract(getOffset().getX(), getOffset().getY(), getOffset().getZ()).add(getPositionVector()));
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return getEntityBoundingBox().grow(2);
	}

	public void getChunksToLoad() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		for (Entry<BlockPos, BlockStorage> bs : getBlocks().entrySet()) {
			Chunk chunk = DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()).getChunk(bs.getKey());
			if (!chunks.contains(chunk))
				chunks.add(chunk);
		}
		for (Chunk chunk : chunks) {
			ForgeChunkManager.forceChunk(loadTicket, chunk.getPos());
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (world.isRemote)
			return true;
		World ws = DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId());
		RayTraceResult ray = getLook(player, ws);

		if (ray == null)
			return false;
		switch (ray.typeOfHit) {
		case MISS:
			return false;
		case BLOCK:
			/*if (ws.getBlockState(ray.getBlockPos()).getBlock() == HBlocks.captainschair) {
				player.startRiding(this);
				seatOffsets.add(getPassengers().indexOf(player), ray.getBlockPos().subtract(getOffset()));
				PacketHandler.INSTANCE
						.sendToAllTracking(new SeatSyncPacket(ray.getBlockPos().subtract(getOffset()).toLong(),
								getEntityId(), getPassengers().indexOf(player)), this);
			}*/
			ws.getBlockState(ray.getBlockPos()).getBlock().onBlockActivated(ws, ray.getBlockPos(),
					ws.getBlockState(ray.getBlockPos()), player, hand, ray.sideHit, (float) ray.hitVec.x,
					(float) ray.hitVec.y, (float) ray.hitVec.z);
			Map<BlockPos, BlockStorage> bm = new HashMap<BlockPos, BlockStorage>();
			for (BlockPos bp : getArea().scanArea(DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()))) {
				BlockStorage bs = new BlockStorage(DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()).getBlockState(bp),
						DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()).getTileEntity(bp),
						DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()).getLight(bp));
				bm.put(bp, bs);
			}
			getStorage().blockMap.clear();
			getStorage().blockMap.putAll(bm);
			PacketHandler.INSTANCE.sendToAllTracking(new VehicleUpdatePacket(this.getEntityId(), bm), this);

			return true;
		case ENTITY:
			ray.entityHit.processInitialInteract(player, hand);
			Map<BlockPos, BlockStorage> bm1 = new HashMap<BlockPos, BlockStorage>();
			for (BlockPos bp : getArea().scanArea(DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()))) {
				BlockStorage bs = new BlockStorage(DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()).getBlockState(bp),
						DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()).getTileEntity(bp),
						DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()).getLight(bp));
				bm1.put(bp, bs);
			}
			getStorage().blockMap.putAll(bm1);
			PacketHandler.INSTANCE.sendToAllTracking(new VehicleUpdatePacket(this.getEntityId(), bm1), this);
			return true;
		}
		return false;
	}

	public RayTraceResult getLook(EntityPlayer player, World ws) {

		Vec3d eye = player.getPositionEyes(1.0F).subtract(getPositionVector()).add(getOffset().getX(),
				getOffset().getY(), getOffset().getZ());
		Vec3d look = eye.add(player.getLookVec()
				.scale(player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue()));

		RayTraceResult ray = ws.rayTraceBlocks(eye, look, false);
		return ray;
	}

	@Override
	public float getCollisionBorderSize() {
		return 0F;
	}

	@Override
	public void setDead() {
		ForgeChunkManager.releaseTicket(loadTicket);
		super.setDead();
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	/*@Override
	protected void removePassenger(Entity passenger) {
		seatOffsets.remove(getPassengers().indexOf(passenger));
		super.removePassenger(passenger);
	}

	public void addSeatOffset(BlockPos pos, int index) {
		seatOffsets.add(index, pos);
	}

	public boolean canPassengerSteer(EntityPlayer passenger) {
		return storage.getBlockState(seatOffsets.get(getPassengers().indexOf(passenger)))
				.getBlock() == HBlocks.captainschair;
	}*/

	@Override
	public void updatePassenger(Entity passenger) {
		if (this.isPassenger(passenger) && getPassengers().indexOf(passenger) < seatOffsets.size()) {
			passenger.setPosition(
					this.posX + seatOffsets.get(getPassengers().indexOf(passenger)).getX() + 0.5, this.posY
							+ seatOffsets.get(getPassengers().indexOf(passenger)).getY() + passenger.getYOffset() + 0.6,
					this.posZ + seatOffsets.get(getPassengers().indexOf(passenger)).getZ() + 0.5);
			passenger.motionX = motionX;
			passenger.motionY = motionY;
			passenger.motionZ = motionZ;
			passenger.velocityChanged = true;
		}
	}
}
