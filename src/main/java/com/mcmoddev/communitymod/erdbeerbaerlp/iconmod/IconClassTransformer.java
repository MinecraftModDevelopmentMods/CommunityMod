package com.mcmoddev.communitymod.erdbeerbaerlp.iconmod;
import static org.objectweb.asm.Opcodes.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.Display;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Frame;

import com.mcmoddev.communitymod.CommunityGlobals;
import com.mcmoddev.communitymod.CommunityMod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;
import scala.tools.asm.Label;

public class IconClassTransformer implements IClassTransformer{
	private static final String[] classesBeingTransformed =
		{
				"net.minecraft.client.Minecraft"
		};


	@Override
	public byte[] transform(String name, String transformedName, byte[] classBeingTransformed)
	{
		boolean isObfuscated = !name.equals(transformedName);
		int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
		return index != -1 ? transform(index, classBeingTransformed, isObfuscated) : classBeingTransformed;
	}

	private static byte[] transform(int index, byte[] classBeingTransformed, boolean isObfuscated)
	{
		try
		{
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(classBeingTransformed);
			classReader.accept(classNode, 0);
			switch(index)
			{

			case 0:
				transformMinecraft(classNode, isObfuscated);
				transformMinecraft2(classNode, isObfuscated);
				break;

			}

			ModClassWriter classWriter = new ModClassWriter(ModClassWriter.COMPUTE_MAXS | ModClassWriter.COMPUTE_FRAMES);
			classNode.accept(classWriter);
			return classWriter.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return classBeingTransformed;
	}
	private static void transformMinecraft(ClassNode classNode, boolean isObfuscated) {
		final String GET = isObfuscated? "func_175609_am":"createDisplay";
		for (MethodNode method : classNode.methods)
		{
			if (method.name.equals(GET) ){
				AbstractInsnNode targetNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray())
				{
					//Deletes the string from "Display.setTitle("Minecraft 1.12.2");"
					if(instruction instanceof LdcInsnNode){
						if(((String)((LdcInsnNode)instruction).cst).startsWith("Minecraft")) {
							targetNode = instruction;
						}
					}
					if(targetNode != null){
						InsnList toInsert = new InsnList();
						//Inserts custom String
						toInsert.add(new LdcInsnNode(CommunityGlobals.gameTitle));
						method.instructions.insertBefore(targetNode, toInsert);
						method.instructions.remove(targetNode);
						break;
					}else{
						//do nothing
					}
				}break;
			}
		}
	}

	private static void transformMinecraft2(ClassNode classNode, boolean isObfuscated) {
		final String GET = isObfuscated ? "func_175594_ao":"setWindowIcon";

		for (MethodNode method : classNode.methods)
		{

			if (method.name.equals(GET)){
				AbstractInsnNode targetNode = null;

				for (AbstractInsnNode instruction : method.instructions.toArray())	
				{
					if(instruction instanceof VarInsnNode && instruction.getNext() instanceof FieldInsnNode){
						if(((FieldInsnNode)instruction.getNext()).desc.equals("Lnet/minecraft/client/resources/DefaultResourcePack;") && ((FieldInsnNode)instruction.getNext()).name.equals(isObfuscated?"field_110450_ap":"defaultResourcePack")) {
							targetNode = instruction;
						}
					}
					if(targetNode != null){
						for(int i = 0; i < 42; i++){
							/*
							 Removes:
						 	inputstream = this.defaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
            				inputstream1 = this.defaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));

            				if (inputstream != null && inputstream1 != null)
            				{
                				Display.setIcon(new ByteBuffer[] {this.readImageToBuffer(inputstream), this.readImageToBuffer(inputstream1)});
            				}
							 */
							targetNode = targetNode.getNext();
							method.instructions.remove(targetNode.getPrevious());
						}
						InsnList toInsert = new InsnList();
						//Add jump code to IconClass.setIcon
						LabelNode label = new LabelNode();//Dummy label as workaround
						toInsert.add(label); 
						toInsert.add(new VarInsnNode(ALOAD, 0));
						toInsert.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/Minecraft", "defaultResourcePack", "Lnet/minecraft/client/resources/DefaultResourcePack;"));
						toInsert.add(new MethodInsnNode(INVOKESTATIC, "com/mcmoddev/communitymod/erdbeerbaerlp/iconmod/IconClass", "setIcon", "(Lnet/minecraft/client/resources/DefaultResourcePack;)V", false));
						method.instructions.insertBefore(targetNode, toInsert);
						break;
					}else{
						//do nothing
					}
				}break;
			}
		}
	}

}
