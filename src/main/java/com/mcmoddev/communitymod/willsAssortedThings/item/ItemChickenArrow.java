package com.mcmoddev.communitymod.willsAssortedThings.item;

import com.mcmoddev.communitymod.willsAssortedThings.entity.EntityChickenArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemChickenArrow extends ItemArrow {

    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        return new EntityChickenArrow(worldIn, shooter);
    }
}
