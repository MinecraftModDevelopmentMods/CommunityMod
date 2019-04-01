package com.mcmoddev.communitymod;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class CommunityConfig {

    private Configuration config;

    public CommunityConfig (File file) {

        config = new Configuration(file);
    }

    public void syncConfigData () {

        config.setCategoryComment("_submods", "Allows submods to be completely disabled");

        for (final SubModContainer container : CommunityMod.INSTANCE.getSubMods()) {
        	
            container.getSubMod().setupConfiguration(config, container.getId());
        }

        if (config.hasChanged()) {
            config.save();
        }
    }
    
    public boolean isSubModEnabled (SubModContainer container) {

        return config.getBoolean(container.getId(), "_submods", container.getSubMod().enabledByDefault(), container.getDescription() + " Author: " + container.getAttribution());
    }
}