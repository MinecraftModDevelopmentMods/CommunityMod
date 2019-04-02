package com.mcmoddev.communitymod.routiduct.gui;

import com.mcmoddev.communitymod.routiduct.block.tiles.TileRoutiduct;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.element.SlotElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ContainerRoutiduct extends Container {
	private final TileRoutiduct tile;
	private final IItemHandler itemHandler;

	public ContainerRoutiduct(TileRoutiduct tile, EntityPlayer player) {
		this.tile = tile;
		itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlots(itemHandler);
		addPlayerSlots(player);
	}

	private void addSlots(IItemHandler inventory) {
		for (SlotElement slotElement : tile.getGuiBlueprint().slots) {
			addSlotToContainer(slotElement.getSlot());
		}
	}

	private void addPlayerSlots(EntityPlayer player) {
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				int index = column + row * 9 + 9;
				int x = tile.getGuiBlueprint().playerInvX + 1 + (column * 18);
				int y = tile.getGuiBlueprint().playerInvY + 1 + (row * 18);
				addSlotToContainer(new Slot(player.inventory, index, x, y));
			}
		}
		for (int column = 0; column < 9; ++column) {
			int x = tile.getGuiBlueprint().playerInvX + 1 + (column * 18);
			int y = tile.getGuiBlueprint().playerInvY + 1 + 58;
			addSlotToContainer(new Slot(player.inventory, column, x, y));
		}
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		Slot slot = inventorySlots.get(index);
		ItemStack stack = slot.getStack().copy();

		if (slot.getHasStack()) {
			int containerSlots = tile.protocol.stacks;
			if (index >= containerSlots) {
				if (!mergeItemStack(stack, 0, containerSlots, false))
					return ItemStack.EMPTY; // Inventory -> Slot
			} else if (!mergeItemStack(stack, containerSlots, containerSlots + 36, true))
				return ItemStack.EMPTY; // Slot -> Inventory
			slot.onSlotChanged();
			if (stack.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			slot.onTake(player, stack);
			return stack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(
		@Nonnull
			EntityPlayer player) {
		return true;
	}
}
