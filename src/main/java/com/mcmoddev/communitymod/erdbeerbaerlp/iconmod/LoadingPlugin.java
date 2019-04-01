package com.mcmoddev.communitymod.erdbeerbaerlp.iconmod;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(LoadingPlugin.NAME)
@IFMLLoadingPlugin.TransformerExclusions({"de.erdbeerbaerlp.iconmod"})
@IFMLLoadingPlugin.SortingIndex(1001)
//@Mod(modid = LoadingPlugin.MODID, name = LoadingPlugin.NAME, version = LoadingPlugin.VERSION, acceptableRemoteVersions = "*", serverSideOnly = true)
public class LoadingPlugin implements IFMLLoadingPlugin {

	public static final String MODID = "iconmod";
	public static final String NAME = "Icon Argument Re-Implementation";
	public static final String VERSION = "1.0.0";

	
	@Override
	public String[] getASMTransformerClass() {
		// TODO Auto-generated method stub
		return new String[]{"de.erdbeerbaerlp.iconmod.ctf"};
	}
	@Override
	public String getModContainerClass() {
		// TODO Auto-generated method stub
		return "de.erdbeerbaerlp.iconmod.mod";
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
