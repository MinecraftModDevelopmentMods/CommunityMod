package com.mcmoddev.communitymod.routiduct.gui;

import net.minecraft.util.ResourceLocation;
import com.mcmoddev.communitymod.routiduct.RoutiductConstants;

public class GuiAssemblerS {
	public static final ResourceLocation BACKGROUND_SHEET = new ResourceLocation(RoutiductConstants.MOD_ID, "textures/gui/assembler_background.png");
	public static final ResourceLocation ROUTIDUCT_ELEMENTS = new ResourceLocation(RoutiductConstants.MOD_ID, "textures/gui/assembler_elements.png");
	public final ResourceLocation customElementSheet;

	public GuiAssemblerS(ResourceLocation elementSheet) {
		this.customElementSheet = elementSheet;
	}

	public GuiAssemblerS() {
		this.customElementSheet = null;
	}

	public void drawDefaultBackground(GuiRoutiduct gui, int x, int y, int width, int height) {

	}

	public void drawRect(GuiRoutiduct gui, int x, int y, int width, int height, int colour) {
	}

	public void drawGradientRect(GuiRoutiduct gui, int x, int y, int width, int height, int startColor, int endColor) {

	}

	public int adjustX(GuiRoutiduct gui, int x) {
		return 0;
	}

	public int adjustY(GuiRoutiduct gui, int y) {
		return 0;
	}

	public boolean isInRect(GuiRoutiduct gui, int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return true;
	}

	public void drawPlayerSlots(GuiRoutiduct gui, int posX, int posY, boolean center) {
	}

	public void drawSlot(GuiRoutiduct gui, int posX, int posY) {
	}

	public void drawString(GuiRoutiduct gui, String string, int x, int y, int color) {
	}

	public void drawString(GuiRoutiduct gui, String string, int x, int y) {
	}

	public void setTextureSheet(ResourceLocation textureLocation) {
	}

	public void drawCenteredString(GuiRoutiduct gui, String string, int y, int colour) {
	}

	public void drawCenteredString(GuiRoutiduct gui, String string, int x, int y, int colour) {
	}

	public int getStringWidth(String string) {
		return 0;
	}
}
