package com.mcmoddev.communitymod.commoble.intradimensional_portals.client;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// copied from ParticleCrit due to oSize having weird protection
@SideOnly(Side.CLIENT)
public class ParticlePortal extends Particle
{
    protected float oSize;
    /** Base spell texture index */
    private int baseSpellTextureIndex = 128;

    public ParticlePortal(World world, double x, double y, double z, double xVel, double yVel, double zVel, float red, float green, float blue)
    {
        
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        //this.motionX *= 0.10000000149011612D;
        //this.motionY *= 0.10000000149011612D;
        //this.motionZ *= 0.10000000149011612D;
        //this.motionX += xVel * 0.4D;
        //this.motionY += yVel * 0.4D;
        //this.motionZ += zVel * 0.4D;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.motionX = xVel;
        this.motionY = yVel;
        this.motionZ = zVel;
        //this.particleScale *= 0.1F;	//0.75F;
        
        this.oSize = this.particleScale;
        
        this.particleMaxAge = (int)(20.0D / (Math.random() * 0.8D + 0.6D));
        //this.particleMaxAge = (int)((float)this.particleMaxAge * scale);
        //this.setParticleTextureIndex(65);
        this.onUpdate();
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        /*float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = this.oSize * f;*/
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));

        this.move(this.motionX, this.motionY, this.motionZ);
        
        /**this.particleRed *= 0.98F;
        this.particleGreen *= 0.98F;
        this.particleBlue *= 0.98F;**/
        
        //this.particleScale *= 0.98F;
        
        //this.particleGreen = (float)(this.particleGreen * 0.975F);
        //this.particleBlue = (float)(this.particleBlue * 0.95F);
        //this.motionX *= 0.9D;
        //if (this.motionY > 0D) this.motionY *= 0.7D;
        //this.motionZ *= 0.9D;
        //this.motionY -= 0.06D;

        /*if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }*/
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticlePortal(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, 1F, 1F, 1F);
        }
    }
}
