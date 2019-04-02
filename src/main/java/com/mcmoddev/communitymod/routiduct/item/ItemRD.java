package com.mcmoddev.communitymod.routiduct.item;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.item.Item;
import com.mcmoddev.communitymod.routiduct.RoutiductConstants;

/**
 * Created by Prospector
 */
public class ItemRD extends Item {

	public ItemRD(String name) {
		super();
		this.setRegistryName(RoutiductConstants.MOD_ID, name);
		this.setTranslationKey(RoutiductConstants.MOD_ID + "." + name);
		setCreativeTab(CommunityGlobals.TAB);
	}
}
