package com.mcmoddev.communtiymod.selim.penguins;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class PenguinRenderer extends RenderLiving<EntityPenguin> {

	public PenguinRenderer(RenderManager manager) {
		super(manager, new ModelPenguin(), 0.35f);
	}

	private static final ResourceLocation texture = new ResourceLocation(
			"community_mod:textures/entities/penguin.png");

	@Override
	protected ResourceLocation getEntityTexture(EntityPenguin entity) {
		return texture;
	}

}
