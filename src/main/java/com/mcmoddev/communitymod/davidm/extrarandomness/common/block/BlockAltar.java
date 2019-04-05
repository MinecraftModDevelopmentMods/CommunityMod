package com.mcmoddev.communitymod.davidm.extrarandomness.common.block;

import java.util.List;
import java.util.Random;

import com.mcmoddev.communitymod.davidm.extrarandomness.common.tileentity.TileEntityAltar;
import com.mcmoddev.communitymod.davidm.extrarandomness.core.AltarItem;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAltar extends Block {
	
	private static AxisAlignedBB COLLISION = new AxisAlignedBB(1 / 16.0D, 0, 1 / 16.0D, 15 / 16.0D, 12 / 16.0D, 15 / 16.0D);

	public BlockAltar() {
		super(Material.ROCK);
		this.setHardness(30F);
		this.setHarvestLevel("pickaxe", 3);
		this.setResistance(50F);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityAltar) {
				((TileEntityAltar) tileEntity).rightClick(player);
			}
		}
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityAltar) {
			ItemStack stack = ((TileEntityAltar) tileEntity).getStack();
			if (!stack.isEmpty()) {
				EntityItem drop = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
				world.spawnEntity(drop);
			}
		}
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityAltar) {
			ItemStack stack = ((TileEntityAltar) tileEntity).getStack();
			if (stack.getItem() instanceof AltarItem) {
				for (int i = 0; i < 10; i++) {
					int xOffset = rand.nextInt(2) * 2 - 1;
					int zOffset = rand.nextInt(2) * 2 - 1;
					
					double xCoord = pos.getX() + 0.5 + 0.25 * xOffset;
					double yCoord = pos.getY() + rand.nextFloat();
					double zCoord = pos.getZ() + 0.5 + 0.25 * zOffset;
					double xSpeed = rand.nextFloat() - 0.5;
					double zSpeed = rand.nextFloat() - 0.5;
					world.spawnParticle(EnumParticleTypes.PORTAL, xCoord, yCoord, zCoord, xSpeed, 0, zSpeed);
				}
			}
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityAltar();
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format("tooltip.community_mod.meme_altar_1"));
		tooltip.add(I18n.format("tooltip.community_mod.meme_altar_2"));
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return COLLISION;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return COLLISION;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
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
	public int getLightOpacity(IBlockState state) {
		return 0;
	}
}
