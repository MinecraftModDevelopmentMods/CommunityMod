package com.mcmoddev.communitymod.bijump;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@SubMod(name = "BiJump", attribution = "SkyHawkB")
public class BiJump implements ISubMod {
    private static Set<EntityPlayerSP> jumpybois = new HashSet<>();
    public static final Enchantment BOINGBOING = new EnchantmentBoingBoing();
    public static KeyBinding jump;

    @Override
    public void onInit(FMLInitializationEvent event) {
        jump = new KeyBinding("key.tutorial", Keyboard.KEY_SPACE, "key.categories.modtut");
        ClientRegistry.registerKeyBinding(jump);
    }

    @SubscribeEvent
    public static void registerEnchants(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().register(BOINGBOING);
    }

    @SubscribeEvent
    public static void onJump(InputEvent.KeyInputEvent event) {
        if(jump.isPressed()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
            int level = EnchantmentHelper.getMaxEnchantmentLevel(BOINGBOING, player);

            if(player.world.getBlockState(pos).getBlock().equals(Blocks.AIR) &&
                   player.world.getBlockState(pos.add(0, -2, 0)).getBlock().equals(Blocks.AIR) &&
                   !jumpybois.contains(player) &&  level > 0) {
                player.sprintingTicksLeft += 40;
                player.addVelocity(0, 1.2 * Math.sqrt(level), 0);
                jumpybois.add(player);
                new Timer().schedule(new TimerTask(){ @Override public void run(){ jumpybois.remove(player);}}, 7000);
            }
        }
    }
}
