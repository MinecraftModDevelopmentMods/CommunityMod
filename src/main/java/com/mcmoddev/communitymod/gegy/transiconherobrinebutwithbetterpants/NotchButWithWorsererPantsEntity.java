package com.mcmoddev.communitymod.gegy.transiconherobrinebutwithbetterpants;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class NotchButWithWorsererPantsEntity extends EntityCreature {
    public NotchButWithWorsererPantsEntity(World world) {
        super(world);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        double x = -Math.sin(Math.toRadians(-this.rotationYaw - 180.0));
        double z = -Math.cos(Math.toRadians(-this.rotationYaw - 180.0));
        this.getMoveHelper().setMoveTo(this.posX + x, this.posY, this.posZ + z, 0.5);

        if (this.onGround) {
            this.jump();
        }
    }
}
