package com.mcmoddev.communitymod.shootingstar;

import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import com.mcmoddev.communitymod.shootingstar.model.ModelMethods;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class ShootingStar {

    protected static List<ModelCompound> modelList = new ArrayList<>();
    protected static List<BlockCompound> blockList = new ArrayList<>();
    protected static List<ItemCompound> itemList = new ArrayList<>();

    public static void registerModel(ModelCompound modelCompound) {
        modelList.add(modelCompound);
    }

    public static void registerModels(String modid) {
        for (ModelCompound compound : modelList) {
            if (compound.getModid().equals(modid)) {
                if (compound.isBlock()) {
                    if (compound.getFileName().equals("shootingstar.undefinedfilename")) {
                        //Checks if block has an item
                        if (Item.getItemFromBlock(compound.getBlock()) != Items.AIR) {
                            ModelMethods.registerItemModel(compound.getItem(), compound.getMeta(), compound.getBlockStatePath(), compound.getInventoryVariant());
                        }
                        if (shouldDoCustomVariant(compound)) {
                            ModelMethods.setBlockStateMapper(compound.getBlock(), compound.getBlockStatePath(), compound.getVariant());
                        }
                        else {
                            ModelMethods.setBlockStateMapper(compound.getBlock(), compound.getBlockStatePath(), compound.getIgnoreProperties());
                        }
                    }
                    else {
                        //Checks if block has an item
                        if (Item.getItemFromBlock(compound.getBlock()) != Items.AIR) {
                            ModelMethods.registerItemModel(compound.getItem(), compound.getMeta(), compound.getFileName(), compound.getBlockStatePath(), compound.getInventoryVariant());
                        }
                        if (shouldDoCustomVariant(compound)) {
                            ModelMethods.setBlockStateMapper(compound.getBlock(), compound.getFileName(), compound.getBlockStatePath(), compound.getVariant());
                        }
                        else {
                            ModelMethods.setBlockStateMapper(compound.getBlock(), compound.getFileName(), compound.getBlockStatePath(), compound.getIgnoreProperties());
                        }
                    }
                }
                if (compound.isItem()) {
                    if (compound.getFileName().equals("shootingstar.undefinedfilename")) {
                        ModelMethods.registerItemModel(compound.getItem(), compound.getMeta(), compound.getBlockStatePath(), compound.getInventoryVariant());
                    }
                    else {
                        ModelMethods.registerItemModel(compound.getItem(), compound.getMeta(), compound.getFileName(), compound.getBlockStatePath(), compound.getInventoryVariant());
                    }
                }
            }
        }

    }

    private static boolean shouldDoCustomVariant(ModelCompound compound) {
        if (!compound.getVariant().equals("shootingstar.undefinedvariant")) {
            return true;
        }
        else {
            return false;
        }
    }

    public static Block getBlock(String modid, String name) {
        for (BlockCompound compound : blockList) {
            if (compound.getRegistryName().equals(new ResourceLocation(modid, name))) {
                return compound.getBlock();
            }
        }
        throw new InvalidParameterException("Block '" + modid + ":" + name + "' cannot be found in the Shooting Star registry");
    }

    public static void registerBlock(BlockCompound blockCompound) {
        blockList.add(blockCompound);
    }

    public static void registerItem(ItemCompound itemCompound) {
        itemList.add(itemCompound);
    }

    public static void registerBlocks(String modid, RegistryEvent.Register<Block> event) {
        for (BlockCompound compound : blockList) {
            if (compound.getModid().equals(modid)) {
                event.getRegistry().register(compound.getBlock());
            }
        }
    }

    public static void registerItems(String modid, RegistryEvent.Register<Item> event) {
        for (BlockCompound compound : blockList) {
            if (compound.hasItemBlock() && compound.getModid().equals(modid)) {
                if (compound.getItem() == null) {
                    event.getRegistry().register(new ItemBlock(compound.getBlock()).setRegistryName(compound.getBlock().getRegistryName()));
                }
                else {
                    event.getRegistry().register(compound.getItem());
                }
            }
        }
        for (ItemCompound compound : itemList) {
            if (compound.getModid().equals(modid)) {
                event.getRegistry().register(compound.getItem());
            }
        }
    }

}
