package com.mcmoddev.communitymod.musksrockets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;

public class ShipStorage extends World implements IBlockAccess {

	public Map<BlockPos, BlockStorage> blockMap;
	private List<TileEntity> tesrs;
	private BlockPos offset;

	public BufferBuilder.State bufferstate;
	public boolean updateRequired = false;
	public World host;

	public ShipStorage(BlockPos bp, World hostWorld) {
		super(hostWorld.getSaveHandler(), hostWorld.getWorldInfo(), hostWorld.provider, hostWorld.profiler,
				hostWorld.isRemote);
		blockMap = new HashMap<BlockPos, BlockStorage>();
		offset = bp;
		tesrs = new ArrayList<TileEntity>();
		host = hostWorld;
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return blockMap.get(pos).tileentity;
	}

	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		int sky = getLightSet(EnumSkyBlock.SKY, pos);
		int map = getLightSet(EnumSkyBlock.BLOCK, pos);

		return sky << 20 | map << 4;
	}

	public int getLightSet(EnumSkyBlock type, BlockPos pos) {
		if (type == EnumSkyBlock.SKY) {
			return 15;
		} else if (blockMap.get(pos) != null) {
			return blockMap.get(pos).light;
		} else {
			return 0;
		}
	}

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		if (blockMap.containsKey(pos)) {
			return blockMap.get(pos.add(offset)).blockstate;
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		return blockMap.get(pos).blockstate.getMaterial() == Material.AIR;
	}

	@Override
	public Biome getBiome(BlockPos pos) {
		return Biome.getBiome(0);
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return 0;
	}

	@Override
	public WorldType getWorldType() {
		return WorldType.DEFAULT;
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		if (!blockMap.containsKey(pos))
			return _default;
		return blockMap.get(pos).blockstate.isSideSolid(this, pos, side);
	}

	public List<TileEntity> getTESRs() {
		return tesrs;
	}

	public void setTESRs() {
		tesrs.clear();
		for (BlockStorage bs : blockMap.values()) {
			if (bs.tileentity != null) {
				@SuppressWarnings("rawtypes")
				TileEntitySpecialRenderer renderer = TileEntityRendererDispatcher.instance.renderers
						.get(bs.tileentity.getClass());
				if (renderer != null)
					tesrs.add(bs.tileentity);
			}
		}
	}

	@Override
	protected IChunkProvider createChunkProvider() {
		return host.getChunkProvider();
	}

	@Override
	protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
		return true;
	}

}
