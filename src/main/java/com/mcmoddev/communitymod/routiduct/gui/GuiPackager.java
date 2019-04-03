/*
package prospector.routiduct.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import prospector.routiduct.api.Package;
import prospector.routiduct.block.tiles.TilePackager;
import prospector.routiduct.container.ContainerPackager;

*/
/**
 * Created by Prospector
 *//*

public class GuiPackager extends GuiContainer {
	public int xSize = 176;
	public int ySize = 176;
	GuiAssembler builder = new GuiAssembler();
	TilePackager tile;
	EntityPlayer player;

	public GuiPackager(TilePackager tile, EntityPlayer player) {
		super(new ContainerPackager(tile, player));
		this.tile = tile;
		this.player = player;
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		builder.drawPlayerSlots(this, guiLeft + xSize / 2, guiTop + 93, true);

		builder.drawSlot(this, guiLeft + 7, guiTop + 30);
		builder.drawSlot(this, guiLeft + 25, guiTop + 30);
		builder.drawSlot(this, guiLeft + 7, guiTop + 48);
		builder.drawSlot(this, guiLeft + 25, guiTop + 48);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		drawTitle();
		drawString("Inventory", 8, 83, 4210752);
		builder.drawProgress(this, 100, 100, 47, 36, Package.EnumColor.PURPLE);
	}

	protected void drawTitle() {
		drawCenteredString(I18n.translateToLocal(tile.getBlockType().getUnlocalizedName() + ".name"), 6, 4210752);
	}

	protected void drawCenteredString(String string, int y, int colour) {
		drawString(string, (xSize / 2 - mc.fontRendererObj.getStringWidth(string) / 2), y, colour);
	}

	protected void drawCenteredString(String string, int y, int colour, int modifier) {
		drawString(string, (xSize / 2 - (mc.fontRendererObj.getStringWidth(string)) / 2) + modifier, y, colour);
	}

	protected void drawString(String string, int x, int y, int colour) {
		mc.fontRendererObj.drawString(string, x, y, colour);
		GlStateManager.color(1, 1, 1, 1);
	}
}
*/
