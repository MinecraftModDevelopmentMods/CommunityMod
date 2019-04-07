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

	private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");
	
	public static void drawPlane(ResourceLocation texture, Vec3d pos_1, Vec3d pos_2, Vec3d pos_3, Vec3d pos_4) {
		drawPlane(texture, pos_1, pos_2, pos_3, pos_4, new double[][] {{0, 0}, {0, 1}, {1, 1}, {1, 0}});
	}
	
	public static void drawPlane(ResourceLocation texture, Vec3d pos_1, Vec3d pos_2, Vec3d pos_3, Vec3d pos_4, double[][] vertPos) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		
		buffer.pos(pos_1.x, pos_1.y, pos_1.z).tex(vertPos[0][0], vertPos[0][1]).endVertex();
		buffer.pos(pos_2.x, pos_2.y, pos_2.z).tex(vertPos[1][0], vertPos[1][1]).endVertex();
		buffer.pos(pos_3.x, pos_3.y, pos_3.z).tex(vertPos[2][0], vertPos[2][1]).endVertex();
		buffer.pos(pos_4.x, pos_4.y, pos_4.z).tex(vertPos[3][0], vertPos[3][1]).endVertex();
		
		tessellator.draw();
	}
	
	public static void drawDoublePlane(ResourceLocation texture, double x, double y, double z, double time, double radius, EnumFacing facing) {
		drawPlane(texture, x, y, z, time, radius, facing);
		drawPlane(texture, x, y, z, time, radius, facing.getOpposite());
	}
	
	public static void drawPlane(ResourceLocation texture, double x, double y, double z, double time, double radius, EnumFacing facing) {
		drawPlane(texture, x, y, z, time, radius, facing, new double[][] {{0, 0}, {0, 1}, {1, 1}, {1, 0}});
	}
	
	public static void drawPlane(ResourceLocation texture, double x, double y, double z, double time, double radius, EnumFacing facing, double[][] vertPos) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		
		double slant = Math.sqrt(radius * radius * 2);
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
	
	public static void drawBeam(double x, double y, double z, double length, double radius, double time, EnumFacing facing) {
		GlStateManager.pushMatrix();
		
		Vec3d[] points = new Vec3d[4];
		
		double temp;
		
		switch(facing) {
			case DOWN: GlStateManager.rotate(180, 0, 0, 1); y = -y; x = -x; break;
			case WEST: GlStateManager.rotate(90, 0, 0, 1); temp = y; y = -x; x = temp; break;
			case SOUTH: GlStateManager.rotate(90, 1, 0, 0); temp = y; y = z; z = -temp; break;
			case NORTH: GlStateManager.rotate(270, 1, 0, 0); temp = y; y = -z; z = temp; break;
			case EAST: GlStateManager.rotate(270, 0, 0, 1); temp = y; y = x; x = -temp; break;
			default: break;
		}
		
		for (int i = 0; i < 4; i++) {
			double rotation = time * 0.085 + i * Math.PI / 2;
			double new_x = x + radius * Math.sin(rotation);
			double new_z = z + radius * Math.cos(rotation);
			points[i] = new Vec3d(new_x, y, new_z);
		}
		
		double startVert = -time / 15;
		double widthScale = radius * 3;
		double[][] vertPos = new double[][] {{0, startVert}, {1, startVert}, {1, startVert + length / widthScale}, {0, startVert + length / widthScale}};
		
		for (int i = 0; i < 4; i++) {
			Vec3d point_1 = points[i];
			Vec3d point_2 = points[(i + 1) % 4];
			Vec3d point_3 = point_2.add(0, length, 0);
			Vec3d point_4 = point_1.add(0, length, 0);
			drawPlane(BEAM_TEXTURE, point_1, point_2, point_3, point_4, vertPos);
		}
		
		GlStateManager.popMatrix();
	}
}
