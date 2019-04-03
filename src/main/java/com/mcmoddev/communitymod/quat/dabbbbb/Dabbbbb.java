package com.mcmoddev.communitymod.quat.dabbbbb;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;
import com.mcmoddev.communitymod.shared.RegUtil;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(
	modid = "dabitem",
	name = "dabbbbbbb",
	description = "Adds an item that really makes you say dab",
	attribution = "quaternary"
)
public class Dabbbbb implements ISubMod {
	public static final String DAB_NAME = "dab";
	
	public static boolean whenUBoppin = true;
	
	@Override
	public void setupConfiguration(Configuration config, String categoryId) {
		whenUBoppin = config.getBoolean("when u boppin", categoryId, true, "Makes the dab extra powerful");
	}
	
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
