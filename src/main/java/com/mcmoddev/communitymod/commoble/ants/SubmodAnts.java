package com.mcmoddev.communitymod.commoble.ants;

import java.util.HashSet;
import java.util.Set;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "Ants", description = "This is how you get ants", attribution = "Commoble")
public class SubmodAnts implements ISubMod
{
	public static Set<Block> blocks_with_ants = new HashSet<Block>();
	
	@SidedProxy(clientSide="com.mcmoddev.communitymod.commoble.ants.client.AntsClientProxy",
				serverSide="com.mcmoddev.communitymod.commoble.ants.AntsServerProxy")
	public static IAntsProxy proxy;
	
	@ObjectHolder(CommunityGlobals.MOD_ID + ":" + "ant_covered_cake")
	public static final Block ant_covered_cake = null;
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> reg)
	{
		registerBlock(reg, new BlockAntCoveredCake(), "ant_covered_cake");
	}
	
	@SubscribeEvent
	public static void onBlockPlaced(EntityPlaceEvent event)
	{
		Block placedBlock = event.getPlacedBlock().getBlock();
		if (placedBlock == Blocks.CAKE)
		{
			Block downBlock = event.getWorld().getBlockState(event.getPos().down()).getBlock();
			if (blocks_with_ants.contains(downBlock))
			{
				event.getWorld().setBlockState(event.getPos(), ant_covered_cake.getDefaultState());
			}
		}
	}
	
	private static Block registerBlock(IForgeRegistry<Block> registry, Block newBlock, String name)
	{
		name = appendPrefix(name);
		newBlock.setTranslationKey(name);
		newBlock.setRegistryName(name);
		registry.register(newBlock);
		return newBlock;
	}
	
	private static String appendPrefix(String unPrefixedString)
	{
		return CommunityGlobals.MOD_ID + ":" + unPrefixedString;
	}
	
	static
	{
		blocks_with_ants.add(Blocks.DIRT);
		blocks_with_ants.add(Blocks.SAND);
		blocks_with_ants.add(Blocks.GRASS);
		blocks_with_ants.add(Blocks.GRASS_PATH);
		blocks_with_ants.add(Blocks.GRAVEL);
		blocks_with_ants.add(Blocks.STONE);
		blocks_with_ants.add(Blocks.MYCELIUM);
	}
}
