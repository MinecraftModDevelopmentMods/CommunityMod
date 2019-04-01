package com.mcmoddev.communitymod.quat.foliage;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IRegistryDelegate;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

@SubMod(
	name = "foliage",
	description = "Adds foliage color variation!",
	attribution = "quaternary",
	clientSideOnly = true
)
@Mod.EventBusSubscriber(
	modid = CommunityGlobals.MOD_ID,
	value = Side.CLIENT
)
public class Foliage implements ISubMod {
	private static final NoiseGeneratorPerlin NOISE = new NoiseGeneratorPerlin(new Random(6969), 4);
	
	@SubscribeEvent
	public static void blockColors(ColorHandlerEvent.Block e) {
		BlockColors bc = e.getBlockColors();
		
		registerExtendedBlockColor(bc, (sup, state, world, pos, tint) -> {
			int color = sup.colorMultiplier(state, world, pos, tint);
			if(world == null || pos == null) return color;
			else {
				int greenLevel = (color & 0x00FF00) >> 8;
				greenLevel += NOISE.getValue(pos.getX() / 50d, pos.getZ() / 50d) * 7 + 7;
				greenLevel = MathHelper.clamp(greenLevel, 0, 255);
				
				return (color & 0xFF00FF) | (greenLevel << 8);
			}
		}, Blocks.GRASS, Blocks.DOUBLE_PLANT, Blocks.TALLGRASS, Blocks.LEAVES, Blocks.LEAVES2, Blocks.VINE, Blocks.WATERLILY, Blocks.REEDS);
		
		registerExtendedBlockColor(bc, (sup, state, world, pos, tint) -> {
			int color = sup.colorMultiplier(state, world, pos, tint);
			if(world == null || pos == null) return color;
			else {
				int blueLevel = color & 0x0000FF;
				blueLevel += NOISE.getValue(pos.getX() / 50d, pos.getZ() / 50d) * 14 + 15;
				blueLevel = MathHelper.clamp(blueLevel, 0, 255);
				
				return (color & 0xFFFF00) | blueLevel;
			}
		}, Blocks.WATER, Blocks.FLOWING_WATER);
	}
	
	private static void registerExtendedBlockColor(BlockColors bc, IExtendedBlockColor extColor, Block... blocks) {
		Map<IRegistryDelegate<Block>, IBlockColor> internalMap = ObfuscationReflectionHelper.getPrivateValue(BlockColors.class, bc, "blockColorMap"); //forge-added field in 1.12, no srg name available
		//TODO someone test this in obf lol
		
		for(Block b : blocks) {
			IRegistryDelegate<Block> delegate = b.delegate;
			IBlockColor regularColor = internalMap.get(delegate);
			
			bc.registerBlockColorHandler(((state, world, pos, tintIndex) -> extColor.colorMultiplierExt(regularColor, state, world, pos, tintIndex)), b);
		}
	}
	
	interface IExtendedBlockColor {
		int colorMultiplierExt(IBlockColor sup, IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tint);
	}
}
