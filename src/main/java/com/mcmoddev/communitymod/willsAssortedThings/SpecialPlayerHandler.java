package com.mcmoddev.communitymod.willsAssortedThings;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpecialPlayerHandler {

    public static Map<UUID, SpecialPlayerEnum> specialUUIDs = new HashMap<>();

    static {
        specialUUIDs.put(UUID.fromString("9b035372-0d8d-4513-8bd5-9808d7f4a9b3"), SpecialPlayerEnum.Willbl3pic);
        specialUUIDs.put(UUID.fromString("330eacd1-1117-4e89-b664-c88f7af73de5"), SpecialPlayerEnum.mojotimmy2);
        specialUUIDs.put(UUID.fromString("8bb30ce6-63f0-4f52-9170-053c646fe86f"), SpecialPlayerEnum.sokratis12GR);
        specialUUIDs.put(UUID.fromString("3db6e5b5-7534-47e4-8640-a32601c4cd01"), SpecialPlayerEnum.calweyland);
        specialUUIDs.put(UUID.fromString("4b8da266-1b9b-4cc3-8697-9afb07178bc2"), SpecialPlayerEnum.Poke1650);
        specialUUIDs.put(UUID.fromString("52d1e4a0-062a-4623-8ac9-4f9ee790f40d"), SpecialPlayerEnum.FiskFille);
        specialUUIDs.put(UUID.fromString("5f98b2e2-658a-4a4c-ad52-332824e51270"), SpecialPlayerEnum.snakefangox);
    }

    public enum SpecialPlayerEnum {
        NONE,
        Willbl3pic,
        mojotimmy2, //Geek of Legends
        sokratis12GR, //SoFoDev
        calweyland,
        Poke1650, //Poke
        FiskFille, //FiskFille
        snakefangox //Snakefangox
    }

    public static SpecialPlayerEnum getSpecialPlayer(EntityPlayer player) {
        return specialUUIDs.getOrDefault(player.getGameProfile().getId(), SpecialPlayerEnum.NONE);
    }
}
