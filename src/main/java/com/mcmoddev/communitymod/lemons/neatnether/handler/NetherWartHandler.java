package com.mcmoddev.communitymod.lemons.neatnether.handler;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID)
public class NetherWartHandler
{
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
