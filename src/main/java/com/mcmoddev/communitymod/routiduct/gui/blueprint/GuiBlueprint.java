package com.mcmoddev.communitymod.routiduct.gui.blueprint;

import com.mcmoddev.communitymod.routiduct.block.tiles.TileRoutiduct;
import com.mcmoddev.communitymod.routiduct.gui.SlotType;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.element.DummySlotElement;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.element.ElementBase;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.element.SlotElement;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class GuiBlueprint {
	public List<ElementBase> elements = new ArrayList<>();
	public List<SlotElement> slots = new ArrayList<>();
	public int xSize = 0;
	public int ySize = 0;
	public int playerInvX = -1;
	public int playerInvY = -1;
	public TileRoutiduct tile;

	public GuiBlueprint(TileRoutiduct tile) {
		this.tile = tile;

	}

	public GuiBlueprint setSize(int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		return this;
	}

	/**
	 * @param x the player's inventory slots will start drawing at this x position (this is the top left corner) [-1 to disable]
	 * @param y the player's inventory slots will start drawing at this y position (this is the top left corner) [-1 to disable]
	 * @return returns the blueprint for fast building of blueprints
	 */
	public GuiBlueprint setPlayerInvPos(int x, int y) {
		this.playerInvX = x;
		this.playerInvY = y;
		if (x > -1 && y > -1) {
			for (int row = 0; row < 3; ++row) {
				for (int column = 0; column < 9; ++column) {
					int index = column + row * 9 + 9;
					int xpos = x + (column * 18);
					int ypos = y + (row * 18);
					this.elements.add(new DummySlotElement(SlotType.NORMAL, xpos, ypos));
				}
			}
			for (int column = 0; column < 9; ++column) {
				int xpos = x + (column * 18);
				int ypos = y + 58;
				this.elements.add(new DummySlotElement(SlotType.NORMAL, xpos, ypos));
			}
		}
		return this;
	}

	public GuiBlueprint addSlot(SlotElement slot) {
		this.slots.add(slot);
		this.elements.add(slot);
		return this;
	}

	public GuiBlueprint addSlot(SlotType type, int x, int y) {
		addSlot(new SlotElement(new SlotItemHandler(tile.itemHandler, slots.size(), x + type.getSlotOffsetX(), y + type.getSlotOffsetY()), type, x, y));
		return this;
	}
}
