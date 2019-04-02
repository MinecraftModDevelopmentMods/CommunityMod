package com.mcmoddev.communitymod.gegy.transiconherobrinebutwithbetterpants;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class NotchButWithWorsererPantsRenderer extends RenderLiving<NotchButWithWorsererPantsEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CommunityGlobals.MOD_ID, "textures/entities/notch_but_with_worserer_pants.png");

    public NotchButWithWorsererPantsRenderer(RenderManager manager) {
        super(manager, new ModelBiped(0.0F), 1.5F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(NotchButWithWorsererPantsEntity entity) {
        return TEXTURE;
    }
}
