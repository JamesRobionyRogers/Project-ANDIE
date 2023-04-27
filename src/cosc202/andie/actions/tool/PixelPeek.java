package cosc202.andie.actions.tool;

import java.awt.image.*;

public class PixelPeek {

    /**
     * Peeks a pixel
     * 
     * Which pixel to peek
     * 
     * (x, y)
     * 
     * @param x coord
     * @param y coord
     * @param image image to search
     */
    public static void pixelPeek(int x, int y, BufferedImage image) {
        if (image == null) return;
        if (x > image.getWidth()) return;
        if (y > image.getHeight()) return;

        int argb = image.getRGB(x, y);
        int a = (argb & 0xFF000000) >> 24;
        int r = (argb & 0x00FF0000) >> 16;
        int g = (argb & 0x0000FF00) >> 8;
        int b = (argb & 0x000000FF);

        System.out.println("("+x+", "+y+") = " + "a: " + a + "r: " + r + ", g: " + g + ", b: " + b);
    }
}
