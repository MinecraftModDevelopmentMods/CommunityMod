/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *//*


package prospector.routiduct.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.itemHandler.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prospector.routiduct.RoutiductConstants;
import prospector.routiduct.api.Package;

*/
/**
 * Created by Prospector
 *//*

@SideOnly(Side.CLIENT)
public class GuiAssembler {
	public static final ResourceLocation GUI_SHEET = new ResourceLocation(RoutiductConstants.PREFIX + "textures/gui/gui_sheet.png");

	public GuiScreen create(GuiBlueprints blueprints) {
		return new GuiContainer() {
			@Override
			public void drawScreen(int mouseX, int mouseY, float partialTicks) {
				super.drawScreen(mouseX, mouseY, partialTicks);
			}

			@Override
			protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
				super.drawGuiContainerForegroundLayer(mouseX, mouseY);
			}

			@Override
			protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

			}
		};
	}

	public void drawSlot(GuiScreen gui, int posX, int posY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(posX, posY, 150, 0, 18, 18);
	}

	public void drawProgress(GuiScreen gui, int progress, int maxProgress, int posX, int posY, Package.EnumColor colour) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(posX, posY, 168, 0, 28, 8);
		int i = (int) ((double) progress / (double) maxProgress * 26);
		if (i < 0)
			i = 0;
		gui.drawTexturedModalRect(posX + 1, posY + 1, 150, colour.ordinal() * 6 + 18, i, 6);
		drawString(gui, getPercentage(maxProgress, progress) + "%", posX + 4, posY - 8, 0xFF000000);

	}

	public void drawOutputBar(GuiScreen gui, int progress, int maxProgress, int posX, int posY, Package.EnumColor colour) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(posX, posY, 0, 186, 90, 12);
		int i = (int) ((double) progress / (double) maxProgress * 90);
		if (i < 0)
			i = 0;
		gui.drawTexturedModalRect(posX + 1, posY + 1, 150, colour.ordinal() * 6 + 18, i, 6);
		drawShadowString(gui, getPercentage(maxProgress, progress) + "%", posX + 4, posY - 8, 0xFF000000);

	}

	public void drawString(GuiScreen gui, String string, int x, int y) {
		drawString(gui, string, x, y, 16777215);
	}

	public void drawString(GuiScreen gui, String string, int x, int y, int colour) {
		gui.mc.fontRendererObj.drawString(string, x, y, colour);
	}

	public void drawShadowString(GuiScreen gui, String string, int x, int y) {
		drawShadowString(gui, string, x, y, 16777215);
	}

	public void drawShadowString(GuiScreen gui, String string, int x, int y, int colour) {
		gui.mc.fontRendererObj.drawStringWithShadow(string, x, y, colour);
	}

	public TextFormatting getPercentageColour(int percentage) {
		if (percentage <= 10) {
			return TextFormatting.RED;
		} else if (percentage >= 75) {
			return TextFormatting.GREEN;
		} else {
			return TextFormatting.YELLOW;
		}
	}

	public int getPercentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}
}
*/
