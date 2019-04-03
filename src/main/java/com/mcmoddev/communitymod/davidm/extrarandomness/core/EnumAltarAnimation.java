package com.mcmoddev.communitymod.davidm.extrarandomness.core;

public enum EnumAltarAnimation {

	SHOCKWAVE(10);
	
	private int animationLength;
	
	private EnumAltarAnimation(int animationLength) {
		this.animationLength = animationLength;
	}
	
	public int animationLength() {
		return this.animationLength;
	}
}
