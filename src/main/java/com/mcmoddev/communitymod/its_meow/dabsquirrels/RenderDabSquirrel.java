package com.mcmoddev.communitymod.its_meow.dabsquirrels;

import javax.annotation.Nonnull;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDabSquirrel extends RenderLiving<EntityDabSquirrel> {
	
	
	private static final String entitytex = CommunityGlobals.MOD_ID + ":textures/entities/";
	
	public static final ResourceLocation squirrel_1 = new ResourceLocation(entitytex + "squirrel_1.png");
	public static final ResourceLocation squirrel_2 = new ResourceLocation(entitytex + "squirrel_2.png");
	public static final ResourceLocation squirrel_3 = new ResourceLocation(entitytex + "squirrel_3.png");

	public RenderDabSquirrel(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelDabSquirrel(), 0.3F);
	}

	@Override
	protected void preRenderCallback(EntityDabSquirrel entitylivingbaseIn, float partialTickTime) {
		if (this.getMainModel().isChild) {
			GlStateManager.scale(4.35D, 4.35D, 4.35D);
		} else {
			GlStateManager.scale(4.5D, 4.5D, 4.5D);
		}
	}

	@Override
	@Nonnull
	protected ResourceLocation getEntityTexture(@Nonnull EntityDabSquirrel entity) {
		int type = entity.getTypeNumber();
		ResourceLocation res = squirrel_1;
		switch (type) {
		case 1:
			res = squirrel_1;
			break;
		case 2:
			res = squirrel_2;
			break;
		case 3:
			res = squirrel_3;
			break;
		default:
			res = squirrel_1;
			break;
		}
		return res;
	}

}