package cosc202.andie.actions.tool;

import cosc202.andie.ImageOperation;

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
* @see cosc202.andie.actions.filter.ConvOpEdge
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
    public RotateTool() {
        this(90);
    }
    /** Rotate tool constructor
     * value will be rounded down to nearest 90 deg rotation and negative values will become their clockwise counterparts
     * @param deg value of rotation
     * 
     */
    public RotateTool(int deg) {
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
                translate = new Translation(-1, 0, 0, -1, (width - 1),(height - 1) , width, height);
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

        BufferedImage output = new BufferedImage(translate.getWidth(), translate.getHeight(), input.getType());

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
}
