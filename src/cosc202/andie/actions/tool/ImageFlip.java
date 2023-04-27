package cosc202.andie.actions.tool;

import cosc202.andie.ImageOperation;
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
 * @see cosc202.andie.actions.filter.ConvOpEdge
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
    public ImageFlip(boolean horizontal){
        this.horizontal = horizontal;
    }

    
    /**
     * Default constructor for ImageFlip, horizontal is true
     */
    public ImageFlip() {
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
