package com.mcmoddev.communitymod.routiduct.init;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.routiduct.RoutiductConstants;
import com.mcmoddev.communitymod.routiduct.api.EnumProtocol;
import com.mcmoddev.communitymod.routiduct.block.BlockPackager;
import com.mcmoddev.communitymod.routiduct.block.BlockRelay;
import com.mcmoddev.communitymod.routiduct.block.BlockRoutiduct;
import com.mcmoddev.communitymod.routiduct.block.BlockUnpackager;
import com.mcmoddev.communitymod.shootingstar.ShootingStar;
import com.mcmoddev.communitymod.shootingstar.model.ModelCompound;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class RoutiductBlocks {

    public static void init(RegistryEvent.Register<Block> event) {
        for (EnumProtocol protocol : EnumProtocol.values()) {
            BlockRoutiduct routiduct = new BlockRoutiduct(protocol);
            register(routiduct, event, "blockRoutiduct", "routiduct" + protocol.name);
            BlockPackager packager = new BlockPackager(protocol);
            register(packager, event, "blockPackager", "packager" + protocol.name);
            BlockUnpackager unpackager = new BlockUnpackager(protocol);
            register(unpackager, event, "blockUnpackager", "unpackager" + protocol.name);
            BlockRelay relay = new BlockRelay(protocol);
            register(relay, event, "blockRelay", "relay" + protocol.name);
        }
    }

    public static void register(Block block, RegistryEvent.Register<Block> event, String... oreNames) {
        block.setCreativeTab(CommunityGlobals.TAB);
        event.getRegistry().register(block);
        ItemBlock itemBlock = new ItemBlock(block);
        ShootingStar.registerModel(new ModelCompound(RoutiductConstants.MOD_ID, itemBlock));
        itemBlock.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);
        for (String oreName : oreNames) {
            OreDictionary.registerOre(oreName, block);
        }
    }

    public Block getBlock(String name, EnumProtocol protocol) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(RoutiductConstants.PREFIX, name + "." + protocol.name.toLowerCase()));
    }

    public Item getItem(String name, EnumProtocol protocol) {
        return Item.getItemFromBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(RoutiductConstants.PREFIX, name + "." + protocol.name.toLowerCase())));
    }

}
