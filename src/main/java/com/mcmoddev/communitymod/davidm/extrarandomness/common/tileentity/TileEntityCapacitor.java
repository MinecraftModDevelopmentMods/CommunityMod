package com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.ExtraRandomness;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketRequestUpdateTileEntity;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumSideConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;

public class TileEntityCapacitor extends TileEntity implements ITickable {

	private EnumCapacitor enumCapacitor;
	private int currentPower;
	
	private EnumSideConfig[] sideConfig;
	
	public TileEntityCapacitor() {
		super();
		
		this.enumCapacitor = EnumCapacitor.TIER_0; // Just in case.
		this.sideConfig = new EnumSideConfig[6];
		for (int i = 0; i < this.sideConfig.length; i++) {
			this.sideConfig[i] = EnumSideConfig.NONE;
		}
	}
	
	public void onRightClick(EntityPlayer player, EnumFacing facing) {
		player.sendMessage(new TextComponentString(String.format("%s/%s", this.currentPower, this.enumCapacitor.getPower())));
	}
	
	public void setCapacitorData(EnumCapacitor enumCapacitor) {
		this.enumCapacitor = enumCapacitor;
	}
	
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
