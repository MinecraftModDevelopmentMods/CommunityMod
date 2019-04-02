package com.mcmoddev.communitymod.gegy.squashable;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class SquashEntityMessage implements IMessage {
    private int entityId;
    private EnumFacing.Axis axis;

    public SquashEntityMessage() {
    }

    public SquashEntityMessage(Entity entity, EnumFacing.Axis axis) {
        this.entityId = entity.getEntityId();
        this.axis = axis;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();

        EnumFacing.Axis[] values = EnumFacing.Axis.values();
        this.axis = values[buf.readByte() % values.length];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.axis.ordinal());
    }

    public static class Handler implements IMessageHandler<SquashEntityMessage, IMessage> {
        @Override
        public IMessage onMessage(SquashEntityMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                WorldClient world = Minecraft.getMinecraft().world;
                Entity entity = world.getEntityByID(message.entityId);
                if (entity == null) return;

                Squashable squashable = entity.getCapability(SquashableMod.squashableCap(), null);
                if (squashable != null) {
                    squashable.squash(message.axis);
                }
            });

            return null;
        }
    }
}
