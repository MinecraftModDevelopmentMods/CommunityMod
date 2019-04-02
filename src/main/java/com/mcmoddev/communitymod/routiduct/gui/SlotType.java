package com.mcmoddev.communitymod.routiduct.gui;

public enum SlotType {
	NORMAL(1, 1, Sprites.SLOT_NORMAL);

	int slotOffsetX;
	int slotOffsetY;
	Sprite sprite;

	SlotType(int slotOffsetX, int slotOffsetY, Sprite sprite) {
		this.slotOffsetX = slotOffsetX;
		this.slotOffsetY = slotOffsetY;
		this.sprite = sprite;
	}

	SlotType(int slotOffset, Sprite sprite) {
		this.slotOffsetX = slotOffset;
		this.slotOffsetY = slotOffset;
		this.sprite = sprite;
	}

	public int getSlotOffsetX() {
		return slotOffsetX;
	}

	public int getSlotOffsetY() {
		return slotOffsetY;
	}

	public Sprite getSprite() {
		return sprite;
	}
}