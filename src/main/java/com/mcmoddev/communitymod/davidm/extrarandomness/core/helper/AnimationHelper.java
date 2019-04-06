package com.mcmoddev.communitymod.davidm.extrarandomness.core.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class AnimationHelper {

	public static void drawPlane(ResourceLocation texture, Vec3d pos_1, Vec3d pos_2, Vec3d pos_3, Vec3d pos_4) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		
		buffer.pos(pos_1.x, pos_1.y, pos_1.z).tex(0, 0).endVertex();
		buffer.pos(pos_2.x, pos_2.y, pos_2.z).tex(0, 1).endVertex();
		buffer.pos(pos_3.x, pos_3.y, pos_3.z).tex(1, 1).endVertex();
		buffer.pos(pos_4.x, pos_4.y, pos_4.z).tex(1, 0).endVertex();
		
		tessellator.draw();
	}
	
	public static void drawDoublePlane(ResourceLocation texture, double x, double y, double z, double time, double radius, EnumFacing facing) {
		drawPlane(texture, x, y, z, time, radius, facing);
		drawPlane(texture, x, y, z, time, radius, facing.getOpposite());
	}
	
	public static void drawPlane(ResourceLocation texture, double x, double y, double z, double time, double radius, EnumFacing facing) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		
		double slant = Math.sqrt(radius * radius * 2);
		int[][] vertPos = {{0, 0}, {0, 1}, {1, 1}, {1, 0}};
		for (int i = 0; i < 4; i++) {
			double offset = Math.toRadians(i * 90 + time);
			double newX = slant * Math.sin(offset);
			double newY = 0;
			double newZ = slant * Math.cos(offset);
			
			double temp;
			
			switch (facing) {
			case UP: break;
			case DOWN: temp = newX; newX = newZ; newZ = temp; break;
			case WEST: temp = newX; newX = newY; newY = newZ; newZ = temp; break;
			case SOUTH: temp = newY; newY = newZ; newZ = temp; break;
			case NORTH: temp = newZ; newZ = newY; newY = newX; newX = temp; break;
			case EAST: temp = newX; newX = newY; newY = temp;
		}
			
			buffer.pos(x + newX, y + newY, z + newZ).tex(vertPos[i][0], vertPos[i][1]).endVertex();
		}
		
		tessellator.draw();
	}
}
