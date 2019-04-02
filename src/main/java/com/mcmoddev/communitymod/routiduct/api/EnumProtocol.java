package com.mcmoddev.communitymod.routiduct.api;

/**
 * Created by Prospector
 */
public enum EnumProtocol {
	RD1("RD1", 4, 2, 50, 10, 64, 0), RD2("RD2", 12, 1, 20, 4, 79, 0);

	public String name;
	public int stacks = 4;
	public int ports;
	public int ticksPerPipe;
	public int ticksToPackage;
	public int ticksToRead;
	public int badgeX;
	public int badgeY;

	EnumProtocol(String prefix, int ports, int ticksPerPipe, int ticksToPackage, int ticksToRead, int badgeX, int badgeY) {
		this.name = prefix;
		this.ports = ports;
		this.ticksPerPipe = ticksPerPipe;
		this.ticksToPackage = ticksToPackage;
		this.ticksToRead = ticksToRead;
		this.badgeX = badgeX;
		this.badgeY = badgeY;
	}
}
