package com.mcmoddev.communitymod.electricboogalo;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import joptsimple.internal.Strings;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemPotion;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.regex.Pattern;

@SubMod(name = "Electric Boogaloo", attribution = "Lomeli", clientSideOnly = true,
        description = "Adds \": Electric Boogaloo\" to items with 2s at the end of their tooltips")
@Mod.EventBusSubscriber(modid = CommunityGlobals.MOD_ID, value = {Side.CLIENT})
public class ElectricBoogaloo implements ISubMod {
    private static String[] twosList;
    private static final String POTION_TIME_REGEX = "\\(\\d{1,2}:\\d{1,2}\\)";
    private static final Pattern TIMER_PATTERN = Pattern.compile(POTION_TIME_REGEX);

    @Override
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        String tooLang = I18n.format("tooltip.community_mod.electric.two").toLowerCase();
        twosList = tooLang.split("/");
    }

    @SubscribeEvent
    public static void itemToolTipEvent(ItemTooltipEvent event) {
        if (twosList == null || twosList.length < 1 || event.getToolTip().isEmpty())
            return;
        boolean isPotion = event.getItemStack().getItem() instanceof ItemPotion;

        for (int i = 0; i < event.getToolTip().size(); i++) {
            String toolTip = event.getToolTip().get(i);
            String lowerTip = toolTip.toLowerCase();
            boolean relocateReset = false;
            if (lowerTip.endsWith("§r")) {
                lowerTip = lowerTip.substring(0, lowerTip.length() - 2);
                toolTip = toolTip.substring(0, toolTip.length() - 2);
                relocateReset = true;
            }
            for (String to : twosList) {
                String boogaloo = null;
                if (isPotion && TIMER_PATTERN.matcher(lowerTip).find()) {
                    String potionName = lowerTip.substring(0, lowerTip.indexOf('(') - 1);
                    if (potionName.endsWith(to)) {
                        int index = toolTip.indexOf('(') - 1;
                        String beforeTimer = toolTip.substring(0, index);
                        String timer = toolTip.substring(index);
                        boogaloo = I18n.format("tooltip.community_mod.electric", beforeTimer) + timer;
                    }
                }
                if (lowerTip.endsWith(to)) {
                    boogaloo = I18n.format("tooltip.community_mod.electric", toolTip);
                    if (relocateReset)
                        boogaloo += "§r";
                }
                if (!Strings.isNullOrEmpty(boogaloo)) {
                    event.getToolTip().set(i, boogaloo);
                    return;
                }
            }
        }
    }
}
