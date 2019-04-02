package com.mcmoddev.communitymod.gegy.squashable;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;

public final class FlattenedModel extends ModelBase {
    private final ModelBase wrapped;
    private final EnumFacing.Axis squashedAxis;

    public FlattenedModel(ModelBase wrapped, EnumFacing.Axis squashedAxis) {
        this.wrapped = wrapped;
        this.squashedAxis = squashedAxis;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float scale) {
        GlStateManager.pushMatrix();

        switch (this.squashedAxis) {
            case X:
                GlStateManager.scale(0.01F, 1.0F, 1.0F);
                break;
            case Z:
                GlStateManager.scale(1.0F, 1.0F, 0.01F);
                break;
        }

        this.wrapped.render(entity, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float scale, Entity entity) {
        this.wrapped.setRotationAngles(limbSwing, limbSwingAmount, age, yaw, pitch, scale, entity);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.wrapped.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);
    }

    @Override
    public void setModelAttributes(ModelBase model) {
        this.wrapped.setModelAttributes(model);
    }
}
