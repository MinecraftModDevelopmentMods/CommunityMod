package com.mcmoddev.communitymod;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class CommunityConfig {

    private Configuration config;

    public CommunityConfig (File file) {

        config = new Configuration(file);
    }

    public void syncConfigData () {

        config.setCategoryComment("_submods", "Allows submods to be completely disabled");

        for (final SubModContainer container : SubModLoader.getLoadedSubMods()) {

            config.setCategoryRequiresMcRestart(container.getId(), container.requiresMcRestart());
            container.getSubMod().setupConfiguration(config, container.getId());
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public Property getSubModEnabled(SubModContainer container)
    {
        return config.get(container.getId(), "_submods", container.getSubMod().enabledByDefault(), container.getDescription() + " Author: " + container.getAttribution());
    }
    
    public boolean isSubModEnabled(SubModContainer container)
    {
        return getSubModEnabled(container).getBoolean();
    }

    public Configuration getConfig()
    {
        return config;
    }
}
