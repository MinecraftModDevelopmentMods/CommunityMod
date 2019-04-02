package com.mcmoddev.communitymod.traverse.world.biomes;

import com.mcmoddev.communitymod.traverse.config.TraverseConfig;
import com.mcmoddev.communitymod.traverse.world.features.WorldGenPlant;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;
import com.mcmoddev.communitymod.traverse.init.TraverseBlocks;
import com.mcmoddev.communitymod.traverse.world.WorldGenConstants;

import java.util.Random;

import static com.mcmoddev.communitymod.traverse.util.TUtils.getBlock;

public class BiomeCanyon extends Biome implements WorldGenConstants {

	public static final WorldGenerator DEAD_GRASS_FEATURE = new WorldGenPlant(TraverseBlocks.blocks.get("dead_grass").getDefaultState());
	public static IBlockState redRock = Blocks.RED_SANDSTONE.getDefaultState();
	public static BiomeProperties properties = new BiomeProperties("Canyon");

	static {
		properties.setTemperature(1F);
		properties.setRainfall(0F);
		properties.setBaseHeight(1.8F);
		properties.setHeightVariation(1F);
	}

	public BiomeCanyon() {
		super(properties);
		if (!TraverseConfig.vanillaCanyonBlocks) {
			redRock = getBlock("red_rock").getDefaultState();
		}
		topBlock = redRock;
		fillerBlock = redRock;
		decorator.treesPerChunk = -999;
		decorator.extraTreeChance = -999;
		decorator.flowersPerChunk = -999;
		decorator.grassPerChunk = 1;
		decorator.generateFalls = false;

		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 3, 3, 5));
	}

	@Override
	public int getModdedBiomeGrassColor(int original) {
		return super.getModdedBiomeGrassColor(0xFF90814D);
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand) {
		return DEAD_GRASS_FEATURE;
	}

	@Override
	public int getSkyColorByTemp(float currentTemperature) {
		if (TraverseConfig.disableCustomSkies)
			return super.getSkyColorByTemp(currentTemperature);
		else
			return 0xFF88DDFF;
	}

	@Override
	public int getModdedBiomeFoliageColor(int original) {
		return super.getModdedBiomeFoliageColor(0xFF9E814D);
	}

	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		int i = worldIn.getSeaLevel();
		IBlockState iblockstate = this.topBlock;
		IBlockState iblockstate1 = this.fillerBlock;
		int j = -1;
		int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int l = x & 15;
		int i1 = z & 15;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int j1 = 255; j1 >= 0; --j1) {
			if (j1 <= rand.nextInt(5)) {
				chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
			} else {
				IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

				if (iblockstate2.getMaterial() == Material.AIR) {
					j = -1;
				} else if (iblockstate2.getBlock() == Blocks.STONE) {
					if (j == -1) {
						if (k <= 0) {
							iblockstate = AIR;
							iblockstate1 = STONE;
						} else if (j1 >= i - 4 && j1 <= i + 1) {
							iblockstate = this.topBlock;
							iblockstate1 = this.fillerBlock;
						}

						if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
							if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F) {
								iblockstate = ICE;
							} else {
								iblockstate = WATER;
							}
						}

						j = k;

						if (j1 >= i - 1) {
							chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
						} else if (j1 < i - 7 - k) {
							iblockstate = AIR;
							iblockstate1 = STONE;
							chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
						} else {
							chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
						}
					} else if (j > 0) {
						--j;
						chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

						if (j == 0 && iblockstate1.getBlock() == Blocks.SAND && k > 1) {
							j = rand.nextInt(4) + Math.max(0, j1 - 63);
							iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
						}

						if (j == 0 && iblockstate == redRock && k > 1) {
							j = rand.nextInt(4) + Math.max(0, j1 - 63);
							iblockstate = redRock;
						}

						if (j == 0 && iblockstate1 == redRock && k > 1) {
							j = rand.nextInt(4) + Math.max(0, j1 - 63);
							iblockstate1 = redRock;
						}
					}
				}
			}
		}
	}
}
