package com.mcmoddev.communitymod.shootingstar.base.item;

import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import net.minecraft.item.Item;

public class StarItem extends Item {
	protected String modId;
	protected String name;

	public StarItem(String modId, String name) {
		this.modId = modId;
		this.name = name;
		this.setRegistryName(modId, name);
		this.setTranslationKey(modId + "." + name);
		ShootingStar.registerModel(new ModelCompound(modId, this));
	}

	public String getModId() {
		return modId;
	}

	public String getName() {
		return name;
	}
}
