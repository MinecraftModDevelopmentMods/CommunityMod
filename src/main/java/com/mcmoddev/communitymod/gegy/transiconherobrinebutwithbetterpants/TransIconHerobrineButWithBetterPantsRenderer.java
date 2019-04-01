package com.mcmoddev.communitymod.gegy.transiconherobrinebutwithbetterpants;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class TransIconHerobrineButWithBetterPantsRenderer extends RenderLiving<TransIconHerobrineButWithBetterPantsEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CommunityGlobals.MOD_ID, "textures/entities/trans_icon_herobrine_but_with_better_pants.png");

    public TransIconHerobrineButWithBetterPantsRenderer(RenderManager manager) {
        super(manager, new TransIconHerobrineButWithBetterPantsModel(0.0F), 0.1F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(TransIconHerobrineButWithBetterPantsEntity entity) {
        return TEXTURE;
    }
}
