package com.mcmoddev.communitymod.musksrockets;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class CellSavedWorldData extends WorldSavedData {

	private Map<String, Integer[]> storageCells = new HashMap<String, Integer[]>();
	private int storageIndex = 0;
	
	public CellSavedWorldData() {
		super(CellDataStorage.DATA_NAME);
	}

	public CellSavedWorldData(String s) {
		super(s);
	}

	public Map<String, Integer[]> getStorageCells() {
		return storageCells;
	}

	public void addStorageCell(String s, Integer[] i) {
		++storageIndex;
		storageCells.put(s, i);
		markDirty();
	}

	public void removeStorageCell(String s) {
		storageCells.remove(s);
		markDirty();
	}
	
	public int getIndex() {
		return storageIndex;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	 	storageIndex = nbt.getInteger("index");
		for (String s : nbt.getKeySet()) {
			Integer[] temp = new Integer[6];
			for (int i = 0; i < nbt.getIntArray(s).length; i++)
				temp[i] = nbt.getIntArray(s)[i];
			storageCells.put(s, temp);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("index", storageIndex);
		for (String s : storageCells.keySet()) {
			int[] temp = new int[6];
			for (int i = 0; i < storageCells.get(s).length; i++) {
				if (storageCells.get(s)[i] != null) {
					temp[i] = storageCells.get(s)[i];
				}
			}
			compound.setIntArray(s, temp);
		}

		return compound;
	}

}