package cosc202.andie;

import java.awt.image.*;

public class SoftBlurFilter implements ImageOperation, java.io.Serializable {

    public SoftBlurFilter() {
        // Construction code goes here 
    }

    /**
     * This method will apply the filter to a given image 
     * @param inputImg the image to apply the filter to 
     */
    public BufferedImage apply(BufferedImage inputImg) {
        // Creating an array of floats to create the kernal from 
        float[] kernalArray = { 
            0,      1/8.0f,  0,
            1/8.0f, 1/2.0f,  1/8.0f,
            0,      1/8.0f,  0 
        }; 

        // Creating a 3x3 filter Kernel from the array 
        Kernel kernal = new Kernel(3, 3, kernalArray); 

        // Making the ConvolveOp from the Kernal  
        ConvolveOp convOp = new ConvolveOp(kernal); 

        // Creating a copy of the input image to store the result of the filter 
        BufferedImage outputImg = new BufferedImage(inputImg.getColorModel(), 
            inputImg.copyData(null), inputImg.isAlphaPremultiplied(), null); 
        
        // Applying the filter to the image and storing it in the result 
        convOp.filter(inputImg, outputImg);

        return outputImg;
    }

    
    
}
