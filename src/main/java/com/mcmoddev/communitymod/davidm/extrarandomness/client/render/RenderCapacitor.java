package com.mcmoddev.communitymod.davidm.extrarandomness.client.render;

import org.lwjgl.opengl.GL11;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.AnimationHelper;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.MathHelper;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class RenderCapacitor extends TileEntitySpecialRenderer<TileEntityCapacitor> {

	private static final ResourceLocation POWER_TEXTURE = new ResourceLocation(CommunityGlobals.MOD_ID, "textures/blocks/meme_power.png");
	
	@Override
	public void render(TileEntityCapacitor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.getPower() != 0) {
			GL11.glColor4f(1, 1, 1, (float) MathHelper.oscillate(te.getWorld().getTotalWorldTime() * 6, 0.5, 0.85));
			this.renderPowerTank(te.getScaledPower(), x, y, z);
			GL11.glColor4f(1, 1, 1, 1);
		}
	}
	
	private void renderPowerTank(double scaledPowerLevel, double x, double y, double z) {
		double r = 0.5 - 0.0625 - 0.001;
		
		double mid_x = x + 0.5;
		double mid_z = z + 0.5;
		double minHeight = y + 1 / 16.0;
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
