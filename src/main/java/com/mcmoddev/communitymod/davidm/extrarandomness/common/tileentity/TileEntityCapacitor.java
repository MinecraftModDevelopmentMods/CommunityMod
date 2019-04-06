package com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.ExtraRandomness;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.MemeWrench;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketRequestUpdateTileEntity;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumSideConfig;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.NetworkHelper;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.memepower.IMemePowerContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class TileEntityCapacitor extends TileEntity implements ITickable, IMemePowerContainer {

	private static final int TRANSPORT_RANGE = 8;
	
	private EnumCapacitor enumCapacitor;
	private int currentPower;
	
	private EnumSideConfig[] sideConfigs;
	
	public TileEntityCapacitor() {
		super();
		
		this.enumCapacitor = EnumCapacitor.TIER_0; // Just in case.
		this.sideConfigs = new EnumSideConfig[6];
		for (int i = 0; i < this.sideConfigs.length; i++) {
			this.sideConfigs[i] = EnumSideConfig.NONE;
		}
	}
	
	public void onRightClick(EntityPlayer player, EnumFacing facing) {
		if (player.inventory.getCurrentItem().getItem() instanceof MemeWrench) {
			this.editSideConfig(facing);
			this.markDirty();
			NetworkHelper.sendTileEntityToNearby(this, 64);
		}
		
		player.sendMessage(new TextComponentString(String.format("%s/%s", this.currentPower, this.enumCapacitor.getPower())));
		player.sendMessage(new TextComponentString(String.valueOf(this.sideConfigs[facing.ordinal()].ordinal())));
	}
	
	public void setCapacitorData(EnumCapacitor enumCapacitor) {
		this.enumCapacitor = enumCapacitor;
		this.markDirty();
	}
	
	private void editSideConfig(EnumFacing facing) {
		int facingIndex = facing.ordinal();
		this.sideConfigs[facingIndex] = EnumSideConfig.values()[(this.sideConfigs[facingIndex].ordinal() + 1) % 3];
	}
	
	public EnumSideConfig[] getSideConfigs() {
		return this.sideConfigs;
	}
	
	public double getScaledPower() {
		return (double) this.currentPower / this.enumCapacitor.getPower();
	}
	
	@Override
	public void update() {
		if (!this.world.isRemote && this.world.getTotalWorldTime() % 20 == 0) {
			List<IMemePowerContainer> receivers = new ArrayList<IMemePowerContainer>();
			
			for (int i = 0; i < this.sideConfigs.length; i++) {
				if (this.sideConfigs[i] == EnumSideConfig.OUTPUT) {
					for (int distance = 0; distance < TRANSPORT_RANGE; distance++) {
						EnumFacing facing = EnumFacing.values()[i];
						BlockPos receiverPos = this.pos.offset(facing, distance);
						
						if (this.world.isAirBlock(receiverPos)) continue;
						
						TileEntity tileEntity = this.world.getTileEntity(receiverPos);
						if (tileEntity instanceof IMemePowerContainer) {
							IMemePowerContainer powerContainer = (IMemePowerContainer) tileEntity;
							if (!powerContainer.isFull() && powerContainer.canReceivePowerFrom(facing.getOpposite())) {
								receivers.add(powerContainer);
							}
						}
					}
				}
			}
			
			if (!receivers.isEmpty()) {
				int avg = this.currentPower / receivers.size();
				this.currentPower -= avg * receivers.size();
				receivers.forEach(receiver -> {
					this.currentPower += receiver.receivePower(avg);
				});
			}
			
			this.markDirty();
			NetworkHelper.sendTileEntityToNearby(this, 64);
		}
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			ExtraRandomness.network.sendToServer(new PacketRequestUpdateTileEntity(this));
			this.world.tickableTileEntities.remove(this);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.enumCapacitor = EnumCapacitor.values()[compound.getInteger("enumCapacitor")];
		this.currentPower = compound.getInteger("power");
		int[] sides = compound.getIntArray("sides");
		if (sides.length == 6) {
			for (int i = 0; i < sides.length; i++) {
				if (sides[i] < EnumSideConfig.values().length) {
					this.sideConfigs[i] = EnumSideConfig.values()[sides[i]];
				}
			}
		}
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("enumCapacitor", this.enumCapacitor.ordinal());
		compound.setInteger("power", this.currentPower);
		int[] sides = new int[6];
		for (int i = 0; i < sides.length; i++) {
			sides[i] = this.sideConfigs[i].ordinal();
		}
		compound.setIntArray("sides", sides);
		return super.writeToNBT(compound);
	}

	@Override
	public int getPower() {
		return this.currentPower;
	}

	@Override
	public boolean isFull() {
		return this.currentPower == this.enumCapacitor.getPower();
	}

	@Override
	public boolean canReceivePowerFrom(EnumFacing facing) {
		return this.sideConfigs[facing.ordinal()] == EnumSideConfig.INPUT;
	}

	@Override
	public int receivePower(int power) {
		this.currentPower += power;
		int overflow = Math.max(this.currentPower - this.enumCapacitor.getPower(), 0);
		this.currentPower -= overflow;
		
		this.markDirty();
		NetworkHelper.sendTileEntityToNearby(this, 64);
		return overflow;
	}
}
