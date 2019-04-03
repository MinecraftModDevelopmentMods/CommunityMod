package com.mcmoddev.communitymod.squirrel;

import static com.mcmoddev.communitymod.CommunityGlobals.MOD_ID;
import static com.mcmoddev.communitymod.CommunityGlobals.TAB;
import static com.mcmoddev.communitymod.shared.ClientUtil.simpleItemModel;
import static net.minecraft.inventory.EntityEquipmentSlot.CHEST;
import static net.minecraft.inventory.EntityEquipmentSlot.FEET;
import static net.minecraft.inventory.EntityEquipmentSlot.HEAD;
import static net.minecraft.inventory.EntityEquipmentSlot.LEGS;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

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
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@SubMod(modid = "squirrel", name = "Squirrel", attribution = "Cadiboo")
@ObjectHolder(MOD_ID)
public final class Squirrel implements ISubMod {

	public static final Block SQUIRREL_ORE = _null();
	public static final Block SQUIRREL_BLOCK = _null();

	public static final Item SQUIRREL_INGOT = _null();
	public static final Item SQUIRREL_NUGGET = _null();
	public static final ItemPickaxe SQUIRREL_PICKAXE = _null();
	public static final ItemAxe SQUIRREL_AXE = _null();
	public static final ItemSword SQUIRREL_SWORD = _null();
	public static final ItemHoe SQUIRREL_HOE = _null();
	public static final ItemSpade SQUIRREL_SHOVEL = _null();
	public static final ItemArmor SQUIRREL_HELMET = _null();
	public static final ItemArmor SQUIRREL_CHESTPLATE = _null();
	public static final ItemArmor SQUIRREL_LEGGINGS = _null();
	public static final ItemArmor SQUIRREL_BOOTS = _null();
	public static final Item SQUIRREL_HORSE_ARMOR = _null();

	@Override
	public void registerBlocks(IForgeRegistry<Block> reg) {
		reg.registerAll(
				setup(new Block(Material.ROCK) {{ this.setSoundType(SoundType.STONE); }}.setHardness(0x1.8p1f).setResistance(0x1.4p2f), "squirrel_ore"),
				setup(new Block(Material.IRON) {{ this.setSoundType(SoundType.METAL); }}.setHardness(0x1.8p2f).setResistance(0x1.4p3f), "squirrel_block")
		);
	}

	@Override
	public void registerItems(IForgeRegistry<Item> reg) {
		reg.registerAll(
				setup(new Item(), "squirrel_ingot"),
				setup(new Item(), "squirrel_nugget"),
				setup(new ItemPickaxe(ToolMaterialHelper.SQUIRREL) {}, "squirrel_pickaxe"),
				setup(new ItemAxe(ToolMaterialHelper.SQUIRREL, ToolMaterialHelper.SQUIRREL.getAttackDamage(), 0b11111111111111111111111111111101) {}, "squirrel_axe"),
				setup(new ItemSword(ToolMaterialHelper.SQUIRREL), "squirrel_sword"),
				setup(new ItemHoe(ToolMaterialHelper.SQUIRREL), "squirrel_hoe"),
				setup(new ItemSpade(ToolMaterialHelper.SQUIRREL), "squirrel_shovel"),
				setup(new ItemArmor(ArmorMaterialHelper.SQUIRREL, 5, HEAD), "squirrel_helmet"),
				setup(new ItemArmor(ArmorMaterialHelper.SQUIRREL, 5, CHEST), "squirrel_chestplate"),
				setup(new ItemArmor(ArmorMaterialHelper.SQUIRREL, 5, LEGS), "squirrel_leggings"),
				setup(new ItemArmor(ArmorMaterialHelper.SQUIRREL, 5, FEET), "squirrel_boots"),
				setup(new Item() {@Override public HorseArmorType getHorseArmorType(final ItemStack stack) { return HorseArmorTypeHelper.SQUIRREL; }}, "squirrel_horse_armor"),

				setup(new ItemBlock(SQUIRREL_ORE), SQUIRREL_ORE.getRegistryName()),
				setup(new ItemBlock(SQUIRREL_BLOCK), SQUIRREL_BLOCK.getRegistryName())
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
				Item.getItemFromBlock(SQUIRREL_ORE),
				new ItemStack(SQUIRREL_INGOT),
				0x1.0p0f
		);

		GameRegistry.addSmelting(
				SQUIRREL_INGOT,
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

								WorldGenMinable generator = new WorldGenMinable(SQUIRREL_ORE.getDefaultState(), 0b100 + random.nextInt(0b100));
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
	@SideOnly(Side.CLIENT)
	public void registerModels(final ModelRegistryEvent event) {

		simpleItemModel(SQUIRREL_INGOT);
		simpleItemModel(SQUIRREL_NUGGET);
		simpleItemModel(SQUIRREL_PICKAXE);
		simpleItemModel(SQUIRREL_AXE);
		simpleItemModel(SQUIRREL_SWORD);
		simpleItemModel(SQUIRREL_HOE);
		simpleItemModel(SQUIRREL_SHOVEL);
		simpleItemModel(SQUIRREL_HELMET);
		simpleItemModel(SQUIRREL_CHESTPLATE);
		simpleItemModel(SQUIRREL_LEGGINGS);
		simpleItemModel(SQUIRREL_BOOTS);
		simpleItemModel(SQUIRREL_HORSE_ARMOR);

		simpleItemModel(Item.getItemFromBlock(SQUIRREL_ORE));
		simpleItemModel(Item.getItemFromBlock(SQUIRREL_BLOCK));

	}

	public static final class ToolMaterialHelper {

		public static final Item.ToolMaterial SQUIRREL = EnumHelper.addToolMaterial(MOD_ID + "_squirrel", 0b11, 0b10000000000, 1.0E01F, 0b100, 0b10100);

	}

	public static final class ArmorMaterialHelper {

		public static final ItemArmor.ArmorMaterial SQUIRREL = EnumHelper.addArmorMaterial(MOD_ID + "_squirrel", new ResourceLocation(MOD_ID, "squirrel").toString(), 0b100000, new int[]{0b11, 0b110, 0b1000, 0b11}, 0b10100, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0E00F);

	}

	public static final class HorseArmorTypeHelper {

		public static final HorseArmorType SQUIRREL = EnumHelper.addHorseArmor(MOD_ID + "_squirrel", new ResourceLocation(MOD_ID, "textures/entity/horse/armor/horse_armor_squirrel.png").toString(), 0b1010);

	}

}
