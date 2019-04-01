package com.mcmoddev.communitymod.quat.sexyfont;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID, value = Side.CLIENT)
public class SexyClientEvents {
	@SubscribeEvent
	public static void frame(TickEvent.RenderTickEvent e) {
		if(e.phase == TickEvent.Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.player == null) {
				SexyFont.sexyTime = false;
			} else {
				Item head = mc.player.inventory.armorItemInSlot(EntityEquipmentSlot.HEAD.getIndex()).getItem();
				SexyFont.sexyTime = head == SexyFont.Items.SEXY_GLASSES;
			}
		}
	}
}
