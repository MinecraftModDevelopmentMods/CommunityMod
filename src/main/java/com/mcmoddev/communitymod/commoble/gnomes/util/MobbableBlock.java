package com.mcmoddev.communitymod.commoble.gnomes.util;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class MobbableBlock
{
	private static final Set<Block> SOFT_BLOCKS = Sets.<Block>newIdentityHashSet();
	private static final Set<Block> MISCHIEF_BLOCKS = Sets.<Block>newIdentityHashSet();
	private static final Set<Block> UNSAFE_BLOCKS = Sets.<Block>newIdentityHashSet();
	private static final Set<Block> MINE_BLOCKS = Sets.<Block>newIdentityHashSet();
	
	public static boolean isSoftBlock(Block block, World world)
	{
		return SOFT_BLOCKS.contains(block);
	}
	
	public static boolean isMineableBlock(Block block, World world)
	{
		return MINE_BLOCKS.contains(block);
	}
	
	public static boolean isMischiefBlock(Block block, World world)
	{
		return MISCHIEF_BLOCKS.contains(block);
	}
	
	public static boolean isUnsafeBlock(Block block)
	{
		return UNSAFE_BLOCKS.contains(block);
	}
	
	static
    {
		SOFT_BLOCKS.add(Blocks.AIR);
        SOFT_BLOCKS.add(Blocks.GRASS);
        SOFT_BLOCKS.add(Blocks.DIRT);
        SOFT_BLOCKS.add(Blocks.SAND);
        SOFT_BLOCKS.add(Blocks.GRAVEL);
        SOFT_BLOCKS.add(Blocks.CLAY);
        SOFT_BLOCKS.add(Blocks.MYCELIUM);
        SOFT_BLOCKS.add(Blocks.SNOW);
        
        MISCHIEF_BLOCKS.add(Blocks.OAK_DOOR);
        MISCHIEF_BLOCKS.add(Blocks.DARK_OAK_DOOR);
        MISCHIEF_BLOCKS.add(Blocks.BIRCH_DOOR);
        MISCHIEF_BLOCKS.add(Blocks.SPRUCE_DOOR);
        MISCHIEF_BLOCKS.add(Blocks.JUNGLE_DOOR);
        MISCHIEF_BLOCKS.add(Blocks.ACACIA_DOOR);
        MISCHIEF_BLOCKS.add(Blocks.LEVER);
        MISCHIEF_BLOCKS.add(Blocks.STONE_BUTTON);
        MISCHIEF_BLOCKS.add(Blocks.WOODEN_BUTTON);
        
        UNSAFE_BLOCKS.add(Blocks.FLOWING_WATER);
        UNSAFE_BLOCKS.add(Blocks.WATER);
        UNSAFE_BLOCKS.add(Blocks.FLOWING_LAVA);
        UNSAFE_BLOCKS.add(Blocks.LAVA);
        UNSAFE_BLOCKS.add(Blocks.FIRE);
        UNSAFE_BLOCKS.add(Blocks.MAGMA);
        UNSAFE_BLOCKS.add(Blocks.CACTUS);
        
        MINE_BLOCKS.add(Blocks.STONE);
        MINE_BLOCKS.add(Blocks.GOLD_ORE);
        MINE_BLOCKS.add(Blocks.IRON_ORE);
        MINE_BLOCKS.add(Blocks.COAL_ORE);
        MINE_BLOCKS.add(Blocks.LAPIS_ORE);
        MINE_BLOCKS.add(Blocks.DIAMOND_ORE);
        MINE_BLOCKS.add(Blocks.STONE);
        MINE_BLOCKS.add(Blocks.STONE);
    	
    }
}
