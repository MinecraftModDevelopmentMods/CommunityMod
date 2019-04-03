package com.mcmoddev.communitymod.davidm;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAltar extends TileEntity implements ITickable {

	private ItemStackHandler inventory = new ItemStackHandler(1) {
		
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				DavidM.network.sendToAllAround(
						new PacketUpdateAltar(TileEntityAltar.this),
						new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
				);
			}
		}
	};
	
	private int cooldown;
	
	public void rightClick(EntityPlayer player) {
		if (this.getStack().isEmpty()) {
			ItemStack playerStack = player.inventory.getCurrentItem();
			ItemStack copy = playerStack.copy();
			copy.setCount(1);
			this.setStack(copy);
			playerStack.shrink(1);
			player.inventory.markDirty();
		} else {
			EntityItem drop = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, this.getStack());
			this.world.spawnEntity(drop);
			this.setStack(ItemStack.EMPTY);
		}
		this.markDirty();
	}
	
	public ItemStack getStack() {
		return this.inventory.getStackInSlot(0);
	}
	
	public void setStack(ItemStack stack) {
		this.inventory.setStackInSlot(0, stack);
	}

	@Override
	public void update() {
		if (!this.world.isRemote) {
			if (this.getStack().getItem() instanceof AltarItem) {
				AltarItem altarItem = (AltarItem) this.getStack().getItem();
				if (this.cooldown++ >= altarItem.getCooldown()) {
					this.cooldown = 0;
					altarItem.onAltarAction(this.world, this.pos);
				}
			}
		}
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			DavidM.network.sendToServer(new PacketRequestUpdateAltar(this));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", this.inventory.serializeNBT());
		compound.setInteger("cooldown", this.cooldown);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.cooldown = compound.getInteger("cooldown");
		super.readFromNBT(compound);
	}
}
