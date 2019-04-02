package com.mcmoddev.communitymod.musksrockets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public class Area {

	public Integer startX = null;
	public Integer startY = null;
	public Integer startZ = null;

	public Integer endX = null;
	public Integer endY = null;
	public Integer endZ = null;
	
	public Area() {
	}

	public Area(BlockPos start, BlockPos end) {
		startX = start.getX();
		startY = start.getY();
		startZ = start.getZ();
		endX = end.getX();
		endY = end.getY();
		endZ = end.getZ();
	}

	public Area(List<BlockPos> blocks) {
		for (BlockPos bp : blocks) {
			if (startX == null || bp.getX() < startX) {
				startX = bp.getX();
			}
			if (endX == null || bp.getX() > endX) {
				endX = bp.getX();
			}

			if (startY == null || bp.getY() < startY) {
				startY = bp.getY();
			}
			if (endY == null || bp.getY() > endY) {
				endY = bp.getY();
			}

			if (startZ == null || bp.getZ() < startZ) {
				startZ = bp.getZ();
			}
			if (endZ == null || bp.getZ() > endZ) {
				endZ = bp.getZ();
			}
		}
	}

	/**
	 * @param char
	 *            c: X Y or Z to return the correct side length.
	 */
	public int getSideLength(char c) {
		switch (c) {
		case 'X':
			return endX - startX;
		case 'Y':
			return endY - startY;
		case 'Z':
			return endZ - startZ;
		default:
			return 0;
		}
	}

	/** Scan an area for stuff */
	public List<BlockPos> scanArea(World world) {
		List<BlockPos> bpl = new ArrayList<BlockPos>();
		Iterable<MutableBlockPos> set = BlockPos.getAllInBoxMutable(startX, startY, startZ, endX, endY, endZ);
		Iterator<MutableBlockPos> in = set.iterator();
		while (in.hasNext()) {
			MutableBlockPos mbp = in.next();
			if (!world.isAirBlock(mbp)) {
				bpl.add(mbp.toImmutable());
			}
		}
		return bpl;
	}

	public int[] serialize() {
		int[] array = new int[6];
		if (startX != null) {
			array[0] = startX;
			array[1] = startY;
			array[2] = startZ;

			array[3] = endX;
			array[4] = endY;
			array[5] = endZ;
		}
		return array;
	}

	public void deserialize(int[] array) {
		startX = array[0];
		startY = array[1];
		startZ = array[2];

		endX = array[3];
		endY = array[4];
		endZ = array[5];
	}
	
	public void deserialize(Integer[] array) {
		startX = array[0];
		startY = array[1];
		startZ = array[2];

		endX = array[3];
		endY = array[4];
		endZ = array[5];
	}

	public Area update(List<BlockPos> blocks) {
		for (BlockPos bp : blocks) {
			if (startX == null || bp.getX() < startX) {
				startX = bp.getX();
			}
			if (endX == null || bp.getX() > endX) {
				endX = bp.getX();
			}

			if (startY == null || bp.getY() < startY) {
				startY = bp.getY();
			}
			if (endY == null || bp.getY() > endY) {
				endY = bp.getY();
			}

			if (startZ == null || bp.getZ() < startZ) {
				startZ = bp.getZ();
			}
			if (endZ == null || bp.getZ() > endZ) {
				endZ = bp.getZ();
			}
		}
		return this;
	}

	public void expandToFit(BlockPos bp) {
		if (startX == null || bp.getX() < startX) {
			startX = bp.getX();
		}
		if (endX == null || bp.getX() > endX) {
			endX = bp.getX();
		}

		if (startY == null || bp.getY() < startY) {
			startY = bp.getY();
		}
		if (endY == null || bp.getY() > endY) {
			endY = bp.getY();
		}

		if (startZ == null || bp.getZ() < startZ) {
			startZ = bp.getZ();
		}
		if (endZ == null || bp.getZ() > endZ) {
			endZ = bp.getZ();
		}
	}

	public boolean isPointInArea(BlockPos bp) {
		if (bp.getX() >= startX && bp.getX() <= endX && bp.getY() >= startY && bp.getY() <= endY && bp.getZ() >= startZ
				&& bp.getZ() <= endZ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPointInOrAdjToArea(BlockPos bp) {
		if ((bp.getX() >= startX || bp.getX() - 1 >= startX) && (bp.getX() <= endX || bp.getX() + 1 <= endX)
				&& (bp.getY() >= startY || bp.getY() - 1 >= startY) && (bp.getY() <= endY || bp.getY() + 1 <= endY)
				&& (bp.getZ() >= startZ || bp.getZ() - 1 >= startZ) && (bp.getZ() + 1 <= endZ)) {
			return true;
		} else {
			return false;
		}
	}

	public void transformAreaAndContents(BlockPos newbp, World oldWorld, World newWorld, BlockPos core) {
		if (!newWorld.isRemote && !oldWorld.isRemote) {

			// TODO Transfer entities as well?
			List<BlockPos> temp = new ArrayList<BlockPos>();

			List<BlockPos> blockpossold = scanArea(oldWorld);
			BlockPos relMod = new BlockPos(startX, startY, startZ).subtract(newbp);
			for (BlockPos pos : blockpossold) {
				BlockPos spos = pos.subtract(relMod);
				newWorld.setBlockState(spos, oldWorld.getBlockState(pos), 3);

				if (oldWorld.getTileEntity(pos) != null) {
					NBTTagCompound nbt = oldWorld.getTileEntity(pos).writeToNBT(new NBTTagCompound());

					nbt.setInteger("x", spos.getX());
					nbt.setInteger("y", spos.getY());
					nbt.setInteger("z", spos.getZ());

					newWorld.getTileEntity(spos).readFromNBT(nbt);
				}

				temp.add(pos);
			}

			for (BlockPos pos : temp) {
				if (oldWorld.getBlockState(pos).getBlock() == Blocks.PISTON_HEAD) {
					EnumFacing enumfacing = ((EnumFacing) oldWorld.getBlockState(pos)
							.getValue(PropertyDirection.create("facing"))).getOpposite();
					pos = pos.offset(enumfacing);
					oldWorld.setBlockState(pos, Blocks.STONE.getDefaultState(), 3);
				}
				oldWorld.removeTileEntity(pos);
				oldWorld.setBlockState(pos, Blocks.STONE.getDefaultState(), 3);
			}

			for (BlockPos pos : temp) {
				oldWorld.setBlockToAir(pos);
			}

			endX -= startX;
			endY -= startY;
			endZ -= startZ;

			startX = newbp.getX();
			startY = newbp.getZ();
			startZ = newbp.getY();

			endX += startX;
			endY += startY;
			endZ += startZ;

		}
	}

	public void loadShipIntoStorage(World world, BaseVehicleEntity entity) {
		for (BlockPos bp : scanArea(world)) {
			BlockStorage bs = new BlockStorage(world.getBlockState(bp), world.getTileEntity(bp), world.getLight(bp));
			entity.getStorage().blockMap.put(bp, bs);
		}
	}

	public List<BlockPos> returnDefiningEdges() {
		List<BlockPos> edges = new ArrayList<BlockPos>();
		edges.add(new BlockPos(startX, startY, startZ));
		edges.add(new BlockPos(endX, endY, endZ));
		return edges;
	}
	
	public void dropBottom(BlockPos pos, World world) {
		Area a = new Area(returnDefiningEdges());
		startY = pos.getY();
		a.endY = pos.getY();
		List<BlockPos> li = a.scanArea(world);
		for(BlockPos bp:li) {
			world.setBlockToAir(bp);
		}
	}

	@Override
	public String toString() {
		return "Area: " + String.valueOf(startX) + ", " + String.valueOf(startY) + ", " + String.valueOf(startZ) + ", "
				+ String.valueOf(endX) + ", " + String.valueOf(endY) + ", " + String.valueOf(endZ);
	}

}
