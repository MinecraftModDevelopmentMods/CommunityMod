package com.mcmoddev.communitymod.musksrockets;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class StorageWorldProvider extends WorldProvider{

    @Override
    public DimensionType getDimensionType() {
	return StorageDimReg.storageDimensionType;
    }
    
    @Override
    public String getSaveFolder() {
        return "shipstorage";
    }
    
    @Override
    public IChunkGenerator createChunkGenerator() {
        return new StorageChunkGenerator(world);
    }

}