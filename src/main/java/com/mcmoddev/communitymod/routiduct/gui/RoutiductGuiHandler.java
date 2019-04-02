package com.mcmoddev.communitymod.routiduct.gui;

import com.mcmoddev.communitymod.routiduct.block.tiles.TileRoutiduct;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class RoutiductGuiHandler implements IGuiHandler {
	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile instanceof TileRoutiduct) {
			return ((TileRoutiduct) tile).getContainer(player);
		}
		return null;
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile instanceof TileRoutiduct) {
			return new GuiRoutiduct(((TileRoutiduct) tile).getGuiBlueprint(), player);
		}
		return null;
	}
}
