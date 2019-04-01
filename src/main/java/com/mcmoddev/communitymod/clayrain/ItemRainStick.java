package com.mcmoddev.communitymod.clayrain;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class ItemRainStick extends Item {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (!player.capabilities.isCreativeMode) heldStack.shrink(1);

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SKELETON_DEATH, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            WorldInfo worldinfo = world.getWorldInfo();
            worldinfo.setCleanWeatherTime(0);
            worldinfo.setRainTime(1200);
            worldinfo.setThunderTime(1200);
            worldinfo.setRaining(true);
            worldinfo.setThundering(false);
        } else player.sendMessage(new TextComponentString("Dark clouds start forming in the sky"));

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, heldStack);
    }
}