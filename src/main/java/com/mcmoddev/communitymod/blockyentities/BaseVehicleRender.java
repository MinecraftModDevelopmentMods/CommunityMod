package com.mcmoddev.communitymod.blockyentities;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class BaseVehicleRender<T extends BaseVehicleEntity> extends Render<T> {
	
	public BaseVehicleRender(RenderManager manager) {
		super(manager);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(BaseVehicleEntity entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public void doRender(BaseVehicleEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		BufferBuilder buf = Tessellator.getInstance().getBuffer();

		GlStateManager.pushMatrix();
		
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

		BlockPos offset = entity.getOffset();
		GlStateManager.translate(x - offset.getX(), y - offset.getY(), z - offset.getZ());

		if (entity.getStorage().bufferstate == null || entity.getStorage().updateRequired) {
			for (BlockPos bp : entity.getBlocks().keySet()) {
				if (bp == null)
					continue;
				Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(entity.getStorage().getBlockState(bp),
						bp, entity.getStorage(), buf);
			}
			entity.getStorage().bufferstate = buf.getVertexState();
		} else {
			buf.setVertexState(entity.getStorage().bufferstate);
		}

		Tessellator.getInstance().draw(); 

		for (TileEntity t : entity.getStorage().getTESRs()) {
			if (t != null) {
				TileEntityRendererDispatcher.instance.render(t, t.getPos().getX(), t.getPos().getY(), t.getPos().getZ(),
						partialTicks);
			}
		}
		GlStateManager.popMatrix();
	}
}
