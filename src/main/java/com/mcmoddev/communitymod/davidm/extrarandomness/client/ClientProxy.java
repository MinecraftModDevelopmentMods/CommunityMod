package com.mcmoddev.communitymod.davidm.extrarandomness.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.IProxy;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ClientProxy implements IProxy {

	public static final ModelResourceLocation OUTPUT_MODEL = new ModelResourceLocation(CommunityGlobals.MOD_ID + "block/output");
	
	public static int sphereOutId;
	public static int sphereInId;
	
	@Override
	public void init() {
		Sphere sphere = new Sphere();
		
		sphere.setDrawStyle(GLU.GLU_FILL);
		sphere.setNormals(GLU.GLU_SMOOTH);
		
		sphere.setOrientation(GLU.GLU_OUTSIDE);
		sphereOutId = GlStateManager.glGenLists(1);
		GlStateManager.glNewList(sphereOutId, GL11.GL_COMPILE);
		sphere.draw(0.5F, 32, 32);
		GlStateManager.glEndList();
		
		sphere.setOrientation(GLU.GLU_INSIDE);
		sphereInId = GlStateManager.glGenLists(1);
		GlStateManager.glNewList(sphereInId, GL11.GL_COMPILE);
		sphere.draw(0.5F, 32, 32);
		sphere.draw(0.5F, 32, 32);
	}
}
