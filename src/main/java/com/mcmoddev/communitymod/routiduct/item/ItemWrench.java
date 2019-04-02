package com.mcmoddev.communitymod.routiduct.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Prospector
 */
public class ItemWrench extends ItemRD {

	public ItemWrench() {
		super("wrench");
		setMaxStackSize(1);
		setHarvestLevel("wrench", 1);
		addPropertyOverride(new ResourceLocation("routiduct:lat"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if (entityIn != null && entityIn.getDisplayName().getUnformattedText().equals("LatvianModder")) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
		addPropertyOverride(new ResourceLocation("routiduct:ugly"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if (entityIn != null && entityIn.getDisplayName().getUnformattedText().equals("InsomniaKitten")) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
		addPropertyOverride(new ResourceLocation("routiduct:drawn"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if (entityIn != null && (entityIn.getDisplayName().getUnformattedText().equals("quaternary") || entityIn.getDisplayName().getUnformattedText().equals("LordSaad"))) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
		addPropertyOverride(new ResourceLocation("routiduct:hawk"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if (entityIn != null && entityIn.getDisplayName().getUnformattedText().equals("Shadow_Hawk")) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && player.isSneaking()) {
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
