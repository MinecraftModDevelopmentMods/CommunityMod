package com.mcmoddev.communitymod.musksrockets;

import com.mcmoddev.communitymod.CommunityGlobals;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShipCore extends Block implements ITileEntityProvider {

	public ShipCore() {
		super(Material.IRON);
		setRegistryName("shipcore");
        setTranslationKey(CommunityGlobals.MOD_ID + ".shipcore");
		setCreativeTab(CommunityGlobals.TAB);
	}
	
	public static final PropertyBool SHIPASSEMBLED = PropertyBool.create("shipassembled");

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
	return new ShipCoreTE();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
	    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
	ShipCoreTE ste = (ShipCoreTE) worldIn.getTileEntity(pos);
	if (!worldIn.isRemote && ste.toConstruct.isEmpty()) {
	    ste.startSearch(playerIn);
	}else if(!worldIn.isRemote) {
	    ste.construct();
	}
	return true;
    }
    
    @Deprecated
    @Override
    public boolean isFullCube(IBlockState state) {
	return false;
    }

}
