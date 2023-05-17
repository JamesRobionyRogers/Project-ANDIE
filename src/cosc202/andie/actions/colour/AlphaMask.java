package cosc202.andie.actions.colour;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;


/**
 * <p>
 * ImageOperation to apply an alpha mask.
 * </p>
 * 
 * <p>
 * A masking filter which applies an alpha mask to the image.
 * This loops over the image multiplying the image alpha values by
 * the masks greyscale values.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see ConvertToGrey
 * @author Xavier Nuttall
 * @version 1.2
 */
public class AlphaMask implements ImageOperation, java.io.Serializable {
    private int[] maskStore; // Array to store the serialized mask image
    private int width; // Width of the mask image
    private int height; // Height of the mask image


    /**
     * Constructs an AlphaMask object with the provided mask image.
     *
     * @param mask The mask image to be applied.
     */
    public AlphaMask(BufferedImage mask) {
        // Copy the image to preserve the original
        BufferedImage colourMask = new BufferedImage(mask.getColorModel(), mask.copyData(null), mask.isAlphaPremultiplied(), null);

        // Convert the mask image to grayscale
        ConvertToGrey grey = new ConvertToGrey();

        // Convert the image to a serializable int array (ops files are now massive)
        maskStore = grey.apply(colourMask).getRGB(0, 0, colourMask.getWidth(), colourMask.getHeight(), null, 0, colourMask.getWidth());
        
        width = colourMask.getWidth();
        height = colourMask.getHeight();
    }

    /**
     * Applies the alpha mask to the input image.
     *
     * @param input The input image to apply the alpha mask to.
     * @return The resulting image with the alpha mask applied.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        
        BufferedImage scaledMask = scaledMask(input);
        
        // Creatse a new BufferedImage with an alpha channel, copy the input image onto it.
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gfx =  output.createGraphics();
        gfx.drawImage(input, 0, 0, null);
        gfx.dispose();

        // Apply the mask pixel by pixel
        for (int x = 0; x < output.getWidth(); x++) {
            for (int y = 0; y < output.getHeight(); y++) {
                output.setRGB(x, y, maskPixel(scaledMask, output, x, y)); // Apply the mask to the pixel
            }
        }
        return output;
    }

    /**
     * Scales the mask image to match the size of the input image.
     *
     * @param input The input image.
     * @return The scaled mask image.
     */
    private BufferedImage scaledMask(BufferedImage input) {
        BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        // Create the mask from the serialized int array
        mask.setRGB(0, 0, width, height, maskStore, 0, width);

        // Scale the mask to match the size of the input image
        Image scaled = mask.getScaledInstance(input.getWidth(), input.getHeight(), Image.SCALE_SMOOTH);

        // Create an output image with the same dimensions as the input image
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), mask.getType());
        Graphics2D g2d = output.createGraphics();

        // Draw the scaled mask onto the output image
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();

        return output;
    }

    /**
     * Applies the alpha mask to a pixel in the output image.
     *
     * @param mask   The scaled mask image.
     * @param output The output image.
     * @param x      The x-coordinate of the pixel.
     * @param y      The y-coordinate of the pixel.
     * @return The pixel value with the alpha mask applied.
     */
    private int maskPixel(BufferedImage mask, BufferedImage output, int x, int y) {
        // Get the pixel values of both the mask and the output image
        int rgb = output.getRGB(x, y);
        int alpha = mask.getRGB(x, y);

        // Extract the alpha channel value from the output image
        int imageAlpha = (rgb & 0xFF000000) >>> 24;

        // Calculate the combined mask and alpha value
        float maskMulti = (255 - (alpha & 0x000000FF)) / 255f;
        int alphaOut = (int) (imageAlpha * maskMulti);

        // Combine the alpha value with the RGB values of the pixel
        return alphaOut << 24 | rgb & 0x00FFFFFF;
    }
}
