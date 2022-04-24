package com.mcmoddev.mmdcommunity.commoble.explodingchickens.client;

import com.mcmoddev.mmdcommunity.commoble.explodingchickens.ExplodingChicken;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ExplodingChickenRenderer extends MobRenderer<ExplodingChicken, ChickenModel<ExplodingChicken>>
{
	private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation("textures/entity/chicken.png");

	public ExplodingChickenRenderer(Context context)
	{
		super(context, new ChickenModel<>(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(ExplodingChicken pEntity)
	{
		return CHICKEN_LOCATION;
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float getBob(ExplodingChicken pLivingBase, float pPartialTicks)
	{
		float flag = Mth.lerp(pPartialTicks, pLivingBase.oFlap, pLivingBase.flap);
		float flapSpeed = Mth.lerp(pPartialTicks, pLivingBase.oFlapSpeed, pLivingBase.flapSpeed);
		return (Mth.sin(flag) + 1.0F) * flapSpeed;
	}

	@Override
	public void render(ExplodingChicken chicken, float yaw, float partialTicks, PoseStack poses, MultiBufferSource buffers, int packedLight)
	{
		super.render(chicken, yaw, partialTicks, poses, buffers, packedLight);
	}

	@Override
	protected void scale(ExplodingChicken chicken, PoseStack poses, float partialTicks)
	{
		float swell = chicken.getSwelling(partialTicks);
		float trigSwell = 1.0F + Mth.sin(swell * 100.0F) * swell * 0.01F;
		swell = Mth.clamp(swell, 0.0F, 1.0F);
		swell *= swell;
		swell *= swell;
		float xzScale = (1.0F + swell * 0.6F) * trigSwell;
		float yScale = (1.0F + swell * 0.15F) / trigSwell;
		poses.scale(xzScale, yScale, xzScale);
	}

	@Override
	protected float getWhiteOverlayProgress(ExplodingChicken chicken, float partialTicks)
	{
		float swell = chicken.getSwelling(partialTicks);
		return (int) (swell * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(swell, 0.5F, 1.0F);
	}
}
