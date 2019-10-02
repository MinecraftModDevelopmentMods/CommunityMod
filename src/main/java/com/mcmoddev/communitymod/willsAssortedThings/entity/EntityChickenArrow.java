package com.mcmoddev.communitymod.willsAssortedThings.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityChickenArrow extends EntityArrow {

    public EntityChickenArrow(World worldIn) {
        super(worldIn);
    }

    public EntityChickenArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityChickenArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    @Override
    protected ItemStack getArrowStack() { return null; }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (this.world.isRemote)
            return;
        this.setDead();
        EntityChicken chicken = new EntityChicken(world);
        Vec3d hit = raytraceResultIn.hitVec;
        chicken.setPosition(hit.x, hit.y, hit.z);
        world.spawnEntity(chicken);
        world.spawnEntity(new EntityItem(world, hit.x, hit.y, hit.z, new ItemStack(Items.EGG)));
    }

}
