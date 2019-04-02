package com.mcmoddev.communitymod.davidm;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;
import com.mcmoddev.communitymod.shared.RegUtil;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "ExtraRandomness", description = "Read: Bunch of random ideas", attribution = "DavidM")
public class DavidM implements ISubMod {

	public static SimpleNetworkWrapper network;
	
	public static Block blockAltar;
	public static ItemBlock itemBlockAltar;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(CommunityGlobals.MOD_ID);
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> event) {
		itemBlockAltar = RegUtil.registerItemBlock(event, new ItemBlock(blockAltar));
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> event) {
		blockAltar = RegUtil.registerBlock(event, new BlockAltar(), "meme_altar");
		
		GameRegistry.registerTileEntity(TileEntityAltar.class, "tile_meme_altar");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ClientUtil.simpleItemModel(itemBlockAltar);
	}
}
