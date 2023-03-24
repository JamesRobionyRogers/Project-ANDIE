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

        //Make output image, this is the image will be flipped into?
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        int x1=0;
        int x2=input.getWidth()-1;
        int y1=0;
        int y2=input.getHeight()-1;
        //System.out.println("image flip class");

        if(horizontal){
            while(y1<y2){
                for(int i=0; i<y2;i++){
                    int small=x1;
                    int large=x2;
                    while(small<=input.getWidth()/2&&large>=input.getWidth()/2){
                        //System.out.println(x1+", "+y1+"   "+x2+" "+y1);
                        int argb1 = input.getRGB(small,y1);
                        int argb2 = input.getRGB(large,y1);
                        output.setRGB(small,y1,argb2);
                        output.setRGB(large,y1,argb1);
                        small++;
                        large--;
                        //System.out.println(small+", "+y1+"   "+large+" "+y2);
                    }
                y1++;
                }
            }
        }else if(!horizontal){
            //System.out.println("vertical loop?");
            while(x1<x2){
                for(int i=0; i<x2;i++){
                    int small = y1;
                    int large = y2;
                    while(small<=input.getHeight()/2&&large>=input.getHeight()/2){
                        //System.out.println(x1+", "+small+"   "+x1+" "+large);
                        int argb1 = input.getRGB(x1,small);
                        int argb2 = input.getRGB(x1,large);
                        output.setRGB(x1,small,argb2);
                        output.setRGB(x1,large,argb1);
                        small++;
                        large--;
                    }
                x1++;
                }
            }
        }

        return output;
    }
    
}
