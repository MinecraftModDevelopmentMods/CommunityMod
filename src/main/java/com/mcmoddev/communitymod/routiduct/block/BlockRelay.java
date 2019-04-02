package com.mcmoddev.communitymod.routiduct.block;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.mcmoddev.communitymod.routiduct.api.AxisUtils;
import com.mcmoddev.communitymod.routiduct.api.EnumProtocol;
import com.mcmoddev.communitymod.routiduct.api.IProtocolProvider;

/**
 * Created by Prospector
 */
public class BlockRelay extends BlockRD implements IProtocolProvider {

	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool DOWN = PropertyBool.create("down");
	private final EnumProtocol protocol;

	public BlockRelay(EnumProtocol protocol) {
		super("relay." + protocol.name.toLowerCase());
		setHardness(0.5F);
		setDefaultState(getDefaultState().withProperty(EAST, false).withProperty(WEST, false).withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(UP, false).withProperty(DOWN, false));
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
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState actualState = state;
		for (EnumFacing facing : EnumFacing.values()) {
			IBlockState offsetState = worldIn.getBlockState(pos.offset(facing));
			Block offsetBlock = offsetState.getBlock();
			if (offsetBlock instanceof IProtocolProvider && ((IProtocolProvider) offsetBlock).getProtocol() == protocol) {
				if (offsetBlock instanceof BlockRoutiduct && offsetState.getValue(BlockRoutiduct.AXIS) == AxisUtils.getAxis(facing) ||
					offsetBlock instanceof BlockRelay ||
					offsetBlock instanceof BlockPackager && offsetState.getValue(BlockPackager.FACING) == facing.getOpposite() ||
					offsetBlock instanceof BlockUnpackager && offsetState.getValue(BlockUnpackager.FACING) == facing.getOpposite()
					)
					actualState = actualState.withProperty(getProperty(facing), true);
			}
		}
		return actualState;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
		//		return new AxisAlignedBB(0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
	}

	public IProperty<Boolean> getProperty(EnumFacing facing) {
		switch (facing) {
			case EAST:
				return EAST;
			case WEST:
				return WEST;
			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case UP:
				return UP;
			case DOWN:
				return DOWN;
			default:
				return EAST;
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { EAST, WEST, NORTH, SOUTH, UP, DOWN });
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState();
	}
}
