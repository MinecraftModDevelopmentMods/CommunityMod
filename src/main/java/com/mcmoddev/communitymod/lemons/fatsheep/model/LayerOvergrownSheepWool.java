package com.mcmoddev.communitymod.lemons.fatsheep.model;

import com.mcmoddev.communitymod.lemons.fatsheep.EntityOvergrownSheep;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerOvergrownSheepWool implements LayerRenderer<EntityOvergrownSheep>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
	private final RenderOvergrownSheep sheepRenderer;
	private final ModelOvergrownSheepFur sheepModel = new ModelOvergrownSheepFur();

	public LayerOvergrownSheepWool(RenderOvergrownSheep sheepRendererIn)
	{
		this.sheepRenderer = sheepRendererIn;
	}

	public void doRenderLayer(EntityOvergrownSheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (!sheep.getSheared() && !sheep.isInvisible())
		{
			this.sheepRenderer.bindTexture(TEXTURE);

			if (sheep.hasCustomName() && "jeb_".equals(sheep.getCustomNameTag()))
			{
				float time = 25F;
				int i = sheep.ticksExisted / 25 + sheep.getEntityId();
				int j = EnumDyeColor.values().length;
				int k = i % j;
				int l = (i + 1) % j;
				float f = ((sheep.ticksExisted % time) + partialTicks) / time;
				float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
				float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
				GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
			}
			else
			{
				float[] afloat = EntitySheep.getDyeRgb(sheep.getFleeceColor());
				GlStateManager.color(afloat[0], afloat[1], afloat[2]);
			}

			this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
			this.sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
			//(float)sheep.getDataManager().get(EntityOvergrownSheep.GROWTH) / 10000000F
			this.sheepModel.render(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	public boolean shouldCombineTextures()
	{
		return true;
	}
}