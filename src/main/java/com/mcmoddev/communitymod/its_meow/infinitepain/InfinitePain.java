package com.mcmoddev.communitymod.its_meow.infinitepain;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "Infinite Pain", description = "Now you fall forever because I said so", attribution = "its_meow")
public class InfinitePain implements ISubMod {

	public static final ItemArmor PAIN_BOOTS = (ItemArmor) new ItemArmor(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.FEET).setRegistryName(new ResourceLocation(CommunityGlobals.MOD_ID, "pain_boots")).setCreativeTab(CommunityGlobals.TAB).setTranslationKey(CommunityGlobals.MOD_ID + ".pain_boots");

	public static int minTriggerHeight = 5;
	public static int heightToAdd = 0;
	public static float damageOnImpact = 2F;

	@Override
	public void registerItems(IForgeRegistry<Item> reg) {
		reg.register(PAIN_BOOTS);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ClientUtil.simpleItemModel(PAIN_BOOTS);
	}
	
	@SubscribeEvent
	public static void onLanding(LivingFallEvent event) {
		EntityLivingBase elb = event.getEntityLiving();
		if(elb.hasItemInSlot(EntityEquipmentSlot.FEET) && elb.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == PAIN_BOOTS) {
			if(event.getDistance() >= minTriggerHeight) {

				boolean notObstructed = true;
				double impactPosition = 0;

				for(int i = (int) elb.posY + 2; i < elb.world.provider.getHeight(); i++) {
					BlockPos pos = new BlockPos(elb.posX, i, elb.posZ);
					IBlockState state = elb.world.getBlockState(pos);
					if(state.isFullBlock() || state.isFullCube()) {
						notObstructed = false;
						impactPosition = i;
						break;
					}
				}


				if(notObstructed) {
					elb.setPositionAndUpdate(elb.posX, elb.world.provider.getHeight() + heightToAdd, elb.posZ);
					event.setDamageMultiplier(0);
				} else {
					elb.addVelocity(0, (impactPosition - elb.posY) / 2, 0);
					elb.attackEntityFrom(DamageSource.GENERIC, damageOnImpact);
					event.setDamageMultiplier(0);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLandingCreative(PlayerFlyableFallEvent event) {
		EntityLivingBase elb = event.getEntityLiving();
		if(elb.hasItemInSlot(EntityEquipmentSlot.FEET) && elb.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == PAIN_BOOTS) {
			if(event.getDistance() >= minTriggerHeight) {

				boolean notObstructed = true;
				double impactPosition = 0;

				for(int i = (int) elb.posY + 2; i < elb.world.provider.getHeight(); i++) {
					BlockPos pos = new BlockPos(elb.posX, i, elb.posZ);
					IBlockState state = elb.world.getBlockState(pos);
					if(state.isFullBlock() || state.isFullCube()) {
						notObstructed = false;
						impactPosition = i;
						break;
					}
				}


				if(notObstructed) {
					elb.setPositionAndUpdate(elb.posX, elb.world.provider.getHeight() + heightToAdd, elb.posZ);
				} else {
					elb.addVelocity(0, (impactPosition - elb.posY) / 2, 0);
				}
			}
		}
	}

	@Override
	public void setupConfiguration(Configuration config, String category) {
		minTriggerHeight = config.getInt("min_trigger_height", category, minTriggerHeight, 0, Integer.MAX_VALUE, "The minimum height to fall initially in order to trigger infinite fall");
		heightToAdd = config.getInt("height_to_add", category, heightToAdd, Integer.MIN_VALUE, Integer.MAX_VALUE, "Height that will be added to the world height for the infinite fall teleport");
		damageOnImpact = config.getFloat("damage_on_impact", category, damageOnImpact, 0, Float.MAX_VALUE, "Amount of damage to deal when impacting a block above you");
	}

}
