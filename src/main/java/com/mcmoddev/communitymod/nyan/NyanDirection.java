package com.mcmoddev.communitymod.nyan;

import net.minecraft.entity.Entity;
import net.minecraft.util.Rotation;

public enum NyanDirection {
	NORTH(0, 45, 0, -1),
	EAST(45, 135, 1, 0),
	SOUTH(135, 225, 0, 1),
	WEST(225, 315, -1, 0);

	private final double minRotation;
	private final double maxRotation;
	private final int xDirection;
	private final int zDirection;

	NyanDirection(double minRotation, double maxRotation, int xDirection, int zDirection) {
		this.minRotation = minRotation;
		this.maxRotation = maxRotation;
		this.xDirection = xDirection;
		this.zDirection = zDirection;
	}

	public static NyanDirection getDirectionFacing(Entity entity, Rotation rotation) {
		double rot = (entity.getRotatedYaw(rotation) - 90) % 360;

		if(rot < 0) {
			rot += 360.0;
		}

		for(NyanDirection direction : values()) {
			if(rot >= direction.minRotation && rot < direction.maxRotation) {
				return direction;
			}
		}

		return NORTH;
	}

	public double getXDirection() {
		return xDirection;
	}

	public double getZDirection() {
		return zDirection;
	}
}
