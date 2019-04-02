package com.mcmoddev.communitymod.blockyentities;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CellDataStorage {

	public static final String DATA_NAME = CommunityGlobals.MOD_ID + "_CellData";

	// TODO Names don't seem to load properly

	@SubscribeEvent
	public static void onLoad(WorldEvent.Load event) {
		if (event.getWorld().isRemote)
			return;
		if (event.getWorld().provider.getDimensionType() != StorageDimReg.storageDimensionType)
			return;

		MapStorage storage = event.getWorld().getPerWorldStorage();
		CellSavedWorldData instance = (CellSavedWorldData) storage.getOrLoadData(CellSavedWorldData.class, DATA_NAME);
	}

	public static CellSavedWorldData getCellData(World world) {
		CellSavedWorldData instance = (CellSavedWorldData) world.getPerWorldStorage()
				.getOrLoadData(CellSavedWorldData.class, CellDataStorage.DATA_NAME);
		if (instance == null) {
			instance = new CellSavedWorldData();
			world.getPerWorldStorage().setData(CellDataStorage.DATA_NAME, instance);
		}
		return instance;

	}

	public static BlockPos findFreeSpace() {
		CellSavedWorldData data = getCellData(DimensionManager.getWorld(StorageDimReg.storageDimensionType.getId()));
		return new BlockPos((BlockyEntities.MaxRocketSize * data.getIndex()) + 8, 0, 0);
	}
}
