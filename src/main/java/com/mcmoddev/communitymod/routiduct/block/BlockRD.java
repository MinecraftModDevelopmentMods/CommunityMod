package com.mcmoddev.communitymod.routiduct.block;

import com.mcmoddev.communitymod.CommunityMod;
import com.mcmoddev.communitymod.routiduct.RoutiductConstants;
import com.mcmoddev.communitymod.routiduct.api.IProtocolProvider;
import com.mcmoddev.communitymod.routiduct.block.tiles.TileRoutiduct;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.GuiBlueprint;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockRD extends Block implements IProtocolProvider {

    public BlockRD(String name) {
        super(Material.IRON);
        this.setRegistryName(RoutiductConstants.MOD_ID, name);
        this.setTranslationKey(RoutiductConstants.MOD_ID + "." + name);
    }

    /**
     * Method doubles as a hasGui()
     *
     * @return the block's GuiBlueprint, if block returns null, it does not have a gui.
     */
    public GuiBlueprint getGuiBlueprint(TileRoutiduct tile) {
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) != null && getGuiBlueprint((TileRoutiduct) worldIn.getTileEntity(pos)) != null) {
            playerIn.openGui(CommunityMod.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        if (hasTileEntity(state)) {
            return new TileRoutiduct();
        }
        return super.createTileEntity(world, state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack,
                               @Nullable
                                       World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(getProtocol().name + " Protocol");
    }

}
