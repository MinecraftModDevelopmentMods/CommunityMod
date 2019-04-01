package com.mcmoddev.communitymod.space;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;

import net.minecraft.item.Item;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SubMod(name = "There is space now", attribution = "Snakefangox")
@EventBusSubscriber
public class Space implements ISubMod {

	public static DimensionType SPACE = DimensionType.register("space", "_space", 78634876, SpaceWorldProvider.class, true);
	static SpaceHelm spaceHelm;
	
	public static void registerDimensions() {
		DimensionManager.registerDimension(SPACE.getId(), SPACE);
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		registerDimensions();
	}
	
	@SubscribeEvent
	public static void travelToSpace(PlayerTickEvent e) {
		if(e.player.world.isRemote)return;
		if(e.player.dimension == 0 && e.player.posY > 256) {
			e.player.changeDimension(78634876, new DimTransfer((WorldServer) e.player.world, e.player.posX, 50, e.player.posZ));
		}else if(e.player.dimension == 78634876 && e.player.posY < 0) {
			e.player.changeDimension(0, new DimTransfer((WorldServer) e.player.world, e.player.posX, 255, e.player.posZ));
			e.player.setFire(30);
		}
	}
	
	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
		spaceHelm = new SpaceHelm();
		event.getRegistry().register(spaceHelm);
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent e) {
        ClientUtil.simpleItemModel(spaceHelm);
    }
}
