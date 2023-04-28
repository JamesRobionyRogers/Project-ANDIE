package cosc202.andie.actions.filter;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayDeque;

/**
 * This class implements a kernel for a convolution, changing images in a given
 * way. For edges of the image it will ignore any values
 * which exist outside the images border and re-normalise the remaining kernel.
 */
public class ConvOpEdge{

    Kernel ker;
    int radius;
    float[] flatKer;
    /**
     * Constructs a {@code ConvOpEdge}  with given kernel
     * @param ker Kernel for convolution this should NEVER sum any non zero elements 
     * to zero at any point when masked by the image edges.
     */
    public ConvOpEdge(Kernel ker) {
        this.ker = ker;
        int kerWidth = ker.getWidth();
        this.radius = kerWidth / 2;
        flatKer = ker.getKernelData(null);
    }

    /**
     * Non-destructively applies convolution to an input image.
     * 
     * @param input BufferedImage to apply convolution to (unchanged).
     * @return convolved image
     */
    public BufferedImage filter(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null),
                input.isAlphaPremultiplied(), null);
        filter(input, output);
        return output;
    }

    /**
     * Applies kernel convolution to the input image and saves it as an output.
     * 
     * @param input  BufferedImage to apply convolution from (unchanged).
     * @param output BufferedImage to apply convolution to (changed).
     * @return 1 on success, -1 on failure input and output were different sizes
     */
    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        if (output == null) {
            return filter(input);
        }
        int width = input.getWidth();
        int height = input.getHeight();
        // input and output not equal size
        if (width != output.getWidth() || height != output.getHeight()) {
            return null;
        }
        // fast convolution centre of image
        ConvolveOp conv = new ConvolveOp(ker, ConvolveOp.EDGE_ZERO_FILL, null);
        conv.filter(input, output);

        // Top and bottom of image
        {
            // Horizontal Operations (whole width)
            ArrayDeque<Integer> horizontalOps = new ArrayDeque<Integer>(width);
            for (int i = 0; i < width; horizontalOps.add(i++));

            // Vertical Operations
            ArrayDeque<Integer> verticalOps = new ArrayDeque<Integer>(radius * 2);

            // Image top for kernel height
            for (int i = 0; i < radius && i < height; verticalOps.add(i++));

            // Image bottom for kernel height
            for (int i = height - 1; i > height - radius - 1 && i > 0; verticalOps.add(i--));
            horizontalOps.parallelStream().forEach(xNum -> {
                verticalOps.parallelStream().forEach(yNum -> {
                    output.setRGB(xNum, yNum, kernelIter(xNum, yNum, input));
                });
            });
        }
        // Sides of image
        {
            // Vertical Operations (all height - top and bot already done)
            ArrayDeque<Integer> verticalOps = new ArrayDeque<Integer>(height);
            for (int i = radius; i < height - radius; verticalOps.add(i++));

            // Horizontal Operations
            ArrayDeque<Integer> horizontalOps = new ArrayDeque<Integer>(radius * 2);

            // Image left for kernel width
            for (int i = 0; i < radius && i < width; horizontalOps.add(i++));


            // Image right for kernel width

            for (int i = width - 1; i > (width - radius-1) && i > 0; horizontalOps.add(i--));

            verticalOps.parallelStream().forEach(yNum -> {
                horizontalOps.parallelStream().forEach(xNum -> {
                    //System.out.println("NOTHING SHOULD HAVE HAPPENED");
                    output.setRGB(xNum, yNum, kernelIter(xNum, yNum, input));

                });
            });
        }
         
        return output;
    }

    /**
     * Applies kernel to the specified pixel
     * this works even if the kernel steps out of the image, it will renormalise the
     * kernel image intersection
     * 
     * @param x     the x pixel
     * @param y     the y pixel
     * @param input image to get the pixel from
     * @return the pixel rgb after convolution
     */
    private int kernelIter(int x, int y, BufferedImage input) {
        int counter = 0;
        
        // r,g,b values of pixels
        float r, g, b;
        r = g = b = 0;
        int argb;

        // step over each relevant pixel and apply kernel to it
        float norm = 0;
        for (int i = y - radius; i < y + radius + 1; i++) {
            if (i >= 0 && i < input.getHeight()) {
                for (int j = x - radius; j < x + radius + 1; j++) {
                    if (j >= 0 && j < input.getWidth()) {
                        // get colour of image
                        argb = input.getRGB(j, i);

                        // assign to store total
                        r += ((argb & 0x00FF0000) >> 16) * flatKer[counter];
                        g += ((argb & 0x0000FF00) >> 8) * flatKer[counter];
                        b += (argb & 0x000000FF) * flatKer[counter];
                        norm += flatKer[counter];
                    }
                    counter++;
                }
            }else{
                counter+=radius*2+1;
            }
            
        }

        // sets alpha not 100% sure this is the correct value for us
        int totalA = -1;

        // applies normalisation

        // if norm is 0 then r,g,b will also necessarily be 0 so this gives 0.0f/0.0f which will give NaN
        r /= norm;
        g /= norm;
        b /= norm;

        // casting NaN to int will give 0 should this be 0?
        int out = (totalA << 24) | (cast(r) << 16) | (cast(g) << 8) | cast(b) << 0;
        return out;
    }

    /** Cast value to RGB range
     * @param x the value to cast
     * @return colour value in correct RGB range
     */
    private int cast(float x){
        if (x < 0) return 0;
        if (x > 255) return 255;
        return (int)x;
    }
}