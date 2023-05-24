package cosc202.andie.actions.filter;

import java.awt.image.BufferedImage;
import cosc202.andie.ImageOperation;
/**
 * <p>
 * ImageOperation to revert to original image
 * <p>
 * 
 * Revert has no user defined parameters
 * <p>
 * @author Jess Tyrrell
 */

public class Revert implements ImageOperation, java.io.Serializable{

private BufferedImage original;
/**
     * <p>
     * Constructs a instance of Revert, taking the current original image as input
     * 
     */
    public Revert(BufferedImage original){
        this.original = original;
    }


/**
     * <p>
     * Revert image to original
     * </p>
     * 
     * @param input The image to revert
     * @return The original image 
     */
    public BufferedImage apply(BufferedImage input){
        return original;
    }
}
