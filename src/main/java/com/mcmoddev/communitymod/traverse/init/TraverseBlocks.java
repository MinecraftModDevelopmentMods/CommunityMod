package com.mcmoddev.communitymod.traverse.init;

import com.mcmoddev.communitymod.shootingstar.BlockCompound;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.traverse.blocks.AutumnalLSCompound;
import com.mcmoddev.communitymod.traverse.blocks.BlockTraverseColdGrass;
import com.mcmoddev.communitymod.traverse.blocks.BlockTraverseDeadGrass;
import com.mcmoddev.communitymod.traverse.blocks.FirLSCompound;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverse;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseSlab;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseStairs;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseWall;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseWoodDoor;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseWoodFence;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseWoodFenceGate;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseWoodLog;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseWoodPlanks;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseWoodSlab;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.item.ItemTraverseWoodDoor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TraverseBlocks {

    public static LinkedHashMap<String, Block> blocks = new LinkedHashMap<>();
    public static Map<Block, String> oreDictNames = new HashMap<>();

    static {
        addAutumnTreeStuff("red");
        addAutumnTreeStuff("brown");
        addAutumnTreeStuff("orange");
        addAutumnTreeStuff("yellow");
        addFirTreeStuff();
        addStone("red_rock", true, true, true);
        addStone("blue_rock", true, true, true);
        register(new BlockTraverseDeadGrass());
        register(new BlockTraverseColdGrass());
    }

    public static void test() {

    }

    static void addAutumnTreeStuff(String colour) {
        if (colour.isEmpty()) {
            return;
        }
        AutumnalLSCompound lsCompound = new AutumnalLSCompound(colour);
        register(lsCompound.lsLeaves);
        oreDictNames.put(lsCompound.lsLeaves, "treeLeaves");
        register(lsCompound.lsSapling);
        oreDictNames.put(lsCompound.lsSapling, "treeSapling");

    }

    public static void register(Block block) {
        blocks.put(block.getRegistryName().getPath(), block);
        ShootingStar.registerBlock(new BlockCompound(TraverseConstants.MOD_ID, block));
    }

    public static void register(Block block, boolean noItemBlock) {
        blocks.put(block.getRegistryName().getPath(), block);
        ShootingStar.registerBlock(new BlockCompound(TraverseConstants.MOD_ID, block, noItemBlock));
    }

    public static void register(Block block, ItemBlock itemBlock) {
        blocks.put(block.getRegistryName().getPath(), block);
        ShootingStar.registerBlock(new BlockCompound(TraverseConstants.MOD_ID, block, itemBlock));
    }

    static void addFirTreeStuff() {
        String fir = "fir";
        FirLSCompound lsCompound = new FirLSCompound();
        register(lsCompound.lsLeaves);
        oreDictNames.put(lsCompound.lsLeaves, "treeLeaves");
        register(lsCompound.lsSapling);
        oreDictNames.put(lsCompound.lsSapling, "treeSapling");

        BlockTraverseWoodLog log = new BlockTraverseWoodLog(fir);
        register(log);
        oreDictNames.put(log, "logWood");

        BlockTraverseWoodPlanks planks = new BlockTraverseWoodPlanks(fir);
        register(planks);
        oreDictNames.put(planks, "plankWood");

        BlockTraverseStairs stairs = new BlockTraverseStairs(planks.getDefaultState(), fir);
        register(stairs);
        oreDictNames.put(stairs, "stairWood");

        BlockTraverseWoodSlab.Half halfSlab = new BlockTraverseWoodSlab.Half(fir);
        register(halfSlab, true);
        oreDictNames.put(halfSlab, "slabWood");
        BlockTraverseWoodSlab.Double doubleSlab = new BlockTraverseWoodSlab.Double(fir, halfSlab);
        register(doubleSlab, (ItemBlock) new ItemSlab(blocks.get(halfSlab.name + "_slab"), halfSlab, doubleSlab).setRegistryName(halfSlab.getRegistryName()));

        BlockTraverseWoodFence fence = new BlockTraverseWoodFence(fir);
        oreDictNames.put(fence, "fenceWood");
        register(fence);

        BlockTraverseWoodFenceGate fenceGate = new BlockTraverseWoodFenceGate(fir);
        oreDictNames.put(fence, "fenceGateWood");
        register(fenceGate);

        BlockTraverseWoodDoor door = new BlockTraverseWoodDoor(fir);
        register(door, new ItemTraverseWoodDoor(door));

    }

    static void addStone(String name, boolean hasBricks, boolean hasSlab, boolean hasCobblestone) {
        String cobbleName = name + "_cobblestone";
        BlockTraverse stone;
        if (hasCobblestone)
            stone = new BlockTraverse(name, Material.ROCK, SoundType.STONE, new ResourceLocation("traverse", cobbleName));
        else
            stone = new BlockTraverse(name, Material.ROCK, SoundType.STONE);

        register(stone);
        oreDictNames.put(stone, "stone");

        if (hasBricks) {
            BlockTraverse bricks = new BlockTraverse(name + "_bricks", Material.ROCK, SoundType.STONE);
            register(bricks);
            register(new BlockTraverseStairs(bricks.getDefaultState(), name + "_bricks"));

            //			BlockTraverse cracked_bricks = new BlockTraverse(name + "_bricks_cracked", Material.ROCK, SoundType.STONE);
            //			register(cracked_bricks);

            BlockTraverseSlab.Half halfSlab = new BlockTraverseSlab.Half(name + "_bricks", Material.ROCK, SoundType.STONE);
            register(halfSlab, true);
            BlockTraverseSlab.Double doubleSlab = new BlockTraverseSlab.Double(name + "_bricks", Material.ROCK, SoundType.STONE, halfSlab);
            register(doubleSlab, (ItemBlock) new ItemSlab(blocks.get(halfSlab.name + "_slab"), halfSlab, doubleSlab).setRegistryName(halfSlab.getRegistryName()));
        }
        if (hasSlab) {
            if (hasBricks) {
                register(new BlockTraverse(name + "_bricks_chiseled", Material.ROCK, SoundType.STONE));
            }
            BlockTraverseSlab.Half halfSlab = new BlockTraverseSlab.Half(name, Material.ROCK, SoundType.STONE);
            register(halfSlab, true);
            BlockTraverseSlab.Double doubleSlab = new BlockTraverseSlab.Double(name, Material.ROCK, SoundType.STONE, halfSlab);
            register(doubleSlab, (ItemBlock) new ItemSlab(blocks.get(halfSlab.name + "_slab"), halfSlab, doubleSlab).setRegistryName(halfSlab.getRegistryName()));
        }

        if (hasCobblestone) {
            BlockTraverse cobblestone = new BlockTraverse(cobbleName, Material.ROCK, SoundType.STONE);
            register(cobblestone);
            oreDictNames.put(cobblestone, "cobblestone");

            register(new BlockTraverseStairs(cobblestone.getDefaultState(), cobbleName));

            BlockTraverseSlab.Half halfSlab = new BlockTraverseSlab.Half(cobbleName, Material.ROCK, SoundType.STONE);
            register(halfSlab, true);
            BlockTraverseSlab.Double doubleSlab = new BlockTraverseSlab.Double(cobbleName, Material.ROCK, SoundType.STONE, halfSlab);
            register(doubleSlab, (ItemBlock) new ItemSlab(blocks.get(halfSlab.name + "_slab"), halfSlab, doubleSlab).setRegistryName(halfSlab.getRegistryName()));

            register(new BlockTraverseWall(cobblestone, cobbleName));

        }
        else {
            register(new BlockTraverse(name, Material.ROCK, SoundType.STONE));

        }
    }

}