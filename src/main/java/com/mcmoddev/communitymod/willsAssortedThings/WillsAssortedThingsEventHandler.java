package com.mcmoddev.communitymod.willsAssortedThings;

import com.mcmoddev.communitymod.willsAssortedThings.item.ModItems;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class WillsAssortedThingsEventHandler {

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.EntityInteract event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.ARROW) {
            if (event.getTarget() instanceof EntityChicken) {
                event.getWorld().playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.VOICE, 2.0f, 1.3f, false);
                player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount()-1);
                player.inventory.addItemStackToInventory(new ItemStack(ModItems.CHICKEN_ARROW));
            }
        }
    }
}
