package com.mcmoddev.communitymod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSubModList extends GuiListExtended
{
    private final Minecraft mc;
    private final List<SubModListEntry> subModEntries;

    private final String header;

    public GuiSubModList(Minecraft mcIn, int width, int height, List<SubModListEntry> list, String headerIn)
    {
        super(mcIn, width, height, 32, height - 55 + 4, 28);
        mc = mcIn;
        header = headerIn;
        subModEntries = list;
        centerListVertically = false;
        setHasListHeader(true, (int) (mcIn.fontRenderer.FONT_HEIGHT * 1.5F));
    }

    @Override
    protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellator)
    {
        String s = TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + header;
        mc.fontRenderer.drawString(s, insideLeft + width / 2 - mc.fontRenderer.getStringWidth(s) / 2, Math.min(top + 3, insideTop), 16777215);
    }

    @Override
    protected int getSize()
    {
        return subModEntries.size();
    }

    @Override
    @Nonnull
    public SubModListEntry getListEntry(int index)
    {
        return subModEntries.get(index);
    }

    @Override
    public int getListWidth()
    {
        return width;
    }

    @Override
    protected int getScrollBarX()
    {
        return right - 6;
    }
}
