package com.mcmoddev.communitymod.anvilsofferadvice;

import com.mcmoddev.communitymod.paranoia.Paranoia;

import net.minecraft.client.gui.GuiRepair;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value=Side.CLIENT) // TODO: This should probably be moved into its own ISubMod
public class MenuEventHandler {

	@SubscribeEvent
	public static void turnMenus(DrawScreenEvent e) {
		if(e.getGui() instanceof GuiRepair)
		e.getGui().drawString(e.getGui().mc.fontRenderer, "Think before you smith", Paranoia.rand.nextInt(e.getGui().width), Paranoia.rand.nextInt(e.getGui().width), Paranoia.rand.nextInt());
	}
}
