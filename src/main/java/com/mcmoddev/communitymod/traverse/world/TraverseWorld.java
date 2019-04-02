package com.mcmoddev.communitymod.traverse.world;

import static net.minecraftforge.common.BiomeDictionary.Type.COLD;
import static net.minecraftforge.common.BiomeDictionary.Type.CONIFEROUS;
import static net.minecraftforge.common.BiomeDictionary.Type.DEAD;
import static net.minecraftforge.common.BiomeDictionary.Type.DENSE;
import static net.minecraftforge.common.BiomeDictionary.Type.DRY;
import static net.minecraftforge.common.BiomeDictionary.Type.FOREST;
import static net.minecraftforge.common.BiomeDictionary.Type.HILLS;
import static net.minecraftforge.common.BiomeDictionary.Type.HOT;
import static net.minecraftforge.common.BiomeDictionary.Type.JUNGLE;
import static net.minecraftforge.common.BiomeDictionary.Type.LUSH;
import static net.minecraftforge.common.BiomeDictionary.Type.MOUNTAIN;
import static net.minecraftforge.common.BiomeDictionary.Type.PLAINS;
import static net.minecraftforge.common.BiomeDictionary.Type.SANDY;
import static net.minecraftforge.common.BiomeDictionary.Type.SNOWY;
import static net.minecraftforge.common.BiomeDictionary.Type.SPARSE;
import static net.minecraftforge.common.BiomeDictionary.Type.SWAMP;
import static net.minecraftforge.common.BiomeDictionary.Type.WET;

