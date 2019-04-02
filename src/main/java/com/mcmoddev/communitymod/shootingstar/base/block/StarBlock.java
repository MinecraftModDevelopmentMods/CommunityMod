package com.mcmoddev.communitymod.shootingstar.base.block;

import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;

public class StarBlock extends Block {
	protected String modId;
	protected String name;

	public StarBlock(String modId, String name, Material materialIn) {
		super(materialIn);
		this.modId = modId;
		this.name = name;
		setRegistryName(modId, name);
		ShootingStar.registerModel(new ModelCompound(modId, this, "block").setInvVariant("normal"));

	}

	public StarBlock(String modId, String name) {
		this(modId, name, Material.ROCK);
	}

	public String getModId() {
		return modId;
	}

	public String getName() {
		return name;
	}
}
