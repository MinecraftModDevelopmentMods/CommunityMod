package com.mcmoddev.communitymod.sokratis12gr.whatarethose;


import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@SubMod(
    name = "What are Those",
    description = "WHAT ARE THOOOOOSE!?",
    attribution = "sokratis12GR"
)
@Mod.EventBusSubscriber
public class WhatAreThose implements ISubMod {

    public static Random rand = new Random();

    @SubscribeEvent()
    public static void onEquipEvent(TickEvent.PlayerTickEvent event) {
        if (rand.nextInt(1000) < 1) {
            EntityPlayer player = event.player;
            BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
            IBlockState state = player.world.getBlockState(pos.add(0, -1, 0));
            if (player.onGround) {
                for (ItemStack stack : player.inventory.armorInventory) {
                    if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).getEquipmentSlot() == EntityEquipmentSlot.FEET && state == Blocks.DIAMOND_BLOCK) {
                        player.sendMessage(new TextComponentString("What are THOOOOOSE!?"));
                    }
                }
            }
        }
    }
}
