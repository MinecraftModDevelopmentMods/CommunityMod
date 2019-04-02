package com.mcmoddev.communitymod.routiduct.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAssembler extends GuiAssemblerS {
	public final ResourceLocation customElementSheet;

	public GuiAssembler(ResourceLocation elementSheet) {
		this.customElementSheet = elementSheet;
	}

	public GuiAssembler() {
		this.customElementSheet = null;
	}

	public void drawDefaultBackground(GuiRoutiduct gui, int x, int y, int width, int height) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);
		setTextureSheet(BACKGROUND_SHEET);
		GlStateManager.disableLighting();
		gui.drawTexturedModalRect(x, y, 0, 0, width / 2, height / 2);
		gui.drawTexturedModalRect(x + width / 2, y, 256 - width / 2, 0, width / 2, height / 2);
		gui.drawTexturedModalRect(x, y + height / 2, 0, 256 - height / 2, width / 2, height / 2);
		gui.drawTexturedModalRect(x + width / 2, y + height / 2, 256 - width / 2, 256 - height / 2, width / 2, height / 2);
	}

	public void drawRect(GuiRoutiduct gui, int x, int y, int width, int height, int colour) {
		drawGradientRect(gui, x, y, width, height, colour, colour);
	}

	/*
		Taken from Gui
	*/
	public void drawGradientRect(GuiRoutiduct gui, int x, int y, int width, int height, int startColor, int endColor) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);

		int left = x;
		int top = y;
		int right = x + width;
		int bottom = y + height;
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos((double) right, (double) top, (double) 0).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos((double) left, (double) top, (double) 0).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos((double) left, (double) bottom, (double) 0).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos((double) right, (double) bottom, (double) 0).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public int adjustX(GuiRoutiduct gui, int x) {
		return gui.getOffsetFactorX() + x;
	}

	public int adjustY(GuiRoutiduct gui, int y) {
		return gui.getOffsetFactorY() + y;
	}

	public boolean isInRect(GuiRoutiduct gui, int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

	public void drawPlayerSlots(GuiRoutiduct gui, int posX, int posY, boolean center) {
		if (center)
			posX -= 81;
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 9; x++)
				drawSlot(gui, posX + x * 18, posY + y * 18);

		for (int x = 0; x < 9; x++) {
			drawSlot(gui, posX + x * 18, posY + 58);
		}
	}

	public void drawSlot(GuiRoutiduct gui, int posX, int posY) {
		posX = adjustX(gui, posX);
		posY = adjustY(gui, posY);
		setTextureSheet(BACKGROUND_SHEET);

		gui.drawTexturedModalRect(posX, posY, 0, 0, 18, 18);
	}

	public void drawString(GuiRoutiduct gui, String string, int x, int y, int color) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);
		gui.mc.fontRenderer.drawString(string, x, y, color);
	}

	public void drawString(GuiRoutiduct gui, String string, int x, int y) {
		drawString(gui, string, x, y, 16777215);
	}

	public void setTextureSheet(ResourceLocation textureLocation) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureLocation);
	}

	public void drawCenteredString(GuiRoutiduct gui, String string, int y, int colour) {
		drawString(gui, string, (gui.getXSize() / 2 - gui.mc.fontRenderer.getStringWidth(string) / 2), y, colour);
	}

	public void drawCenteredString(GuiRoutiduct gui, String string, int x, int y, int colour) {
		drawString(gui, string, (x - gui.mc.fontRenderer.getStringWidth(string) / 2), y, colour);
	}

	@Override
	public int getStringWidth(String string) {
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(string);
	}
}
