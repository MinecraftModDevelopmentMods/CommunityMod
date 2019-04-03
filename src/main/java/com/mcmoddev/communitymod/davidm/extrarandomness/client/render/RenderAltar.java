package com.mcmoddev.communitymod.davidm.extrarandomness.client.render;

import org.lwjgl.opengl.GL11;

import com.mcmoddev.communitymod.davidm.extrarandomness.client.ClientProxy;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityAltar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class RenderAltar extends TileEntitySpecialRenderer<TileEntityAltar> {

	private static final int SHOCKWAVE_DIAMETER = 40;
	
	@Override
	public void render(TileEntityAltar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = te.getStack();
		if (!stack.isEmpty()) {
			GlStateManager.enableRescaleNormal();
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.85, z + 0.5);
			GlStateManager.rotate(te.getWorld().getTotalWorldTime() * 4, 0, 1, 0);
			
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
			
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			
			if (te.altarAnimation != null) {
				switch (te.altarAnimation) {
					case SHOCKWAVE:
						this.renderShockwave(te.animationProgress, te.altarAnimation.animationLength(), x, y, z);
						break;
					default: break;
				}
			}
		}
	}
	
	private void renderShockwave(int progress, int totalProgress, double x, double y, double z) {
		if (totalProgress == 0) return;
		
		double scale = progress * SHOCKWAVE_DIAMETER / (float) totalProgress;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 1, z + 0.5);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1 - progress / (float) totalProgress);
		GlStateManager.enableAlpha();
		
		GlStateManager.callList(ClientProxy.sphereOutId);
		GlStateManager.callList(ClientProxy.sphereInId);
		
		GlStateManager.popMatrix();
	}
}
