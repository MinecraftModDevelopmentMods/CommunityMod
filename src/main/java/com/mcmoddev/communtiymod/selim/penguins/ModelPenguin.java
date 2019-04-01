// Distributed under MIT, originally from https://github.com/Selim042/SM-Penguins

package com.mcmoddev.communtiymod.selim.penguins;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPenguin extends ModelBase {

	ModelRenderer rightFoot;
	ModelRenderer leftFoot;
	ModelRenderer body;
	ModelRenderer head;
	ModelRenderer leftWing;
	ModelRenderer rightWing;
	ModelRenderer beak;

	public ModelPenguin() {
		textureWidth = 64;
		textureHeight = 32;

		rightFoot = new ModelRenderer(this, 36, 11);
		rightFoot.addBox(0F, 0F, 0F, 3, 1, 8);
		rightFoot.setRotationPoint(-4F, 23F, -3F);
		rightFoot.setTextureSize(64, 32);
		rightFoot.mirror = true;
		setRotation(rightFoot, 0F, 0F, 0F);
		leftFoot = new ModelRenderer(this, 36, 11);
		leftFoot.addBox(0F, 0F, 0F, 3, 1, 8);
		leftFoot.setRotationPoint(1F, 23F, -3F);
		leftFoot.setTextureSize(64, 32);
		leftFoot.mirror = true;
		setRotation(leftFoot, 0F, 0F, 0F);
		body = new ModelRenderer(this, 0, 0);
		body.addBox(0F, 0F, 0F, 10, 13, 8);
		body.setRotationPoint(-5F, 10F, -2F);
		body.setTextureSize(64, 32);
		body.mirror = true;
		setRotation(body, 0F, 0F, 0F);
		head = new ModelRenderer(this, 36, 20);
		head.addBox(0F, 0F, 0F, 7, 6, 6);
		head.setRotationPoint(-3.5F, 4F, -1F);
		head.setTextureSize(64, 32);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		leftWing = new ModelRenderer(this, 36, 0);
		leftWing.addBox(0F, 0F, 0F, 1, 8, 3);
		leftWing.setRotationPoint(4.5F, 12F, 1F);
		leftWing.setTextureSize(64, 32);
		leftWing.mirror = true;
		setRotation(leftWing, 0F, 0F, -0.2617994F);
		rightWing = new ModelRenderer(this, 36, 0);
		rightWing.addBox(0F, 0F, 0F, 1, 8, 3);
		rightWing.setRotationPoint(-5.5F, 12F, 1F);
		rightWing.setTextureSize(64, 32);
		rightWing.mirror = true;
		setRotation(rightWing, 0F, 0F, 0.2617994F);
		beak = new ModelRenderer(this, 30, 21);
		beak.addBox(0F, 0F, 0F, 2, 1, 1);
		beak.setRotationPoint(-1F, 8F, -2F);
		beak.setTextureSize(64, 32);
		beak.mirror = true;
		setRotation(beak, 0F, 0F, 0F);
	}

	public static final float childYOffset = 7.0F;
	public static final float childZOffset = -1.25F;

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale,
				entity);
		if (this.isChild) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, childYOffset * scale, childZOffset * scale);
			head.render(scale);
			beak.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			rightFoot.render(scale);
			leftFoot.render(scale);
			body.render(scale);
			leftWing.render(scale);
			rightWing.render(scale);
			GlStateManager.popMatrix();
		} else {
			rightFoot.render(scale);
			leftFoot.render(scale);
			body.render(scale);
			head.render(scale);
			leftWing.render(scale);
			rightWing.render(scale);
			beak.render(scale);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scale, Entity entity) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale,
				entity);
	}

}
