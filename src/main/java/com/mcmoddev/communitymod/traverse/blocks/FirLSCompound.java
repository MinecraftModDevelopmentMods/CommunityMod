package com.mcmoddev.communitymod.traverse.blocks;

import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseLeaves;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;
import com.mcmoddev.communitymod.traverse.world.features.TraverseTreeGenerator;
import com.mcmoddev.communitymod.traverse.world.features.WorldGenFirTree;

import java.util.Random;

public class FirLSCompound {
    public LSLeaves lsLeaves;
    public LSSapling lsSapling;

    public FirLSCompound() {
        lsLeaves = new LSLeaves();
        lsSapling = new LSSapling();
    }

    public class LSLeaves extends BlockTraverseLeaves {

        public LSLeaves() {
            super("fir", null, 60);
        }

        @Override
        public Item getItemDropped(IBlockState state, Random rand, int fortune) {
            return Item.getItemFromBlock(lsSapling);
        }
    }

    public class LSSapling extends BlockTraverseSapling {

        public LSSapling() {
            super("fir", new TraverseTreeGenerator(false));
        }

        @Override
        public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
            if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
            worldIn.setBlockToAir(pos);
            if (!new WorldGenFirTree(true).generate(worldIn, rand, pos)) {
                worldIn.setBlockState(pos, state);
            }
        }
    }

}
