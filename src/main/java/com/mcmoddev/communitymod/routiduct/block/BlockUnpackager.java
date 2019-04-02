package com.mcmoddev.communitymod.routiduct.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import com.mcmoddev.communitymod.routiduct.api.EnumProtocol;

/**
 * Created by Prospector
 */
public class BlockUnpackager extends BlockRD {

	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.<EnumFacing>create("facing", EnumFacing.class);
	public static final PropertyBool CONNECTION = PropertyBool.create("connection");
	public final EnumProtocol protocol;

	public BlockUnpackager(EnumProtocol protocol) {
		super("unpackager." + protocol.name.toLowerCase());
		setHardness(0.5F);
		this.protocol = protocol;
	}

	@Override
	public EnumProtocol getProtocol() {
		return protocol;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, 0);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(getDefaultState());
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (isEqual(state, EnumFacing.EAST, false))
			return 0;
		else if (isEqual(state, EnumFacing.WEST, false))
			return 1;
		else if (isEqual(state, EnumFacing.NORTH, false))
			return 2;
		else if (isEqual(state, EnumFacing.SOUTH, false))
			return 3;
		else if (isEqual(state, EnumFacing.UP, false))
			return 4;
		else if (isEqual(state, EnumFacing.DOWN, false))
			return 5;
		else if (isEqual(state, EnumFacing.EAST, true))
			return 6;
		else if (isEqual(state, EnumFacing.WEST, true))
			return 7;
		else if (isEqual(state, EnumFacing.NORTH, true))
			return 8;
		else if (isEqual(state, EnumFacing.SOUTH, true))
			return 9;
		else if (isEqual(state, EnumFacing.UP, true))
			return 10;
		else if (isEqual(state, EnumFacing.DOWN, true))
			return 11;
		return 0;
	}

	public boolean isEqual(IBlockState state, EnumFacing facingValue, boolean connectionValue) {
		if (state.equals(getDefaultState().withProperty(FACING, facingValue).withProperty(CONNECTION, connectionValue))) {
			return true;
		}
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, getFacingFromMeta(meta)).withProperty(CONNECTION, getConnectionFromMeta(meta));
	}

	public EnumFacing getFacingFromMeta(int meta) {
		if (meta == 0 || meta == 6) {
			return EnumFacing.EAST;
		}
		if (meta == 1 || meta == 7) {
			return EnumFacing.WEST;
		}
		if (meta == 2 || meta == 8) {
			return EnumFacing.NORTH;
		}
		if (meta == 3 || meta == 9) {
			return EnumFacing.SOUTH;
		}
		if (meta == 4 || meta == 10) {
			return EnumFacing.UP;
		}
		if (meta == 5 || meta == 11) {
			return EnumFacing.DOWN;
		}
		return EnumFacing.EAST;
	}

	public boolean getConnectionFromMeta(int meta) {
		if (meta >= 0 && meta <= 5) {
			return false;
		}
		if (meta >= 6 && meta <= 11) {
			return true;
		}
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, CONNECTION });
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, facing.getOpposite()).withProperty(CONNECTION, true);
	}
}
