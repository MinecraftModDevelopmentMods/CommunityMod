package com.mcmoddev.communitymod.tightpants;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(name = "Tight Pants", attribution = "Darkhax", description = "Lets you can wear the tightest of pants!")
@EventBusSubscriber(modid = CommunityGlobals.MOD_ID)
public class SubModTightPants implements ISubMod {
	
	private static Item tightPants = new ItemTightPants();
	private static ResourceLocation pantsId = new ResourceLocation(CommunityGlobals.MOD_ID, "tight_pants");
	
	private static float spawnChance = 0.005f;
	
    @Override
    public void setupConfiguration(Configuration config, String categoryId) {
		
    	spawnChance = config.getFloat("spawnChance", categoryId, 0.005f, 0f, 1f, "The chance of a mob spawning with tight pants.");
	}
    
	@Override
    public void registerItems (IForgeRegistry<Item> reg) {
        
		tightPants.setRegistryName(pantsId);
		reg.register(tightPants);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels (ModelRegistryEvent event) {
        
    	ModelLoader.setCustomModelResourceLocation(tightPants, 0, new ModelResourceLocation(pantsId, "inventory"));
    }
    
    @SubscribeEvent
    public static void onSpecialSpawn (SpecialSpawn event) {
    	
    	EntityLivingBase entity = event.getEntityLiving();
    	if (Math.random() < (spawnChance * event.getWorld().getDifficulty().getId()) && (entity instanceof EntityZombie || entity instanceof EntitySkeleton)) {
    		
    		entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(tightPants));
    	}
    }
}