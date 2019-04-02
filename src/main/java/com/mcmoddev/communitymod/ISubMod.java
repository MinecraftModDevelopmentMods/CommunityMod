package com.mcmoddev.communitymod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * This interface is used to back the skeleton of the sub mod system. Your submod must
 * implement this class to be loaded properly.
 */
public interface ISubMod {
    
    default void onConstruction (FMLConstructionEvent event) {
        
    }
    
    default void onPreInit (FMLPreInitializationEvent event) {
        
    }
    
    default void onInit (FMLInitializationEvent event) {
        
    }
    
    default void onPostInit (FMLPostInitializationEvent event) {
        
    }
    
    default void onLoadComplete (FMLLoadCompleteEvent event) {
        
    }
    
    default void onServerStarting (FMLServerStartingEvent event) {
        
    }
    
    default void onServerStopped (FMLServerStoppedEvent event) {
        
    }
    
    default void registerBlocks (IForgeRegistry<Block> reg) {
        
    }

    default void registerItems (IForgeRegistry<Item> reg) {

    }

    default void registerEntities (IForgeRegistry<EntityEntry> reg) {

    }
    
    @SideOnly(Side.CLIENT)
    default void registerModels (ModelRegistryEvent event) {
        
    }

	default boolean enabledByDefault() {
		
		return true;
	}

	default void setupConfiguration(Configuration config, String categoryId) {
		
	}
}
