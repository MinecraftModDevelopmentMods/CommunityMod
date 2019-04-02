//package prospector.routiduct.container;
//
//import net.minecraft.entity.player.EntityPlayer;
//import prospector.routiduct.block.tiles.TilePackager;
//import reborncore.client.gui.slots.BaseSlot;
//
///**
// * Created by Prospector
// */
//public class ContainerPackager extends ContainerRoutiductBase {
//	TilePackager tile;
//	EntityPlayer player;
//
//	public ContainerPackager(TilePackager tile, EntityPlayer player) {
//		super();
//		this.tile = tile;
//		this.player = player;
//
//		drawPlayersInvAndHotbar(player, 8, 94);
//		addSlotToContainer(new BaseSlot(tile.itemHandler, 0, 8, 31));
//		addSlotToContainer(new BaseSlot(tile.itemHandler, 1, 26, 31));
//		addSlotToContainer(new BaseSlot(tile.itemHandler, 2, 8, 49));
//		addSlotToContainer(new BaseSlot(tile.itemHandler, 3, 26, 49));
//	}
//}
