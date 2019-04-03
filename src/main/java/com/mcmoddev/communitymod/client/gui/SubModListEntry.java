package com.mcmoddev.communitymod.client.gui;

import com.mcmoddev.communitymod.SubModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class SubModListEntry implements GuiListExtended.IGuiListEntry
{
    private static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");

    private final Minecraft mc = Minecraft.getMinecraft();
    private final GuiCommunityConfig parentScreen;

    public final SubModContainer subModEntry;

    public SubModListEntry(GuiCommunityConfig parent, SubModContainer container)
    {
        parentScreen = parent;
        subModEntry = container;
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
    {
        if (parentScreen.requiresRestart.contains(this))
        {
            GlStateManager.color(1, 1, 1, 1);
            Gui.drawRect(x - 1, y - 1, x + listWidth - 9, y + slotHeight + 1, -8978432);
            mc.fontRenderer.drawStringWithShadow(TextFormatting.RED + "*", x + listWidth - 13, y - 2, -1);
        }

        // TODO: Custom icons
        // bindResourcePackIcon();
        // GlStateManager.color(1, 1, 1, 1);
        // Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 24, 24, 24, 24);

        if (showHoverOverlay() && (mc.gameSettings.touchscreen || isSelected))
        {
            mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
            Gui.drawRect(x, y, x + 24, y + 24, -1601138544);
            GlStateManager.color(1, 1, 1, 1);

            String s = "";

            if (canMoveRight())
            {
                s = "+";

                if (mouseX - x < 24)
                {
                    s = TextFormatting.GREEN + s;
                }
            }
            else if (canMoveLeft())
            {
                s = "-";

                if (mouseX - x < 24)
                {
                    s = TextFormatting.RED + s;
                }
            }

            float scale = 2;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            mc.fontRenderer.drawStringWithShadow(s, (x + 12) / scale - mc.fontRenderer.getStringWidth(s) / 2F, (y + 12) / scale - mc.fontRenderer.FONT_HEIGHT / 2F, 8421504);
            GlStateManager.popMatrix();
        }

//        if (isSelected)
//        {
//            List<String> list = new ArrayList<>();
//            list.add(subModEntry.getName());
//            list.addAll(mc.fontRenderer.listFormattedStringToWidth(subModEntry.getDescription(), 157));
//            parentScreen.drawHoveringText(list, mouseX, mouseY);
//        }

        mc.fontRenderer.drawStringWithShadow(trimStringToWidth(subModEntry.getName(), 157), x + 24 + 2, y + 1, 16777215);
        mc.fontRenderer.drawStringWithShadow(trimStringToWidth(subModEntry.getAttribution(), 157), x + 24 + 2, y + 1 + 10, 8421504);
    }

    private String trimStringToWidth(String s, int maxWidth)
    {
        int w = mc.fontRenderer.getStringWidth(s);

        if (w > maxWidth)
        {
            s = mc.fontRenderer.trimStringToWidth(s, maxWidth - mc.fontRenderer.getStringWidth("...")) + "...";
        }

        return s;
    }

    private boolean showHoverOverlay()
    {
        return true;
    }

    private boolean canMoveRight()
    {
        return !parentScreen.hasSubModEntry(this);
    }

    private boolean canMoveLeft()
    {
        return parentScreen.hasSubModEntry(this);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
    {
        if (showHoverOverlay() && relativeX <= 32)
        {
            if (canMoveRight())
            {
                parentScreen.move(this, false);
                return true;
            }

            if (canMoveLeft())
            {
                parentScreen.move(this, true);
                return true;
            }
        }

        return false;
    }

    @Override
    public void updatePosition(int slotIndex, int x, int y, float partialTicks)
    {
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }

//    private void bindResourcePackIcon()
//    {
//        subModEntry.bindTexturePackIcon(mc.getTextureManager());
//    }
}
