package com.mcmoddev.communitymod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = CommunityGlobals.MOD_ID, name = "Community Mod", version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class CommunityMod {
    
    public static final Logger LOGGER = LogManager.getLogger("Community Mod");
    private final List<ISubMod> subMods = new ArrayList<>();
    
    @Mod.Instance(CommunityGlobals.MOD_ID)
    public CommunityMod INSTANCE;
    
    @EventHandler
    public void onConstruction (FMLConstructionEvent event) {
        
        final long startTime = System.currentTimeMillis();
        final Set<ASMData> datas = event.getASMHarvestedData().getAll(SubMod.class.getCanonicalName());
        
        for (final ASMData data : datas) {
            
            final String modName = (String) data.getAnnotationInfo().get("name");
            
            try {
                
                if (!data.getAnnotationInfo().containsKey("clientSideOnly") || (boolean) data.getAnnotationInfo().get("clientSideOnly") && event.getSide() == Side.CLIENT) {
                    
                    final ISubMod subMod = (ISubMod) Class.forName(data.getClassName()).newInstance();
                    this.subMods.add(subMod);
                    LOGGER.info("Loaded submod {} from class {}.", modName, data.getClassName());
                }
            }
            
            catch (final Exception e) {
                
                LOGGER.error("Failed to load submod {}.", modName);
                LOGGER.catching(e);
            }
        }
        
        LOGGER.info("Loaded {} submods in {}ms.", this.subMods.size(), System.currentTimeMillis() - startTime);
        for (final ISubMod subMod : this.subMods) {
            
            subMod.onConstruction(event);
        }
    }
    
    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {
    
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        
        for (final ISubMod subMod : this.subMods) {
            
            subMod.onPreInit(event);
        }
    }
    
    @EventHandler
    public void onInit (FMLInitializationEvent event) {
        
        for (final ISubMod subMod : this.subMods) {
            
            subMod.onInit(event);
        }
    }
    
    @EventHandler
    public void onPostInit (FMLPostInitializationEvent event) {
        
        for (final ISubMod subMod : this.subMods) {
            
            subMod.onPostInit(event);
        }
    }
    
    @EventHandler
    public void onLoadComplete (FMLLoadCompleteEvent event) {
        
        for (final ISubMod subMod : this.subMods) {
            
            subMod.onLoadComplete(event);
        }
    }
    
    @EventHandler
    public void onServerStarting (FMLServerStartingEvent event) {
        
        for (final ISubMod subMod : this.subMods) {
            
            subMod.onServerStarting(event);
        }
    }
    
    @EventHandler
    public void onServerStopped (FMLServerStoppedEvent event) {
        
        for (final ISubMod subMod : this.subMods) {
            
            subMod.onServerStopped(event);
        }
    }
    
    @SubscribeEvent
    public void blocks (RegistryEvent.Register<Block> event) {
        
        IForgeRegistry<Block> reg = event.getRegistry();
    
        for (final ISubMod subMod : this.subMods) {
        
            subMod.registerBlocks(reg);
        }
    }
    
    @SubscribeEvent
    public void items (RegistryEvent.Register<Item> event) {
        
        IForgeRegistry<Item> reg = event.getRegistry();
    
        for (final ISubMod subMod : this.subMods) {
        
            subMod.registerItems(reg);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void models (ModelRegistryEvent event) {
        
        for (final ISubMod subMod : this.subMods) {
            
            subMod.registerModels(event);
        }
    }
}