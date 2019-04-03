package com.mcmoddev.communitymod.commoble.intradimensional_portals;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(modid = "intradimensionalportals", name = "IntradimensionalPortals", description = "holes in the sky", attribution = "Commoble")
public class SubmodIntradimensionalPortals implements ISubMod
{
	@SidedProxy(clientSide="com.mcmoddev.communitymod.commoble.intradimensional_portals.client.IntraportalClientProxy",
				serverSide="com.mcmoddev.communitymod.commoble.intradimensional_portals.IntraportalServerProxy")
	public static IIntraportalProxy proxy;
	
	public static WorldGenManagerForIntraportals worldgen = new WorldGenManagerForIntraportals();
	
	@ObjectHolder(CommunityGlobals.MOD_ID + ":" + "intradimensional_portal_base")
	public static final Block intradimensional_portal_base = null;
	
	@ObjectHolder(CommunityGlobals.MOD_ID + ":" + "intradimensional_portal_glowy_air")
	public static final Block intradimensional_portal_glowy_air = null;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent event)
	{
		GameRegistry.registerTileEntity(TileEntityPortalBase.class, new ResourceLocation(CommunityGlobals.MOD_ID, "te_intradimensional_portal_base"));
		GameRegistry.registerWorldGenerator(worldgen, 0);
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> reg)
	{
		registerBlock(reg, new BlockPortalBase(), "intradimensional_portal_base");
		registerBlock(reg, new BlockPortalGlowyAir(), "intradimensional_portal_glowy_air");
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
}
