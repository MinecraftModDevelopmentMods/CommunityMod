package com.mcmoddev.communitymod.shootingstar.base.block;

import com.mcmoddev.communitymod.shootingstar.base.blockentity.StarBlockEntity;
import net.minecraft.block.material.Material;

public class StarBlockWithEntity extends StarBlock {
	protected Class<? extends StarBlockEntity> blockEntityClass;

	public StarBlockWithEntity(String modId, String name, Material materialIn, Class<? extends StarBlockEntity> blockEntityClass) {
		super(modId, name, materialIn);
		this.blockEntityClass = blockEntityClass;
	}

	public StarBlockWithEntity(String modId, String name, Class<? extends StarBlockEntity> blockEntityClass) {
		this(modId, name, Material.ROCK, blockEntityClass);
	}

	public Class<? extends StarBlockEntity> getBlockEntityClass() {
		return blockEntityClass;
	}
}
