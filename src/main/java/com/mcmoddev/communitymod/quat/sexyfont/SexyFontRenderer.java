package com.mcmoddev.communitymod.quat.sexyfont;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class SexyFontRenderer extends FontRenderer {
	public SexyFontRenderer(GameSettings settings, ResourceLocation res, TextureManager tx, boolean unicode) {
		super(settings, res, tx, unicode);
		
		//Load the font texture n shit.
		onResourceManagerReload(null);
	}
	
	@Override
	public int drawString(String text, float x, float y, int color, boolean dropShadow) {
		//Make it only happen to random pieces of text
		int hash = text.hashCode();
		int offset = (int) (Minecraft.getSystemTime() / 230f) % 8;
		
		if((hash + offset) % 8 != 0) {
			return super.drawString(text, x, y, color, dropShadow);
		}
		
		//Ok this is epic
		float posX = x;
		float huehuehue = (Minecraft.getSystemTime() / 700f) % 1;
		float huehuehueStep = rangeRemap((float) (Math.sin(Minecraft.getSystemTime() / 2000f) % 6.28318f), -1, 1, 0.01f, 0.15f);
		
		String textRender = ChatFormatting.stripFormatting(text);
		
		for(int i = 0; i < textRender.length(); i++) {
			int c = (color & 0xFF000000) | MathHelper.hsvToRGB(huehuehue, .8f, 1);
			
			float yOffset = (float) Math.sin(i + (Minecraft.getSystemTime() / 300f));
			
			posX = super.drawString(String.valueOf(textRender.charAt(i)), posX, y + yOffset, c, true) - 1;
			
			huehuehue += huehuehueStep;
			huehuehue %= 1;
		}
		
		return (int) posX;
	}
	
	private static float rangeRemap(float value, float low1, float high1, float low2, float high2) {
		return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
	}
}
