package com.mcmoddev.communitymod.gegy.squashable;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

@SubMod(
		modid = "squashable",
        name = "squashable",
        description = "Flatten entities with a piston",
        attribution = "gegy1000"
)
public class SquashableMod implements ISubMod {
    @CapabilityInject(Squashable.class)
    private static Capability<Squashable> squashableCapability;

    private static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(CommunityGlobals.MOD_ID + ".squashable");

    private static final Field PISTON_PUSH_TIME_FIELD = ObfuscationReflectionHelper.findField(Entity.class, "pistonDeltasGameTime");
    private static final Field PISTON_DELTAS_FIELD = ObfuscationReflectionHelper.findField(Entity.class, "pistonDeltas");
    private static final Field RENDERER_MODEL_FIELD = ObfuscationReflectionHelper.findField(RenderLivingBase.class, "mainModel");

    private static long getPistonPushTime(Entity entity) {
        try {
            return (long) PISTON_PUSH_TIME_FIELD.get(entity);
        } catch (IllegalAccessException e) {
            return 0;
        }
    }

    private static double[] getPistonDeltas(Entity entity) {
        try {
            return (double[]) PISTON_DELTAS_FIELD.get(entity);
        } catch (IllegalAccessException e) {
            return new double[3];
        }
    }

    private static void setMainModel(RenderLivingBase<?> renderer, ModelBase model) {
        try {
            RENDERER_MODEL_FIELD.set(renderer, model);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(Squashable.class, Squashable.Storage.INSTANCE, Squashable::new);

        NETWORK.registerMessage(SquashEntityMessage.Handler.class, SquashEntityMessage.class, 0, Side.CLIENT);
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(new ResourceLocation(CommunityGlobals.MOD_ID, "squashable"), new Squashable());
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote) {
            return;
        }

        if (entity.collidedHorizontally && isBeingPushedByPiston(entity)) {
            Squashable squashable = entity.getCapability(squashableCap(), null);
            if (squashable == null) {
                return;
            }

            double[] pistonDeltas = getPistonDeltas(entity);
            double pushedAngle = Math.atan2(pistonDeltas[2], pistonDeltas[0]);

            EnumFacing.Axis faceAxis = EnumFacing.fromAngle(entity.rotationYaw).getAxis();
            EnumFacing.Axis pushAxis = EnumFacing.fromAngle(pushedAngle).getAxis();

            EnumFacing.Axis squashAxis = faceAxis == pushAxis ? EnumFacing.Axis.Z : EnumFacing.Axis.X;

            squashable.squash(squashAxis);

            NETWORK.sendToAllTracking(new SquashEntityMessage(entity, squashAxis), entity);
        }
    }

    @SubscribeEvent
    public static void onEntityTrack(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        Squashable squashable = target.getCapability(squashableCap(), null);
        if (squashable == null) {
            return;
        }

        EnumFacing.Axis squashedAxis = squashable.getSquashedAxis();
        if (squashedAxis != null) {
            NETWORK.sendTo(new SquashEntityMessage(target, squashedAxis), (EntityPlayerMP) event.getEntityPlayer());
        }
    }

    private static boolean isBeingPushedByPiston(Entity entity) {
        // we're always a tick late
        long pistonPushTime = getPistonPushTime(entity) + 1;
        return pistonPushTime == entity.world.getTotalWorldTime();
    }

    public static Capability<Squashable> squashableCap() {
        if (squashableCapability == null) {
            throw new IllegalStateException("Squashable capability not initialize");
        }
        return squashableCapability;
    }

    @SideOnly(Side.CLIENT)
    private static ModelBase originalModel;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?> event) {
        EntityLivingBase entity = event.getEntity();
        Squashable squashable = entity.getCapability(squashableCap(), null);
        if (squashable == null) {
            return;
        }

        EnumFacing.Axis squashedAxis = squashable.getSquashedAxis();
        if (squashedAxis != null) {
            RenderLivingBase<?> renderer = event.getRenderer();
            originalModel = renderer.getMainModel();
            setMainModel(renderer, new FlattenedModel(originalModel, squashedAxis));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderLivingPost(RenderLivingEvent.Post<?> event) {
        if (originalModel != null) {
            setMainModel(event.getRenderer(), originalModel);
            originalModel = null;
        }
    }
}
