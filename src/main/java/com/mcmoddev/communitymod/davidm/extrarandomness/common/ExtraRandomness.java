package com.mcmoddev.communitymod.davidm.extrarandomness.common;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.davidm.extrarandomness.client.render.RenderAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.block.BlockAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.GoldenEgg;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.LexWand;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketRequestUpdateAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketUpdateAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketUpdateAltar.Handler;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.AltarItem;
import com.mcmoddev.communitymod.shared.ClientUtil;
import com.mcmoddev.communitymod.shared.RegUtil;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "ExtraRandomness", description = "Read: Bunch of random ideas", attribution = "DavidM")
public class ExtraRandomness implements ISubMod {

	public static SimpleNetworkWrapper network;
	
	public static Block blockAltar;
	public static ItemBlock itemBlockAltar;
	
	public static List<AltarItem> altarItems;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(CommunityGlobals.MOD_ID);
		network.registerMessage(new PacketUpdateAltar.Handler(), PacketUpdateAltar.class, 0, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdateAltar.Handler(), PacketRequestUpdateAltar.class, 1, Side.SERVER);
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> event) {
		altarItems = new ArrayList<AltarItem>();
		
		itemBlockAltar = RegUtil.registerItemBlock(event, new ItemBlock(blockAltar));
		
		altarItems.add(RegUtil.<AltarItem>registerItem(event, new LexWand(), "lex_wand"));
		altarItems.add(RegUtil.<AltarItem>registerItem(event, new GoldenEgg(), "golden_egg"));
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
		
		altarItems.forEach(ClientUtil::simpleItemModel);
		
		// I am lazy.
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new RenderAltar());
	}
}
