package com.mcmoddev.communitymod.quat.dabbbbb;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemThatMakesYouSayDab extends Item {
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			String text = Math.random() >= 0.01 ? "Dab" : "Neat (is a mod by Vazkii)";
			world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentTranslation("chat.type.text", player.getName(), text));
			
			if(Dabbbbb.whenUBoppin) {
				for(int i = 0; i < 10; i++) {
					world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1, (i / 10f) + 0.5f);
				}
				player.motionY += 10;
				player.velocityChanged = true;
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
