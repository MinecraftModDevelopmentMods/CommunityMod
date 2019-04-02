package com.mcmoddev.communitymod.blockyentities;

import com.mcmoddev.communitymod.musksrockets.packets.SeatSyncPacket;
import com.mcmoddev.communitymod.musksrockets.packets.VehicleUpdatePacket;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public PacketHandler() {
    }

    public static int nextID() {
        return packetId++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(VehicleUpdatePacket.Handler.class, VehicleUpdatePacket.class,  nextID(), Side.CLIENT);
        INSTANCE.registerMessage(SeatSyncPacket.Handler.class, SeatSyncPacket.class,  nextID(), Side.CLIENT);
    }
}