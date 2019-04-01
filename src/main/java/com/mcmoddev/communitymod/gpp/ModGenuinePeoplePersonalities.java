package com.mcmoddev.communitymod.gpp;


import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mcmoddev.communitymod.CommunityMod;
import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import com.mcmoddev.communitymod.poke.PokeMod;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@SubMod(name = "Genuine People Personalities", attribution = "Sirius Cybernetics Corporation")
@Mod.EventBusSubscriber(modid = "community_mod", value = Side.CLIENT)
public class ModGenuinePeoplePersonalities implements ISubMod {
    private static String obnoxious = TextFormatting.AQUA + "" + TextFormatting.BOLD;
    private static Object2IntOpenHashMap<UUID> cooldownMap = new Object2IntOpenHashMap<>();

    private static boolean testCooldown(EntityPlayer player) {
        UUID uuid = player.getUniqueID();
        int cooldown;
        if (cooldownMap.containsKey(uuid)) {
            cooldown = cooldownMap.getInt(uuid);
        } else {
            cooldown = 0;
        }

        if (cooldown - player.ticksExisted < 0) {
            cooldown = player.ticksExisted - 1;
        }

        if (cooldown > player.ticksExisted) {
            CommunityMod.LOGGER.info(((cooldown - player.ticksExisted) / 20) + " seconds until next event.");
            return false;
        }

        return true;
    }

