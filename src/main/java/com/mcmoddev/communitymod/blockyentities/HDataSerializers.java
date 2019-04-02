package com.mcmoddev.communitymod.blockyentities;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public class HDataSerializers {

	public static final DataSerializer<Area> AREA = new DataSerializer<Area>() {

		public void write(PacketBuffer buf, Area value) {
			for (int i : value.serialize()) {
				buf.writeInt(i);
			}
		}

		public Area read(PacketBuffer buf) throws IOException {
			Area temparea = new Area();
			int[] array = new int[6];
			int i = 0;
			while (i < array.length) {
				array[i] = buf.readInt();
				++i;
			}
			temparea.deserialize(array);
			return temparea;
		}

		public DataParameter<Area> createKey(int id) {
			return new DataParameter<>(id, this);
		}

		public Area copyValue(Area value) {
			return value;
		}
	};

	public static void register() {
		DataSerializers.registerSerializer(AREA);
	}

}
