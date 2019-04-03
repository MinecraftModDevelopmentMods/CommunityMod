package com.mcmoddev.communitymod.traverse.world.biomes;

import com.mcmoddev.communitymod.traverse.config.TraverseConfig;
import com.mcmoddev.communitymod.traverse.world.features.WorldGenPatch;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import com.mcmoddev.communitymod.traverse.world.WorldGenConstants;

import java.util.Random;

public class BiomeMountainousDesert extends BiomeDesert implements WorldGenConstants {

    protected static final WorldGenPatch STONE_PATCH_FEATURE = new WorldGenPatch(Blocks.STONE.getDefaultState(), 6, Blocks.SAND, Blocks.SANDSTONE);
    protected static final WorldGenBlockBlob COBBLESTONE_BOULDER_FEATURE = new WorldGenBlockBlob(Blocks.COBBLESTONE, 1);
    public static BiomeProperties properties = new BiomeProperties("Mountainous Desert");

    static {
        properties.setTemperature(Biomes.DESERT.getDefaultTemperature());
        properties.setRainfall(Biomes.DESERT.getRainfall());
        properties.setRainDisabled();
        properties.setBaseHeight(0.4F);
        properties.setHeightVariation(1.1F);
    }

    public BiomeMountainousDesert() {
        super(properties);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        int stoneChance = rand.nextInt(9);
        if (stoneChance == 0) {
            int k6 = rand.nextInt(16) + 8;
            int l = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(k6, 0, l));
            STONE_PATCH_FEATURE.generate(worldIn, rand, blockpos);
        }

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
