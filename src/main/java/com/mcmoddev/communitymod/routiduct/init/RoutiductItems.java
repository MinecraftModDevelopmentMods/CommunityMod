package com.mcmoddev.communitymod.routiduct.init;

import com.mcmoddev.communitymod.routiduct.RoutiductConstants;
import com.mcmoddev.communitymod.routiduct.item.ItemRD;
import com.mcmoddev.communitymod.routiduct.item.ItemWrench;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreDictionary;

public class RoutiductItems {

    public static final ItemRD WRENCH = new ItemWrench();

    public static void init(RegistryEvent.Register<Item> event) {
        register(WRENCH, event, "itemWrench", "wrenchRoutiduct");
    }

    public static void register(Item item, RegistryEvent.Register<Item> event, String... oreNames) {
        ShootingStar.registerModel(new ModelCompound(RoutiductConstants.MOD_ID, item));
        event.getRegistry().register(item);
        for (String oreName : oreNames) {
            OreDictionary.registerOre(oreName, item);
        }
    }

}
