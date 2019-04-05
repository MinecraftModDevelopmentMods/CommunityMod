package com.mcmoddev.communitymod.davidm.extrarandomness.core.memepower;

import net.minecraft.util.EnumFacing;

public interface IMemePowerContainer {

	public int getPower();
	
	public boolean isFull();
	
	public boolean canReceivePowerFrom(EnumFacing facing);
	
	public void receivePower(int power);
}
