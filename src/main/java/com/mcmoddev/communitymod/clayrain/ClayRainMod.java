package com.mcmoddev.communitymod.clayrain;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;
import com.mcmoddev.communitymod.shared.RegUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

//TODO everything
@SubMod(name = "Clay Clay Go Away", attribution = "al132", description = "When it rains, it rains clay too.")
public class ClayRainMod implements ISubMod {

    static int maxRange = 12;

    public static ItemRainStick rainStick;

    @Override
    public void onPreInit(FMLPreInitializationEvent e) {
        FMLLog.log.info("Clay clay go away, Come again another day");
        FMLLog.log.info("Green is not a creative color");
    }

    @Override
    public void onInit(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void rain(TickEvent.WorldTickEvent e) {
        if (e.world.isRaining()) {
            if (e.world.rand.nextInt(5) == 0) {
                for (EntityPlayer player : e.world.playerEntities) {
                    if (e.world.getBiome(player.getPosition()).canRain()) {
                        double x = e.world.rand.nextInt(maxRange * 2) - maxRange;
                        double y = 255;
                        if (player.posY < 100) y = 130;
                        double z = e.world.rand.nextInt(maxRange * 2) - maxRange;

                        EntityItem clay = new EntityItem(e.world, x + player.posX, y, z + player.posZ, new ItemStack(Items.CLAY_BALL));
                        clay.lifespan = 200;
                        e.world.spawnEntity(clay);
                    }
                }
            }
        }
    }

    @Override
    public void registerItems(IForgeRegistry<Item> reg) {
        rainStick = RegUtil.registerItem(reg, new ItemRainStick(), "rain_stick");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent e) {
        ClientUtil.simpleItemModel(rainStick);
    }
}