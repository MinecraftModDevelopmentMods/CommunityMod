package com.mcmoddev.communitymod.routiduct.gui;

import net.minecraft.util.ResourceLocation;

public class Sprite {
	public final ResourceLocation textureLocation;
	public final int x;
	public final int y;
	public final int width;
	public final int height;

	public Sprite(ResourceLocation textureLocation, int x, int y, int width, int height) {
		this.textureLocation = textureLocation;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
