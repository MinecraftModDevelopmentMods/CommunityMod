package com.mcmoddev.communitymod.davidm.extrarandomness.core;

import net.minecraft.block.material.Material;

public enum EnumCapacitor {

	TIER_0(5, Material.ROCK, 32000, 100),
	TIER_1(5, Material.ROCK, 128000, 1280),
	TIER_2(5, Material.ROCK, 512000, 5120),
	TIER_3(50, Material.ROCK, 2048000, 20480),
	TIER_4(100, Material.ROCK, 2000000000, 2000000000);
	
	private int hardness;
	private Material material;
	private int power;
	private int transferRate;
	
	private EnumCapacitor(int hardness, Material material, int power, int transferRate) {
		this.hardness = hardness;
		this.material = material;
		this.power = power;
		this.transferRate = transferRate;
	}

	public int getHardness() {
		return this.hardness;
	}

	public Material getMaterial() {
		return this.material;
	}

	public int getPower() {
		return this.power;
	}
	
	public int getTransferRate() {
		return this.transferRate;
	}
	
	public String getStringTier() {
		return String.valueOf(this.ordinal());
	}
}
