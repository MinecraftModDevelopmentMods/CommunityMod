package com.mcmoddev.communitymod.tightpants;

import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTightPants extends ItemArmor {

	public static final ArmorMaterial SPANDEX = EnumHelper.addArmorMaterial("SPANDEX", CommunityGlobals.MOD_ID + ":" + "spandex", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f);
	
	public ItemTightPants() {
		
		super(SPANDEX, 0, EntityEquipmentSlot.LEGS);
		this.setTranslationKey(CommunityGlobals.MOD_ID + ".tight_pants");
		this.setCreativeTab(CommunityGlobals.TAB);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("tooltip.community_mod.tight_pants." + (isActive(stack) ? "activated" : "inactive")));
    }
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	
    	toggle(playerIn.getHeldItem(handIn));
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
		
        return isActive(stack) || super.hasEffect(stack);
    }
	
    public static void toggle(ItemStack stack) {
    	
    	NBTTagCompound tagCompound = stack.getTagCompound();
    	
    	if (tagCompound == null) {
    		
    		tagCompound = new NBTTagCompound();
    		stack.setTagCompound(tagCompound);
    	}
    	
    	tagCompound.setBoolean("Activated", !tagCompound.getBoolean("Activated"));
    }
    
	public static boolean isActive(ItemStack stack) {
		
		return stack.hasTagCompound() && stack.getTagCompound().getBoolean("Activated");
	}
}