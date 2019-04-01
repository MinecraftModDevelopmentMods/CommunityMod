package com.mcmoddev.communitymod.its_meow.chickenificator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderStupidChicken extends RenderLiving<EntityLiving> {
    private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation("textures/entity/chicken.png");

    public RenderStupidChicken() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelChicken(), 1F);
    }
    
    @Override
	protected void preRenderCallback(EntityLiving entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(3.0F, 3.0F, 3.0F);
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	protected ResourceLocation getEntityTexture(EntityLiving entity) {
        return CHICKEN_TEXTURES;
    }
}