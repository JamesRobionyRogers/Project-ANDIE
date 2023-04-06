package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * Image Operation to flip an image.
 * </p>
 * 
 * <p>
 * The Image flip operation takes the pixels in an image and swap them to flip an image
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


public class ImageFlip implements ImageOperation, java.io.Serializable {

    /**
     * Determines if horizontal or vertical flip
     * Horizontal is true, Vertical is false
     * Boolean
     */
    private boolean horizontal;
     
    /**
     * Constructor for ImageFlip
     * @param horizontal
     */
    ImageFlip(boolean horizontal){
        this.horizontal = horizontal;
    }

    
    /**
     * Default constructor for ImageFlip, horizontal is true
     */
    ImageFlip() {
        this(true);
    }


    /**
     * Code which, if horizontal is true, replaces the pixels so the image is
     * flipped horizontally if false, the pixels are replaced vertically
     * 
     * @param input the image to be flipped
     */
    public BufferedImage apply(BufferedImage input){
        Translation translate = new Translation(0, 0, 0, 0, 0, 0, 0, 0);

        if (horizontal){
            translate = new Translation(-1, 0, 0, 1, input.getWidth()-1, 0, input.getWidth(), input.getHeight());
        }else{
            translate = new Translation(1, 0, 0, -1, 0, input.getHeight()-1, input.getWidth(), input.getHeight());
        }

        BufferedImage output = new BufferedImage(translate.width, translate.height, input.getType());

        // loop over every x
        for (int x = 0; x < input.getWidth(); x++) {

            // loop over every y
            for (int y = 0; y < input.getHeight(); y++) {

                // write old pxl to new location
                output.setRGB(translate.getX(x, y), translate.getY(x, y), input.getRGB(x, y));
            }
        }

        return output;
    }

    private class Translation {
        int a, b, c, d, xOff, yOff, height, width;
        
        /**
         * Translation constructor with points 
         * | a  b |
         * | c  d |
         * 
         * 
         * @param a value 'a'
         * @param b value 'b'
         * @param c value 'c'
         * @param d value 'd'
         * @param xOffset amount to offset any x movement by caused by matrix
         * @param yOffset amount to offset any y moveemen by caused by matrix
         * @param width width of image after matrix has been applied
         * @param height heigh of image after matrix has been applied
         */
        Translation(int a, int b, int c, int d, int xOffset, int yOffset, int width, int height) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.xOff = xOffset;
            this.yOff = yOffset;
            this.height = height;
            this.width = width;
        }


        /** Gets x coord after applying matrix to point x, y
         * @param x current x coord
         * @param y current y coord
         * @return x coord after matrix application
         */
        int getX(int x, int y) {
            return x * a + y * b + xOff;
        }

        /** Gets y coord after applying matrix to point x, y
         * @param x current x coord
         * @param y current y coord
         * @return y coord after matrix application
         */
        int getY(int x, int y) {
            return x * c + y * d + yOff;
        }

    }
}
