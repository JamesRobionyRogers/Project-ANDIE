package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Gaussian Blur filter.
 * </p>
 * 
 * <p>
 * A Gaussian Blur filter blurs an image by replacing each pixel by the average of the
 * pixels in a surrounding neighbourhood, and is implemented by a convoloution.
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author James Robiony-Rogers
 * @version 1.0
 */
public class GaussianBlurFilter implements ImageOperation, java.io.Serializable {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a
     * 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Gaussian Blur filter with the given size <code>radius</code>.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed GaussianBlurFilter
     */
    public GaussianBlurFilter(int radius) {
        this.radius = radius;    
    }

    /**
     * <p>
     * Construct a Gaussian Blur filter with the default size of 1
     * </p>
     * 
     * @see GaussianBlurFilter(int)
     */
    public GaussianBlurFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Gaussian Blur filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters the Gaussian Blur filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.
     * Larger radii lead to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Gaussian filter to.
     * @return The resulting blurred image.
     */
    public BufferedImage apply(BufferedImage input) {
        // Calculating the size of the convolution kernel and creating an empty gaussianKernalArray 
        int size = (2*this.radius+1) * (2*this.radius+1);
        float [] gaussianKernalArray = new float[size];

        // Filling the gaussianKernalArray with values based on the Gaussian equation 
        populateKernalArray(gaussianKernalArray); 

        // Normalise the gaussianKernalArray 
        normaliseGaussianArray(gaussianKernalArray);

        // Creating a Kernel from the gaussianKernalArray
        Kernel kernel = new Kernel((2 * this.radius + 1), (2 * this.radius + 1), gaussianKernalArray);

        // Creating a ConvolveOp from the Kernel
        ConvolveOp convOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        // Creating a copy of the input image to store the result of the filter
        // BufferedImage output = new BufferedImage(input.getColorModel(),
            // input.copyData(null), input.isAlphaPremultiplied(), null);
        
        // Applying the filter to the image and storing it in the result
        BufferedImage output = convOp.filter(input, null);
        
        return output;
    }

    /**
     * <p>
     * Populates the <code>gaussianKernalArray</code> with values calculated from the
     * Gaussian equation.
     * </p>
     * 
     * @param gaussianKernalArray The array to populate with values
     * @see gaussianEquation(int, int)
     */
    private void populateKernalArray(float[] gaussianKernalArray) {
        int diameter = (2 * this.radius) + 1; 
        
        // Iterating over the kernal as a 2d array for ease of calulating the gaussian equation
        for (int row = 0; row < diameter; row++) {
            for (int col = 0; col < diameter; col++) {
                // Calculeting the index of the current element in the kernal array
                int index = (row * diameter) + col;

                // Pluggin the (x, y) values into the gassuian equation.
                // Middle of the array is 0. Left/below middle are negitive, right/above middle are positive
                gaussianKernalArray[index] = gaussianEquation((col - this.radius), (row - this.radius));
            }
        }
    }
        
    /**
     * <p>
     * Calculates the value of the Gaussian equation at the given (x, y) point.
     * </p>
     * 
     * @param x the x coordinate of the point to calculate
     * @param y the y coordinate of the point to calculate
     * @return the value of the Gaussian equation at the given (x, y) point
     */
    private float gaussianEquation(int x, int y) {

        // General rule of thumb: varience = 1/3 of the radius
        double variance = (1.0/3.0) * this.radius;

        // Gaussian equation is: 1/(2*pi*variance^2) * e^(-(x^2 + y^2)/(2*variance^2))
        double value = (1/(2*Math.PI*Math.pow(variance, 2)) * Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2))/(2*Math.pow(variance, 2))));
        
        return (float) value;
    }

    /**
     * <p>
     * Calculates the sum of all the values in the <code>gaussianKernalArray</code> 
     * then divides each value by the calculated sum, thus "normalising" the 
     * array. 
     * </p>
     * 
     * @param gaussianKernalArray The array to "normalise"
     */
    private void normaliseGaussianArray(float[] gaussianKernalArray) {
        // Calculating the sum of all the values in the gaussianKernalArray
        float sum = 0;
        for (int i = 0; i < gaussianKernalArray.length; i++) {
            sum += gaussianKernalArray[i];
        }

        // Dividing each value in the gaussianKernalArray by the sum of the array
        for (int i = 0; i < gaussianKernalArray.length; i++) {
            gaussianKernalArray[i] /= sum;
        }

    }

}
