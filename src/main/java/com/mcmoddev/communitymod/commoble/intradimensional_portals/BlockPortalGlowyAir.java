package com.mcmoddev.communitymod.commoble.intradimensional_portals;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * The two blocks y-adjacent to the primary portal block
 */
public class BlockPortalGlowyAir extends Block
{
    protected BlockPortalGlowyAir()
    {
        super(Material.PORTAL);
        this.setSoundType(SoundType.GLASS);
        this.lightValue = 15;
        this.setTickRandomly(true);
        this.setBlockUnbreakable();
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.RED;
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
    	// check if portal base has been removed
		if (world.getBlockState(pos.up()).getBlock() != SubmodIntradimensionalPortals.intradimensional_portal_base
				&& world.getBlockState(pos.down()).getBlock() != SubmodIntradimensionalPortals.intradimensional_portal_base)
		{
			world.setBlockToAir(pos);
		}
    }

    /**
     * Called when the block is right clicked by a player.
     */
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || !(playerIn instanceof EntityPlayerMP))
        {
            return true;
        }
        else
        {
        	// redirect to adjacent portal base if it exists, otherwise do nothing
    		if (world.getBlockState(pos.up()).getBlock() == SubmodIntradimensionalPortals.intradimensional_portal_base)
    		{
    			return SubmodIntradimensionalPortals.intradimensional_portal_base.onBlockActivated(world, pos.up(), state, playerIn, hand, facing, hitX, hitY, hitZ);
    		}
    		else if (world.getBlockState(pos.down()).getBlock() == SubmodIntradimensionalPortals.intradimensional_portal_base)
    		{
    			return SubmodIntradimensionalPortals.intradimensional_portal_base.onBlockActivated(world, pos.down(), state, playerIn, hand, facing, hitX, hitY, hitZ);
    		}
    		else
    		{
    			return false;
    		}
        }
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
     * <p>
     * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that
     * does not fit the other descriptions and will generally cause other things not to connect to the face.
     * 
     * @return an approximation of the form of the given face
     */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
