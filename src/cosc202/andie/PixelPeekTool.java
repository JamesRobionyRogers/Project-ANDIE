package cosc202.andie;

import java.awt.image.*;

public class PixelPeekTool implements ImageOperation, java.io.Serializable {

    private int y;
    private int x;

    /**
     * Peeks a pixel
     * 
     * Default peeks (0, 0)
     * 
     * @see ResizeTool(int)
     */
    PixelPeekTool() {
        this(0,0);
    }

    /**
     * Peeks a pixel
     * 
     * Which pixel to peek
     * 
     * (x, y)
     * 
     * @param x coord
     * @param y coord
     */
    PixelPeekTool(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Resize the image
     * 
     * The tool creates a scaled image then applies it to a buffers graphic.
     * 
     * @param input The image to apply the resize to
     * @return The resulting image
     */
    public BufferedImage apply(BufferedImage input) {

        int argb = input.getRGB(x, y);
        int a = (argb & 0xFF000000) >> 24;
        int r = (argb & 0x00FF0000) >> 16;
        int g = (argb & 0x0000FF00) >> 8;
        int b = (argb & 0x000000FF);

        System.out.println("("+this.x+", "+this.y+") = " + "a: " + a + "r: " + r + ", g: " + g + ", b: " + b);
        return input;

    }

}
