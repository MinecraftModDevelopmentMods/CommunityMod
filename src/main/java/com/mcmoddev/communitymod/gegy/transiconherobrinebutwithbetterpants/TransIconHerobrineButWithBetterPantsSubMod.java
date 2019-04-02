package com.mcmoddev.communitymod.gegy.transiconherobrinebutwithbetterpants;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(
        name = "transiconherobrinebutwithbetterpants",
        description = "Trans Icon Herobrine (but with better pants)",
        attribution = "anon1449, gegy1000"
)
public class TransIconHerobrineButWithBetterPantsSubMod implements ISubMod {
    @Override
    public void registerModels(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(TransIconHerobrineButWithBetterPantsEntity.class, TransIconHerobrineButWithBetterPantsRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(NotchButWithWorsererPantsEntity.class, NotchButWithWorsererPantsRenderer::new);
    }

    @Override
    public void registerEntities(IForgeRegistry<EntityEntry> reg) {
        reg.register(EntityEntryBuilder.create()
                .entity(TransIconHerobrineButWithBetterPantsEntity.class)
                .factory(TransIconHerobrineButWithBetterPantsEntity::new)
                .name(CommunityGlobals.MOD_ID + ".trans_icon_herobrine_but_with_better_pants")
                .id(new ResourceLocation(CommunityGlobals.MOD_ID, "trans_icon_herobrine_but_with_better_pants"), CommunityGlobals.entity_id++)
                .tracker(128, 3, true)
                .spawn(EnumCreatureType.CREATURE, 3, 0, 1, Biome.REGISTRY)
                .egg(0xAA7D66, 0x32394D)
                .build()
        );

        reg.register(EntityEntryBuilder.create()
                .entity(NotchButWithWorsererPantsEntity.class)
                .factory(NotchButWithWorsererPantsEntity::new)
                .name(CommunityGlobals.MOD_ID + ".notch_but_with_worserer_pants")
                .id(new ResourceLocation(CommunityGlobals.MOD_ID, "notch_but_with_worserer_pants"), CommunityGlobals.entity_id++)
                .tracker(80, 3, true)
                .spawn(EnumCreatureType.CREATURE, 3, 2, 3, Biome.REGISTRY)
                .egg(0xAAAAAA, 0xAAAAAA)
                .build()
        );
    }
}
