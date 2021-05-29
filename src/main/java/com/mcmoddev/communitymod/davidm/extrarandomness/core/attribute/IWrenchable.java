package com.mcmoddev.communitymod.davidm.extrarandomness.core.attribute;

import net.minecraft.entity.player.EntityPlayer;

/**
 * If a block can be picked up by shift right
 * clicking with the Meme Wrench in hand.
 * 
 * @author DavidM
 */
public interface IWrenchable {

	public void onWrench(EntityPlayer player);
}
