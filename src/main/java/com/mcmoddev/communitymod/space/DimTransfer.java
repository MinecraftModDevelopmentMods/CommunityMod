package com.mcmoddev.communitymod.space;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class DimTransfer extends Teleporter {

    private double posX, posY, posZ;

    public DimTransfer(WorldServer worldIn, double x, double y, double z) {
        super(worldIn);
        posX = x;
        posY = y;
        posZ = z;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        entityIn.setPosition(posX, posY, posZ);
        entityIn.motionX = 0.0D;
        entityIn.motionY = 0.0D;
        entityIn.motionZ = 0.0D;
    }
}
