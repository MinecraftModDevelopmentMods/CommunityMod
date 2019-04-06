package com.mcmoddev.communitymod.davidm.extrarandomness.core.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class AnimationHelper {

	public static void drawPlane(ResourceLocation texture, Vec3d pos_1, Vec3d pos_2, Vec3d pos_3, Vec3d pos_4) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, 0);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		
		buffer.pos(pos_1.x, pos_1.y, pos_1.z).tex(0, 0).endVertex();
		buffer.pos(pos_2.x, pos_2.y, pos_2.z).tex(0, 1).endVertex();
		buffer.pos(pos_3.x, pos_3.y, pos_3.z).tex(1, 1).endVertex();
		buffer.pos(pos_4.x, pos_4.y, pos_4.z).tex(1, 0).endVertex();
		
		tessellator.draw();
		
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
