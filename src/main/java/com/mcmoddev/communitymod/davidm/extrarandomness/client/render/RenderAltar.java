package com.mcmoddev.communitymod.davidm.extrarandomness.client.render;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityAltar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class RenderAltar extends TileEntitySpecialRenderer<TileEntityAltar> {

	private static final int SHOCKWAVE_RADIUS = 20;
	
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
		}
	}
}
