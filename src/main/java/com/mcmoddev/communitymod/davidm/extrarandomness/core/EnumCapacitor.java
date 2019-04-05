package com.mcmoddev.communitymod.davidm.extrarandomness.core;

import net.minecraft.block.material.Material;

public enum EnumCapacitor {

	TIER_0(5, Material.ROCK, 32000),
	TIER_1(5, Material.ROCK, 128000),
	TIER_2(5, Material.ROCK, 512000),
	TIER_3(50, Material.ROCK, 2048000),
	TIER_4(100, Material.ROCK, 2000000000);
	
	private int hardness;
	private Material material;
	private int power;
	
	private EnumCapacitor(int hardness, Material material, int power) {
		this.hardness = hardness;
		this.material = material;
		this.power = power;
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
}
