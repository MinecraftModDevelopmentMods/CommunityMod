package com.mcmoddev.communitymod.lemons.neatnether.handler;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID)
public class GeneralNeatNetherEventHandler
{
	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if(event.getWorld().provider.getDimension() == -1)
		{
			if(event.getEntity() instanceof EntitySkeleton)
			{

				event.setCanceled(true);
				EntityWitherSkeleton witherSkeleton = new EntityWitherSkeleton(event.getWorld());
				witherSkeleton.setPosition(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ);
				event.getWorld().spawnEntity(witherSkeleton);
				witherSkeleton.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
				witherSkeleton.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.GOLDEN_SWORD));
			}
			else if(event.getEntity() instanceof EntityWitherSkeleton && event.getWorld().rand.nextInt(5) == 0)
			{
				Random rand = event.getWorld().rand;
				ItemStack stack = new ItemStack(Items.BOW);

				if(rand.nextBoolean())
				{
					EnchantmentHelper.addRandomEnchantment(rand, stack, (int) (5.0F + 1 * rand.nextInt(18)), false);
				}
				((EntityWitherSkeleton)event.getEntity()).setHeldItem(EnumHand.MAIN_HAND, stack);
			}
		}
	}

	@SubscribeEvent
	public static void onCropGrow(BlockEvent.CropGrowEvent.Post event)
	{
		if(event.getWorld().provider.getDimension() == -1 && event.getState().getBlock() == Blocks.NETHER_WART)
		{
			int age =  event.getState().getValue(BlockNetherWart.AGE);
			if(age < 3 && event.getWorld().rand.nextBoolean())
			{
				event.getWorld().setBlockState(event.getPos(), event.getState().withProperty(BlockNetherWart.AGE, age + 1));
			}

		}
	}
}
