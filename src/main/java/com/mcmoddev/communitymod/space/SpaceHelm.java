package com.mcmoddev.communitymod.space;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class SpaceHelm extends ItemArmor {
	public SpaceHelm() {
		super(Space.SPACEHELM, 0, EntityEquipmentSlot.HEAD);
		setRegistryName("spacehelm");
        setTranslationKey(CommunityGlobals.MOD_ID + ".spacehelm");
		setCreativeTab(CommunityGlobals.TAB);
	}
}
