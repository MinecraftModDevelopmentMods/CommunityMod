package com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.ExtraRandomness;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.block.BlockMemeCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.LexWand;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.item.MemeWrench;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketRequestUpdateCapacitorBeam;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketRequestUpdateTileEntity;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.network.PacketUpdateCapacitorBeam;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumCapacitor;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.EnumSideConfig;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.attribute.IWrenchable;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.helper.NetworkHelper;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.memepower.IMemePowerContainer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntityCapacitor extends TileEntity implements ITickable, IMemePowerContainer, IWrenchable {

	private static final int TRANSPORT_RANGE = 8;
	
	private EnumCapacitor enumCapacitor;
	private int currentPower;
	
	private EnumSideConfig[] sideConfigs;
	private BlockPos[] beams;
	
	public TileEntityCapacitor() {
		super();
		
		this.enumCapacitor = EnumCapacitor.TIER_0;
		this.sideConfigs = new EnumSideConfig[6];
		for (int i = 0; i < this.sideConfigs.length; i++) {
			this.sideConfigs[i] = EnumSideConfig.NONE;
		}
		this.beams = new BlockPos[6];
		for (int i = 0; i < this.beams.length; i++) {
			this.beams[i] = new BlockPos(this.pos);
		}
	}
	
	public void onRightClick(EntityPlayer player, EnumFacing facing) {
		if (player.inventory.getCurrentItem().getItem() instanceof MemeWrench) {
			this.editSideConfig(facing);
			this.markDirty();
			NetworkHelper.sendTileEntityToNearby(this, 64);
		} else if (player.inventory.getCurrentItem().getItem() instanceof LexWand && player.isCreative()) {
			this.receivePower(this.enumCapacitor.getPower());
		} else {
			player.sendMessage(new TextComponentString(String.format("%s/%s", this.currentPower, this.enumCapacitor.getPower())));
		}
	}
	
	public void setCapacitorData(EnumCapacitor enumCapacitor) {
		this.enumCapacitor = enumCapacitor;
		this.markDirty();
	}
	
	private void editSideConfig(EnumFacing facing) {
		int facingIndex = facing.ordinal();
		this.sideConfigs[facingIndex] = EnumSideConfig.values()[(this.sideConfigs[facingIndex].ordinal() + 1) % 3];
	}
	
	public EnumSideConfig[] getSideConfigs() {
		return this.sideConfigs;
	}
	
	public BlockPos[] getBeams() {
		return this.beams;
	}
	
	public void setBeams(BlockPos[] beams) {
		this.beams = beams;
	}
	
	public double getScaledPower() {
		return (double) this.currentPower / this.enumCapacitor.getPower();
	}
	
	@Override
	public void update() {
		if (!this.world.isRemote && this.world.getTotalWorldTime() % 20 == 0) {
			List<IMemePowerContainer> receivers = new ArrayList<IMemePowerContainer>();
			BlockPos[] newBeams = new BlockPos[6];
			
			for (int i = 0; i < newBeams.length; i++) {
				newBeams[i] = new BlockPos(this.pos);
			}
			
			for (int i = 0; i < this.sideConfigs.length; i++) {
				if (this.sideConfigs[i] == EnumSideConfig.OUTPUT) {
					for (int distance = 1; distance < TRANSPORT_RANGE; distance++) {
						EnumFacing facing = EnumFacing.values()[i];
						BlockPos receiverPos = this.pos.offset(facing, distance);
						
						if (this.world.isAirBlock(receiverPos)) continue;
						
						TileEntity tileEntity = this.world.getTileEntity(receiverPos);
						if (tileEntity instanceof IMemePowerContainer) {
							IMemePowerContainer powerContainer = (IMemePowerContainer) tileEntity;
							if (powerContainer.canReceivePowerFrom(facing.getOpposite())) {
								if (!powerContainer.isFull()) {
									receivers.add(powerContainer);
								}
								
								newBeams[i] = new BlockPos(tileEntity.getPos());
							}
						}
						break;
					}
				}
			}
			
			boolean changed = false;
			
			for (int i = 0; i < newBeams.length; i++) {
				if (!newBeams[i].equals(this.beams[i])) {
					changed = true;
					break;
				}
			}
			
			if (changed) {
				this.beams = newBeams;
				
				int dimension = this.world.provider.getDimension();
				ExtraRandomness.network.sendToAllAround(
						new PacketUpdateCapacitorBeam(this), 
						new NetworkRegistry.TargetPoint(dimension, pos.getX(), pos.getY(), pos.getZ(), 64)
				);
			}
			
			if (!receivers.isEmpty()) {
				int avg = Math.min(this.currentPower / receivers.size(), this.enumCapacitor.getTransferRate());
				this.currentPower -= avg * receivers.size();
				receivers.forEach(receiver -> {
					this.currentPower += receiver.receivePower(avg);
				});
			}
			
			this.markDirty();
			NetworkHelper.sendTileEntityToNearby(this, 64);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			ExtraRandomness.network.sendToServer(new PacketRequestUpdateTileEntity(this));
			ExtraRandomness.network.sendToServer(new PacketRequestUpdateCapacitorBeam(this));
			this.world.tickableTileEntities.remove(this);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.enumCapacitor = EnumCapacitor.values()[compound.getInteger("enumCapacitor")];
		this.currentPower = compound.getInteger("power");
		int[] sides = compound.getIntArray("sides");
		if (sides.length == 6) {
			for (int i = 0; i < sides.length; i++) {
				if (sides[i] < EnumSideConfig.values().length) {
					this.sideConfigs[i] = EnumSideConfig.values()[sides[i]];
				}
			}
		}
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("enumCapacitor", this.enumCapacitor.ordinal());
		compound.setInteger("power", this.currentPower);
		int[] sides = new int[6];
		for (int i = 0; i < sides.length; i++) {
			sides[i] = this.sideConfigs[i].ordinal();
		}
		compound.setIntArray("sides", sides);
		return super.writeToNBT(compound);
	}

	@Override
	public int getPower() {
		return this.currentPower;
	}

	@Override
	public boolean isFull() {
		return this.currentPower == this.enumCapacitor.getPower();
	}

	@Override
	public boolean canReceivePowerFrom(EnumFacing facing) {
		return this.sideConfigs[facing.ordinal()] == EnumSideConfig.INPUT;
	}

	@Override
	public int receivePower(int power) {
		int overflow_1 = Math.max(power - this.enumCapacitor.getTransferRate(), 0);
		power -= overflow_1;
		this.currentPower += power;
		int overflow_2 = Math.max(this.currentPower - this.enumCapacitor.getPower(), 0);
		this.currentPower -= overflow_2;
		
		this.markDirty();
		NetworkHelper.sendTileEntityToNearby(this, 64);
		return overflow_1 + overflow_2;
	}

	@Override
	public void onWrench(EntityPlayer player) {
		NonNullList<ItemStack> drops = NonNullList.<ItemStack>create();
		IBlockState state = this.world.getBlockState(this.pos);
		Block block = state.getBlock();
		if (block instanceof BlockMemeCapacitor) {
			block.getDrops(drops, this.world, this.pos, state, 0);
			block.harvestBlock(this.world, player, pos, state, this, player.inventory.getCurrentItem());
		}
	}
}
