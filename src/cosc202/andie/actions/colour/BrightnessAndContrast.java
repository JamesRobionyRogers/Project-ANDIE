package cosc202.andie.actions.colour;

import java.awt.image.*;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to change an images Brightness and Contrast.
 * </p>
 * 
 * <p>
 * The images produced are altered versions of the original image, where the rgb
 * values are changed based upon the wanted
 * brightness and contrast change.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Jack McDonnell
 * @version 1.0
 */

public class BrightnessAndContrast implements ImageOperation, java.io.Serializable {

    private double brightness;

    private double contrast;

    /**
     * <p>
     * Create a new BrightnessAndContrast operation.
     * </p>
     */

    public BrightnessAndContrast(int brightness, int contrast) {

        this.brightness = brightness;

        this.contrast = contrast;

    }

    /**
     * <p>
     * Apply a Brightness and Contrast conversion to an image with the given
     * formula.
     * </p>
     * 
     * <p>
     * The conversion of the red, green, and blue values uses a formula given in the
     * lab book.
     * </p>
     * 
     * @param input The image to be altered
     * @return The resulting altered image.
     */

    public BufferedImage apply(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        // loop over each pixels
        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {
                // set rgb at x, y
                output.setRGB(x, y, getARGB(x, y, input));
            }
        }
        return output;
    }

    private int getARGB(int x, int y, BufferedImage input) {
        int argb = input.getRGB(x, y);

        int a = (argb & 0xFF000000) >> 24;

        int r = (argb & 0x00FF0000) >> 16;

        int g = (argb & 0x0000FF00) >> 8;

        int b = (argb & 0x000000FF);

        int red = calculateColour(r);

        int green = calculateColour(g);

        int blue = calculateColour(b);

        // If the pixels new value is out of range, put it at the extreme it went over.
        red = cast(red);
        blue = cast(blue);
        green = cast(green);

        // Assign the new values onto the pixel
        argb = (a << 24) | (red << 16) | (green << 8) | blue;
        return argb;
    }

    // calculates colour based on previous colour value and new contrast and brightness
    private int calculateColour(int x){
        return (int) Math.round(((1 + (contrast / 100)) * (x - 127.5)) + (127.5 * (1 + (brightness / 100))));
    }

    // casts colour value to rgb range
    private int cast(int x){
        if (x > 255) return 255;
        if (x < 0) return 0;
        return x;
    }

}