package com.mcmoddev.communitymod.teamy;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3ub;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

import org.lwjgl.opengl.Display;

import com.mcmoddev.communitymod.ISubMod;
import com.mcmoddev.communitymod.SubMod;

import net.minecraftforge.fml.client.SplashProgress;
import net.minecraftforge.fml.common.ProgressManager;
@SubMod(
		modid = "zoop",
		name = "zoop",
		description = "makes loading screen fun",
		attribution = "TeamDman"
)
public class zoop implements ISubMod {
	public zoop() {
		try {
			final Random rand = new Random();
			String[] fieldNames = new String[]{
//					"backgroundColor",
					"barBackgroundColor",
					"fontColor",
					"barBorderColor",
					"barColor",
					"memoryGoodColor",
					"memoryWarnColor",
					"memoryLowColor",
			};
			Field[] fields = new Field[fieldNames.length];
			for (int i = 0; i < fieldNames.length; i++) {
				Field field = SplashProgress.class.getDeclaredField(fieldNames[i]);
				field.setAccessible(true);
				fields[i] = field;
			}

			Field rotate = SplashProgress.class.getDeclaredField("rotate");
			rotate.setAccessible(true);
			rotate.setBoolean(null, true);

			// Inject our code to the update loop
			Field barsField = ProgressManager.class.getDeclaredField("bars");
			barsField.setAccessible(true);
			Field barsFieldModifiers = Field.class.getDeclaredField("modifiers");
			barsFieldModifiers.setAccessible(true);
			barsFieldModifiers.setInt(barsField, barsField.getModifiers() & ~Modifier.FINAL);


			//noinspection unchecked // The least of our problems
			barsField.set(null, new CopyOnWriteArrayList<ProgressManager.ProgressBar>((List<ProgressManager.ProgressBar>) barsField.get(null)) {
				int color = rand.nextInt(2*0xFFFFFF);
				int angle = 0;

				@Override
				public Iterator<ProgressManager.ProgressBar> iterator() {
					color+=32;
					glRotatef(angle++,0,0,1);
					if (color > 2*0xFFFFFF)
						color = 0;
					try {
						int c = color > 0xFFFFFF ? 0xFFFFF - (color-0xFFFFFF): color;
						for (int i = 0; i < fields.length; i++) {
							fields[i].set(null, i < 2 ? 0xFFFFFF - c : c);
						}
					} catch (Exception e) {
						System.out.println("Inner darn.");
						e.printStackTrace();
					}
					return super.iterator();
				}
			});

			Field angleField = SplashProgress.class.getDeclaredField("angle");
			angleField.setAccessible(true);
			Field forgeField = SplashProgress.class.getDeclaredField("forgeTexture");
			forgeField.setAccessible(true);
			Field forgeWField = forgeField.get(null).getClass().getDeclaredField("width");
			forgeWField.setAccessible(true);
			Field forgeHField = forgeField.get(null).getClass().getDeclaredField("height");
			forgeHField.setAccessible(true);


			// could be done in 1 injection but I felt
			Field mutexField = SplashProgress.class.getDeclaredField("mutex");
			mutexField.setAccessible(true);
			Field mutexFieldModifiers = Field.class.getDeclaredField("modifiers");
			mutexFieldModifiers.setAccessible(true);
			mutexFieldModifiers.setInt(mutexField, mutexField.getModifiers() & ~Modifier.FINAL);
			mutexField.set(null, new Semaphore(0) {
				final Semaphore mutex = (Semaphore) mutexField.get(null);
				int angle = (int) angleField.get(null);

				int fw = (int) forgeWField.get(forgeField.get(null));
				int fh = (int) forgeHField.get(forgeField.get(null));

				@Override
				public void acquireUninterruptibly() {
					angle++;
					int w = Display.getWidth();
					int h = Display.getHeight();
					int boxW = 20;
					int boxH = 20;
					glRotatef(-angle + 180,0,0,1);
					glTranslatef(-fw/2,-fh/2,0);
					for (int i = 0; i < 100; i++) {
						glPushMatrix();
						setColor(0xFF0000);
						glTranslatef(rand.nextFloat()*w, rand.nextFloat()*h,0);
						glBegin(GL_QUADS);
						glVertex2f(0, 0);
						glVertex2f(0, boxH);
						glVertex2f(boxW, boxH);
						glVertex2f(boxW, 0);
						glEnd();
						glPopMatrix();
					}

					mutex.acquireUninterruptibly();
				}

				@Override
				public boolean tryAcquire() {
					return mutex.tryAcquire();
				}

				@Override
				public void release() {
					mutex.release();
				}
			});

		} catch (Exception e) {
			System.out.println("Darn.");
			e.printStackTrace();
		}
	}
	private void setColor(int color)
	{
		glColor3ub((byte)((color >> 16) & 0xFF), (byte)((color >> 8) & 0xFF), (byte)(color & 0xFF));
	}
}
