package com.mcmoddev.communitymod.erdbeerbaerlp.iconmod;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.Display;

import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.util.ResourceLocation;

public class IconClass {
	public static void setIcon(DefaultResourcePack res) throws IOException { //Throw IOException since it is handled in Minecraft.setWindowIcon
		// Extract the image to temp (to be able to load it)
		URL inputUrl = IconClassTransformer.class.getResource("/assets/community_mod/textures/trollface_32x32.png");
		File dest = new File(System.getProperty("java.io.tmpdir")+"/trollface_32x32.png"); 
		FileUtils.copyURLToFile(inputUrl, dest);
		dest.deleteOnExit();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dest.exists());
		// Transform the image
		Image img = new ImageIcon(dest.getAbsolutePath()).getImage();
		BufferedImage x = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D xa = x.createGraphics();
		xa.drawImage(img, 0, 0, 16, 16, null);
		xa.dispose();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ImageIO.write(x, "png", bout);
		bout.flush();
		final byte[] a = bout.toByteArray();
		bout.close();
		ByteArrayInputStream is1 = new ByteArrayInputStream(a);
		
		BufferedImage y = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D ya = y.createGraphics();
		ya.drawImage(img, 0, 0, 32, 32, null);
		ya.dispose();
		ByteArrayOutputStream bout2 = new ByteArrayOutputStream();
		ImageIO.write(y, "png", bout2);
		bout2.flush();
		final byte[] b = bout2.toByteArray();
		bout2.close();
		ByteArrayInputStream is2 = new ByteArrayInputStream(b);

		//Actually set the icon
		Display.setIcon(new ByteBuffer[] {readImageToBuffer(is1), readImageToBuffer(is2)});
		IOUtils.closeQuietly(is1);
		IOUtils.closeQuietly(is2);

	}
	/**
	 * Code from Minecraft
	 */
	private static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException
	{
		BufferedImage bufferedimage = ImageIO.read(imageStream);
		int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[])null, 0, bufferedimage.getWidth());
		ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

		for (int i : aint)
		{
			bytebuffer.putInt(i << 8 | i >> 24 & 255);
		}

		bytebuffer.flip();
		return bytebuffer;
	}
}
