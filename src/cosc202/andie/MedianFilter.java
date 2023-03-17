package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Median filter.
 * </p>
 * 
 * <p>
 * The median filter takes all of the pixel values in a local neighbourhood and sorts them
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Sola Woodhouse
 * @version 1.0
 */

public class MedianFilter implements ImageOperation, java.io.Serializable {
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, radius of 2 is 5x5, etc
     */
    private int radius;

    /**
     * Construct a Median filter with the given size
     * 
     * Size of filter is radius
     * 
     * The larger the filter, the stronger the effect
     * 
     * @param radius The radius of the newly constructed MedianFilter
     */
    MedianFilter(int radius){
        this.radius = radius;
    }

    /**
     * Construct a Median filter with the default size
     * 
     * Default is radius of 1
     * 
     * @see MeanFilter(int)
     */
    MedianFilter() {
        this(1);
    }

    /**
     * Apply a Median filter to an image
     * 
     * The filter is implemented via a convolution
     * The size of kernel is specified by the {@link radius}
     * 
     * @param input The image to apply the median filter to
     * @return The resulting image
     */
    public BufferedImage apply (BufferedImage input){
        
        //int size = (2*radius+1)*(2*radius+1);
        //float [] array = new float [size];
        //Arrays.fill(array, 1.0f/size);

        //Kernel kernel = new Kernel(2*radius+1, 2*radius+1, array);
        //ConvolveOp convOp = new ConvolveOp(kernel);

        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        int areaSize = (radius*2+1)*(radius*2+1);

        //This part sets each pixel of the image to be the median in its area
        for (int y = radius; y < input.getHeight()-radius; ++y) {
            for (int x = radius; x < input.getWidth()-radius; ++x) {
                int[] rArr = new int[areaSize];
                int[] gArr = new int[areaSize];
                int[] bArr = new int[areaSize];
                int counter = 0;
                for(int i=-radius; i<radius;i++){
                    for(int j=-radius; j<radius;j++){
                        int argb = input.getRGB(x+i, y+j);
                        rArr[counter] = (argb & 0x00FF0000) >> 16;
                        gArr[counter] = (argb & 0x0000FF00) >> 8;
                        bArr[counter] = (argb & 0x000000FF);
                        counter ++;
                    }
                }
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >> 24;
                Arrays.sort(rArr);
                Arrays.sort(gArr);
                Arrays.sort(bArr);
                int r = rArr[radius+1];
                int g = gArr[radius+1];
                int b = bArr[radius+1];
                argb = (a<<24) | (r<<16) | (g<<8) | b;
                //This below sets the pixel colour of x,y to the median in output without altering the surrounding pixels
                //This makes sure the the original isn't altered, and each time a pixel is changed, it's from th original.
                output.setRGB(x, y, argb);
            }
        }
        return output;
    }

}
