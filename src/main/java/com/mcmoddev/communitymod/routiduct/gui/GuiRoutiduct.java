package com.mcmoddev.communitymod.routiduct.gui;

import com.mcmoddev.communitymod.routiduct.Routiduct;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.GuiBlueprint;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.element.ElementBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class GuiRoutiduct extends GuiContainer implements IDynamicAdjustmentGUI {

    public final GuiBlueprint blueprint;
    public int xFactor;
    public int yFactor;
    public ContainerRoutiduct container = (ContainerRoutiduct) inventorySlots;

    public GuiRoutiduct(GuiBlueprint blueprint, EntityPlayer player) {
        super(blueprint.tile.getContainer(player));
        this.blueprint = blueprint;
        xSize = blueprint.xSize;
        ySize = blueprint.ySize;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        xFactor = 0;
        yFactor = 0;
        drawTitle();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        xFactor = guiLeft;
        yFactor = guiTop;
        Routiduct.proxy.getGuiAssembler().drawDefaultBackground(this, 0, 0, xSize, ySize);
        for (ElementBase e : blueprint.elements) {
            e.draw(this);
        }
    }

    @Override
    public int getOffsetFactorX() {
        return xFactor;
    }

    @Override
    public int getOffsetFactorY() {
        return yFactor;
    }

    protected void drawTitle() {
        Routiduct.proxy.getGuiAssembler().drawCenteredString(this, I18n.format(blueprint.tile.getBlockType().getTranslationKey() + ".name"), 6, 4210752);
    }

}
