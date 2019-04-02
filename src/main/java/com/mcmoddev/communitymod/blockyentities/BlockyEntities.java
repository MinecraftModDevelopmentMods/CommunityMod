package com.mcmoddev.communitymod.blockyentities;

import java.util.List;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.CommunityMod;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "blocky entities", attribution = "Snakefangox")
@EventBusSubscriber
public class BlockyEntities implements ISubMod {

	public static int MaxRocketSize = 10000;
	ShipCore shipCore;
	
	@Override
	public void registerModels(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(BaseVehicleEntity.class, new IRenderFactory<BaseVehicleEntity>(){public Render<BaseVehicleEntity> createRenderFor(RenderManager manager) {return new BaseVehicleRender(manager);}}); 
		ClientUtil.simpleItemModel(Item.getItemFromBlock(shipCore));
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		PacketHandler.registerMessages("hyperstellar");
		EntityRegistry.registerModEntity(new ResourceLocation(CommunityGlobals.MOD_ID, "BaseVehicle"), BaseVehicleEntity.class, "BaseVehicle", 45, CommunityMod.INSTANCE, 64, 3, true);
		if (!ForgeChunkManager.getConfig().hasCategory(CommunityGlobals.MOD_ID)) {
			ForgeChunkManager.getConfig().get(CommunityGlobals.MOD_ID, "maximumChunksPerTicket", MaxRocketSize / 16)
					.setMinValue(0);
			ForgeChunkManager.getConfig().get(CommunityGlobals.MOD_ID, "maximumTicketCount", 10000).setMinValue(0);
			ForgeChunkManager.getConfig().save();
		}
		HDataSerializers.register();
	}

	@Override
	public void onInit(FMLInitializationEvent event) {
		StorageDimReg.regStorageDim();
		ForgeChunkManager.setForcedChunkLoadingCallback(CommunityMod.INSTANCE, new ChunkCallback());
	}
	
	public class ChunkCallback implements LoadingCallback {

		@Override
		public void ticketsLoaded(List<Ticket> tickets, World world) {
			
		}
		
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> reg) {
		shipCore = new ShipCore();
		reg.register(shipCore);
		GameRegistry.registerTileEntity(ShipCoreTE.class, new ResourceLocation(CommunityGlobals.MOD_ID, shipCore.getRegistryName().getPath() + "block"));
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> reg) {
		reg.register(new ItemBlock(shipCore).setRegistryName(shipCore.getRegistryName()));
	}

	@SubscribeEvent
	public static void onCollision(GetCollisionBoxesEvent e) {
		if (e.getEntity() != null) {
			for (BaseVehicleEntity bsv : e.getWorld().getEntitiesWithinAABB(BaseVehicleEntity.class,
					e.getAabb().grow(16))) {
				if (bsv != e.getEntity()) {
					for (BlockPos bp : bsv.getBlocks().keySet()) {
						if (bp != null) {
							bsv.getBlocks().get(bp).blockstate.addCollisionBoxToList(bsv.getStorage(),
									bsv.localBlockPosToGlobal(bp), e.getAabb(), e.getCollisionBoxesList(),
									e.getEntity(), false);
						}
					}
				}
			}
		}
	}
}
