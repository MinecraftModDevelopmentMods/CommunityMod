package com.mcmoddev.communitymod.client.gui;

import com.mcmoddev.communitymod.CommunityMod;
import com.mcmoddev.communitymod.SubModContainer;
import com.mcmoddev.communitymod.SubModLoader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class GuiCommunityConfig extends GuiScreen
{
    private final GuiScreen parentScreen;
    
    private List<SubModListEntry> availableSubMods;
    private List<SubModListEntry> selectedSubMods;
    
    private GuiSubModList availableSubModsList;
    private GuiSubModList selectedSubModsList;

    public Set<SubModListEntry> requiresRestart = new HashSet<>();
    private boolean changed;

    public GuiCommunityConfig(GuiScreen parentScreenIn)
    {
        parentScreen = parentScreenIn;
    }

    @Override
    public void initGui()
    {
//        buttonList.add(new GuiOptionButton(2, width / 2 - 154, height - 48, I18n.format("resourcePack.openFolder")));
        buttonList.add(new GuiOptionButton(1, width / 2 + 4, height - 48, I18n.format("gui.done")));

        if (!changed)
        {
            availableSubMods = new ArrayList<>();
            selectedSubMods = new ArrayList<>();

            Collection<SubModContainer> mods = SubModLoader.getSubMods();
            mods = mods.stream().sorted(Comparator.comparing(SubModContainer::getName)).collect(Collectors.toList());

            for (SubModContainer container : mods)
            {
                boolean flag = CommunityMod.getConfig().isSubModEnabled(container);
                SubModListEntry entry = new SubModListEntry(this, container);

                if (flag != SubModLoader.isSubModLoaded(container.getSubMod()))
                {
                    requiresRestart.add(entry);
                }

                if (flag)
                {
                    selectedSubMods.add(entry);
                }
                else
                {
                    availableSubMods.add(entry);
                }
            }
        }

        availableSubModsList = new GuiSubModList(mc, 200, height, availableSubMods, "Available Submods");
        availableSubModsList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
        availableSubModsList.registerScrollButtons(7, 8);
        selectedSubModsList = new GuiSubModList(mc, 200, height, selectedSubMods, "Selected Submods");
        selectedSubModsList.setSlotXBoundsFromLeft(width / 2 + 4);
        selectedSubModsList.registerScrollButtons(7, 8);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        selectedSubModsList.handleMouseInput();
        availableSubModsList.handleMouseInput();
    }

    public boolean hasSubModEntry(SubModListEntry entry)
    {
        return selectedSubMods.contains(entry);
    }

    public List<SubModListEntry> getListContaining(SubModListEntry entry)
    {
        return hasSubModEntry(entry) ? selectedSubMods : availableSubMods;
    }

    public List<SubModListEntry> getAvailableSubMods()
    {
        return availableSubMods;
    }

    public List<SubModListEntry> getSelectedSubMods()
    {
        return selectedSubMods;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                if (changed)
                {
                    for (SubModListEntry entry : selectedSubMods)
                    {
                        SubModLoader.load(entry.subModEntry.getSubMod());
                    }

                    for (SubModListEntry entry : availableSubMods)
                    {
                        SubModLoader.unload(entry.subModEntry.getSubMod());
                    }

                    if (CommunityMod.getConfig().getConfig().hasChanged())
                    {
                        CommunityMod.getConfig().getConfig().save();
                    }

//                    if (requiresMcRestart)
//                    {
//                        flag = false;
//                        mc.displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle",
//                                new TextComponentString(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
//                    }
                }

                mc.displayGuiScreen(parentScreen);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        availableSubModsList.mouseClicked(mouseX, mouseY, mouseButton);
        selectedSubModsList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawBackground(0);
        availableSubModsList.drawScreen(mouseX, mouseY, partialTicks);
        selectedSubModsList.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(fontRenderer, "Select Submods", width / 2, 16, 16777215);

        if (!requiresRestart.isEmpty())
        {
            String s = TextFormatting.RED + "Restart needed *";
            drawString(fontRenderer, s, width / 2 - 14 - fontRenderer.getStringWidth(s), height - 44, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void move(SubModListEntry entry, boolean left)
    {
        changed = true;
        getListContaining(entry).remove(entry);

        if (left)
        {
            if (entry.subModEntry.requiresMcRestart() && SubModLoader.isSubModLoaded(entry.subModEntry.getSubMod()))
            {
                requiresRestart.add(entry);
            }
            else
            {
                requiresRestart.remove(entry);
            }

            getAvailableSubMods().add(0, entry);
        }
        else
        {
            if (entry.subModEntry.requiresMcRestart() && !SubModLoader.isSubModLoaded(entry.subModEntry.getSubMod()))
            {
                requiresRestart.add(entry);
            }
            else
            {
                requiresRestart.remove(entry);
            }

            getSelectedSubMods().add(0, entry);
        }
    }
}
