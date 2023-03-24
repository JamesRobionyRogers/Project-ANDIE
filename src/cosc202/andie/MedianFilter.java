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

        //This part sets each pixel of the image, not corners or sides, to be the median in its area
        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {
                //This below sets the pixel colour of x,y to the median in output without altering the surrounding pixels
                //This makes sure the the original isn't altered, and each time a pixel is changed, it's from th original.
                output.setRGB(x, y, setARGB(x, y, input));
            }
        }
        //System.out.println("Almost there");
        return output;
    }

    //This method sets the ARGB for each pixel, and allows less clutter in prev method
    public int setARGB(int x, int y, BufferedImage input){

        //set areaSize 
        int areaSize = 0;

        for(int i=x-radius; i<x+radius+1; i++){
            if(i>=0 && i<input.getWidth()){
                for(int j=y-radius; j<y+radius+1; j++){
                    if(j>=0 && j<input.getHeight()){
                        areaSize++;
                    }
                }
            }
        }
        int middle = areaSize/2;

        int[] rArr = new int[areaSize];
        int[] gArr = new int[areaSize];
        int[] bArr = new int[areaSize];
        int counter = 0;

        for(int i=x-radius; i<x+radius+1; i++){
            // check index 'i' within image bounds 
            if(i>=0 && i<input.getWidth()){
                for(int j=y-radius; j<y+radius+1; j++){
                    // check index 'j' within image bounds 
                    if(j>=0 && j<input.getHeight()){
                        //System.out.println("(" + i + ", " + j + ")");
                        int argb = input.getRGB(i, j);
                        rArr[counter] = (argb & 0x00FF0000) >> 16;
                        gArr[counter] = (argb & 0x0000FF00) >> 8;
                        bArr[counter] = (argb & 0x000000FF);
                        counter ++;
                    }
                }
            }
        }

        int argb = input.getRGB(x, y);
        // Unsure when this would occur
        if(areaSize==0){
            System.out.println("Something went horribly wrong, area size was 0");
            return argb;
        }
        int a = (argb & 0xFF000000) >> 24;
        Arrays.sort(rArr);
        Arrays.sort(gArr);
        Arrays.sort(bArr);
        int r=0;
        int g=0;
        int b=0;
        if(middle==0){
            r = (rArr[middle -1] + rArr[middle])/2;
            g = (gArr[middle -1] + gArr[middle])/2;
            b = (bArr[middle -1] + bArr[middle])/2;
        }else{
            r = rArr[middle];
            g = gArr[middle];
            b = bArr[middle];
        }
        int argb1 = (a<<24) | (r<<16) | (g<<8) | b;
        //System.out.println("inside loop");
        return argb1;
    }

}
