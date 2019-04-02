package com.mcmoddev.communitymod.blockyentities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class ShipCoreTE extends TileEntity implements ITickable {

	private boolean doScan = false;
    public List<BlockPos> toConstruct = new ArrayList<BlockPos>();
    private List<BlockPos> scanned = new ArrayList<BlockPos>();
    private EntityPlayer callback;
    
    public static final BlockPos[] search = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0),
    	    new BlockPos(0, 1, 0), new BlockPos(0, -1, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };

    @Override
    public void update() {
	if (doScan && toConstruct.size() < BlockyEntities.MaxRocketSize) {
	    doScan = false;
	    if (toConstruct.isEmpty())
		toConstruct.add(pos);

	    int max = 0;

	    List<BlockPos> toAdd = new ArrayList<BlockPos>();
	    List<BlockPos> toAddScanned = new ArrayList<BlockPos>();
	    for (BlockPos b : toConstruct) {
		// if (!scanned.contains(b)) {
		List<BlockPos> bp = searchAdjBlocks(b);
		if (!bp.isEmpty())
		    doScan = true;
		toAdd.addAll(bp);
		// toAddScanned.add(b);
		// }
		++max;
		if (max > 100)
		    break;
	    }
	    toConstruct.addAll(toAdd);
	    // scanned.addAll(toAddScanned);

	    if (!world.isRemote) {
		WorldServer ws = (WorldServer) world;
		for (BlockPos b : toConstruct) {
		    ws.spawnParticle(EnumParticleTypes.REDSTONE, 0.5f + b.getX(), 0.5f + b.getY(), 0.5f + b.getZ(), 1,
			    0, 1, 0, 0, null);
		}
	    }
	    if (doScan == false)
		callback.sendMessage(new TextComponentString(
			TextFormatting.RED + "Ship scan complete! Right-click again to assemble."));
	}

    }

    private List<BlockPos> searchAdjBlocks(BlockPos pos) {
	List<BlockPos> found = new ArrayList<BlockPos>();
	for (BlockPos bp : search) {
	    BlockPos apos = pos.add(bp);
	    if (world.getBlockState(apos).getBlock() != Blocks.AIR && !toConstruct.contains(apos)) {
		found.add(apos);
	    }
	}
	return found;
    }

    public void construct() {
	Area area = new Area(toConstruct);
	BlockPos freespace = CellDataStorage.findFreeSpace();
	BlockPos core = pos.subtract(new BlockPos(area.startX, area.startY, area.startZ).subtract(freespace));
		
	area.transformAreaAndContents(freespace, world, DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()), pos);
	
	CellDataStorage.getCellData(DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId())).addStorageCell("Test",
		Arrays.stream(area.serialize()).boxed().toArray(Integer[]::new));
	
	BaseVehicleEntity bve = new BaseVehicleEntity(world, area, core);
	bve.setPositionAndUpdate((float)pos.getX(), pos.getY(), (float)pos.getZ());
	world.spawnEntity(bve);
    }

    public void startSearch(EntityPlayer player) {
	doScan = true;
	callback = player;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
	return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
	NBTTagCompound nbtTag = new NBTTagCompound();
	this.writeToNBT(nbtTag);
	return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
	this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
	super.readFromNBT(compound);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
	super.writeToNBT(compound);
	return compound;
    }
}
