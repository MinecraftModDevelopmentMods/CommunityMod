package com.mcmoddev.communitymod.traverse.client;

import com.google.common.collect.ImmutableSet;
import com.mcmoddev.communitymod.traverse.config.TraverseConfig;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraftforge.fml.common.Loader;
import com.mcmoddev.communitymod.traverse.core.TraverseMod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Traverse2Textures extends AbstractResourcePack {
    public Traverse2Textures() {
        super(Loader.instance().activeModContainer().getSource());
        overrides.put("pack.mcmeta", "/proxypack.mcmeta");
    }
    private static final String BARE_FORMAT = "assets/%s/%s/%s/%s.%s";
    private static final String OVERRIDE_FORMAT = "/assets/%s/%s/%s/overrides/%s.%s";

    private static final Set<String> RESOURCE_DOMAINS = ImmutableSet.of("traverse");
    private static final Map<String, String> overrides = new HashMap<>();

    public void addTextureOverride(String dir, String file) {
        addResourceOverride("textures",dir,file,"png");
    }

    public void addResourceOverride(String space, String dir, String file, String ext) {
        String bare = String.format(BARE_FORMAT, "traverse", space, dir, file, ext);
        String override = String.format(OVERRIDE_FORMAT, "traverse", space, dir, file, ext);
        overrides.put(bare, override);
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        return TraverseMod.class.getResourceAsStream(overrides.get(name));
    }

    @Override
    protected boolean hasResourceName(String name) {
        return name.equals("pack.mcmeta") || TraverseConfig.enableNewTextures && overrides.containsKey(name);
    }

    @Override
    public Set<String> getResourceDomains() {
        return RESOURCE_DOMAINS;
    }
}
