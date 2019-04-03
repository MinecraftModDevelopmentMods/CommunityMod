package com.mcmoddev.communitymod.davidm.extrarandomness.core.memepower;

import net.minecraft.util.EnumFacing;

public interface IMemePowerReceiver extends IMemePowerBlock {

	public boolean isFull();
	
	public void canReceivePower(EnumFacing facing);
	
	public void receivePower();
}
