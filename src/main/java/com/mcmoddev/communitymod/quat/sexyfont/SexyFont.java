package com.mcmoddev.communitymod.quat.sexyfont;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;
import com.mcmoddev.communitymod.shared.RegUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(
	name = "sexyfont",
	description = "Makes the Minecraft font very sexy!",
	attribution = "quaternary"
)
public class SexyFont implements ISubMod {
	public static boolean sexyTime = false;
	
	public static boolean alwaysSexyTime = false;
	public static boolean intermittentSexyTime = false;
	
	@Override
	public void setupConfiguration(Configuration config, String categoryId) {
		alwaysSexyTime = config.getBoolean("alwaysSexyFont", categoryId, false, "Should sexyfont always be enabled even if you don't wear the glasses?");
		
		intermittentSexyTime = config.getBoolean("intermittentSexyFont", categoryId, false, "Should the sexy font be pretty flickery?");
	}
	
	public static final class Names {
		public static final String SEXY_GLASSES_NAME = "sexy_glasses";
	}
	
	@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
	public static final class Items {
		@GameRegistry.ObjectHolder(Names.SEXY_GLASSES_NAME)
		public static final ItemSexyGlasses SEXY_GLASSES = null;
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> reg) {
		RegUtil.registerItem(reg, new ItemSexyGlasses(), Names.SEXY_GLASSES_NAME);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ClientUtil.simpleItemModel(Items.SEXY_GLASSES);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("deprecation") //how to mod 101
	public void onInit(FMLInitializationEvent event) {
		IReloadableResourceManager mgr = ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager());
		
		mgr.registerReloadListener((x) -> {
			Minecraft mc = Minecraft.getMinecraft();
			
			mc.fontRenderer = new SexyFontRenderer(
				mc.gameSettings,
				new ResourceLocation("textures/font/ascii.png"),
				mc.renderEngine,
				mc.isUnicode()
			);
		});
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void frame(TickEvent.RenderTickEvent e) {
		if(e.phase == TickEvent.Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.player == null) {
				SexyFont.sexyTime = false;
			} else {
				Item head = mc.player.inventory.armorItemInSlot(EntityEquipmentSlot.HEAD.getIndex()).getItem();
				SexyFont.sexyTime = head == SexyFont.Items.SEXY_GLASSES;
			}
		}
	}
}
