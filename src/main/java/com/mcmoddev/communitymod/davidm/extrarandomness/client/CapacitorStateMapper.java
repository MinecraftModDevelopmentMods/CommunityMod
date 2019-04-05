package com.mcmoddev.communitymod.davidm.extrarandomness.client;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.davidm.extrarandomness.common.block.BlockMemeCapacitor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class CapacitorStateMapper extends StateMapperBase {

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		BlockMemeCapacitor capacitor = (BlockMemeCapacitor) state.getBlock();
		String tier = String.valueOf(capacitor.getEnumCapacitor().ordinal());
		return new ModelResourceLocation(CommunityGlobals.MOD_ID + "meme_capacitor", "tier=" + tier);
	}
}
