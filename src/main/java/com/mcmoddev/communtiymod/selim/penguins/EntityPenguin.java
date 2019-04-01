// Distributed under MIT, originally from https://github.com/Selim042/SM-Penguins

package com.mcmoddev.communtiymod.selim.penguins;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityPenguin extends EntityAnimal {

	public EntityPenguin(World world) {
		super(world);
		this.setSize(0.6f, 1.25f);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return super.getMaxSpawnedInChunk();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
				.setBaseValue(0.15000000298023224D);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() instanceof ItemFishFood;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0,
				new EntityAIAvoidEntity<EntityPolarBear>(this, EntityPolarBear.class, 16, 0.5D, 1.5D));
		this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
		this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.FISH, false));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(6, new EntityAIMate(this, 0.5D));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityPenguin(ageable.world);
	}

}