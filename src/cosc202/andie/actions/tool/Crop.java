package cosc202.andie.actions.tool;

import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

public class Crop implements ImageOperation{
    private int xStart, yStart, width, height;

    public Crop(int xStart, int yStart, int width, int height) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
    }

    public BufferedImage apply(BufferedImage input){

        //BufferedImage output = new BufferedImage(width, height, input.getType(), input.getColorModel());
        BufferedImage output = new BufferedImage(width, height, input.getType());
        int[] rgbaArray = input.getRGB(xStart, yStart, width, height, null, 0, width);
        output.setRGB(0, 0, width, height, rgbaArray, 0,width);
        return output;
    } 
}