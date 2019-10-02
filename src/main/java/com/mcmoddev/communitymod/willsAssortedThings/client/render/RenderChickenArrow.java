package com.mcmoddev.communitymod.willsAssortedThings.client.render;

import com.mcmoddev.communitymod.willsAssortedThings.entity.EntityChickenArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderChickenArrow extends RenderArrow<EntityChickenArrow> {

    private ResourceLocation texture = new ResourceLocation("minecraft:textures/entity/chicken.png");

    public RenderChickenArrow(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityChickenArrow entity) {
        return texture;
    }
}
