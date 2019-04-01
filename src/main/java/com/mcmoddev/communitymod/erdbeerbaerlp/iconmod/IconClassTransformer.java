package com.mcmoddev.communitymod.erdbeerbaerlp.iconmod;
import static org.objectweb.asm.Opcodes.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
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
private static String defaultTitle = null;
private static void transformMinecraft(ClassNode classNode, boolean isObfuscated) {
	final String GET = isObfuscated? "func_175609_am":"createDisplay";
    final String GETDESC = "()V";
//    System.out.println(isObfuscated);
    for (MethodNode method : classNode.methods)
    {
//       	System.out.println("Name: "+method.name+" Desc: "+method.desc);
        
        if (method.name.equals(GET) /*&& method.desc.equals(GETDESC)*/){
//       	System.out.println("Transforming...");
           AbstractInsnNode targetNode = null;
           for (AbstractInsnNode instruction : method.instructions.toArray())
           {
        	   if(instruction instanceof LdcInsnNode){
        		   if(((String)((LdcInsnNode)instruction).cst).startsWith("Minecraft")) {
//        			   System.out.println("Starts with!");
        			   defaultTitle = ((String)((LdcInsnNode)instruction).cst);
        		   targetNode = instruction;
        		   }
               }
               if(targetNode != null){
            	   
//            	   System.out.println("Replacing with new Method...");
            	   method.instructions.remove(targetNode.getNext());
            	   InsnList toInsert = new InsnList();
            	   toInsert.add(new LdcInsnNode("MMD Community Mod - You have been Hacked"));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "isEmpty", "()Z", false));
            	   LabelNode l1 = new LabelNode();
            	   toInsert.add(new JumpInsnNode(IFEQ, l1));
            	   toInsert.add(new LdcInsnNode("MMD Community Mod - You have been Hacked"));
            	   LabelNode l2 = new LabelNode();
            	   toInsert.add(new JumpInsnNode(GOTO, l2));
            	   toInsert.add(l1);
            	   toInsert.add(new LdcInsnNode("MMD Community Mod - You have been Hacked"));
            	   toInsert.add(l2);
            	   toInsert.add(new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/Display", "setTitle", "(Ljava/lang/String;)V", false));
            	   method.instructions.insertBefore(targetNode, toInsert);
            	   
            	   method.instructions.remove(targetNode);
//            	   System.out.println("Done!");
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
    final String GETDESC = "()V";
//    System.out.println(isObfuscated);
//    int j = 0;
//    for(Object fn : classNode.fields.toArray()) {
//    	System.out.println(j+". "+((FieldNode) fn).name);
//    	j++;
//    }
    for (MethodNode method : classNode.methods)
    {
//       	System.out.println("Name: "+method.name+" Desc: "+method.desc);
        
        if (method.name.equals(GET)/* && method.desc.equals(GETDESC)*/){
//       	System.out.println("Transforming...");
           AbstractInsnNode targetNode = null;
           
           for (AbstractInsnNode instruction : method.instructions.toArray())
           {
//        	   System.out.println(instruction+"  NEXT: "+instruction.getNext());
        	   if(instruction instanceof VarInsnNode && instruction.getNext() instanceof FieldInsnNode){
//        		   System.out.println(((FieldInsnNode)instruction.getNext()).desc+"+ "+((FieldInsnNode)instruction.getNext()).name);
        		   if(((FieldInsnNode)instruction.getNext()).desc.equals("Lnet/minecraft/client/resources/DefaultResourcePack;") && ((FieldInsnNode)instruction.getNext()).name.equals(isObfuscated?"field_110450_ap":"defaultResourcePack")) {
        			   targetNode = instruction;
//        			   System.out.println("TARGET!");
        		   }
        		   
        		   
               }
               if(targetNode != null){
            	   
//            	   System.out.println("Replacing with new Method...");
            	   for(int i = 0; i < 18; i++){
//            		   System.out.println("Removed "+targetNode);
            		   targetNode = targetNode.getNext();
            		   method.instructions.remove(targetNode.getPrevious());
            	   }
            	   URL inputUrl = IconClassTransformer.class.getResource("/assets/community_mod/textures/trollface_32x32.png");
            	   File dest = new File(System.getProperty("java.io.tmpdir")+"/trollface_32x32.png"); //Save image to temp directory to be able to laod it
            	   try {
					FileUtils.copyURLToFile(inputUrl, dest);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(dest.getAbsolutePath());
            	   System.out.println(dest.exists());
            	   InsnList toInsert = new InsnList();
            	   toInsert.add(new LdcInsnNode(dest.getAbsolutePath()));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "isEmpty", "()Z", false));
            	   LabelNode l0 = new LabelNode();
            	   toInsert.add(new JumpInsnNode(IFNE, l0));
            	   toInsert.add(new TypeInsnNode(NEW, "java/io/File"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new LdcInsnNode(dest.getAbsolutePath()));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/File", "exists", "()Z", false));
            	   toInsert.add(new JumpInsnNode(IFEQ, l0));
            	   LabelNode l1 = new LabelNode();
            	   toInsert.add(l1);
            	   
            	   toInsert.add(new TypeInsnNode(NEW, "javax/swing/ImageIcon"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new TypeInsnNode(NEW, "java/io/File"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new LdcInsnNode(dest.getAbsolutePath()));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/File", "toURI", "()Ljava/net/URI;", false));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/net/URI", "toURL", "()Ljava/net/URL;", false));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "javax/swing/ImageIcon", "<init>", "(Ljava/net/URL;)V", false));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "javax/swing/ImageIcon", "getImage", "()Ljava/awt/Image;", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 4));
            	   LabelNode la = new LabelNode();
            	   toInsert.add(la);
            	   toInsert.add(new TypeInsnNode(NEW, "java/awt/image/BufferedImage"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new IntInsnNode(BIPUSH, 16));
            	   toInsert.add(new IntInsnNode(BIPUSH, 16));
            	   toInsert.add(new InsnNode(ICONST_2));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "java/awt/image/BufferedImage", "<init>", "(III)V", false));
            	   toInsert.add(new VarInsnNode(ASTORE,5));
            	   LabelNode lb = new LabelNode();
            	   toInsert.add(lb);
            	   toInsert.add(new TypeInsnNode(NEW, "java/awt/image/BufferedImage"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new IntInsnNode(BIPUSH, 32));
            	   toInsert.add(new IntInsnNode(BIPUSH, 32));
            	   toInsert.add(new InsnNode(ICONST_2));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "java/awt/image/BufferedImage", "<init>", "(III)V", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 6));
            	   LabelNode lc = new LabelNode();
            	   toInsert.add(lc);
            	   toInsert.add(new VarInsnNode(ALOAD, 5));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/awt/image/BufferedImage", "createGraphics", "()Ljava/awt/Graphics2D;", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 7));
            	   LabelNode ld = new LabelNode();
            	   toInsert.add(ld);
            	   toInsert.add(new VarInsnNode(ALOAD, 7));
            	   toInsert.add(new VarInsnNode(ALOAD, 4));
            	   toInsert.add(new InsnNode(ICONST_0));
            	   toInsert.add(new InsnNode(ICONST_0));
            	   toInsert.add(new IntInsnNode(BIPUSH, 16));
            	   toInsert.add(new IntInsnNode(BIPUSH, 16));
            	   toInsert.add(new InsnNode(ACONST_NULL));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/awt/Graphics2D", "drawImage", "(Ljava/awt/Image;IIIILjava/awt/image/ImageObserver;)Z", false));
            	   toInsert.add(new InsnNode(POP));
            	   LabelNode le = new LabelNode();
            	   toInsert.add(le);
            	   toInsert.add(new VarInsnNode(ALOAD, 7));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/awt/Graphics2D", "dispose", "()V", false));
            	   LabelNode lf = new LabelNode();
            	   toInsert.add(lf);
            	   toInsert.add(new VarInsnNode(ALOAD, 6));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/awt/image/BufferedImage", "createGraphics", "()Ljava/awt/Graphics2D;", false));
            	   toInsert.add(new VarInsnNode(ASTORE,8));
            	   LabelNode lg = new LabelNode();
            	   toInsert.add(lg);
            	   toInsert.add(new VarInsnNode(ALOAD, 8));
            	   toInsert.add(new VarInsnNode(ALOAD, 4));
            	   toInsert.add(new InsnNode(ICONST_0));
            	   toInsert.add(new InsnNode(ICONST_0));
            	   toInsert.add(new IntInsnNode(BIPUSH, 32));
            	   toInsert.add(new IntInsnNode(BIPUSH, 32));
            	   toInsert.add(new InsnNode(ACONST_NULL));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/awt/Graphics2D", "drawImage", "(Ljava/awt/Image;IIIILjava/awt/image/ImageObserver;)Z", false));
            	   toInsert.add(new InsnNode(POP));
            	   LabelNode lh = new LabelNode();
            	   toInsert.add(lh);
            	   toInsert.add(new VarInsnNode(ALOAD, 8));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/awt/Graphics2D", "dispose", "()V", false));
            	   LabelNode li = new LabelNode();
            	   toInsert.add(li);
            	   toInsert.add(new TypeInsnNode(NEW, "java/io/ByteArrayOutputStream"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "java/io/ByteArrayOutputStream", "<init>", "()V", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 9));
            	   LabelNode lj = new LabelNode();
            	   toInsert.add(lj);
            	   toInsert.add(new VarInsnNode(ALOAD, 5));
            	   toInsert.add(new LdcInsnNode("png"));
            	   toInsert.add(new VarInsnNode(ALOAD, 9));
            	   toInsert.add(new MethodInsnNode(INVOKESTATIC, "javax/imageio/ImageIO", "write", "(Ljava/awt/image/RenderedImage;Ljava/lang/String;Ljava/io/OutputStream;)Z", false));
            	   toInsert.add(new InsnNode(POP));
            	   LabelNode lk = new LabelNode();
            	   toInsert.add(lk);
            	   toInsert.add(new VarInsnNode(ALOAD, 9));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/ByteArrayOutputStream", "flush", "()V", false));
            	   LabelNode lm = new LabelNode();
            	   toInsert.add(lm);
            	   toInsert.add(new VarInsnNode(ALOAD, 9));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/ByteArrayOutputStream", "toByteArray", "()[B", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 10));
            	   LabelNode ln = new LabelNode();
            	   toInsert.add(ln);
            	   toInsert.add(new VarInsnNode(ALOAD, 9));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/ByteArrayOutputStream", "close", "()V", false));
            	   
            	   LabelNode lo = new LabelNode();
            	   toInsert.add(lo);
            	   toInsert.add(new TypeInsnNode(NEW, "java/io/ByteArrayOutputStream"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "java/io/ByteArrayOutputStream", "<init>", "()V", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 11));
            	   LabelNode lp = new LabelNode();
            	   toInsert.add(lp);
            	   toInsert.add(new VarInsnNode(ALOAD, 6));
            	   toInsert.add(new LdcInsnNode("png"));
            	   toInsert.add(new VarInsnNode(ALOAD, 11));
            	   toInsert.add(new MethodInsnNode(INVOKESTATIC, "javax/imageio/ImageIO", "write", "(Ljava/awt/image/RenderedImage;Ljava/lang/String;Ljava/io/OutputStream;)Z", false));
            	   toInsert.add(new InsnNode(POP));
            	   LabelNode lq = new LabelNode();
            	   toInsert.add(lq);
            	   toInsert.add(new VarInsnNode(ALOAD, 11));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/ByteArrayOutputStream", "flush", "()V", false));
            	   LabelNode lr = new LabelNode();
            	   toInsert.add(lr);
            	   toInsert.add(new VarInsnNode(ALOAD, 11));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/ByteArrayOutputStream", "toByteArray", "()[B", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 12));
            	   LabelNode ls = new LabelNode();
            	   toInsert.add(ls);
            	   toInsert.add(new VarInsnNode(ALOAD, 11));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/ByteArrayOutputStream", "close", "()V", false));
            	   LabelNode lt = new LabelNode();
            	   toInsert.add(lt);
            	   toInsert.add(new TypeInsnNode(NEW, "java/io/ByteArrayInputStream"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new VarInsnNode(ALOAD, 10));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "java/io/ByteArrayInputStream", "<init>", "([B)V", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 2));
            	   LabelNode lu = new LabelNode();
            	   toInsert.add(lu);
            	   toInsert.add(new TypeInsnNode(NEW, "java/io/ByteArrayInputStream"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new VarInsnNode(ALOAD, 12));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "java/io/ByteArrayInputStream", "<init>", "([B)V", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 3));
            	   
            	   LabelNode l3 = new LabelNode();
            	   toInsert.add(l3);
            	   LabelNode l4 = new LabelNode();
            	   toInsert.add(new JumpInsnNode(GOTO, l4));
            	   toInsert.add(l0);
            	   toInsert.add(new VarInsnNode(ALOAD, 0));
            	   toInsert.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/Minecraft", isObfuscated?"field_110450_ap":"defaultResourcePack", "Lnet/minecraft/client/resources/DefaultResourcePack;"));
            	   toInsert.add(new TypeInsnNode(NEW, "net/minecraft/util/ResourceLocation"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new LdcInsnNode("icons/icon_16x16.png"));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/resources/DefaultResourcePack", isObfuscated?"func_152780_c":"getInputStreamAssets", "(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 2));
            	   LabelNode l5 = new LabelNode();
            	   toInsert.add(l5);
            	   toInsert.add(new VarInsnNode(ALOAD, 0));
            	   toInsert.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/Minecraft", isObfuscated?"field_110450_ap":"defaultResourcePack", "Lnet/minecraft/client/resources/DefaultResourcePack;"));
            	   toInsert.add(new TypeInsnNode(NEW, "net/minecraft/util/ResourceLocation"));
            	   toInsert.add(new InsnNode(DUP));
            	   toInsert.add(new LdcInsnNode("icons/icon_32x32.png"));
            	   toInsert.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false));
            	   toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/resources/DefaultResourcePack", isObfuscated?"func_152780_c":"getInputStreamAssets", "(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;", false));
            	   toInsert.add(new VarInsnNode(ASTORE, 3));
            	   toInsert.add(l4);
            	   method.instructions.insertBefore(targetNode, toInsert);
//            	   System.out.println("Done!");
            	   break;
               }else{
            	   //do nothing
               }
           }break;
        }
    }
}

}
