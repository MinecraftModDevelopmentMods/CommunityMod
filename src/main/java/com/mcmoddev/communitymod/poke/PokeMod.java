package com.mcmoddev.communitymod.poke;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mcmoddev.communitymod.CommunityGlobals.MOD_ID;
import static com.mcmoddev.communitymod.CommunityGlobals.TAB;
import static com.mcmoddev.communitymod.shared.ClientUtil.simpleItemModel;
import static net.minecraft.inventory.EntityEquipmentSlot.CHEST;
import static net.minecraft.inventory.EntityEquipmentSlot.FEET;
import static net.minecraft.inventory.EntityEquipmentSlot.HEAD;
import static net.minecraft.inventory.EntityEquipmentSlot.LEGS;
import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SubMod(name = "Poke", attribution = "Cadiboo")
@ObjectHolder(MOD_ID)
@EventBusSubscriber(modid = MOD_ID)
public final class PokeMod implements ISubMod {

	public static final Block POKE_ORE = _null();
	public static final Block POKE_BLOCK = _null();

	public static final Item POKE_INGOT = _null();
	public static final Item POKE_NUGGET = _null();
	public static final ItemPickaxe POKE_PICKAXE = _null();
	public static final ItemAxe POKE_AXE = _null();
	public static final ItemSword POKE_SWORD = _null();
	public static final ItemHoe POKE_HOE = _null();
	public static final ItemSpade POKE_SHOVEL = _null();
	public static final ItemArmor POKE_HELMET = _null();
	public static final ItemArmor POKE_CHESTPLATE = _null();
	public static final ItemArmor POKE_LEGGINGS = _null();
	public static final ItemArmor POKE_BOOTS = _null();
	public static final Item POKE_HORSE_ARMOR = _null();

