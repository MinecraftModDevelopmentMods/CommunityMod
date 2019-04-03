package com.mcmoddev.communitymod.lemons.neatnether;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Random;

@SubMod(modid = "neatnether", name = "Neat Nether", description = "Adds some neat stuff to the Nether", attribution = "Lemons")
public class NeatNether implements ISubMod
{
	@Override
	public void onPostInit (FMLPostInitializationEvent event)
	{
		FurnaceRecipes.instance().addSmeltingRecipeForBlock(Blocks.SOUL_SAND, new ItemStack(NeatNetherBlocks.SOUL_GLASS), 0.2F);
	}

	@Override
	public void registerItems(IForgeRegistry<Item> reg)
	{
		NeatNetherBlocks.registerItems(reg);
	}

	@Override
	public void registerBlocks(IForgeRegistry<Block> reg)
	{
		NeatNetherBlocks.registerBlocks(reg);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event)
	{
		NeatNetherBlocks.registerModels(event);
	}

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
					EnchantmentHelper.addRandomEnchantment(rand, stack, (int) (5.0F + rand.nextInt(18)), false);
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
