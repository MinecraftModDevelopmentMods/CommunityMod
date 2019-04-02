package com.mcmoddev.communitymod.erdbeerbaerlp.BSOD_Item;

import java.util.Random;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.tightpants.ItemTightPants;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "BSOD Item", attribution = "ErdbeerbaerLP")
public class Main implements ISubMod{
	private static Item bsod = new ItemBSOD();
	/**
	 * Some random errors
	 */
	private static String[] ERRORS = new String[] {"MEMORY_MANAGEMENT", "INVALID_SOFTWARE_INTERRUPT", "NO_EXCEPTION_HANDLING_SUPPORT", "MISSINGNO", "DAB", "DATA_BUS_ERROR", "CACHE_MANAGER", "ERROR_LOADING_BSOD", "NO_SUCH_PARTITION", "NO_BOOT_DEVICE", "OBJECT_INITIALIZATION_FAILED", "SECURITY_INITIALIZATION_FAILED", "MEMORY1_INITIALIZATION_FAILED", "FILE_INITIALIZATION_FAILED"};
	final static Random r = new Random();
	@Override
	public void registerItems (IForgeRegistry<Item> reg) {

		bsod.setRegistryName(new ResourceLocation(CommunityGlobals.MOD_ID, "bsod"));
		reg.register(bsod);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels (ModelRegistryEvent event) {

		ModelLoader.setCustomModelResourceLocation(bsod, 0, new ModelResourceLocation(new ResourceLocation(CommunityGlobals.MOD_ID, "BSOD"), "inventory"));
	}
	
	public static String getRandomBSODError() {
		System.out.println(ERRORS.length);
		return ERRORS[r.nextInt(ERRORS.length-1)];
	}
}
