package com.mcmoddev.communitymod.traverse.world.features;

import com.mcmoddev.communitymod.traverse.config.TraverseConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import com.mcmoddev.communitymod.traverse.world.WorldGenConstants;

import java.util.Random;

public class WorldGenFirTree extends WorldGenAbstractTree implements WorldGenConstants {

    public final boolean isWorldGen;
    private final int minTreeHeight;
    private IBlockState stateWood;
    private IBlockState stateLeaves;

    public WorldGenFirTree(boolean isWorldGen) {
        this(isWorldGen, 15);
    }

    public WorldGenFirTree(boolean isWorldGen, int minTreeHeight) {
        this(isWorldGen, minTreeHeight, FIR_LOG, FIR_LEAVES);
    }

    public WorldGenFirTree(boolean isWorldGen, int minTreeHeight, IBlockState stateWood, IBlockState stateLeaves) {
        super(!isWorldGen);
        this.isWorldGen = isWorldGen;
        this.minTreeHeight = minTreeHeight;
        if (TraverseConfig.useVanillaWood) {
            this.stateWood = DARK_OAK_LOG;
        } else {
            this.stateWood = stateWood;
        }
        this.stateLeaves = stateLeaves;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int treeHeight = rand.nextInt(15) + minTreeHeight;
        int nakedLogHeight = 4 + rand.nextInt(2);
        int leavesHeight = treeHeight - nakedLogHeight;
        int l = 2 + rand.nextInt(2);
        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + treeHeight + 1 <= worldIn.getHeight()) {
            for (int i1 = position.getY(); i1 <= position.getY() + 1 + treeHeight && flag; ++i1) {
                int j1;

                if (i1 - position.getY() < nakedLogHeight) {
                    j1 = 0;
                } else {
                    j1 = l;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int k1 = position.getX() - j1; k1 <= position.getX() + j1 && flag; ++k1) {
                    for (int l1 = position.getZ() - j1; l1 <= position.getZ() + j1 && flag; ++l1) {
                        if (i1 >= 0 && i1 < worldIn.getHeight()) {
                            IBlockState state = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k1, i1, l1));

                            if (!state.getBlock().isAir(state, worldIn, blockpos$mutableblockpos.setPos(k1, i1, l1)) && !state.getBlock().isLeaves(state, worldIn, blockpos$mutableblockpos.setPos(k1, i1, l1))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                BlockPos down = position.down();
                IBlockState state = worldIn.getBlockState(down);

                if (state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling) Blocks.SAPLING) && position.getY() < worldIn.getHeight() - treeHeight - 1) {
                    state.getBlock().onPlantGrow(state, worldIn, down, position);
                    int i3 = rand.nextInt(2);
                    int j3 = 1;
                    int k3 = 0;

                    for (int l3 = 0; l3 <= leavesHeight; ++l3) {
                        int j4 = position.getY() + treeHeight - l3;

                        for (int i2 = position.getX() - i3; i2 <= position.getX() + i3; ++i2) {
                            int j2 = i2 - position.getX();

                            for (int k2 = position.getZ() - i3; k2 <= position.getZ() + i3; ++k2) {
                                int l2 = k2 - position.getZ();

                                if (Math.abs(j2) != i3 || Math.abs(l2) != i3 || i3 <= 0) {
                                    BlockPos blockpos = new BlockPos(i2, j4, k2);
                                    state = worldIn.getBlockState(blockpos);

                                    if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos)) {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, stateLeaves);
                                    }
                                }
                            }
                        }

                        if (i3 >= j3) {
                            i3 = k3;
                            k3 = 1;
                            ++j3;

                            if (j3 > l) {
                                j3 = l;
                            }
                        } else {
                            ++i3;
                        }
                    }

                    int i4 = rand.nextInt(3);

                    for (int k4 = 0; k4 < treeHeight - i4; ++k4) {
                        BlockPos upN = position.up(k4);
                        state = worldIn.getBlockState(upN);

                        if (state.getBlock().isAir(state, worldIn, upN) || state.getBlock().isLeaves(state, worldIn, upN)) {
                            this.setBlockAndNotifyAdequately(worldIn, position.up(k4), stateWood);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}