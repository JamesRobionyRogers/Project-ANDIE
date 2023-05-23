package cosc202.andie.actions.tool;

import java.awt.image.BufferedImage;

/**
 * Crop Tool
 * 
 * Crops the image by a given area
 * 
 * @author Jack McDonnell
 * @version 1.0
 */


public class Crop extends Selection {

    public BufferedImage apply(BufferedImage input){
        BufferedImage output = new BufferedImage(getWidth(), getHeight(), input.getType());
        int[] rgbaArray = input.getRGB(getX(), getY(), getWidth(), getHeight(), null, 0, getWidth());
        output.setRGB(0, 0, getWidth(), getHeight(), rgbaArray, 0, getWidth());
        return output;
    }
}