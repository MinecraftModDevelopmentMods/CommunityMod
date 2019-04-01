package com.mcmoddev.communitymod.commoble.gnomes.util;


public class DirectionUtil
{
	enum EnumDir
	{
		NODIR,
		UP,
		DOWN,
		NORTH,
		SOUTH,
		EAST,
		WEST
	}
	
	public static final EnumDir[] ALL_DIRS_AND_NULL = {
		EnumDir.NODIR,
		EnumDir.UP,
		EnumDir.DOWN,
		EnumDir.NORTH,
		EnumDir.SOUTH,
		EnumDir.EAST,
		EnumDir.WEST
	};
	
	public static final EnumDir[] ALL_DIRS = {
			EnumDir.UP,
			EnumDir.DOWN,
			EnumDir.NORTH,
			EnumDir.SOUTH,
			EnumDir.EAST,
			EnumDir.WEST
	};
	
	public static final EnumDir[] ALL_HORIZONTAL_DIRS = {
			EnumDir.NORTH,
			EnumDir.SOUTH,
			EnumDir.EAST,
			EnumDir.WEST
	};
	
	public static final EnumDir[] ALL_HORIZONTAL_DIRS_AND_NULL = {
			EnumDir.NODIR,
			EnumDir.NORTH,
			EnumDir.SOUTH,
			EnumDir.EAST,
			EnumDir.WEST
	};
	
	public static final EnumDir[] ALL_VERTICAL_DIRS = {
			EnumDir.UP,
			EnumDir.DOWN
	};
	
	public static final EnumDir[] ALL_VERTICAL_DIRS_AND_NULL = {
			EnumDir.NODIR,
			EnumDir.UP,
			EnumDir.DOWN
	};
}