	@SubscribeEvent
	public static void regBlocks(final RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
				setup(new Block(Material.ROCK) {{ this.setSoundType(SoundType.STONE); }}.setHardness(0x1.8p1f).setResistance(0x1.4p2f), "poke_ore"),
				setup(new Block(Material.IRON) {{ this.setSoundType(SoundType.METAL); }}.setHardness(0x1.8p2f).setResistance(0x1.4p3f), "poke_block")
		);
	}

	@SubscribeEvent
	public static void regItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				setup(new Item(), "poke_ingot"),
				setup(new Item(), "poke_nugget"),
				setup(new ItemPickaxe(ToolMaterialHelper.POKE) {}, "poke_pickaxe"),
				setup(new ItemAxe(ToolMaterialHelper.POKE, ToolMaterialHelper.POKE.getAttackDamage(), 0b11111111111111111111111111111101) {}, "poke_axe"),
				setup(new ItemSword(ToolMaterialHelper.POKE), "poke_sword"),
				setup(new ItemHoe(ToolMaterialHelper.POKE), "poke_hoe"),
				setup(new ItemSpade(ToolMaterialHelper.POKE), "poke_shovel"),
				setup(new ItemArmor(ArmorMaterialHelper.POKE, 5, HEAD), "poke_helmet"),
				setup(new ItemArmor(ArmorMaterialHelper.POKE, 5, CHEST), "poke_chestplate"),
				setup(new ItemArmor(ArmorMaterialHelper.POKE, 5, LEGS), "poke_leggings"),
				setup(new ItemArmor(ArmorMaterialHelper.POKE, 5, FEET), "poke_boots"),
				setup(new Item() {@Override public HorseArmorType getHorseArmorType(final ItemStack stack) { return HorseArmorTypeHelper.POKE; }}, "poke_horse_armor"),

				setup(new ItemBlock(POKE_ORE), POKE_ORE.getRegistryName()),
				setup(new ItemBlock(POKE_BLOCK), POKE_BLOCK.getRegistryName())
		);
	}

	private static <T extends IForgeRegistryEntry<T>> T setup(final IForgeRegistryEntry<T> entry, final String name) {
		return setup(entry, new ResourceLocation(MOD_ID, name));
	}

	private static <T extends IForgeRegistryEntry<T>> T setup(final IForgeRegistryEntry<T> entry, final ResourceLocation registryName) {
		if (entry instanceof Item) {
			final Item item = (Item) entry;
			item.setTranslationKey(MOD_ID + "." + registryName.getPath());
			item.setCreativeTab(TAB);
		} else if (entry instanceof Block) {
			final Block block = (Block) entry;
			block.setTranslationKey(MOD_ID + "." + registryName.getPath());
			block.setCreativeTab(TAB);
		}
		return entry.setRegistryName(registryName);
	}

	/**
	 * Suppresses IDE warnings and suggestions about objects that are going to be filled by @{@link ObjectHolder} being null
	 *
	 * @return null
	 */
	@Nonnull
	public static <T> T _null() {
		return null;
	}

	public static <T extends IForgeRegistryEntry<T>> List<T> getModEntries(final IForgeRegistry<T> registry) {
		return registry.getValues().stream()
				.filter(entry -> Objects.requireNonNull(entry.getRegistryName()).getNamespace().equals(MOD_ID))
				.collect(Collectors.toList());
	}

	@Override
	public void onInit(final FMLInitializationEvent event) {

		GameRegistry.addSmelting(
				Item.getItemFromBlock(POKE_ORE),
				new ItemStack(POKE_INGOT),
				0x1.0p0f
		);

		GameRegistry.addSmelting(
				POKE_INGOT,
				new ItemStack(Items.DIAMOND),
				0x1.0p0f
		);

		GameRegistry.registerWorldGenerator(
				(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider) -> {
					if (world.provider.getDimension() == 0) {
						int deltaY = 0b1000000 - 0b10000;

						final PooledMutableBlockPos pos = PooledMutableBlockPos.retain();
						try {
							for (int i = 0; i < 0b110; ++i) {
								pos.setPos((chunkX << 0b100) + random.nextInt(0b10000), 0b10000 + random.nextInt(deltaY), (chunkZ << 0b100) + random.nextInt(0b10000));

								WorldGenMinable generator = new WorldGenMinable(POKE_ORE.getDefaultState(), 0b100 + random.nextInt(0b100));
								generator.generate(world, random, pos);
							}
						} finally {
							pos.release();
						}
					}
				},
				3
		);

	}

	@Override
	public void registerModels(final ModelRegistryEvent event) {

		simpleItemModel(POKE_INGOT);
		simpleItemModel(POKE_NUGGET);
		simpleItemModel(POKE_PICKAXE);
		simpleItemModel(POKE_AXE);
		simpleItemModel(POKE_SWORD);
		simpleItemModel(POKE_HOE);
		simpleItemModel(POKE_SHOVEL);
		simpleItemModel(POKE_HELMET);
		simpleItemModel(POKE_CHESTPLATE);
		simpleItemModel(POKE_LEGGINGS);
		simpleItemModel(POKE_BOOTS);
		simpleItemModel(POKE_HORSE_ARMOR);

		simpleItemModel(Item.getItemFromBlock(POKE_ORE));
		simpleItemModel(Item.getItemFromBlock(POKE_BLOCK));

	}

	public static final class ToolMaterialHelper {

		public static final Item.ToolMaterial POKE = EnumHelper.addToolMaterial(MOD_ID + "_poke", 0b11, 0b10000000000, 1.0E01F, 0b100, 0b10100);

	}

	public static final class ArmorMaterialHelper {

		public static final ItemArmor.ArmorMaterial POKE = EnumHelper.addArmorMaterial(MOD_ID + "_poke", new ResourceLocation(MOD_ID, "poke").toString(), 0b100000, new int[]{0b11, 0b110, 0b1000, 0b11}, 0b10100, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0E00F);

	}

	public static final class HorseArmorTypeHelper {

		public static final HorseArmorType POKE = EnumHelper.addHorseArmor(MOD_ID + "_poke", new ResourceLocation(MOD_ID, "textures/entity/horse/armor/horse_armor_poke.png").toString(), 0b1010);

	}

}
