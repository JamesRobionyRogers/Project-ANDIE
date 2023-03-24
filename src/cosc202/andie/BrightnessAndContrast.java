package cosc202.andie; 

import java.awt.image.*; 

/**
 * <p>
 * ImageOperation to change an images Brightness and Contrast.
 * </p>
 * 
 * <p>
 * The images produced are altered versions of the original image, where the rgb values are changed based upon the wanted
 * brightness and contrast change.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Jack McDonnell
 * @version 1.0
 */


public class BrightnessAndContrast implements ImageOperation, java.io.Serializable { 

    private double brightness; 

    private double contrast; 
 
    /**
     * <p>
     * Create a new BrightnessAndContrast operation.
     * </p>
     */

    BrightnessAndContrast(int brightness, int contrast){ 

        this.brightness = brightness; 

        this.contrast = contrast; 

    } 

      /**
     * <p>
     * Apply a Brightness and Contrast conversion to an image with the given formula.
     * </p>
     * 
     * <p>
     * The conversion of the red, green, and blue values uses a formula given in the lab book.
     * </p>
     * 
     * @param input The image to be altered
     * @return The resulting altered image.
     */

    public BufferedImage apply(BufferedImage input) { 

        for (int y = 0; y < input.getHeight(); ++y) { 

             for (int x = 0; x < input.getWidth(); ++x) { 

                int argb = input.getRGB(x, y); 

                int a = (argb & 0xFF000000) >> 24; 

                int r = (argb & 0x00FF0000) >> 16; 

                int g = (argb & 0x0000FF00) >> 8; 

                int b = (argb & 0x000000FF); 
 
                int red = (int)Math.round(((1+(contrast/100))*(r-127.5))+(127.5*(1+(brightness/100))));  

                int green = (int)Math.round(((1+(contrast/100))*(g-127.5))+(127.5*(1+(brightness/100)))); 

                int blue = (int)Math.round(((1+(contrast/100))*(b-127.5))+(127.5*(1+(brightness/100)))); 

                //If the pixels new value is out of range, put it at the extreme it went over.
                if(blue>255){
                    blue = 255;
                } else if(blue<0){
                    blue = 0;
                }

                if(red>255){
                    red = 255;
                }else if(red<0){
                    red = 0;
                }

                if(green>255){
                    green = 255;
                }else if(green<0){
                    green = 0;
                }

            //Assign the new values onto the pixel
            argb = (a << 24) | (red << 16) | (green << 8) | blue; 

            input.setRGB(x, y, argb);             

            } 

        } 

    return input; 

    }    

} 