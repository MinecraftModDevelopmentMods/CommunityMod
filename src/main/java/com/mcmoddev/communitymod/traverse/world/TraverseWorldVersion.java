package com.mcmoddev.communitymod.traverse.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcmoddev.communitymod.shootingstar.version.Version;
import com.mcmoddev.communitymod.traverse.core.TraverseConstants;

import java.io.*;

public class TraverseWorldVersion {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public File versionConfig;
    public Version version;

    public TraverseWorldVersion(File worldDir) {
        File traverseDir = new File(worldDir, TraverseConstants.MOD_ID);
        if (!traverseDir.exists()) {
            traverseDir.mkdir();
        }
        versionConfig = new File(traverseDir, "instance_version.json");

        if (TraverseConstants.MOD_VERSION_MAJOR.equals("@major@")) {
            version = TraverseConstants.DEV_VERSION;
        } else {
            version = new Version(Integer.parseInt(TraverseConstants.MOD_VERSION_MAJOR), Integer.parseInt(TraverseConstants.MOD_VERSION_MINOR), Integer.parseInt(TraverseConstants.MOD_VERSION_PATCH));
        }

        reloadVersionFile();
    }

    public void reloadVersionFile() {
        if (!versionConfig.exists()) {
            writeVersionFile(new VersionConfig(version));
        }
        if (versionConfig.exists()) {
            VersionConfig config = null;
            try (Reader reader = new FileReader(versionConfig)) {
                config = GSON.fromJson(reader, VersionConfig.class);
                version = config.version;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (config == null) {
                config = new VersionConfig(version);
                writeVersionFile(config);
            }
        }
    }

    public void writeVersionFile(VersionConfig config) {
        try (Writer writer = new FileWriter(versionConfig)) {
            GSON.toJson(config, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reloadVersionFile();
    }

    public static class VersionConfig {

        public Version version;

        public VersionConfig(Version version) {
            this.version = version;
        }

        public Version getVersion() {
            return version;
        }

        public void setVersion(Version version) {
            this.version = version;
        }
    }
}
