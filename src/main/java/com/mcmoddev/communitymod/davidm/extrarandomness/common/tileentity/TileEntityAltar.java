package com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.ExtraRandomness;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketRequestUpdateTileEntity;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumAltarAnimation;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.attribute.AltarItem;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.NetworkHelper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAltar extends TileEntity implements ITickable {

	private ItemStackHandler inventory = new ItemStackHandler(1) {
		
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				NetworkHelper.sendTileEntityToNearby(TileEntityAltar.this, 80);
			}
		}
	};
	
	private int cooldown;
	
	public EnumAltarAnimation altarAnimation;
	public int animationProgress;
	
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
				
				TileEntity tileEntity = this.world.getTileEntity(this.pos.offset(EnumFacing.DOWN));
				if (tileEntity instanceof TileEntityCapacitor) {
					((TileEntityCapacitor) tileEntity).receivePower(10);
				}
			}
		} else {
			if (this.altarAnimation != null) {
				if (this.animationProgress++ >= this.altarAnimation.animationLength()) {
					this.altarAnimation = null;
					this.animationProgress = 0;
				}
			}
		}
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			ExtraRandomness.network.sendToServer(new PacketRequestUpdateTileEntity(this));
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.cooldown = compound.getInteger("cooldown");
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", this.inventory.serializeNBT());
		compound.setInteger("cooldown", this.cooldown);
		return super.writeToNBT(compound);
	}
}
