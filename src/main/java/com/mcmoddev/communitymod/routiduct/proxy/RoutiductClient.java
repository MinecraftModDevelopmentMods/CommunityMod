package com.mcmoddev.communitymod.routiduct.proxy;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.mcmoddev.communitymod.routiduct.RoutiductConstants;
import com.mcmoddev.communitymod.routiduct.gui.GuiAssembler;
import com.mcmoddev.communitymod.routiduct.gui.GuiAssemblerS;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;

/**
 * Created by Prospector
 */
@SideOnly(Side.CLIENT)
public class RoutiductClient extends RoutiductServer {

	public void preInit() {
		ShootingStar.registerModels(RoutiductConstants.MOD_ID);
	}

	public void init() {

	}

	public void postInit() {

	}

	public GuiAssemblerS getGuiAssembler() {
		return new GuiAssembler();
	}
}
