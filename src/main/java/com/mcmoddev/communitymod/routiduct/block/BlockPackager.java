package com.mcmoddev.communitymod.routiduct.block;

import com.mcmoddev.communitymod.routiduct.block.tiles.TileRoutiduct;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import com.mcmoddev.communitymod.routiduct.api.EnumProtocol;
import com.mcmoddev.communitymod.routiduct.api.IProtocolProvider;
import com.mcmoddev.communitymod.routiduct.api.Package;
import com.mcmoddev.communitymod.routiduct.gui.GuiAssemblerS;
import com.mcmoddev.communitymod.routiduct.gui.SlotType;
import com.mcmoddev.communitymod.routiduct.gui.Sprite;
import com.mcmoddev.communitymod.routiduct.gui.Sprites;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.GuiBlueprint;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.element.ElementBase;
import com.mcmoddev.communitymod.routiduct.gui.blueprint.element.TextElement;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class BlockPackager extends BlockRD implements IProtocolProvider {
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
	public static final PropertyBool CONNECTION = PropertyBool.create("connection");
	private final EnumProtocol protocol;

	public BlockPackager(EnumProtocol protocol) {
		super("packager." + protocol.name.toLowerCase());
		setHardness(0.5F);
		setDefaultState(getStateFromMeta(0));
		this.protocol = protocol;
	}

	@Override
	public EnumProtocol getProtocol() {
		return protocol;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, 0);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(getDefaultState());
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (isEqual(state, EnumFacing.EAST))
			return 0;
		else if (isEqual(state, EnumFacing.WEST))
			return 1;
		else if (isEqual(state, EnumFacing.NORTH))
			return 2;
		else if (isEqual(state, EnumFacing.SOUTH))
			return 3;
		else if (isEqual(state, EnumFacing.UP))
			return 4;
		else if (isEqual(state, EnumFacing.DOWN))
			return 5;
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos.offset(state.getValue(FACING).getOpposite())).getBlock() instanceof BlockRelay && ((BlockRelay) worldIn.getBlockState(pos.offset(state.getValue(FACING).getOpposite())).getBlock()).getProtocol() == protocol || worldIn.getBlockState(pos.offset(state.getValue(FACING).getOpposite())).getBlock() instanceof BlockRoutiduct && ((BlockRoutiduct) worldIn.getBlockState(pos.offset(state.getValue(FACING).getOpposite())).getBlock()).getProtocol() == protocol && worldIn.getBlockState(pos.offset(state.getValue(FACING).getOpposite())).getValue(BlockRoutiduct.AXIS) == getAxis(worldIn, pos))
			return state.withProperty(CONNECTION, true);
		return state;
	}

	public BlockRoutiduct.EnumAxis getAxis(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		EnumFacing facing = state.getValue(BlockPackager.FACING);
		if (facing == EnumFacing.EAST || facing == EnumFacing.EAST) {
			return BlockRoutiduct.EnumAxis.X;
		} else if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
			return BlockRoutiduct.EnumAxis.Z;
		} else {
			return BlockRoutiduct.EnumAxis.Y;
		}
	}

	public boolean isEqual(IBlockState state, EnumFacing facingValue) {
		if (state.equals(getDefaultState().withProperty(FACING, facingValue))) {
			return true;
		}
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, getFacingFromMeta(meta));
	}

	public EnumFacing getFacingFromMeta(int meta) {
		if (meta == 0) {
			return EnumFacing.EAST;
		}
		if (meta == 1) {
			return EnumFacing.WEST;
		}
		if (meta == 2) {
			return EnumFacing.NORTH;
		}
		if (meta == 3) {
			return EnumFacing.SOUTH;
		}
		if (meta == 4) {
			return EnumFacing.UP;
		}
		if (meta == 5) {
			return EnumFacing.DOWN;
		}
		return EnumFacing.EAST;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, CONNECTION });
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, facing.getOpposite()).withProperty(CONNECTION, false);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public GuiBlueprint getGuiBlueprint(TileRoutiduct tile) {
		GuiBlueprint blueprint = new GuiBlueprint(tile).setSize(176, 241).setPlayerInvPos(7, 158);
		blueprint.addSlot(SlotType.NORMAL, 7, 30);
		blueprint.addSlot(SlotType.NORMAL, 25, 30);
		blueprint.addSlot(SlotType.NORMAL, 43, 30);
		blueprint.addSlot(SlotType.NORMAL, 7, 48);
		//		blueprint.addSlot(SlotType.NORMAL, 25, 48);
		blueprint.elements.add(new TextElement("Input", 4210752, 9, 21));
		blueprint.elements.add(new TextElement("Inventory", 4210752, 9, 149));
		blueprint.elements.add(new ElementBase(new Sprite(GuiAssemblerS.ROUTIDUCT_ELEMENTS, protocol.badgeX, protocol.badgeY, 15, 10), 154, 7));
		for (int i = 0; i < protocol.ports; i++)
			blueprint.elements.add(new ElementBase(Sprites.PACKAGE_BAR, 69, 21 + i * 11));
		blueprint.elements.add(new TextElement("Input", 4210752, 9, 21));
		blueprint.elements.add(new TextElement("100%", 0xFF000000, 25, 72, true));
		blueprint.elements.add(new ElementBase(Sprites.PROGRESS_BAR_BACKGROUND, 11, 81));
		for (Package.EnumColor color : Package.EnumColor.values()) {
			if (color.ordinal() < protocol.ports) {
				String text = I18n.translateToLocal(color.unlocalisedName) + " Package";
				String number = (color.ordinal() + 1) + ".";
				blueprint.elements.add(new TextElement(number, 0xFFC4C4C4, 78, 23 + color.ordinal() * 11, true));
				blueprint.elements.add(new TextElement(I18n.translateToLocal(color.unlocalisedName) + " Package", color.colour, 86, 23 + color.ordinal() * 11, 74));
				blueprint.elements.add(new ElementBase(new Sprite(GuiAssemblerS.ROUTIDUCT_ELEMENTS, 26, color.textureY, 8, 6), 159, 24 + color.ordinal() * 11));
			}
		}
		blueprint.elements.add(new ElementBase(new Sprite(GuiAssemblerS.ROUTIDUCT_ELEMENTS, 0, Package.EnumColor.values()[protocol.ports].textureY, 26, 6), 12, 82));
		return blueprint;
	}

	@Override
	public void update(TileRoutiduct tile) {
		//		System.out.println(protocol);
		if (tile.protocol != ((BlockPackager) tile.getWorld().getBlockState(tile.getPos()).getBlock()).getProtocol()) {
			tile.protocol = ((BlockPackager) tile.getWorld().getBlockState(tile.getPos()).getBlock()).getProtocol();
		}
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < tile.itemHandler.getSlots(); i++) {
			if (!tile.itemHandler.getStackInSlot(i).isEmpty()) {
				stacks.add(tile.itemHandler.getStackInSlot(i));
			}
		}
		if (stacks.size() == protocol.stacks) {
			return;
		}
		EnumFacing facing = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockPackager.FACING);
		TileEntity tileEntity = tile.getWorld().getTileEntity(tile.getPos().offset(facing));
		if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
			IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
			for (int i = 0; i < itemHandler.getSlots(); i++) {
				ItemStack stack1 = itemHandler.getStackInSlot(i);
				ItemStack extractedStack = itemHandler.extractItem(i, 1, true);
				for (int j = 0; j < protocol.stacks; j++) {
					//					System.out.println("do the stuff");
					//					if (tile.itemHandler.isItemValidForSlot(j, extractedStack)) {
					//						int amount = InventoryHelper.testInventoryInsertion(inventory, extractedStack, null);
					//						if (amount > 0) {
					//							extractedStack = itemHandler.extractItem(i, 1, false);
					//							extractedStack.setCount(1);
					//							InventoryHelper.insertItemIntoInventory(inventory, extractedStack);
					//						}
					//					}
				}
			}
		}
	}
}
