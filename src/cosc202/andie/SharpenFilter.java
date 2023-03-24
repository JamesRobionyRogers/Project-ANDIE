package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a Sharpen filter.
 * <p>
 * <p>
 * A sharpen filter enhances difference between neighbouring values and is 
 * implemented by a convulution.
 * <p>
 * <p>
 * Sharpen filter has no user defined parameters
 * <p>
 */

public class SharpenFilter implements ImageOperation, java.io.Serializable{
    
    /**
     * <p>
     * Construct a sharpen filter
     * 
     */
    SharpenFilter() {
    }
      /**
     * <p>
     * Apply a sharpen filter to an image.
     * </p>
     * 
     * <p>
     * Sharpen filter is implemented via convulution.
     * The size of the convolution kernel is fixed (hardcoded).  
     * </p>
     * 
     * @param input The image to apply the sharpen filter to.
     * @return The resulting (sharpened)) image.
     */
    public BufferedImage apply(BufferedImage input) {
        float [] array = {0, (float)-1/2, 0,
                         (float)-1/2, 3, (float)-1/2,
                         0, (float)-1/2, 0};

        Kernel kernel = new Kernel(3, 3, array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(input, output);
        return output;
    }

}
