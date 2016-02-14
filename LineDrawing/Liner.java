import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Liner {
	public static void main(String[] args) throws IOException {
		BufferedImage anIm = ImageIO.read(new File("i1ii5P4.png"));
		createBlue(anIm);
		
		BufferedImage bi = new BufferedImage(anIm.getWidth(), anIm.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		g.dispose();
		
		int[] ori = new int[anIm.getWidth()*anIm.getHeight()];
		createArray(anIm, ori);
		
		int[] array1 = new int[anIm.getWidth()*anIm.getHeight()];
		
		long bestScore = Long.MAX_VALUE;
		int q = 0;
		do {
			q += 1;
			BufferedImage best = deepCopy(bi);
			for (int r = 0; r < 30; r++) {
				BufferedImage copy = deepCopy(bi);
				drawRandomLine(copy);
				array1 = createArray(copy, array1);
				long newScore = score(ori, array1);
				if (newScore < bestScore) {
					best = deepCopy(copy);
					bestScore = newScore;
				}
			}
			System.out.println(bestScore-200000000);
			System.out.println(q);
			bi = deepCopy(best);
		} while (bestScore-200000000 > 0);

		ImageIO.write(bi, "png", new File("out.png"));
	}

	static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static void createBlue(BufferedImage anImg) {
		int[] anArray = new int[anImg.getWidth() * anImg.getHeight()];
		anArray = createArray(anImg, anArray);
		for (int q = 0; q < anArray.length; q++) {
			anArray[q] = anArray[q] & 0xFF0000FF;
		}
		anImg.setRGB(0, 0, anImg.getWidth(), anImg.getHeight(), anArray, 0, anImg.getWidth());
	}

	public static int[] createArray(BufferedImage anImg, int[] array) {
		return anImg.getRGB(0, 0, anImg.getWidth(), anImg.getHeight(), array, 0, anImg.getWidth());
	}

	public static long score(int[] ori, int[] comp) {
		long score = 0;
		for (int q = 0; q < ori.length; q++) {
			score += Math.abs((ori[q] & 0xff) - (comp[q] & 0xff));
		}
		return score;
	}

	public static void drawRandomLine(BufferedImage anImage) {
		Graphics2D g = (Graphics2D) anImage.createGraphics();
		g.setColor(new Color(0f,0f,0f,(float)Math.random()*0.3f +0.2f));
		
		double x1 = Math.random() * anImage.getWidth();
		double x2 = Math.random() * anImage.getWidth();
		double y1 = Math.random() * anImage.getHeight();
		double y2 = Math.random() * anImage.getHeight();
		
		// left top = 0;
		// left right = 1;
		// left bot = 2;
		// top right = 3;
		// top bot = 4;
		// right bot = 5;
		switch((int)(Math.random()*6)) {
		case 0:
			x1 = 0;
			y2 = 0;
			break;
		case 1:
			x1 = 0;
			x2 = anImage.getWidth();
			break;
		case 2:
			x1 = 0;
			y2 = anImage.getHeight();
			break;
		case 3:
			y1 = 0;
			x2 = anImage.getWidth();
			break;
		case 4:
			y1 = 0;
			y2 = anImage.getHeight();
			break;
		case 5:
			x1 = anImage.getWidth();
			y2 = anImage.getHeight();
			break;
		}
		
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		g.dispose();
	}

}
