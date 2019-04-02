package com.mcmoddev.communitymod.erdbeerbaerlp.BSOD_Item;

import java.util.List;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBSOD extends Item {
	public ItemBSOD() {
		this.setTranslationKey(CommunityGlobals.MOD_ID + ".bsod");
	}
	@Override
	@SideOnly(Side.CLIENT)
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) { //Just in case ;)
			new BlueScreenofDeath();
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.clear();
		tooltip.add(I18n.format("tooltip.community_mod.bsod"));
	}
	
}
