package com.mcmoddev.communitymod;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name("Community Mod")
@IFMLLoadingPlugin.TransformerExclusions({"com.mcmoddev.communitymod.erdbeerbaerlp.iconmod"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class CommunityCoremodLoadingPlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		// TODO Auto-generated method stub
		return new String[]{"com.mcmoddev.communitymod.erdbeerbaerlp.iconmod.IconClassTransformer"}; //Add your class transformers here
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

	@Override
	public String getModContainerClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
