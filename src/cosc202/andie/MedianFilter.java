package cosc202.andie;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Arrays;

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


        // create pools
        ArrayDeque<Integer> horizontalOps = new ArrayDeque<Integer>(input.getWidth());
        for (int i = 0; i < input.getWidth(); horizontalOps.add(i++));

        ArrayDeque<Integer> verticalOps = new ArrayDeque<Integer>(input.getHeight());
        for (int i = 0; i < input.getHeight(); verticalOps.add(i++));

        // start work
        long time = System.currentTimeMillis();
        horizontalOps.parallelStream().forEach(x -> {
            verticalOps.parallelStream().forEach(y -> {
                // do a thing
                output.setRGB(x, y, setARGB(x, y, input));
            });
        });
        time = System.currentTimeMillis() - time;

        System.out.println(time/1000);
        return output;
    }
    

    //This method sets the ARGB for each pixel, and allows less clutter in prev method
    public int setARGB(int x, int y, BufferedImage input){

        //set areaSize 
        int areaSize = 0;
        short height = 0;
        short width = 0;
        for(int i=x-radius; i<x+radius+1; i++){
            if(i>=0 && i<input.getWidth()){
                width++;
            }
        }
        for(int i=y-radius; i<y+radius+1; i++){
            if(i>=0 && i<input.getHeight()){
                height++;
            }
        }
        areaSize = height*width;

        int middle = areaSize/2;

        byte[] rArr = new byte[areaSize];
        byte[] gArr = new byte[areaSize];
        byte[] bArr = new byte[areaSize];
        short counter = 0;
        
        for(int i=x-radius; i<x+radius+1; i++){
            // check index 'i' within image bounds 
            if(i>=0 && i<input.getWidth()){
                for(int j=y-radius; j<y+radius+1; j++){
                    // check index 'j' within image bounds 
                    if(j>=0 && j<input.getHeight()){
                        int argb = input.getRGB(i, j);
                        // Unsign
                        rArr[counter] = (byte)(((argb & 0x00FF0000) >> 16)-128);
                        gArr[counter] = (byte)(((argb & 0x0000FF00) >> 8)-128);
                        bArr[counter] = (byte)((argb & 0x000000FF)-128);
                        counter ++;
                        
                    }
                }
            }
        }

        
        // Unsure when this would occur I think this is just for testing idk
        if(areaSize==0){
            int argb = input.getRGB(x, y);
            System.out.println("Something went horribly wrong, area size was 0");
            return argb;
        }
        int a = -1;
        Arrays.sort(rArr);
        Arrays.sort(gArr);
        Arrays.sort(bArr);
        int r=0;
        int g=0;
        int b=0;
        if(areaSize%2==0){
            r = (rArr[middle -1] + rArr[middle])/2+128;
            g = (gArr[middle -1] + gArr[middle])/2+128;
            b = (bArr[middle -1] + bArr[middle])/2+128;

        }else{
            r = rArr[middle]+128;
            g = gArr[middle]+128;
            b = bArr[middle]+128;
        }
        int argb1 = (a<<24) | (r<<16) | (g<<8) | b;
        return argb1;
    } 

}
