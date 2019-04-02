package com.mcmoddev.communitymod.commoble.intradimensional_portals;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * The primary portal blocks in the centers of portals
 */
public class BlockPortalBase extends BlockContainer
{
    public BlockPortalBase()
    {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
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


    /**
     * Checks if this block can be placed exactly at the given position.
     */
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
    	// can place here if this and the two y-adjacent blocks are replaceable
    	// first, make sure it's not too close to the y-boundaries of the world
    	if (pos.getY() <= 6 || pos.getY() >= worldIn.getActualHeight() - 7)
    	{
    		return false;
    	}
    	for (int i=-1; i<=1; i++)
    	{
    		if (!worldIn.getBlockState(pos.up(i)).getBlock().isReplaceable(worldIn, pos.up(i)))
    		{
    			return false;
    		}
    	}
    	return super.canPlaceBlockAt(worldIn, pos);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
    	super.onBlockAdded(worldIn, pos, state);
    	// set the two y-adjacent blocks to glowy air
		worldIn.setBlockState(pos.up(), SubmodIntradimensionalPortals.intradimensional_portal_glowy_air.getDefaultState());
		worldIn.setBlockState(pos.down(), SubmodIntradimensionalPortals.intradimensional_portal_glowy_air.getDefaultState());
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     * when broken, break the two adjacent portal blocks
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
    	if (worldIn.getBlockState(pos.up()).getBlock() == SubmodIntradimensionalPortals.intradimensional_portal_glowy_air)
    	{
    		worldIn.setBlockToAir(pos.up());
    	}
    	if (worldIn.getBlockState(pos.down()).getBlock() == SubmodIntradimensionalPortals.intradimensional_portal_glowy_air)
    	{
    		worldIn.setBlockToAir(pos.down());
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
            EntityPlayerMP entityplayermp = (EntityPlayerMP)playerIn;

            if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == world && !entityplayermp.isPlayerSleeping())
            {
                
	            if (playerIn.isRiding())
	            {
	            	playerIn.dismountRidingEntity();
	            }

	            playerIn.setPositionAndUpdate(world.rand.nextDouble()*2000000D-1000000D, 2550D, world.rand.nextDouble()*2000000D-1000000D);
	            
            }
            return true;
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

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		// TODO Auto-generated method stub
		return new TileEntityPortalBase();
	}
}