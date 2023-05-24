package cosc202.andie.actions.filter;

import java.awt.image.BufferedImage;
import cosc202.andie.ImageOperation;
import cosc202.andie.actions.ImageAction;
/*
 * <p>
 * ImageOperation to revert to original image
 * <p>
 * 
 * Revert has no user defined parameters
 * <p>
 * 
 * @author Jess Tyrrell
 * @version 1.1
 */

public class Revert implements ImageOperation, java.io.Serializable {

    /**
     * <p>
     * Constructs a instance of Revert, taking the current original image as input
     * 
     */
    public Revert() {
        
    }

    /**
     * <p>
     * Revert image to original
     * </p>
     * 
     * @param input The image to revert
     * @return The original image
     */
    public BufferedImage apply(BufferedImage input) {
        BufferedImage original = ImageAction.getTarget().getImage().getOriginal();
        BufferedImage output = new BufferedImage(original.getColorModel(), original.getRaster(), original.isAlphaPremultiplied(), null);
        return output;
    }
}
