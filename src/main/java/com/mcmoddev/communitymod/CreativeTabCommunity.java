package com.mcmoddev.communitymod;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.communitymod.quat.dabbbbb.Dabbbbb;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabCommunity extends CreativeTabs {

	private List<ResourceLocation> entityCache;

	public CreativeTabCommunity() {

		super(CommunityGlobals.MOD_ID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack createIcon() {

		return new ItemStack(Dabbbbb.dab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {

		super.displayAllRelevantItems(itemList);

		if (entityCache == null) {

			entityCache = new ArrayList<>();

			for (EntityEntry entityEntry : ForgeRegistries.ENTITIES.getValuesCollection()) {

				if (entityEntry.getRegistryName().getNamespace().equalsIgnoreCase(CommunityGlobals.MOD_ID)) {

					entityCache.add(entityEntry.getRegistryName());
				}
			}
		}

		for (final ResourceLocation id : entityCache) {

			final ItemStack spawner = new ItemStack(Items.SPAWN_EGG);
			ItemMonsterPlacer.applyEntityIdToItemStack(spawner, id);
			itemList.add(spawner);
		}

	}
}