package com.mcmoddev.communitymod.traverse.blocks;

import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseLeaves;
import com.mcmoddev.communitymod.traverse.blocks.base.BlockTraverseSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;
import com.mcmoddev.communitymod.traverse.world.features.TraverseTreeGenerator;

import java.util.Random;

public class AutumnalLSCompound {
    public String name;
    public LSLeaves lsLeaves;
    public LSSapling lsSapling;

    public AutumnalLSCompound(String colour) {
        this.name = colour + "_autumnal";
        lsLeaves = new LSLeaves();
        lsSapling = new LSSapling();
    }

    public class LSLeaves extends BlockTraverseLeaves {

        public LSLeaves() {
            super(name, null, 20);
        }

        @Override
        public Item getItemDropped(IBlockState state, Random rand, int fortune) {
            return Item.getItemFromBlock(lsSapling);
        }

        protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
            if (worldIn.rand.nextInt(chance) == 0)
                spawnAsEntity(worldIn, pos, new ItemStack(Items.APPLE));
        }
    }

    public class LSSapling extends BlockTraverseSapling {

        public LSSapling() {
            super(name, new TraverseTreeGenerator(false));
        }

        @Override
        public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
            if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
            worldIn.setBlockToAir(pos);
            if (!new TraverseTreeGenerator(true, 4, TraverseTreeGenerator.OAK_LOG, lsLeaves.getDefaultState()).generate(worldIn, rand, pos)) {
                worldIn.setBlockState(pos, state);
            }
        }
    }

}
