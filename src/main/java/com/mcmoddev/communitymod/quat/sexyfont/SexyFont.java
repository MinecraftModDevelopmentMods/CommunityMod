package com.mcmoddev.communitymod.quat.sexyfont;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SubMod(
	name = "sexyfont",
	description = "Makes the Minecraft font very sexy",
	attribution = "quaternary",
	clientSideOnly = true
)
public class SexyFont implements ISubMod {
	@Override
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
}
