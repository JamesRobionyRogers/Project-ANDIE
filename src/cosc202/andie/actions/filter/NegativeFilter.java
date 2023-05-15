package cosc202.andie.actions.filter;

import cosc202.andie.ImageOperation;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.util.ArrayDeque;

/**
 * <p>
 * ImageOperation to apply a Negative filter.
 * </p>
 * 
 * <p>
 * The negative filter is used for detecting edges and embossing images
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see cosc202.andie.actions.filter.ConvOpEdge
 * @author Xavier Nuttall
 * @version 1.0
 */

public class NegativeFilter implements ImageOperation, java.io.Serializable {
    public static final int EMBOSS_LEFT = 0;
    public static final int EMBOSS_TOP_LEFT = 1;
    public static final int EMBOSS_TOP = 2;
    public static final int EMBOSS_TOP_RIGHT = 3;
    public static final int EMBOSS_RIGHT = 4;
    public static final int EMBOSS_BOTTOM_RIGHT = 5;
    public static final int EMBOSS_BOTTOM = 6;
    public static final int EMBOSS_BOTTOM_LEFT = 7;
    public static final int EDGE_HORIZONTAL = 8;
    public static final int EDGE_VERTICAL = 9;

    private float[] option;
    /**
     * Construct a negative filter
     * 
     * Size of filter is radius
     * 
     * The larger the filter, the stronger the effect
     * 
     * @param radius The radius of the newly constructed MedianFilter
     */
    public NegativeFilter(int choice){
        // all filters are 3x3
        

                
        option = switch(choice) {
            case EMBOSS_LEFT -> new float[] {0,0,0,1,0,-1,0,0,0};
            case EMBOSS_TOP_LEFT -> new float[] {1,0,0,0,0,0,0,0,-1};
            case EMBOSS_TOP -> new float[] {0,1,0,0,0,0,0,-1,0};
            case EMBOSS_TOP_RIGHT -> new float[] {0,0,1,0,0,0,-1,0,0};
            case EMBOSS_RIGHT -> new float[] {0,0,0,-1,0,1,0,0,0};
            case EMBOSS_BOTTOM_RIGHT -> new float[] {-1,0,0,0,0,0,0,0,1};
            case EMBOSS_BOTTOM -> new float[] {0,-1,0,0,0,0,0,1,0};
            case EMBOSS_BOTTOM_LEFT -> new float[] {0,0,-1,0,0,0,1,0,0};
            case EDGE_HORIZONTAL -> new float[] {-0.5f,0,0.5f,-1,0,1,-0.5f,0,0.5f};
            case EDGE_VERTICAL -> new float[] {-0.5f,-1,-0.5f,0,0,0,0.5f,1,0.5f};
            default -> throw new IllegalArgumentException("Unknown filter type");
        };


    }

    public NegativeFilter(){
        this(0);
    }

    /**
     * Apply a Negative filter to an image
     * 
     * The filter applies a negative and positive convolution 
     * then subtracts the difference and casts it to the range
     * 
     * @param input The image to apply the negative filter to
     * @return The resulting image
     */
    public BufferedImage apply (BufferedImage input){

        float [] positiveArray = new float[option.length];
        float[] negativeArray = new float[option.length];

        for (int i = 0; i < option.length; i++) {
             if (option[i] > 0){
                positiveArray[i] = option[i];
             } else {
                // if theres ever a bug it was probably here setting 0.0f to -0.0f
                negativeArray[i] = -option[i];
             }
        }
    
        Kernel positiveKernel = new Kernel(3,3, positiveArray);
        Kernel negativeKernel = new Kernel(3,3, negativeArray);

        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null),
                input.isAlphaPremultiplied(), null);

        ConvOpEdge negative = new ConvOpEdge(negativeKernel);
        ConvOpEdge positive = new ConvOpEdge(positiveKernel);

        BufferedImage positiveBuffer = positive.filter(input);
        BufferedImage negativeBuffer = negative.filter(input);

        // create pools
        ArrayDeque<Integer> horizontalOps = new ArrayDeque<Integer>(input.getWidth());
        for (int i = 0; i < input.getWidth(); horizontalOps.add(i++));

        ArrayDeque<Integer> verticalOps = new ArrayDeque<Integer>(input.getHeight());
        for (int i = 0; i < input.getHeight(); verticalOps.add(i++));

        // start work
        horizontalOps.parallelStream().forEach(x -> {
            verticalOps.parallelStream().forEach(y -> {
                // do a thing
                

                output.setRGB(x, y, (input.getRGB(x, y) & 0xFF000000) | setARGB(x, y, positiveBuffer, negativeBuffer));
            });
        });
        return output;
    }
    
    
    /** Returns the RGB value post filtering 
     * @param x horizontal position of pixel
     * @param y vertical position of pixel
     * @param positiveBuffer the RGB which will be subtracted from
     * @param negativeBuffer the RGB which will be subtracting
     * @return the value post filter
     */
    private int setARGB(int x, int y, BufferedImage positiveBuffer, BufferedImage negativeBuffer){
        int positive = positiveBuffer.getRGB(x,y);
        int negative = negativeBuffer.getRGB(x,y);
        return calculate(positive, negative);
    }

    /** performs positive - negative and returns the RGB int
     * @param positive RGBA int
     * @param negative RGBA int 
     * @return
     */
    private int calculate(int positive, int negative){
        int[] rgbP = decompose(positive);
        int[] rgbN = decompose(negative);
        int r = shiftCentre(rgbP[0] - rgbN[0]);
        int g = shiftCentre(rgbP[1] - rgbN[1]);
        int b = shiftCentre(rgbP[2] - rgbN[2]);
        
        return recompose(r,g,b);
    }


    /** takes and ARGB int and turns it into an int array of colour values
     * @param rgba the value to decompose
     * @return an int array with the correct values
     */
    private int[] decompose(int rgba){
        int r = (rgba & 0x00FF0000) >> 16;
        int g = (rgba & 0x0000FF00) >> 8;
        int b = (rgba & 0x000000FF);
        return new int[] {r,g,b};
    }

    /** turns seperate argb values and makes it a single argb value
     * @param r red value
     * @param g green value
     * @param b blue value
     * @return all the values as an argb value
     */
    private int recompose(int r, int g, int b){
        int argb = (r << 16) | (g << 8) | b;
        return argb;
    }

    /** moves and casts the int value recentering it
     * @param x a colour value to cast
     * @return the corrected value
     */
    private int shiftCentre(int x){
        x += 128;
        if (x > 255) return 255;
        if (x < 0) return 0;
        return x;
    }

}
