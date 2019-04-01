package com.mcmoddev.communitymod.lemons.neatnether.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "community_mod")
public class NeatNetherGen
{
	@SubscribeEvent
	public static void onDecorate(DecorateBiomeEvent.Pre event)
	{
		if(event.getWorld().provider.getDimension() == -1)  //Only gen in nether
		{
			for(int i = 0; i < 4; i++)
			{
				if(event.getWorld().rand.nextInt(25) == 0)
				{
					WorldGenSmoulderingAsh ashGen = new WorldGenSmoulderingAsh(2 + event.getRand().nextInt(5));

					int x = event.getChunkPos().getXStart() + event.getRand().nextInt(16) + 8;
					int z = event.getChunkPos().getZStart() + event.getRand().nextInt(16) + 8;

					int y = event.getRand().nextInt((100 - 40) + 1) + 40;
					ashGen.generate(event.getWorld(), event.getRand(), new BlockPos(x, y, z));
				}
			}
		}
	}
}
