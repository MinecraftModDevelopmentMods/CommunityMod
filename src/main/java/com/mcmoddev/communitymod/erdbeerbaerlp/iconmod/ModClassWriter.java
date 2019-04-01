package com.mcmoddev.communitymod.erdbeerbaerlp.iconmod;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.Launch;

public class ModClassWriter extends ClassWriter {
	public ModClassWriter(int flags) {
		super(flags);
	}
	
	public ModClassWriter(ClassReader classReader, int flags) {
		super(classReader, flags);
	}
	
	protected String getCommonSuperClass(final String type1, final String type2) {
		Class<?> c, d;
		ClassLoader classLoader = Launch.classLoader;
		try {
			c = Class.forName(type1.replace('/', '.'), false, classLoader);
			d = Class.forName(type2.replace('/', '.'), false, classLoader);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		if (c.isAssignableFrom(d)) {
			return type1;
		}
		if (d.isAssignableFrom(c)) {
			return type2;
		}
		if (c.isInterface() || d.isInterface()) {
			return "java/lang/Object";
		} else {
			do {
				c = c.getSuperclass();
			} while (!c.isAssignableFrom(d));
			return c.getName().replace('.', '/');
		}
	}
}
