// Distributed under MIT, originally from https://github.com/Selim042/SM-Penguins

package com.mcmoddev.communtiymod.selim.penguins;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class PenguinRenderer extends RenderLiving<EntityPenguin> {

	public PenguinRenderer(RenderManager manager) {
		super(manager, new ModelPenguin(), 0.35f);
	}

	private static final ResourceLocation texture = new ResourceLocation(
			CommunityGlobals.MOD_ID + ":textures/entities/penguin.png");

	@Override
	protected ResourceLocation getEntityTexture(EntityPenguin entity) {
		return texture;
	}

}
