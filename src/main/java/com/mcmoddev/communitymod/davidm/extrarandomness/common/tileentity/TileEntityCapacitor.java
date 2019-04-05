package com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.ExtraRandomness;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketRequestUpdateTileEntity;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumCapacitor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityCapacitor extends TileEntity implements ITickable {

	private EnumCapacitor enumCapacitor;
	private int currentPower;
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			ExtraRandomness.network.sendToServer(new PacketRequestUpdateTileEntity(this));
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.enumCapacitor = EnumCapacitor.values()[compound.getInteger("enumCapacitor")];
		this.currentPower = compound.getInteger("power");
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("enumCapacitor", this.enumCapacitor.ordinal());
		compound.setInteger("power", this.currentPower);
		return super.writeToNBT(compound);
	}
}
