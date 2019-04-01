package com.mcmoddev.communitymod.quat.dabbbbb;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;
import com.mcmoddev.communitymod.shared.RegUtil;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(
	name = "dabbbbbbb",
	description = "Really makes you dabbbbbb",
	attribution = "quaternary"
)
public class Dabbbbb implements ISubMod {
	public static final String DAB_NAME = "dab";
	
	public static ItemThatMakesYouSayDab dab;
	
	@Override
	public void registerItems(IForgeRegistry<Item> reg) {
		dab = RegUtil.registerItem(reg, new ItemThatMakesYouSayDab(), DAB_NAME);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ClientUtil.simpleItemModel(dab);
	}
}
