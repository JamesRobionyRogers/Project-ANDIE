package cosc202.andie.actions.colour; 

import java.awt.image.*;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to invert an images colours.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Xavier Nuttall
 * @version 1.0
 */
public class InvertColour implements ImageOperation, java.io.Serializable {

    /**
     * <p>
     * Create a new InvertColour operation.
     * </p>
     */
    public InvertColour() {

    }

    /**
     * <p>
     * Apply colour invertion opperation to the image.
     * </p>
     * 
     * @param input The image to be converted to greyscale
     * @return The resulting greyscale image.
     */
    public BufferedImage apply(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >> 24;
                int r = 255 - ((argb & 0x00FF0000) >> 16);
                int g = 255 - ((argb & 0x0000FF00) >> 8);
                int b = 255 - (argb & 0x000000FF);

                argb = (a << 24) | (r << 16) | (g << 8) | b;
                output.setRGB(x, y, argb);
            }
        }
        
        return output;
    }
    
}
