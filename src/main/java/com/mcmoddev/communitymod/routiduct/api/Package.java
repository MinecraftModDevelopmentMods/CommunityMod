package com.mcmoddev.communitymod.routiduct.api;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prospector
 */
public class Package {
	public static final Package EMPTY = new Package(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);

	public ItemStack stack1;
	public ItemStack stack2;
	public ItemStack stack3;
	public ItemStack stack4;

	public Package(ItemStack stack1, ItemStack stack2, ItemStack stack3, ItemStack stack4) {
		this.stack1 = stack1;
		this.stack2 = stack2;
		this.stack3 = stack3;
		this.stack4 = stack4;
	}

	public List<ItemStack> getStacks() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(stack1);
		list.add(stack2);
		list.add(stack3);
		list.add(stack4);
		return list;
	}

	public ItemStack getStack1() {
		return stack1;
	}

	public ItemStack getStack2() {
		return stack2;
	}

	public ItemStack getStack3() {
		return stack3;
	}

	public ItemStack getStack4() {
		return stack4;
	}

	public boolean isEmpty() {
		if (this != EMPTY) {
			return true;
		}
		return false;
	}

	public enum EnumColor {
		TEAL("color.routiduct.teal.name", 0xFF00FFFA, 0xFF00D4D0, 18),
		SKY("color.routiduct.sky.name", 0xFF009EFF, 0xFF0083D4, 24),
		BLUE("color.routiduct.blue.name", 0xFF2100FF, 0xFF1C00D4, 30),
		PURPLE("color.routiduct.purple.name", 0xFF9000FF, 0xFF7800D4, 36),
		PINK("color.routiduct.pink.name", 0xFFF200FF, 0xFFC900D4, 42),
		RED("color.routiduct.red.name", 0xFFFF0800, 0xFFD40700, 48),
		ORANGE("color.routiduct.orange.name", 0xFFFF6A00, 0xFFD45800, 54),
		YELLOW("color.routiduct.yellow.name", 0xFFFFE100, 0xFFD4BB00, 60),
		LIME("color.routiduct.lime.name", 0xFF80FF00, 0xFF6AD400, 66),
		GREEN("color.routiduct.green.name", 0xFF21A943, 0xFF0B7C27, 72),
		BLACK("color.routiduct.black.name", 0xFF191919, 0xFF000000, 78),
		GREY("color.routiduct.grey.name", 0xFF666666, 0xFF484848, 84),
		SILVER("color.routiduct.silver.name", 0xFFB2B2B2, 0xFF949494, 90),
		WHITE("color.routiduct.white.name", 0xFFFFFFFF, 0xFFDEDEDE, 96);

		public final int colour;
		public final int altColour;
		public final int textureY;
		public String unlocalisedName;

		EnumColor(String unlocalisedName, int colour, int altColour, int textureY) {
			this.unlocalisedName = unlocalisedName;
			this.colour = colour;
			this.altColour = altColour;
			this.textureY = textureY;
		}
	}
}
