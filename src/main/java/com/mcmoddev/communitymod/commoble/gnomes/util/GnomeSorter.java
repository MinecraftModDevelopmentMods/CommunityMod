package com.mcmoddev.communitymod.commoble.gnomes.util;

import java.util.Comparator;

import com.mcmoddev.communitymod.commoble.gnomes.EntityGnome;
import com.mcmoddev.communitymod.commoble.gnomes.TileEntityGnode;

import net.minecraft.tileentity.TileEntity;

/**
 * Sorts gnomes based on their distance to a gnode
 *
 */
@SuppressWarnings("rawtypes")
public class GnomeSorter implements Comparator
{
	private TileEntity gnode;
	
	public GnomeSorter(TileEntityGnode gnode)
	{
		this.gnode = gnode;
	}
	
	private int compareGnomeDistance(EntityGnome gnome1, EntityGnome gnome2)
	{
		double d1 = this.gnode.getDistanceSq(gnome1.posX, gnome1.posY, gnome1.posZ);
		double d2 = this.gnode.getDistanceSq(gnome2.posX, gnome2.posY, gnome2.posZ);
		return d1 < d2 ? -1 : (d1 > d2 ? 1 : 0);
	}

	@Override
	public int compare(Object obj1, Object obj2)
	{
		return this.compareGnomeDistance((EntityGnome)obj1, (EntityGnome)obj2);
	}
}
