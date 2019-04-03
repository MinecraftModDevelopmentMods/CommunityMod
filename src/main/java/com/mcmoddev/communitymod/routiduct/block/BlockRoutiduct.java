package com.mcmoddev.communitymod.routiduct.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.mcmoddev.communitymod.routiduct.api.EnumProtocol;
import com.mcmoddev.communitymod.routiduct.item.ItemWrench;

import static com.mcmoddev.communitymod.routiduct.api.AxisUtils.getAxis;

/**
 * Created by Prospector
 */
public class BlockRoutiduct extends BlockRD {

	public static final PropertyEnum<EnumAxis> AXIS = PropertyEnum.<EnumAxis>create("axis", EnumAxis.class);
	private final EnumProtocol protocol;

	public BlockRoutiduct(EnumProtocol protocol) {
		super("routiduct." + protocol.name.toLowerCase());
		setHardness(0.5F);
		this.protocol = protocol;
	}

	@Override
	public EnumProtocol getProtocol() {
		return protocol;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemWrench && playerIn.isSneaking()) {
			worldIn.destroyBlock(pos, true);
			return true;
		}
		return false;
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
		return state.getValue(AXIS).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AXIS, EnumAxis.getFromMetadata(meta));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AXIS });
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		IBlockState north = world.getBlockState(pos.north());
		IBlockState south = world.getBlockState(pos.south());
		IBlockState east = world.getBlockState(pos.east());
		IBlockState west = world.getBlockState(pos.west());
		IBlockState up = world.getBlockState(pos.up());
		IBlockState down = world.getBlockState(pos.down());
		IBlockState xState = getDefaultState().withProperty(AXIS, EnumAxis.X);
		IBlockState yState = getDefaultState().withProperty(AXIS, EnumAxis.Y);
		IBlockState zState = getDefaultState().withProperty(AXIS, EnumAxis.Z);
		if (isSideCompatible(east, xState) && getAxis(facing) == EnumAxis.X) {
			if (world.getBlockState(pos.east()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.east(), xState);
			return xState;
		} else if (isSideCompatible(west, xState) && getAxis(facing) == EnumAxis.X) {
			if (world.getBlockState(pos.west()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.west(), xState);
			return xState;
		} else if (isSideCompatible(north, zState) && getAxis(facing) == EnumAxis.Z) {
			if (world.getBlockState(pos.north()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.north(), zState);
			return zState;
		} else if (isSideCompatible(south, zState) && getAxis(facing) == EnumAxis.Z) {
			if (world.getBlockState(pos.south()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.south(), zState);
			return zState;
		} else if (isSideCompatible(up, yState) && getAxis(facing) == EnumAxis.Y) {
			if (world.getBlockState(pos.up()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.up(), yState);
			return yState;
		} else if (isSideCompatible(down, yState) && getAxis(facing) == EnumAxis.Y) {
			if (world.getBlockState(pos.down()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.down(), yState);
			return yState;
		} else if (areTwoSidesCompatible(east, west, xState)) {
			if (world.getBlockState(pos.east()).getBlock() instanceof BlockRoutiduct && world.getBlockState(pos.east()).getValue(AXIS) != xState.getValue(AXIS))
				world.setBlockState(pos.east(), xState);
			if (world.getBlockState(pos.west()).getBlock() instanceof BlockRoutiduct && world.getBlockState(pos.west()).getValue(AXIS) != xState.getValue(AXIS))
				world.setBlockState(pos.west(), xState);
			return xState;
		} else if (areTwoSidesCompatible(north, south, zState)) {
			if (world.getBlockState(pos.north()).getBlock() instanceof BlockRoutiduct && world.getBlockState(pos.north()).getValue(AXIS) != zState.getValue(AXIS))
				world.setBlockState(pos.north(), zState);
			if (world.getBlockState(pos.south()).getBlock() instanceof BlockRoutiduct && world.getBlockState(pos.south()).getValue(AXIS) != zState.getValue(AXIS))
				world.setBlockState(pos.south(), zState);
			return zState;
		} else if (areTwoSidesCompatible(up, down, yState)) {
			if (world.getBlockState(pos.up()).getBlock() instanceof BlockRoutiduct && world.getBlockState(pos.up()).getValue(AXIS) != yState.getValue(AXIS))
				world.setBlockState(pos.up(), yState);
			if (world.getBlockState(pos.down()).getBlock() instanceof BlockRoutiduct && world.getBlockState(pos.down()).getValue(AXIS) != yState.getValue(AXIS))
				world.setBlockState(pos.down(), yState);
			return yState;
		} else if (isSideCompatible(east, xState)) {
			if (world.getBlockState(pos.east()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.east(), xState);
			return xState;
		} else if (isSideCompatible(west, xState)) {
			if (world.getBlockState(pos.west()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.west(), xState);
			return xState;
		} else if (isSideCompatible(north, zState)) {
			if (world.getBlockState(pos.north()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.north(), zState);
			return zState;
		} else if (isSideCompatible(south, zState)) {
			if (world.getBlockState(pos.south()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.south(), zState);
			return zState;
		} else if (isSideCompatible(up, yState)) {
			if (world.getBlockState(pos.up()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.up(), yState);
			return yState;
		} else if (isSideCompatible(down, yState)) {
			if (world.getBlockState(pos.down()).getBlock() instanceof BlockRoutiduct)
				world.setBlockState(pos.down(), yState);
			return yState;
		}

		return getDefaultState().withProperty(AXIS, EnumAxis.NEUTRAL);
	}

	public boolean areTwoSidesCompatible(IBlockState firstState, IBlockState secondState, IBlockState directionState) {
		if (isSideCompatible(firstState, directionState) && isSideCompatible(secondState, directionState)) {
			return true;
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(AXIS).aabb;
	}

	public boolean isSideCompatible(IBlockState state, IBlockState directionState) {
		IBlockState neutralState = getDefaultState().withProperty(AXIS, EnumAxis.NEUTRAL);
		System.out.println(directionState);
		if ((state.equals(directionState) || state.equals(neutralState) || state.getBlock() instanceof BlockRelay && ((BlockRelay) state.getBlock()).getProtocol() == protocol || state.getBlock() instanceof BlockPackager && ((BlockPackager) state.getBlock()).getProtocol() == protocol)) {
			return true;
		}
		return false;
	}

	public enum EnumAxis implements IStringSerializable {
		X("x", new AxisAlignedBB(0, 0.25, 0.25, 1, 0.75, 0.75)),
		Y("y", new AxisAlignedBB(0.25, 0, 0.25, 0.75, 1, 0.75)),
		Z("z", new AxisAlignedBB(0.25, 0.25, 0, 0.75, 0.75, 1)),
		NEUTRAL("neutral", new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75));

		public final AxisAlignedBB aabb;
		private String name;

		EnumAxis(String name, AxisAlignedBB aabb) {
			this.name = name;
			this.aabb = aabb;
		}

		public static int getMetadata(EnumAxis axis) {
			if (axis == X) {
				return 1;
			} else if (axis == Y) {
				return 2;
			} else if (axis == Z) {
				return 3;
			} else
				return 0;
		}

		public static EnumAxis getFromMetadata(int meta) {
			if (meta == 1) {
				return X;
			} else if (meta == 2) {
				return Y;
			} else if (meta == 3) {
				return Z;
			} else
				return NEUTRAL;
		}

		public int getMetadata() {
			return getMetadata(this);
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
