package cosc202.andie;
import java.awt.image.*;
import java.util.*;

public class RotateTool implements ImageOperation, java.io.Serializable {

    private int deg;
     RotateTool(){
        this(90);
     }

     RotateTool(int deg) {
        this.deg = deg;
     }


      public BufferedImage apply(BufferedImage input){
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        return output;
    }
}
    

