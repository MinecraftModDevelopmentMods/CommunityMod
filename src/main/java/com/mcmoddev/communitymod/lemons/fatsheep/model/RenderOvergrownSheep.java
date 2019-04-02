package com.mcmoddev.communitymod.lemons.fatsheep.model;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.lemons.fatsheep.EntityOvergrownSheep;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderOvergrownSheep extends RenderLiving<EntityOvergrownSheep>
{
	private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation(CommunityGlobals.MOD_ID, "textures/entity/sheep/sheep.png");

	public RenderOvergrownSheep(RenderManager rm)
	{
		super(rm, new ModelSheep2(), 0.7F);
		this.addLayer(new LayerOvergrownSheepWool(this));
	}

	protected ResourceLocation getEntityTexture(EntityOvergrownSheep entity)
	{
		return SHEARED_SHEEP_TEXTURES;
	}
}