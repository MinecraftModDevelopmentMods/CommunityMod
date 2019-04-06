package com.mcmoddev.communitymod.davidm.extrarandomness.core.helper;

public class MathHelper {

	public static double oscillate(double time, double min, double max) {
		return min + (Math.sin(Math.toRadians(time)) + 1) / 2 * (max - min);
	}
}
