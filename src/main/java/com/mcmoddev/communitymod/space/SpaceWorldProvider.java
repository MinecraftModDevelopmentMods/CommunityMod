package com.mcmoddev.communitymod.space;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public class SpaceWorldProvider extends WorldProvider {

	@Override
	public DimensionType getDimensionType() {
		return Space.SPACE;
	}
	
	@Override
	public boolean canDoRainSnowIce(Chunk chunk) {
		return false;
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return 0.3f;
	}
	
	@Override
	public float getSunBrightness(float par1) {
		return 0;
	}
	
	@Override
	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
		return new Vec3d(0, 0, 0);
	}
	
	@Override
    public IChunkGenerator createChunkGenerator() {
        return new SpaceChunkGenerator(world);
    }

}
