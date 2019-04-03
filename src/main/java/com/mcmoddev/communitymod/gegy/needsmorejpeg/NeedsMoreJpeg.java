package com.mcmoddev.communitymod.gegy.needsmorejpeg;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import com.mcmoddev.communitymod.shared.ClientUtil;
import com.mcmoddev.communitymod.shared.RegUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SubMod(
		modid  = "jpeg",
        name = "needsmorejpeg",
        description = "We need more JPEG",
        attribution = "gegy1000"
)
@GameRegistry.ObjectHolder(CommunityGlobals.MOD_ID)
public class NeedsMoreJpeg implements ISubMod {
    public static final Item JPEG_GOGGLES = Items.AIR;

    private static final ResourceLocation SHADER_LOCATION = new ResourceLocation(CommunityGlobals.MOD_ID, "shaders/post/jpeg.json");

    @SideOnly(Side.CLIENT)
    private static boolean hadGoggles;

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        ClientUtil.simpleItemModel(JPEG_GOGGLES);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> reg) {
        RegUtil.registerItem(reg, new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD), "jpeg_goggles");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft client = Minecraft.getMinecraft();

            boolean hasGoggles = hasGoggles(client);
            if (hasGoggles != hadGoggles) {
                client.entityRenderer.loadEntityShader(null);
            }

            if (hasGoggles && shouldChangeShader(client)) {
                ShaderGroup activeGroup = client.entityRenderer.getShaderGroup();
                if (activeGroup != null) {
                    activeGroup.deleteShaderGroup();
                }
                client.entityRenderer.loadShader(SHADER_LOCATION);
            }

            hadGoggles = hasGoggles;
        }
    }

    @SideOnly(Side.CLIENT)
    private static boolean shouldChangeShader(Minecraft client) {
        ShaderGroup activeGroup = client.entityRenderer.getShaderGroup();
        return activeGroup == null || !activeGroup.getShaderGroupName().equals(SHADER_LOCATION.toString());
    }

    @SideOnly(Side.CLIENT)
    private static boolean hasGoggles(Minecraft client) {
        if (client.player == null) {
            return false;
        }

        ItemStack headStack = client.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return headStack.getItem() == JPEG_GOGGLES;
    }
}
