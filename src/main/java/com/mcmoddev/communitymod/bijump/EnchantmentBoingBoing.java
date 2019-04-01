package com.mcmoddev.communitymod.bijump;

import com.mcmoddev.communitymod.CommunityGlobals;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentBoingBoing extends Enchantment {
    public EnchantmentBoingBoing() {
        super(Rarity.RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
        this.setRegistryName(new ResourceLocation(CommunityGlobals.MOD_ID, "bijump"));
        this.setName("bijump");
    }

    public boolean isTreasureEnchantment() {
        return true;
    }
    public int getMaxLevel() {
        return 3;
    }
}
