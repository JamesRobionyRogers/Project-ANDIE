package cosc202.andie;
import java.awt.image.*;

public class RotateTool implements ImageOperation, java.io.Serializable {

    private int deg;
     RotateTool(){
        this(90);
     }


     RotateTool(int deg) {
        // any rotation above 360 is unnesesary

        // 3 = 270
        // 2 = 180
        // 1 = 90
        // 0 = 360
        // force clockwise rotation
        while(deg<0){
            deg+=360;
        }

        this.deg = (deg%360)/90;
     }


      public BufferedImage apply(BufferedImage input){

        //multiple of 360 no rotation
        if (this.deg == 0){
            return input;
        }
        int width = input.getWidth();
        int height = input.getHeight();

        int a,b,c,d,xOffset,yOffset;
        a = b = c = d = xOffset = yOffset = 0;

        /*
         * | a,  b |
         * | c,  d |
         */
        int store;
        switch (deg){
            // 90 deg rotation
            case(1):
                
                store = width;

                xOffset = (height-1);
                width = height;
                height = store;

                a = 0;
                b = -1;
                c = 1;
                d = 0;

                break;

            // 180 deg rotation
            case(2):
                a = -1;
                b = 0;
                c = 0;
                d = -1;

                yOffset = (height-1);
                xOffset = (width-1);


                break;
            
                // 270 deg rotation
            case(3):
                store = width;
                a = 0;
                b = 1;
                c = -1;
                d = 0;
                yOffset = (width-1);

                width = height;
                height = store;
                break;

            // something went wrong
            default:
                System.out.println("Something went wrong while rotating");
                return input;

        }


        BufferedImage output = new BufferedImage(width,height, input.getType());
            
        // loop over every x
        for (int x = 0; x < input.getWidth(); x++){

            // loop over every y
            for (int y = 0; y < input.getHeight(); y++){

                // generate new coord
                int newX = x * a + y * b + xOffset;
                int newY = x * c + y * d + yOffset;

                //System.out.println("(" + x + ", " + y +") --> (" + newX + ", " + newY + ")");

                // write old pxl to new location
                output.setRGB(newX,newY, input.getRGB(x, y));
            }
        }

        return output;
    }
}
    

