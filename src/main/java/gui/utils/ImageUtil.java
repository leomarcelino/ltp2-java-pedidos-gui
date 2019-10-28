package gui.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {

	public static BufferedImage loadImage(String name) throws IOException {
		try {
			return ImageIO.read(ImageUtil.class.getResource("/images/" + name));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ImageIcon loadIcon(String name) {
		try {
			return new ImageIcon(loadImage(name));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
