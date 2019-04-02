package com.mcmoddev.communitymod.routiduct.gui.blueprint.element;

import com.mcmoddev.communitymod.routiduct.gui.SlotType;

public class DummySlotElement extends ElementBase {
	SlotType type;

	public DummySlotElement(SlotType type, int x, int y) {
		super(type.getSprite(), x, y);
		this.type = type;
	}

	public SlotType getType() {
		return type;
	}
}