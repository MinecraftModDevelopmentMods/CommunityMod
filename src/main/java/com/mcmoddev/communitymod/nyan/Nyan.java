package com.mcmoddev.communitymod.nyan;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.commoble.explodingchickens.EntityExplodingChicken;
import com.mcmoddev.communitymod.lemons.fatsheep.EntityOvergrownSheep;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

@SubMod(
		modid = "nyan",
		name = "Nyan",
		description = "Fixes the frankly unacceptable lack of nyan entities in Minecraft",
		attribution = "TheRandomLabs"
)
public class Nyan implements ISubMod {
	public static final String HAS_NYAN_ENTITY_KEY = "HasNyanEntity";
	public static final String NYAN_ENTITY_UUID_KEY = "NyanEntityUUID";
	public static final String NYANED_ENTITY_UUID_KEY = "NyanedEntityUUID";

	private static final Random random = new Random();

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		final Entity entity = event.getEntity();
		final World world = entity.getEntityWorld();

		if(world.isRemote) {
			return;
		}

		final MinecraftServer server = world.getMinecraftServer();
		final NBTTagCompound data = entity.getEntityData();

		if(data.hasUniqueId(NYANED_ENTITY_UUID_KEY)) {
			final Entity nyanedEntity =
					server.getEntityFromUuid(data.getUniqueId(NYANED_ENTITY_UUID_KEY));

			if(nyanedEntity == null || nyanedEntity.isDead) {
				world.removeEntity(entity);
				return;
			}
		}

		if(data.getBoolean(HAS_NYAN_ENTITY_KEY)) {
			updateNyanEntity(server, (WorldServer) world, entity, data);
		} else if(!data.hasKey(HAS_NYAN_ENTITY_KEY)) {
			if(entity instanceof EntityPlayer || random.nextInt(10) == 0) {
				data.setBoolean(HAS_NYAN_ENTITY_KEY, true);
				updateNyanEntity(server, (WorldServer) world, entity, data);
			} else {
				data.setBoolean(HAS_NYAN_ENTITY_KEY, false);
			}
		}
	}

	private static void updateNyanEntity(MinecraftServer server, WorldServer world, Entity entity,
			NBTTagCompound data) {
		Entity nyanEntity = null;

		if(data.hasUniqueId(NYAN_ENTITY_UUID_KEY)) {
			nyanEntity = server.getEntityFromUuid(data.getUniqueId(NYAN_ENTITY_UUID_KEY));
		}

		boolean shouldSpawn = false;

		if(nyanEntity == null || nyanEntity.isDead) {
			nyanEntity = newNyanEntity(world);
			nyanEntity.getEntityData().setUniqueId(NYANED_ENTITY_UUID_KEY, entity.getUniqueID());
			data.setUniqueId(NYAN_ENTITY_UUID_KEY, nyanEntity.getUniqueID());
			shouldSpawn = true;
		}

		final BlockPos entityPos = entity.getPosition();
		double entityHeightMultiplier = world.getTotalWorldTime() % 2 == 0 ? 3.5 : 4.0;

		//Account for dab particles
		if(entity instanceof EntityPlayer) {
			entityHeightMultiplier += 2.0;
		}

		//I *could* disable the AI, but this is more fun
		nyanEntity.setPositionAndRotation(
				entityPos.getX(),
				entityPos.getY() + entity.height * entityHeightMultiplier + nyanEntity.height,
				entityPos.getZ(),
				entity.rotationYaw,
				entity.rotationPitch
		);
		nyanEntity.setRotationYawHead(entity.getRotationYawHead());
		nyanEntity.setEntityInvulnerable(true);

		if(shouldSpawn) {
			world.spawnEntity(nyanEntity);
		}

		final BlockPos nyanPos = nyanEntity.getPosition();
		final NyanDirection direction =
				NyanDirection.getDirectionFacing(nyanEntity, Rotation.CLOCKWISE_90);

		world.spawnParticle(
				//Wool
				EnumParticleTypes.REDSTONE,
				//Not long distance
				false,
				nyanPos.getX() + direction.getXDirection() * 12.0,
				nyanPos.getY() - 2.5,
				nyanPos.getZ() + direction.getZDirection() * 12.0,
				//Number of particles
				10,
				direction.getXDirection() * 5.0,
				0.0,
				direction.getZDirection() * 5.0,
				//Speed
				10.0
		);
	}

	private static Entity newNyanEntity(World world) {
		final int nextInt = random.nextInt(100);

		if(nextInt < 25) {
			return new EntityCow(world);
		}

		if(nextInt < 50) {
			return new EntityOvergrownSheep(world);
		}

		if(nextInt < 75) {
			return new EntityExplodingChicken(world);
		}

		if(nextInt < 95) {
			return new EntityOcelot(world);
		}

		if(nextInt < 99) {
			return new EntityGiantZombie(world);
		}

		return new EntityWither(world);
	}
}
