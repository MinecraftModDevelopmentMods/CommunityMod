package com.mcmoddev.communitymod.bijump;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SubMod(modid = "bijump", name = "BiJump", attribution = "SkyHawkB")
public class BiJump implements ISubMod {
    public static final Enchantment BOINGBOING = new EnchantmentBoingBoing();

    @SubscribeEvent
    public static void registerEnchants(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().register(BOINGBOING);
    }
}
