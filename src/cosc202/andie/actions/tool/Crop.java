package cosc202.andie.actions.tool;

import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

/**
 * Crop Tool
 * 
 * Crops the image by a given area
 * 
 * @author Jack McDonnell
 * @version 1.0
 */


public class Crop implements ImageOperation {
    private int x, y, width, height;

    public Crop(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public BufferedImage apply(BufferedImage input){
        BufferedImage output = new BufferedImage(width, height, input.getType());
        int[] rgbaArray = input.getRGB(x, y, width, height, null, 0, width);
        output.setRGB(0, 0, width, height, rgbaArray, 0, width);
        return output;
    }
}