    private static boolean resetCooldown(EntityPlayer player) {
        int cooldown = player.ticksExisted + (15 * 20 + (player.world.rand.nextInt(5) * 20));
        cooldownMap.put(player.getUniqueID(), cooldown);
        return true;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockHit(PlayerInteractEvent.LeftClickBlock event) {
        EventData data = new EventData(event);
        if (data.getPlayer() == null || data.getPlayer().world.isRemote) return;
        if (data.getHand() != EnumHand.MAIN_HAND) return;
        if (!testCooldown(data.getPlayer())) return;
        if (generateComplaint(StringType.ATTACK, data.getPlayer(), data.getBlock(), data)) {
            resetCooldown(data.getPlayer());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        EventData data = new EventData(event);
        if (data.getPlayer() == null || data.getPlayer().world.isRemote) return;
        if (data.getHand() != EnumHand.MAIN_HAND) return;
        if (!testCooldown(data.getPlayer())) return;
        if (generateComplaint(StringType.ACTIVATE, data.getPlayer(), data.getBlock(), data)) {
            resetCooldown(data.getPlayer());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemActivated(PlayerInteractEvent.RightClickItem event) {
        EventData data = new EventData(event);
        if (data.getPlayer() == null || data.getPlayer().world.isRemote) return;
        if (!testCooldown(data.getPlayer())) return;
        if (generateComplaint(StringType.ACTIVATE, data.getPlayer(), event.getItemStack(), data)) {
            resetCooldown(data.getPlayer());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockDestroyed(BlockEvent.BreakEvent event) {
        EventData data = new EventData(event);
        if (data.getPlayer() == null || data.getPlayer().world.isRemote) return;
        if (!testCooldown(data.getPlayer())) return;
        if (generateComplaint(StringType.BREAK, data.getPlayer(), data.getBlock(), data)) {
            resetCooldown(data.getPlayer());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockPlace(BlockEvent.PlaceEvent event) {
        EventData data = new EventData(event);
        if (data.getPlayer() == null || data.getPlayer().world.isRemote) return;
        if (!testCooldown(data.getPlayer())) return;
        if (generateComplaint(StringType.PLACE, data.getPlayer(), data.getBlock(), data)) {
            resetCooldown(data.getPlayer());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        EventData data = new EventData(event);
        if (data.getPlayer() == null || data.getPlayer().world.isRemote) return;
        if (!testCooldown(data.getPlayer())) return;
        if (generateComplaint(StringType.TRAMPLE, data.getPlayer(), data.getBlock(), data)) {
            resetCooldown(data.getPlayer());
        }
    }

    private static class EventData {
        // Block Event
        private final IBlockState state;
        private final Block block;
        // Player Event
        private final EnumHand hand;
        private final EntityPlayer player;
        // Shared
        private final World world;
        private final BlockPos pos;
        private final Event event;
        public final Random random;

        public EventData(BlockEvent event) {
            this.event = event;
            this.pos = event.getPos();
            this.world = event.getWorld();
            this.state = event.getState();
            this.random = world.rand;
            this.block = this.state.getBlock();
            this.hand = null;
            this.player = null;
        }

        public EventData(PlayerInteractEvent event) {
            this.event = event;
            this.pos = event.getPos();
            this.world = event.getWorld();
            this.player = event.getEntityPlayer();
            this.hand = event.getHand();
            this.random = world.rand;
            this.state = world.getBlockState(this.pos);
            this.block = this.state.getBlock();
        }

        public Block getBlock() {
            return block;
        }

        @Nullable
        public IBlockState getState() {
            return state;
        }

        @Nullable
        public EnumHand getHand() {
            return hand;
        }

        @Nullable
        public EntityPlayer getPlayer() {
            if (player != null) return player;
            if (event instanceof BlockEvent.PlaceEvent) {
                return ((BlockEvent.PlaceEvent) event).getPlayer();
            } else if (event instanceof BlockEvent.BreakEvent) {
                return ((BlockEvent.BreakEvent) event).getPlayer();
            } else if (event instanceof BlockEvent.HarvestDropsEvent) {
                EntityPlayer player = ((BlockEvent.HarvestDropsEvent) event).getHarvester();
                if (player instanceof FakePlayer) return null;
                return player;
            } else if (event instanceof BlockEvent.FarmlandTrampleEvent) {
                Entity entity = ((BlockEvent.FarmlandTrampleEvent) event).getEntity();
                if (!(entity instanceof EntityPlayer)) return null;
                return (EntityPlayer) entity;
            }

            return null;
        }

        public World getWorld() {
            return world;
        }

        public BlockPos getPos() {
            return pos;
        }

        public Event getEvent() {
            return event;
        }
    }

    private enum StringType {
        ATTACK,
        ACTIVATE,
        BREAK,
        PLACE,
        TRAMPLE
    }

    private static boolean generateComplaint(StringType type, EntityPlayer player, Block block, EventData data) {
        switch (type) {
            case ACTIVATE:
                if (block instanceof BlockDoor) {
                    switch (data.random.nextInt(5)) {
                        case 0:
                            player.sendMessage(new TextComponentString(obnoxious + "The door sighs with delight!"));
                            return true;
                        case 1:
                            player.sendMessage(new TextComponentString(obnoxious + "\"See you soon,\" says the door."));
                            return true;
                        case 2:
                            player.sendMessage(new TextComponentString(obnoxious + "\"I promise I won't hit you on the way out!\" says the door."));
                            return true;
                        case 3:
                            player.sendMessage(new TextComponentString(obnoxious + "\"Sirius Cybernetics Corporation thanks you for your patronage\", says the door."));
                            return true;
                        case 4:
                            player.sendMessage(new TextComponentString(obnoxious + "The door lets out a happy little moan"));
                            return true;
                    }
                }
                return false;
            case ATTACK:
                if (block == Blocks.DIAMOND_ORE) {
                    switch (data.random.nextInt(3)) {
                        case 0:
                            player.sendMessage(new TextComponentString(obnoxious + "\"Damn it!\" cries the diamond ore, \"You found me again...\""));
                            break;
                        case 1:
                            player.sendMessage(new TextComponentString(obnoxious + "\"Come to get your bling on?\" asks the diamond ore."));
                            break;
                        case 2:
                            player.sendMessage(new TextComponentString(obnoxious + "The diamond ore sighs. \"I need to get better at hide and seek.\""));
                            break;
                    }
                    return true;
                } else if (block == Blocks.LOG || block == Blocks.LOG2) {
                    if (player.getHeldItemMainhand().isEmpty()) {
                        switch (data.random.nextInt(4)) {
                            case 0:
                                player.sendMessage(new TextComponentString(obnoxious + "\"You know, being bludgeoned to death is not a nice way to go!\" yells the tree."));
                                break;
                            case 1:
                                player.sendMessage(new TextComponentString(obnoxious + "The tree sighs. \"An axe might be more painful, but at least it doesn't take all day.\""));
                                break;
                            case 2:
                            case 3:
                                player.sendMessage(new TextComponentString(obnoxious + "\"Even a wooden axe would make the process quicker...\" exclaims the tree."));
                                break;
                        }
                    }
                    return true;
                }
                return false;
            case BREAK:
                if (block == Blocks.COAL_ORE) {
                    player.sendMessage(new TextComponentString(obnoxious + "\"God-damn it, now I'm all over the places!\" cries the coal ore."));
                    return true;
                } else if (block == Blocks.REDSTONE_ORE) {
                    player.sendMessage(new TextComponentString(obnoxious + "\"Would you still want me if I did glow?\" asks the redstone ore."));
                    return true;
                } else if (block == Blocks.IRON_ORE) {
                    switch (data.random.nextInt(4)) {
                        case 0:
                            player.sendMessage(new TextComponentString(obnoxious + "The iron ore says, \"Eh, don't worry. I can take it.\""));
                            break;
                        case 1:
                            player.sendMessage(new TextComponentString(obnoxious + "The iron ore whispers, \"You aren't planning to melt me or anything, are you? Good.\""));
                            break;
                        case 2:
                        case 3:
                            player.sendMessage(new TextComponentString(obnoxious + "The iron ore says, \"Barely a scratch on me!\""));
                            break;
                    }
                    return true;
                } else if (block == Blocks.EMERALD_ORE) {
                    switch (data.random.nextInt(4)) {
                        case 0:
                            player.sendMessage(new TextComponentString(obnoxious + "The emerald ore says, \"Huh?\""));
                            break;
                        case 1:
                            player.sendMessage(new TextComponentString(obnoxious + "The emerald ore says, \"Haah!\""));
                            break;
                        case 2:
                            player.sendMessage(new TextComponentString(obnoxious + "The emerald ore says, \"Huh.\""));
                            break;
                        case 3:
                            player.sendMessage(new TextComponentString(obnoxious + "The emerald ore says, \"Huh? Hah!\""));
                            break;
                    }
                    return true;
                } else if (block == PokeMod.POKE_BLOCK) {
                    player.sendMessage(new TextComponentString(obnoxious + "\"Dab\" says the poke block."));
                    return true;
                } else if (block == PokeMod.POKE_ORE) {
                    player.sendMessage(new TextComponentString(obnoxious + "\"Dab\" says the poke ore."));
                    return true;
                }
                return false;
            case TRAMPLE:
                player.sendMessage(new TextComponentString(obnoxious + "You hear a thousand distant carrots cry out in horror, then suddenly silence."));
                return true;
            case PLACE:
                if (data.random.nextInt(10) == 0) {
                    player.sendMessage(new TextComponentString(obnoxious + "The block yells, \"I'm free!!!\""));
                    return true;
                }
                return false;
        }
        return false;
    }

    private static boolean generateComplaint(StringType type, EntityPlayer player, ItemStack stack, EventData data) {
        switch (type) {
            case ACTIVATE:
                if (stack.getItem() == Items.APPLE) {
                    switch (data.random.nextInt(5)) {
                        case 0:
                            player.sendMessage(new TextComponentString(obnoxious + "The apple screams, \"No! Think of my children!\""));
                            break;
                        case 1:
                            player.sendMessage(new TextComponentString(obnoxious + "The apple sobs mournfully as it is devoured."));
                            break;
                        case 2:
                            player.sendMessage(new TextComponentString(obnoxious + "The apple seems resigned to its oncoming demise."));
                            break;
                        case 3:
                        case 4:
                            player.sendMessage(new TextComponentString(obnoxious + "The apple seems resigned to its oncoming demise."));
                            break;
                    }
                    return true;
                } else if (stack.getItem() == Items.FISH || stack.getItem() == Items.PORKCHOP || stack.getItem() == Items.MUTTON || stack.getItem() == Items.BEEF) {
                    player.sendMessage(new TextComponentString(obnoxious + String.format("The %s yells, \"Serves you right if you get sick! Bastard.\"", stack.getDisplayName())));
                    return true;
                }
        }
        return false;
    }
}
