package com.mcmoddev.communitymod.davidm.extrarandomness.client.render;

import org.lwjgl.opengl.GL11;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumSideConfig;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.AnimationHelper;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.MathHelper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class RenderCapacitor extends TileEntitySpecialRenderer<TileEntityCapacitor> {

	private static final ResourceLocation POWER_TEXTURE = new ResourceLocation(CommunityGlobals.MOD_ID, "textures/blocks/meme_power.png");
	private static final ResourceLocation CONFIG_TEXTURE = new ResourceLocation(CommunityGlobals.MOD_ID, "textures/blocks/config.png");
	private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");
	
	@Override
	public void render(TileEntityCapacitor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		long totalWorldTime = te.getWorld().getTotalWorldTime();
		
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		if (te.getPower() != 0) {
			GlStateManager.color(1, 1, 1, (float) MathHelper.oscillate(totalWorldTime * 6, 0.5, 0.85));
			this.renderPowerTank(te.getScaledPower(), x, y, z);
		}
		
		this.renderSideConfig(te, x, y, z);
		GlStateManager.color(1, 1, 1, 1);
		
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
	
	private void renderSideConfig(TileEntityCapacitor te, double x, double y, double z) {
		EnumSideConfig[] sideConfigs = te.getSideConfigs();
		double center_x = x + 0.5;
		double center_y = y + 0.5;
		double center_z = z + 0.5;
		long time = te.getWorld().getTotalWorldTime();
		
		for (int i = 0; i < sideConfigs.length; i++) {
			if (sideConfigs[i] == EnumSideConfig.NONE) continue;
			
			double new_x = center_x;
			double new_y = center_y;
			double new_z = center_z;
			double r = 0.75;
			EnumFacing facing = EnumFacing.values()[i];
			
			if (sideConfigs[i] == EnumSideConfig.INPUT) {
				GlStateManager.color(0.05F, 0.47F, 0.82F, (float) MathHelper.oscillate(time * 6, 0.5, 0.85));
			} else {
				GlStateManager.color(0.83F, 0.43F, 0.11F, (float) MathHelper.oscillate(time * 6, 0.5, 0.85));
			}
			
			switch(facing) {
				case UP: new_y += r; break;
				case DOWN: new_y -= r; break;
				case SOUTH: new_z += r; break;
				case NORTH: new_z -= r; break;
				case EAST: new_x += r; break;
				case WEST: new_x -= r; break;
				default: break;
			}
			
			AnimationHelper.drawDoublePlane(CONFIG_TEXTURE, new_x, new_y, new_z, time, 0.5, facing);
		}
	}
	
	private void renderPowerTank(double scaledPowerLevel, double x, double y, double z) {
		double r = 0.5 - 0.0625 - 0.001;
		
		double mid_x = x + 0.5;
		double mid_z = z + 0.5;
		double minHeight = y + 1 / 16.0 + 0.001;
		double maxHeight = minHeight + 14 * scaledPowerLevel / 16.0;
		AnimationHelper.drawPlane(
				POWER_TEXTURE,
				new Vec3d(mid_x - r, maxHeight, mid_z - r),
				new Vec3d(mid_x - r, maxHeight, mid_z + r),
				new Vec3d(mid_x + r, maxHeight, mid_z + r),
				new Vec3d(mid_x + r, maxHeight, mid_z - r)
		);
		
		AnimationHelper.drawPlane(
				POWER_TEXTURE,
				new Vec3d(mid_x - r, minHeight, mid_z - r),
				new Vec3d(mid_x - r, minHeight, mid_z + r),
				new Vec3d(mid_x - r, maxHeight, mid_z + r),
				new Vec3d(mid_x - r, maxHeight, mid_z - r)
		);
		
		AnimationHelper.drawPlane(
				POWER_TEXTURE,
				new Vec3d(mid_x + r, minHeight, mid_z + r),
				new Vec3d(mid_x + r, minHeight, mid_z - r),
				new Vec3d(mid_x + r, maxHeight, mid_z - r),
				new Vec3d(mid_x + r, maxHeight, mid_z + r)
		);
		
		AnimationHelper.drawPlane(
				POWER_TEXTURE,
				new Vec3d(mid_x + r, minHeight, mid_z - r),
				new Vec3d(mid_x - r, minHeight, mid_z - r),
				new Vec3d(mid_x - r, maxHeight, mid_z - r),
				new Vec3d(mid_x + r, maxHeight, mid_z - r)
		);
		
		AnimationHelper.drawPlane(
				POWER_TEXTURE,
				new Vec3d(mid_x - r, minHeight, mid_z + r),
				new Vec3d(mid_x + r, minHeight, mid_z + r),
				new Vec3d(mid_x + r, maxHeight, mid_z + r),
				new Vec3d(mid_x - r, maxHeight, mid_z + r)
		);
	}
}
