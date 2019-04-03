package com.mcmoddev.communitymod.lemons.fatsheep;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.lemons.fatsheep.model.RenderOvergrownSheep;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Random;

@SubMod(modid = "fatsheep", name = "Fat Sheep", description = "Fixes sheep not getting fat", attribution = "Lemons")
public class FatSheep implements ISubMod
{

	@Override
	@SideOnly(Side.CLIENT)
	public void onPreInit (FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityOvergrownSheep.class, RenderOvergrownSheep::new);
	}

	@Override
	public void registerEntities(IForgeRegistry<EntityEntry> reg)
	{
		reg.register(EntityEntryBuilder.create().name("Overgrown Sheep").entity(EntityOvergrownSheep.class).id(new ResourceLocation(CommunityGlobals.MOD_ID, "overgrown_sheep"), CommunityGlobals.entity_id++).tracker(80, 3, true).build());
	}

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event)
	{
		Entity target = event.getTarget();
		boolean isRegularSheep = target.getClass() == EntitySheep.class;    //If we should handle the entity as a regular sheep
		World world = target.world;
		ItemStack interactStack = event.getItemStack();

		if(interactStack.isEmpty() || !(interactStack.getItem() instanceof ItemFood)) return;

		if(target instanceof EntitySheep)   //Only deal with sheep
		{
			EntitySheep sheep = (EntitySheep) target;
			boolean canGrow;

			if(!world.isRemote)
			{
				if(isRegularSheep) //If it's a regular sheep, turn the sheep into an overgrown sheep
				{
					//Copy sheep as overgrown sheep
					EntityOvergrownSheep overgrownSheep = new EntityOvergrownSheep(world);
					overgrownSheep.setLocationAndAngles(sheep.posX, sheep.posY, sheep.posZ, sheep.rotationYaw, sheep.rotationPitch);
					overgrownSheep.setFleeceColor(sheep.getFleeceColor());
					overgrownSheep.rotationYaw = sheep.rotationYaw;
					overgrownSheep.rotationPitch = sheep.rotationPitch;
					overgrownSheep.rotationYawHead = sheep.rotationYawHead;
					overgrownSheep.prevRotationPitch = sheep.prevRotationPitch;
					overgrownSheep.prevRotationYaw = sheep.prevRotationYaw;
					overgrownSheep.prevRotationYawHead = sheep.prevRotationYawHead;
					overgrownSheep.setCustomNameTag(sheep.getCustomNameTag());

					world.spawnEntity(overgrownSheep);
					sheep.setDead();
					sheep = overgrownSheep;

				}
				//If the sheep can grow
				canGrow = ((EntityOvergrownSheep) sheep).getGrowth() < EntityOvergrownSheep.MAX_GROWTH;

				if(canGrow) //Grow the sheep
				{
					interactStack.shrink(1);
					sheep.setSheared(false);
				}

			}else    //if we're on the client
			{
				//If we're a regular sheep or the sheep can grow, spawn particles
				if(isRegularSheep || ((EntityOvergrownSheep) sheep).getGrowth() < EntityOvergrownSheep.MAX_GROWTH)
				{
					Random rand = world.rand;
					double posX = sheep.posX;
					double posY = sheep.posY;
					double posZ = sheep.posZ;
					double width = sheep.width * 2F;
					double height = sheep.height * 2F;

					for(int i = 0; i < 5; i++)
						world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height - 0.25D, posZ + (rand.nextDouble() - 0.5D) * width, (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);

				}
			}
		}
	}

}
