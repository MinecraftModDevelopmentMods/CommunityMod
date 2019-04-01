package com.mcmoddev.communitymod;

import com.mcmoddev.communitymod.quat.dabbbbb.Dabbbbb;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommunityGlobals {
	
	public static int entity_id = 0;
	public static final String MOD_ID = "community_mod";
	public static final CreativeTabs TAB = new CreativeTabs(MOD_ID) {
		@SideOnly(Side.CLIENT)
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Dabbbbb.dab);
		}
	};
}
