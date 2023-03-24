package cosc202.andie;

import java.awt.Image;
import java.awt.image.*;
import java.awt.Graphics2D;

/**
 * Resize Tool
 * 
 * Resize the image by a given percentage
 * 
 * @author Xavier Nuttall
 * @version 1.0
 */
public class ResizeTool implements ImageOperation, java.io.Serializable {

    /**
     * How much to scale the image by. 150% = 1.5x
     */
    private int scale;

    /**
     * Construct a Scale Tool with default size.
     * 
     * Default scales by nothing
     * 
     * @see ResizeTool(int)
     */
    ResizeTool() {
        this(100);
    }

    /**
     * Construct a Scale Rool with given scale
     * 
     * How much to scale by
     * 
     * Larger the scale larger the image
     * 
     * @param scale How much to scale the image by as a percentage
     */
    ResizeTool(int scale) {
        this.scale = scale;
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

        int height, width;

        // make sure scaling doesn't get larger than int max value
        // if double is larger than MAX_INTEGER will be downscaled to there
        height = (int) (input.getHeight() * (double) this.scale / 100);
        width = (int) (input.getWidth() * (double) this.scale / 100);

        if (width == Integer.MAX_VALUE || height == Integer.MAX_VALUE) {
            System.out.println("Scaled past max");
            return input;
        }

        if (width == 0 || height == 0){
            System.out.println("0x0 Image error");
            return input;
        }

        // create scaled image
        Image scaled = input.getScaledInstance(width, height, 0);

        // create output slate
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = output.createGraphics();

        // draw onto slate
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();
        return output;

    }

}
