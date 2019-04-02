package com.mcmoddev.communitymod.routiduct.block.tiles;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import com.mcmoddev.communitymod.routiduct.api.EnumProtocol;
import com.mcmoddev.communitymod.routiduct.block.BlockRD;
import com.mcmoddev.communitymod.routiduct.gui.ContainerRoutiduct;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.GuiBlueprint;

@SuppressWarnings("javadoc")
public class TileRoutiduct extends TileEntity implements ITickable {

	public EnumProtocol protocol = EnumProtocol.RD1;
	public IItemHandler itemHandler;
	private BlockRD protocolProviderBlock = null;
	private GuiBlueprint blueprint;

	public TileRoutiduct() {
		super();

	}

	@Override
	public void update() {
		if (world != null) {
			if (protocolProviderBlock == null) {
				protocolProviderBlock = (BlockRD) world.getBlockState(pos).getBlock();
				itemHandler = new ItemStackHandler(protocolProviderBlock.getProtocol().stacks);
			}
			protocolProviderBlock.update(this);
		}
	}

	public Block getProtocolProviderBlock() {
		return protocolProviderBlock;
	}

	public void setProtocolProviderBlock(BlockRD protocolProviderBlock) {
		this.protocolProviderBlock = protocolProviderBlock;
	}

	public ContainerRoutiduct getContainer(EntityPlayer player) {
		return new ContainerRoutiduct(this, player);
	}

	public GuiBlueprint getGuiBlueprint() {
		//		if (blueprint == null) {
		blueprint = protocolProviderBlock.getGuiBlueprint(this);
		//		}
		return blueprint;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemHandler;
		}
		return super.getCapability(capability, facing);
	}
}
