package com.mcmoddev.communitymod.space;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public class SpaceChunkGenerator implements IChunkGenerator {

    World world;
    
    public SpaceChunkGenerator(World w){
	world = w;
    }
    
    @Override
    public Chunk generateChunk(int x, int z) {
	Chunk chunk = new Chunk(world, x, z);
	chunk.generateSkylightMap();
	return chunk;
    }

    @Override
    public void populate(int x, int z) {}

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
	return false;
    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
	return new ArrayList<Biome.SpawnListEntry>();
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
	    boolean findUnexplored) {
	return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
	return false;
    }

}
