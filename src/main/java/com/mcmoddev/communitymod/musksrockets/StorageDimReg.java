package com.mcmoddev.communitymod.musksrockets;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class StorageDimReg {

	public static DimensionType storageDimensionType;
	
	public static void regStorageDim() {
		registerDimensionTypes();
		registerDimensions();
	}
	
	private static void registerDimensionTypes() {
		storageDimensionType = DimensionType.register(CommunityGlobals.MOD_ID, "_storage", 984637,
				StorageWorldProvider.class, true);
	}
	
	private static void registerDimensions() {
		DimensionManager.registerDimension(storageDimensionType.getId(), storageDimensionType);
	}
	
}
