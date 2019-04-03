package com.mcmoddev.communitymod.davidm;

import java.util.List;
import java.util.Random;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class LexWand extends AltarItem {

	private static final AxisAlignedBB AOE = new AxisAlignedBB(0, 0, 0, 1, 1, 1).grow(4);
	
	private static final String[] BAN_MSG = {
			"because LEARN JAVA BEFORE MAKING A MOD!!!",
			"for using a CRACKED client!!!",
			"for being an INSUFFERABLE twat!!!",
			"for not reading the EAQ!!!",
			"for BAN EVASION!!!",
			"for being ANNOYING!!!",
			"because DO NOT insult staffs!!!",
			"because don't SPAM!!!",
			"for creating a CRACKED client!!!"
	};
	
	@Override
	public int getCooldown() {
		return 60;
	}

	@Override
	public void onAltarAction(World world, BlockPos pos) {
		List<Entity> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, AOE.offset(pos));
		System.out.println(entities.size());
		if (entities.isEmpty()) return;
			
		Entity entity = entities.get(world.rand.nextInt(entities.size()));
		
		world.getEntitiesWithinAABB(EntityPlayer.class, AOE.grow(6).offset(pos)).forEach((player) -> {
			player.sendMessage(new TextComponentString(getBanMsg(entity, world.rand)));
		});
		
		BlockPos entityPos = entity.getPosition();
		world.addWeatherEffect(new EntityLightningBolt(world, entityPos.getX(), entityPos.getY(), entityPos.getZ(), false));
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if (!player.world.isRemote) {
			BlockPos entityPos = target.getPosition();
			player.sendMessage(new TextComponentString(getBanMsg(target, player.world.rand)));
			target.world.addWeatherEffect(new EntityLightningBolt(target.world, entityPos.getX(), entityPos.getY(), entityPos.getZ(), false));
		}
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + I18n.format("tooltip.community_mod.lex_wand"));
		super.addInformation(stack, world, tooltip, flagIn);
	}
	
	private static String getBanMsg(Entity entity, Random rand) {
		String ban_header = "<Lex> BANNED " + TextFormatting.RED + entity.getName();
		String ban_reason =  TextFormatting.WHITE + " " + BAN_MSG[rand.nextInt(BAN_MSG.length)];
		return ban_header + ban_reason;
	}
}
