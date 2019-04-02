package com.mcmoddev.communitymod.traverse.world.biomes;

import com.mcmoddev.communitymod.traverse.config.TraverseConfig;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import com.mcmoddev.communitymod.traverse.world.WorldGenConstants;

import java.util.Random;

public class BiomeCliffs extends Biome implements WorldGenConstants {

    protected static final WorldGenBlockBlob COBBLESTONE_BOULDER_FEATURE = new WorldGenBlockBlob(Blocks.COBBLESTONE, 2);
    public static BiomeProperties properties = new BiomeProperties("Cliffs");

    static {
        properties.setTemperature(0.4F);
        properties.setRainfall(1.2F);
        properties.setRainDisabled();
        properties.setBaseHeight(3.6F);
        properties.setHeightVariation(0.5F);
    }

    public BiomeCliffs() {
        super(properties);
        this.topBlock = Blocks.STONE.getDefaultState();
        this.fillerBlock = Blocks.STONE.getDefaultState();
        this.spawnableCreatureList.clear();
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        if (!TraverseConfig.disallowBoulders && net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, pos, DecorateBiomeEvent.Decorate.EventType.ROCK)) {
            int genChance = rand.nextInt(9);
            if (genChance == 0) {
                int k6 = rand.nextInt(16) + 8;
                int l = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(k6, 0, l));
                COBBLESTONE_BOULDER_FEATURE.generate(worldIn, rand, blockpos);
            }
        }
        super.decorate(worldIn, rand, pos);
    }
}
