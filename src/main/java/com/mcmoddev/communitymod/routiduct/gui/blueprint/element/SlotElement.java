package com.mcmoddev.communitymod.routiduct.gui.blueprint.element;

import net.minecraft.inventory.Slot;
import com.mcmoddev.communitymod.routiduct.gui.SlotType;

public class SlotElement extends ElementBase {
	protected Slot slot;
	protected SlotType type;

	public SlotElement(Slot slot, SlotType type, int x, int y) {
		super(type.getSprite(), x, y);
		this.slot = slot;
		this.type = type;
	}

	public Slot getSlot() {
		return slot;
	}

	public SlotType getType() {
		return type;
	}
}