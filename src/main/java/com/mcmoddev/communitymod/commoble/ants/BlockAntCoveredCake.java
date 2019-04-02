package com.mcmoddev.communitymod.commoble.ants;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAntCoveredCake extends Block
{
	protected static final AxisAlignedBB CAKE_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D);

    protected BlockAntCoveredCake()
    {
        super(Material.CAKE);
        this.setDefaultState(this.blockState.getBaseState());
        this.setTickRandomly(true);
    }

    /**
     * @deprecated call via {@link IBlockState#getBoundingBox(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CAKE_AABB;
    }

    /**
     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     * @deprecated call via {@link IBlockState#isOpaqueCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    /**
     * Checks if this block can be placed exactly at the given position.
     */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canBlockStay(worldIn, pos))
        {
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.CAKE);
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
     * <p>
     * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that
     * does not fit the other descriptions and will generally cause other things not to connect to the face.
     * 
     * @return an approximation of the form of the given face
     * @deprecated call via {@link IBlockState#getBlockFaceShape(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    
    
    /**
     * Called when the block is right clicked by a player.
     */
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
        	playerIn.sendStatusMessage(new TextComponentTranslation("tile.antcake.full_of_ants", new Object[0]), true);
        }
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand)
    {
		spawnAntParticleOnBlock(world, pos);
    }
    
    public static void spawnAntParticleOnBlock(World world, BlockPos pos)
	{
        //EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
		List<EnumFacing> faces = getAntableSides(world, pos);
		for (EnumFacing face : faces)
		{
			AxisAlignedBB aabb = world.getBlockState(pos).getCollisionBoundingBox(world, pos);
			if (aabb != null)
			{
				Vec3d offset = getParticleSpawnOffsetForFace(aabb, face, world.rand);
				Vec3d init_vel = getParticleInitVelocityForFace(face, world.rand, 0.01D + world.rand.nextDouble() * 0.04);
				SubmodAnts.proxy.spawnAntParticle(world, pos.getX() + offset.x, pos.getY() + offset.y, pos.getZ() + offset.z, init_vel.x, init_vel.y, init_vel.z, 0.5F, face);
			}
		}
	}
    
    /**
	 * Get a list of sides of a given block that ant particles can spawn on.
	 * Ant particles can spawn on a face if any of the following are true:
	 * -- the adjacent block returns false for doesSideBlockRendering (most blocks just call isOpaqueCube for this)
	 * -- the given block's collision AABB does not fully extend to that side
	 * @param world The world
	 * @param pos The blockpos of the relevant block
	 * @return A List of EnumFacings that ant particles can spawn on
	 */
	public static List<EnumFacing> getAntableSides(World world, BlockPos pos)
	{
		List<EnumFacing> out = new LinkedList<EnumFacing>();
		AxisAlignedBB aabb = world.getBlockState(pos).getCollisionBoundingBox(world, pos);
		
		for (EnumFacing face : EnumFacing.VALUES)
		{
			if (!world.getBlockState(pos.offset(face)).doesSideBlockRendering(world, pos, face.getOpposite())
				|| !doesAABBExtendToFace(aabb, face))
			{
				out.add(face);
			}
		}
		
		return out;
	}
	
	/**
	 * Checks whether a block's bounding box has a minimum coordinate of 0 or a maximum of 1 for the respective axis
	 * @param aabb A block's bounding box (i.e. a full cube AABB has minimum values of 0 and maximum values of 1)
	 * @param face The EnumFacing being compared to the AABB
	 * @return TRUE if a block's bounding box fully extends to a facing, FALSE otherwise
	 */
	public static boolean doesAABBExtendToFace(AxisAlignedBB aabb, EnumFacing face)
	{
		if (aabb == null)
		{
			return false;
		}
		switch(face)
		{
			case DOWN:
				return aabb.minY == 0D;
			case UP:
				return aabb.maxY == 1D;
			case NORTH:
				return aabb.minZ == 0D;
			case SOUTH:
				return aabb.maxZ == 1D;
			case WEST:
				return aabb.minX == 0D;
			case EAST:
				return aabb.maxX == 1D;
			default:
				return false;	// AABB doesn't extend in higher dimensions
		}
	}
	
	/**
	 * Returns a random spawn position relative to a block based on its aabb and a given face
	 * @param aabb The collision AABB of a block
	 * @param face The face of the AABB on which to spawn a particle
	 * @param rand An RNG (generally the world's rand)
	 * @return A Vec3D corresponding to a position on the given face of the surface of the given AABB
	 */
	public static Vec3d getParticleSpawnOffsetForFace(AxisAlignedBB aabb, EnumFacing face, Random rand)
	{
		Vec3d out;
		switch(face)
		{
			case DOWN:
				out = new Vec3d(
						aabb.minX + rand.nextDouble()*(aabb.maxX - aabb.minX),
						aabb.minY,
						aabb.minZ + rand.nextDouble()*(aabb.maxZ - aabb.minZ));
				break;
			case UP:
				out = new Vec3d(
						aabb.minX + rand.nextDouble()*(aabb.maxX - aabb.minX),
						aabb.maxY,
						aabb.minZ + rand.nextDouble()*(aabb.maxZ - aabb.minZ));
				break;
			case NORTH:
				out = new Vec3d(
						aabb.minX + rand.nextDouble()*(aabb.maxX - aabb.minX),
						aabb.minY + rand.nextDouble()*(aabb.maxY - aabb.minY),
						aabb.minZ);
				break;
			case SOUTH:
				out = new Vec3d(
						aabb.minX + rand.nextDouble()*(aabb.maxX - aabb.minX),
						aabb.minY + rand.nextDouble()*(aabb.maxY - aabb.minY),
						aabb.maxZ);
				break;
			case WEST:
				out = new Vec3d(
						aabb.minX,
						aabb.minY + rand.nextDouble()*(aabb.maxY - aabb.minY),
						aabb.minZ + rand.nextDouble()*(aabb.maxZ - aabb.minZ));
				break;
			case EAST:
				out = new Vec3d(
						aabb.maxX,
						aabb.minY + rand.nextDouble()*(aabb.maxY - aabb.minY),
						aabb.minZ + rand.nextDouble()*(aabb.maxZ - aabb.minZ));
				break;
			default:
				out = Vec3d.ZERO;
				break;
		}
		
		out = out.add(0.05*face.getXOffset(), 0.05*face.getYOffset(), 0.05*face.getZOffset());
		return out;
	}
	
	/**
	 * Returns a velocity vector for an ant particle that will spawn on the given face
	 * @param face The face of the block that the particle spawns on
	 * @param rand An RNG, generally the world's rand
	 * @param vScale The factor to scale the velocity by (if vScale = 1D, velocity values will be between -0.5D and 0.5D)
	 * @return A vec3D containing the (x,y,z) initial velocities of an ant particle
	 */
	public static Vec3d getParticleInitVelocityForFace(EnumFacing face, Random rand, double vScale)
	{
		Vec3i faceVec = face.getDirectionVec();
		return new Vec3d(
				faceVec.getX() != 0 ? 0D : (rand.nextDouble() - 0.5D) * vScale,
				faceVec.getY() != 0 ? 0D : (rand.nextDouble() - 0.5D) * vScale,
				faceVec.getZ() != 0 ? 0D : (rand.nextDouble() - 0.5D) * vScale);
	}
}
