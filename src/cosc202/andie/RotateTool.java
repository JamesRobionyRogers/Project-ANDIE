package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a rotate tool.
 * </p>
* 
* <p>
* Rotates image using a matrix
* </p>
* 
* <p> 
* <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
* </p>
* 
* @see java.awt.image.ConvolveOp
* @version 1.0
*/
public class RotateTool implements ImageOperation, java.io.Serializable {

    /*
     * Degrees of rotation
     */
    private int deg;

    /** Default rotate constructor 
     * rotates by 90 degrees by default
    */
    RotateTool() {
        this(90);
    }
    /** Rotate tool constructor
     * value will be rounded down to nearest 90 deg rotation and negative values will become their clockwise counterparts
     * @param deg value of rotation
     * 
     */
    RotateTool(int deg) {
        // any rotation above 360 is unnesesary

        // 3 = 270
        // 2 = 180
        // 1 = 90
        // 0 = 360
        // force clockwise rotation
        while (deg < 0) {
            deg += 360;
        }

        this.deg = (deg % 360) / 90;
    }

    /**
     * <p>
     * Apply a Rotation tool to an image.
     * </p>
     * 
     * <p>
     * This filter is applied through matrix multiplication of a point x,y with matrix 
     * | a  b |
     * | c  d |
     * 
     * </p>
     * 
     * @param input The image to apply the Mean filter to.
    * @return The resulting (blurred)) image.
    */
    public BufferedImage apply(BufferedImage input) {
        Translation translate;
        // multiple of 360 no rotation
        if (this.deg == 0) {
            return input;
        }
        int width = input.getWidth();
        int height = input.getHeight();

        /*
         * | a, b |
         * | c, d |
         */
        // creates matrix based on specified degrees of rotation
        switch (deg) {
            // 90 deg rotation
            case (1):
                translate = new Translation(0, -1, 1, 0, (height - 1), 0, height, width);
                break;

            // 180 deg rotation
            case (2):
                translate = new Translation(-1, 0, 0, -1, (height - 1), (width - 1), width, height);
                break;

            // 270 deg rotation
            case (3):
                translate = new Translation(0, 1, -1, 0, 0, (width - 1), height, width);
                break;

            // something went wrong
            default:
                System.out.println("Something went wrong while rotating");
                return input;

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


    
    /** Handels (x,y) point rotation accourding to matrix
     * | a  b |
     * | c  d |
     * also stores heigh and width new image should be
     */
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
