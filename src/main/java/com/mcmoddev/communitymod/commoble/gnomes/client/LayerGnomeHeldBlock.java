package com.mcmoddev.communitymod.commoble.gnomes.client;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;

// mostly copied from LayerHeldBlock, which is enderman-specific
public class LayerGnomeHeldBlock<GnomeType extends EntityGnome, RenderType extends RenderLiving> implements LayerRenderer<GnomeType>
{
	private final RenderType render;

    public LayerGnomeHeldBlock(RenderType render)
    {
        this.render = render;
    }

	@Override
	public void doRenderLayer(GnomeType entitylivingbaseIn, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks,
			float netHeadYaw, float headPitch, float scale)
	{
        IBlockState iblockstate = entitylivingbaseIn.getCarried();

        if (iblockstate != null)
        {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.25F, 1.125F, -0.25F);
            //GlStateManager.translate(0.0F, 0.6875F, -0.75F);
            GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
            //GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
            //GlStateManager.translate(0.25F, 0.1875F, 0.25F);
            GlStateManager.scale(-0.5F, -0.5F, 0.5F);
            int i = entitylivingbaseIn.getBrightnessForRender();
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.render.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0F);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
	}

	@Override
	public boolean shouldCombineTextures()
	{
		// same as enderman
		return false;
	}
}
