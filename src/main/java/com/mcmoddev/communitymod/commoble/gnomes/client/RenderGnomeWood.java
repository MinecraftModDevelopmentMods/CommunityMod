package com.mcmoddev.communitymod.commoble.gnomes.client;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnomeWood;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


// Referenced classes of package net.minecraft.src:
//            RenderLiving, JWorld_EntityExample, ModelBase, EntityLiving, 
//            Entity


@SideOnly(Side.CLIENT)
public class RenderGnomeWood extends RenderLiving<EntityGnomeWood>
{
	private static final ResourceLocation texture = new ResourceLocation("community_mod:textures/entities/gnomewood.png");
    private ModelGnome model;
	public static final Factory FACTORY = new Factory();
	
	public static class Factory implements IRenderFactory<EntityGnomeWood>
	{
		@Override
		public Render<? super EntityGnomeWood> createRenderFor(RenderManager manager)
		{
			return new RenderGnomeWood(manager);
		}
	}
	
    @Override
    protected ResourceLocation getEntityTexture(EntityGnomeWood par1Entity)
    {
        return texture;
    }
	
	public RenderGnomeWood(RenderManager rm)
	{
        super(rm, new ModelGnome(), 0.3F);
        this.model = (ModelGnome)super.mainModel;
        this.addLayer(new LayerGnomeHeldBlock<EntityGnomeWood, RenderGnomeWood>(this));
	}

	public void renderGnomeWood(EntityGnomeWood ent, double d, double d1, double d2, 
            float f, float f1)
    {
    	this.model.isCarrying = (ent.getCarried().getBlock() != Blocks.AIR);
        super.doRender(ent, d, d1, d2, f, f1);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
    	renderGnomeWood((EntityGnomeWood)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(EntityGnomeWood entity, double d, double d1, double d2, 
            float f, float f1)
    {
    	renderGnomeWood((EntityGnomeWood)entity, d, d1, d2, f, f1);
    }
}