import com.mcmoddev.communitymod.shootingstar.version.Version;
import com.mcmoddev.communitymod.traverse.config.TraverseConfig;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeAridHighland;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeAutumnalWoodedHills;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeAutumnalWoods;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeBadlands;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeCanyon;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeCliffs;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeConiferousForest;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeCragCliffs;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeDesertShrubland;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeForestedHills;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeGlacier;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeLushHills;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeLushSwamp;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeMeadow;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeMiniJungle;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeMountainousDesert;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeRedDesert;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeRockyPlains;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeRockyPlateau;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeThicket;
import com.mcmoddev.communitymod.traverse.world.biomes.BiomeWoodlands;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class TraverseWorld {

    public static final Version V0 = new Version(1, 0, 0);
    public static final Version V1 = new Version(1, 1, 0);
    public static final Version V2 = new Version(1, 2, 0);
    public static final Version V3 = new Version(1, 3, 0);
    public static final Version V4 = new Version(1, 4, 0);
    public static final Version V5 = new Version(1, 5, 0);

    public static final VillageReplacements NO_VILLAGES = originalState -> null;
    public static final VillageReplacements NO_REPLACEMENTS = originalState -> originalState;
    public static final VillageReplacements RED_DESERT_REPLACEMENTS = originalState -> {
        if (originalState.getBlock() == Blocks.LOG || originalState.getBlock() == Blocks.LOG2) {
            return Blocks.RED_SANDSTONE.getDefaultState();
        }
        if (originalState.getBlock() == Blocks.COBBLESTONE) {
            return Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.DEFAULT);
        }
        if (originalState.getBlock() == Blocks.PLANKS) {
            return Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH);
        }
        if (originalState.getBlock() == Blocks.OAK_STAIRS) {
            return Blocks.RED_SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, originalState.getValue(BlockStairs.FACING));
        }
        if (originalState.getBlock() == Blocks.STONE_STAIRS) {
            return Blocks.RED_SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, originalState.getValue(BlockStairs.FACING));
        }
        if (originalState.getBlock() == Blocks.GRAVEL) {
            return Blocks.RED_SANDSTONE.getDefaultState();
        }
        return originalState;
    };
    public static final VillageReplacements DESERT_REPLACEMENTS = originalState -> {
        if (originalState.getBlock() == Blocks.LOG || originalState.getBlock() == Blocks.LOG2) {
            return Blocks.SANDSTONE.getDefaultState();
        }

        if (originalState.getBlock() == Blocks.COBBLESTONE) {
            return Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.DEFAULT);
        }

        if (originalState.getBlock() == Blocks.PLANKS) {
            return Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH);
        }

        if (originalState.getBlock() == Blocks.OAK_STAIRS) {
            return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, originalState.getValue(BlockStairs.FACING));
        }

        if (originalState.getBlock() == Blocks.STONE_STAIRS) {
            return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, originalState.getValue(BlockStairs.FACING));
        }

        if (originalState.getBlock() == Blocks.GRAVEL) {
            return Blocks.SANDSTONE.getDefaultState();
        }
        return originalState;
    };

    public static List<TraverseBiomeEntry> biomeList = new ArrayList<>();
    public static Map<Biome, VillageReplacements> villageReplacementBiomes = new HashMap<>();
    public static Biome autumnalWoodsBiome = new BiomeAutumnalWoods();
    public static Biome woodlandsBiome = new BiomeWoodlands();
    public static Biome miniJungleBiome = new BiomeMiniJungle();
    public static Biome meadowBiome = new BiomeMeadow();
    public static Biome lushSwampBiome = new BiomeLushSwamp();
    public static Biome redDesertBiome = new BiomeRedDesert();
    public static Biome temperateRainforestBiome = new BiomeConiferousForest(false);
    public static Biome badlandsBiome = new BiomeBadlands();
    public static Biome mountainousDesertBiome = new BiomeMountainousDesert();
    public static Biome rockyPlateauBiome = new BiomeRockyPlateau();
    public static Biome forestedHillsBiome = new BiomeForestedHills(BiomeForest.Type.NORMAL, "Forested Hills");
    public static Biome birchForestedHillsBiome = new BiomeForestedHills(BiomeForest.Type.BIRCH, "Birch Forested Hills");
    public static Biome autumnalWoodedHillsBiome = new BiomeAutumnalWoodedHills();
    public static Biome cliffsBiome = new BiomeCliffs();
    public static Biome glacierBiome = new BiomeGlacier(false);
    public static Biome glacierSpikesBiome = new BiomeGlacier(true);
    public static Biome snowyConiferousForestBiome = new BiomeConiferousForest(true);
    public static Biome lushHillsBiome = new BiomeLushHills();
    public static Biome canyonBiome = new BiomeCanyon();
    public static Biome cragCliffsBiome = new BiomeCragCliffs();
    public static Biome desertShrublandBiome = new BiomeDesertShrubland();
    public static Biome thicketBiome = new BiomeThicket();
    public static Biome aridHighlandBiome = new BiomeAridHighland();
    public static Biome rockyPlainsBiome = new BiomeRockyPlains();

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        register(V0, autumnalWoodsBiome, BiomeType.COOL, "autumnal_woods", 8, TraverseConfig.disableAutumnalWoods, true, event, FOREST);
        register(V0, woodlandsBiome, BiomeType.WARM, "woodlands", 9, TraverseConfig.disableWoodlands, true, NO_REPLACEMENTS, event, PLAINS);
        register(V0, miniJungleBiome, BiomeType.WARM, "mini_jungle", 3, TraverseConfig.disableMiniJungle, true, event, DENSE, JUNGLE, HOT, WET);
        register(V0, meadowBiome, BiomeType.COOL, "meadow", 7, TraverseConfig.disableMeadow, true, NO_REPLACEMENTS, event, PLAINS, LUSH, WET);
        register(V0, lushSwampBiome, BiomeType.WARM, "green_swamp", 6, TraverseConfig.disableLushSwamp, false, event, LUSH, WET, SWAMP);
        register(V0, redDesertBiome, BiomeType.DESERT, "red_desert", 6, TraverseConfig.disableRedDesert, false, RED_DESERT_REPLACEMENTS, event, HOT, DRY, SANDY);
        register(V0, temperateRainforestBiome, BiomeType.COOL, "temperate_rainforest", 8, TraverseConfig.disableTemperateRainforest, true, event, FOREST, CONIFEROUS);
        register(V1, badlandsBiome, BiomeType.WARM, "badlands", 5, TraverseConfig.disableBadlands, false, NO_REPLACEMENTS, event, PLAINS, DRY, HOT, SPARSE);
        register(V1, mountainousDesertBiome, BiomeType.DESERT, "mountainous_desert", 2, TraverseConfig.disableMountainousDesert, false, event, MOUNTAIN, DRY, HOT, SANDY);
        register(V1, rockyPlateauBiome, BiomeType.WARM, "rocky_plateau", 4, TraverseConfig.disableRockyPlateau, false, NO_REPLACEMENTS, event, HOT, MOUNTAIN);
        register(V1, forestedHillsBiome, BiomeType.COOL, "forested_hills", 6, TraverseConfig.disableForestedHills, true, event, FOREST, HILLS);
        register(V1, birchForestedHillsBiome, BiomeType.COOL, "birch_forested_hills", 2, TraverseConfig.disableBirchForestedHills, true, event, FOREST, HILLS);
        register(V1, autumnalWoodedHillsBiome, BiomeType.COOL, "autumnal_wooded_hills", 1, TraverseConfig.disableAutumnalWoodedHills, true, event, FOREST, HILLS);
        register(V2, cliffsBiome, BiomeType.COOL, "cliffs", 2, TraverseConfig.disableCliffs, false, event, MOUNTAIN, COLD, HILLS);
        register(V2, glacierBiome, BiomeType.ICY, "glacier", 6, TraverseConfig.disableGlacier, false, event, MOUNTAIN, COLD, SNOWY);
        register(V2, glacierSpikesBiome, BiomeType.ICY, "glacier_spikes", 2, TraverseConfig.disableGlacierSpikes, false, event, MOUNTAIN, COLD, SNOWY);
        register(V2, snowyConiferousForestBiome, BiomeType.ICY, "snowy_coniferous_forest", 5, TraverseConfig.disableSnowyConiferousForest, true, event, COLD, SNOWY, FOREST, CONIFEROUS);
        register(V3, lushHillsBiome, BiomeType.COOL, "lush_hills", 6, TraverseConfig.disableLushHills, true, NO_REPLACEMENTS, event, LUSH, HILLS, SPARSE, WET);
        register(V3, canyonBiome, BiomeType.DESERT, "canyon", 5, TraverseConfig.disableCanyon, false, event, DRY, DEAD);
        register(V3, cragCliffsBiome, BiomeType.COOL, "crag_cliffs", 4, TraverseConfig.disableCragCliffs, false, event, COLD, DEAD);
        register(V4, desertShrublandBiome, BiomeType.DESERT, "desert_shrubland", 5, TraverseConfig.disableDesertShrubland, false, DESERT_REPLACEMENTS, event, HOT, DRY, SANDY, DEAD);
        register(V5, thicketBiome, BiomeType.WARM, "thicket", 6, TraverseConfig.disableThicket, true, event, DENSE, FOREST);
        register(V5, aridHighlandBiome, BiomeType.DESERT, "arid_highland", 4, TraverseConfig.disableAridHighland, false, DESERT_REPLACEMENTS, event, HOT, DRY, SPARSE, MOUNTAIN, HILLS);
        register(V5, rockyPlainsBiome, BiomeType.COOL, "rocky_plains", 4, TraverseConfig.disableRockyPlains, true, NO_REPLACEMENTS, event, PLAINS, HILLS, SPARSE);
    }

    public static void register(Version versionAdded,
                                Biome biome,
                                BiomeType type,
                                String name,
                                int weight,
                                boolean disabled,
                                boolean canSpawn,
                                VillageReplacements villageReplacements,
                                RegistryEvent.Register<Biome> event,
                                BiomeDictionary.Type... biomeDictTypes) {
        if (!disabled) {
            biome.setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name));
            event.getRegistry().register(biome);
            for (BiomeDictionary.Type biomeDictType : biomeDictTypes) {
                BiomeDictionary.addTypes(biome, biomeDictType);
            }
            biomeList.add(new TraverseBiomeEntry(biome, type, weight, canSpawn, !TraverseConfig.disallowVillages && villageReplacements != NO_VILLAGES, versionAdded));
            if (!TraverseConfig.disallowVillages && villageReplacements != NO_VILLAGES && villageReplacements != NO_REPLACEMENTS) {
                villageReplacementBiomes.put(biome, villageReplacements);
            }
        }
    }

    public static void register(Version versionAdded,
                                Biome biome,
                                BiomeType type,
                                String name,
                                int weight,
                                boolean disabled,
                                boolean canSpawn,
                                RegistryEvent.Register<Biome> event,
                                BiomeDictionary.Type... biomeDictTypes) {
        register(versionAdded, biome, type, name, weight, disabled, canSpawn, NO_VILLAGES, event, biomeDictTypes);

    }

    @SubscribeEvent
    public static void replaceVillageBlocks(BiomeEvent.GetVillageBlockID event) {
        if (villageReplacementBiomes.keySet().contains(event.getBiome())) {
            event.setReplacement(villageReplacementBiomes.get(event.getBiome()).getBiomeSpecificState(event.getOriginal()));
            event.setResult(Event.Result.DENY);
        }
    }

    public static interface VillageReplacements {

        public IBlockState getBiomeSpecificState(IBlockState originalState);

    }

    public static class TraverseBiomeEntry {

        private Biome biome;
        private BiomeType type;
        private Version versionAdded;
        private int weight;
        private boolean canSpawn;
        private boolean hasVillages;
        private BiomeManager.BiomeEntry entry;

        public TraverseBiomeEntry(Biome biome, BiomeType type, int weight, boolean canSpawn, boolean hasVillages, Version versionAdded) {
            this.biome = biome;
            this.type = type;
            this.weight = weight;
            this.canSpawn = canSpawn;
            this.hasVillages = hasVillages;
            this.versionAdded = versionAdded;
            this.entry = new BiomeManager.BiomeEntry(biome, weight);
        }

        public Biome getBiome() {
            return biome;
        }

        public BiomeManager.BiomeEntry getEntry() {
            return entry;
        }

        public BiomeType getType() {
            return type;
        }

        public Version getVersionAdded() {
            return versionAdded;
        }

        public int getWeight() {
            return weight;
        }

        public boolean hasVillages() {
            return hasVillages;
        }

        public boolean canSpawn() {
            return canSpawn;
        }

    }

}
