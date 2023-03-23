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
        Translation translate;
        //multiple of 360 no rotation
        if (this.deg == 0){
            return input;
        }
        int width = input.getWidth();
        int height = input.getHeight();

        /*
         * | a,  b |
         * | c,  d |
         */
        switch (deg){
            // 90 deg rotation
            case(1):
                translate = new Translation(0,-1,1,0,(height-1),0,height,width);
                break;

            // 180 deg rotation
            case(2):
                translate = new Translation(-1,0,0,-1,(height-1),(width-1),width,height);
                break;
            
                // 270 deg rotation
            case(3):
                translate = new Translation(0,1,-1,0,0,(width-1),height,width);
                break;

            // something went wrong
            default:
                System.out.println("Something went wrong while rotating");
                return input;

        }

        
        BufferedImage output = new BufferedImage(translate.width,translate.height, input.getType());
            
        // loop over every x
        for (int x = 0; x < input.getWidth(); x++){

            // loop over every y
            for (int y = 0; y < input.getHeight(); y++){

                // write old pxl to new location
                output.setRGB(translate.getX(x,y),translate.getY(x,y), input.getRGB(x, y));
            }
        }

        return output;
    }
    
    private class Translation{
        int a,b,c,d,xOff,yOff,height,width;
        Translation(int a, int b, int c,int d,int xOffset,int yOffset,int width,int height){
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.xOff = xOffset;
            this.yOff = yOffset;
            this.height = height;
            this.width = width;
        }
        int getY(int x, int y){
            return x * c + y * d + yOff;
        }
        int getX(int x, int y){
            return x * a + y * b + xOff;
        }
    }
}
    

