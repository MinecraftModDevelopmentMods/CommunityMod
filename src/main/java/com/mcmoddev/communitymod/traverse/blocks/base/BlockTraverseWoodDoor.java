package com.mcmoddev.communitymod.traverse.blocks.base;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;
import com.mcmoddev.communitymod.traverse.core.TraverseTab;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTraverseWoodDoor extends BlockDoor {

    public BlockTraverseWoodDoor(String name) {
        super(Material.WOOD);
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name + "_door"));
        setTranslationKey(getRegistryName().toString());
        setCreativeTab(TraverseTab.TAB);
        setSoundType(SoundType.WOOD);
        setHardness(3.0F);
        setHarvestLevel("axe", 0);
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this, "door", POWERED));
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return SoundType.WOOD;
    }

    @Override
    public SoundType getSoundType() {
        return SoundType.WOOD;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : getDoorItem().getItem();
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return getDoorItem();
    }

    private ItemStack getDoorItem() {
        return new ItemStack(Item.getItemFromBlock(this));
    }
}
