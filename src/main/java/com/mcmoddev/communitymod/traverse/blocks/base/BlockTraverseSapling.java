package com.mcmoddev.communitymod.traverse.blocks.base;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;
import com.mcmoddev.communitymod.traverse.world.features.TraverseTreeGenerator;

import java.util.Random;

public class BlockTraverseSapling extends BlockSapling {

    public TraverseTreeGenerator generator;

    public BlockTraverseSapling(String name, TraverseTreeGenerator generator) {
        super();
        this.generator = generator;
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_sapling"));
        setTranslationKey(getRegistryName().toString());
        setCreativeTab(TraverseTab.TAB);
        setSoundType(SoundType.PLANT);
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "sapling", STAGE, TYPE));
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        if (generator.generate(worldIn, rand, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }
}
