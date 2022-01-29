package com.mcmoddev.communitymod.davidm.extrarandomness.common.item;

import java.util.List;

import com.mcmoddev.communitymod.davidm.extrarandomness.core.attribute.AltarItem;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GoldenEgg extends Item implements AltarItem {

	public GoldenEgg() {
		super();
		this.setMaxStackSize(1);
	}
	
	@Override
	public int getCooldown() {
		return 2;
	}

	@Override
	public void onAltarAction(World world, BlockPos pos) {
		EntityEgg egg = new EntityEgg(world, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
		world.spawnEntity(egg);
		egg.addVelocity(world.rand.nextFloat() * 2 - 1, world.rand.nextFloat(), world.rand.nextFloat() * 2 - 1);
		egg.velocityChanged = true;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.community_mod.altar_item"));
		tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.community_mod.golden_egg_extra"));
	}
}
