package com.mcmoddev.communitymod.davidm;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAltar extends TileEntity implements ITickable {

	private ItemStackHandler inventory = new ItemStackHandler(1);
	
	public void rightClick(EntityPlayer player) {
		System.out.println(321);
		if (this.getStack().isEmpty()) {
			ItemStack playerStack = player.inventory.getCurrentItem();
			ItemStack copy = playerStack.copy();
			copy.setCount(1);
			this.setStack(copy);
			playerStack.shrink(1);
		} else {
			EntityItem drop = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, this.getStack());
			this.world.spawnEntity(drop);
			this.setStack(ItemStack.EMPTY);
		}
		this.markDirty();
	}
	
	private ItemStack getStack() {
		return this.inventory.getStackInSlot(0);
	}
	
	private void setStack(ItemStack stack) {
		if (this.getStack().isEmpty()) {
			this.inventory.setStackInSlot(0, stack);
		}
	}

	@Override
	public void tick() {
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", this.inventory.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		super.readFromNBT(compound);
	}
}
