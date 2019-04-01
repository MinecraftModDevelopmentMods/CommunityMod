package com.mcmoddev.communitymod.gegy.transiconherobrinebutwithbetterpants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TransIconHerobrineButWithBetterPantsModel extends ModelPlayer {
    public TransIconHerobrineButWithBetterPantsModel(float modelSize) {
        super(modelSize, false);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, age, yaw, pitch, scaleFactor, entity);

        float dominanceAnimation = this.getDominanceAnimation(entity);
        if (dominanceAnimation > 0.0F) {
            this.bipedLeftArm.rotateAngleZ = this.animateAngles(-90.0F, dominanceAnimation);
            this.bipedRightArm.rotateAngleZ = this.animateAngles(90.0F, dominanceAnimation);
        } else {
            float dabAnimation = this.getDabAnimation(entity);
            float dabFactor = Math.abs(dabAnimation);

            if (dabAnimation < 0.0F) {
                this.animateDab(this.bipedLeftArm, this.bipedRightArm, dabAnimation, dabFactor);
            } else if (dabAnimation > 0.0F) {
                this.animateDab(this.bipedRightArm, this.bipedLeftArm, dabAnimation, dabFactor);
            }
        }

        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }

    private void animateDab(ModelRenderer primaryArm, ModelRenderer secondaryArm, float animation, float factor) {
        primaryArm.rotateAngleX += this.animateAngles(-90.0F, factor);
        primaryArm.rotateAngleY += this.animateAngles(-30.0F, animation);
        primaryArm.rotateAngleZ += this.animateAngles(-20.0F, animation);

        this.bipedHead.rotateAngleX += this.animateAngles(45.0F, factor);
        this.bipedHead.rotateAngleY += this.animateAngles(30.0F, animation);

        secondaryArm.rotateAngleX += this.animateAngles(-90.0F, factor);
        secondaryArm.rotateAngleY += this.animateAngles(-70.0F, animation);
        secondaryArm.rotateAngleZ += this.animateAngles(-15.0F, animation);
    }

    private float animateAngles(float to, float factor) {
        return (float) Math.toRadians(to * factor);
    }

    private float getDabAnimation(Entity entity) {
        if (entity instanceof TransIconHerobrineButWithBetterPantsEntity) {
            TransIconHerobrineButWithBetterPantsEntity herobrine = (TransIconHerobrineButWithBetterPantsEntity) entity;
            return herobrine.getDabAnimation(Minecraft.getMinecraft().getRenderPartialTicks());
        }
        return 0.0F;
    }

    private float getDominanceAnimation(Entity entity) {
        if (entity instanceof TransIconHerobrineButWithBetterPantsEntity) {
            TransIconHerobrineButWithBetterPantsEntity herobrine = (TransIconHerobrineButWithBetterPantsEntity) entity;
            return herobrine.getDominanceAnimation(Minecraft.getMinecraft().getRenderPartialTicks());
        }
        return 0.0F;
    }
}
