package com.mcmoddev.communitymod.traverse.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;

import java.util.Random;

public class BlockTraverse extends Block {

    public ResourceLocation drop = null;

    public BlockTraverse(String name, Material material, SoundType soundType, ResourceLocation drop) {
        super(material);
        this.drop = drop;
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name));
        setTranslationKey(getRegistryName().toString());
        setCreativeTab(TraverseTab.TAB);
        setHardness(1.5F);
        setResistance(10.0F);
        setHarvestLevel("pickaxe", 0);
        setSoundType(soundType);
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this));

    }

    public BlockTraverse(String name, Material material, SoundType soundType) {
        this(name, material, soundType, null);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return drop != null ? ForgeRegistries.ITEMS.getValue(drop) : super.getItemDropped(state, rand, fortune);
    }
}
