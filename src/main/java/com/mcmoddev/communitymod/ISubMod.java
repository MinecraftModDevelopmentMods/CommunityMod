package com.mcmoddev.communitymod;

import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

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
}