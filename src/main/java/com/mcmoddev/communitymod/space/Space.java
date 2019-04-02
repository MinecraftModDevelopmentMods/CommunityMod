package com.mcmoddev.communitymod.space;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "There is space now", attribution = "Snakefangox")
public class Space implements ISubMod {

	public static final int SPACE_DIM_ID = 78634876; // You're welcome, Snake :P /Fisk

	public static DimensionType SPACE = DimensionType.register("space", "_space", SPACE_DIM_ID, SpaceWorldProvider.class, true);
	static SpaceHelm spaceHelm;
	static int damageTick = 20;

	public static final ItemArmor.ArmorMaterial SPACEHELM = EnumHelper.addArmorMaterial(CommunityGlobals.MOD_ID + "_spacehelm", new ResourceLocation(CommunityGlobals.MOD_ID, "spacesuit").toString(), 100, new int[]{2, 2, 2, 2}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 5);
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
			e.player.changeDimension(SPACE_DIM_ID, new DimTransfer((WorldServer) e.player.world, e.player.posX, 50, e.player.posZ));
		}else if(e.player.dimension == SPACE_DIM_ID && e.player.posY < 0) {
			e.player.changeDimension(0, new DimTransfer((WorldServer) e.player.world, e.player.posX, 255, e.player.posZ));
			e.player.setFire(30);
		}

		if(damageTick == 0 && e.player.dimension == 78634876) {
			damageTick = 20;
			if(e.player.inventory.armorItemInSlot(3).getItem() != spaceHelm)
			e.player.attackEntityFrom(DamageSource.GENERIC, 2f);
		}else if(e.player.dimension == 78634876){
			if(e.player.world.playerEntities.indexOf(e.player) == 0)
			--damageTick;
		}
	}

	@Override
	public void registerItems(IForgeRegistry<Item> reg) {
		spaceHelm = new SpaceHelm();
		reg.register(spaceHelm);
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent e) {
        ClientUtil.simpleItemModel(spaceHelm);
    }
}
