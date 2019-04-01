package com.mcmoddev.communitymod.fatsheep.model;

import com.mcmoddev.communitymod.fatsheep.EntityOvergrownSheep;
import net.minecraft.client.model.ModelSheep1;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;


public class ModelOvergrownSheepFur extends ModelSheep1
{
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

		if (this.isChild)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
			this.head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.body.render(scale);
			this.leg1.render(scale);
			this.leg2.render(scale);
			this.leg3.render(scale);
			this.leg4.render(scale);
			GlStateManager.popMatrix();
		}
		else
		{
			this.leg1.render(scale);
			this.leg2.render(scale);
			this.leg3.render(scale);
			this.leg4.render(scale);
			this.head.render(scale);

			GlStateManager.pushMatrix();
			float sc = entityIn.getDataManager().get(EntityOvergrownSheep.GROWTH);
			GlStateManager.scale(sc, sc, sc);
			float tran = Math.max(0, -1F + (sc / 1.25F));
			GlStateManager.translate(0F, -tran, 0);
			this.body.render(scale);

			GlStateManager.popMatrix();
		}
	}
}