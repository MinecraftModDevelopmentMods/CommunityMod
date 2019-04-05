package com.mcmoddev.communitymod.davidm.extrarandomness.common;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.davidm.extrarandomness.client.render.RenderAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.block.BlockAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.block.BlockMemeCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.GoldenEgg;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.LexWand;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.MemeWrench;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.Shocker;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketAltarAnimation;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketRequestUpdateTileEntity;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketUpdateTileEntity;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.IProxy;
import com.mcmoddev.communitymod.shared.ClientUtil;
import com.mcmoddev.communitymod.shared.RegUtil;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
	
	@SidedProxy(
			clientSide = "com.mcmoddev.communitymod.davidm.extrarandomness.client.ClientProxy",
			serverSide = "com.mcmoddev.communitymod.davidm.extrarandomness.common.ServerProxy"
	)
	public static IProxy proxy;
	
	public static Block blockAltar;
	public static ItemBlock itemBlockAltar;
	
	public static List<Block> blockCapacitors;
	
	public static List<Item> altarItems;
	public static List<Item> miscItems;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(CommunityGlobals.MOD_ID);
		network.registerMessage(new PacketUpdateTileEntity.Handler(), PacketUpdateTileEntity.class, 0, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdateTileEntity.Handler(), PacketRequestUpdateTileEntity.class, 1, Side.SERVER);
		network.registerMessage(new PacketAltarAnimation.Handler(), PacketAltarAnimation.class, 2, Side.CLIENT);
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> event) {
		altarItems = new ArrayList<Item>();
		miscItems = new ArrayList<Item>();
		
		itemBlockAltar = RegUtil.registerItemBlock(event, new ItemBlock(blockAltar));
		
		blockCapacitors.forEach(capacitor -> {
			RegUtil.registerItemBlock(event, new ItemBlock(capacitor));
		});
		
		altarItems.add(RegUtil.registerItem(event, new LexWand(), "lex_wand"));
		altarItems.add(RegUtil.registerItem(event, new GoldenEgg(), "golden_egg"));
		altarItems.add(RegUtil.registerItem(event, new Shocker(), "shocker"));
		
		miscItems.add(RegUtil.registerItem(event, new MemeWrench(), "meme_wrench"));
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> event) {
		blockCapacitors = new ArrayList<Block>();
		
		blockAltar = RegUtil.registerBlock(event, new BlockAltar(), "meme_altar");
		for (EnumCapacitor enumCapacitor: EnumCapacitor.values()) {
			String stringTier = enumCapacitor.getStringTier();
			Block blockCapacitor = new BlockMemeCapacitor(enumCapacitor);
			blockCapacitors.add(blockCapacitor);
			RegUtil.registerBlock(event, blockCapacitor, "meme_capacitor_tier_" + stringTier);
		}
		
		GameRegistry.registerTileEntity(TileEntityAltar.class, new ResourceLocation(CommunityGlobals.MOD_ID, "tile_meme_altar"));
		GameRegistry.registerTileEntity(TileEntityCapacitor.class, new ResourceLocation(CommunityGlobals.MOD_ID, "tile_meme_capacitor"));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ClientUtil.simpleItemModel(itemBlockAltar);
		
		blockCapacitors.forEach(capacitor -> {
			ClientUtil.simpleItemModel(Item.getItemFromBlock(capacitor));
		});
		
		altarItems.forEach(ClientUtil::simpleItemModel);
		miscItems.forEach(ClientUtil::simpleItemModel);
		
		// I am lazy.
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new RenderAltar());
	}
	
	@Override
	public void onInit(FMLInitializationEvent event) {
		proxy.init();
	}
}
