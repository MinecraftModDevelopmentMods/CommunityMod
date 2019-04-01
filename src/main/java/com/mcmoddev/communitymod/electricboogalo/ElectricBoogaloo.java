package com.mcmoddev.communitymod.electricboogalo;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SubMod(name = "Electric Boogaloo", attribution = "Lomeli", clientSideOnly = true,
        description = "Adds \": Electric Boogaloo\" to items with 2s at the end of their tooltips")
@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID)
public class ElectricBoogaloo implements ISubMod {

    private static String[] twosList;

    @Override
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        String tooLang = I18n.format("tooltip.community_mod.electric.two");
        twosList = tooLang.split("/");
    }

    @SubscribeEvent
    public static void itemToolTipEvent(ItemTooltipEvent event) {
        if (twosList == null || twosList.length < 1 || event.getToolTip().isEmpty())
            return;
        for (int i = 0; i < event.getToolTip().size(); i++) {
            String toolTip = event.getToolTip().get(i);
            String lowerTip = toolTip.toLowerCase();
            for (String to : twosList) {
                if (lowerTip.endsWith(to.toLowerCase())) {
                    event.getToolTip().set(i, I18n.format("tooltip.community_mod.electric", toolTip));
                    return;
                }
            }
        }
    }
}
