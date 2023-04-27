package cosc202.andie.actions.tool;

/** Handels (x,y) point rotation accourding to matrix
 * | a  b |
 * | c  d |
 * also stores heigh and width new image should be
 */
public class Translation {
    int a, b, c, d, xOff, yOff, height, width;
    
    /**
     * Translation constructor with points 
     * | a  b |
     * | c  d |
     * 
     * 
     * @param a value 'a'
     * @param b value 'b'
     * @param c value 'c'
     * @param d value 'd'
     * @param xOffset amount to offset any x movement by caused by matrix
     * @param yOffset amount to offset any y moveemen by caused by matrix
     * @param width width of image after matrix has been applied
     * @param height heigh of image after matrix has been applied
     */
    protected Translation(int a, int b, int c, int d, int xOffset, int yOffset, int width, int height) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.xOff = xOffset;
        this.yOff = yOffset;
        this.height = height;
        this.width = width;
    }


    /** Gets x coord after applying matrix to point x, y
     * @param x current x coord
     * @param y current y coord
     * @return x coord after matrix application
     */
    int getX(int x, int y) {
        return x * a + y * b + xOff;
    }

    /** Gets y coord after applying matrix to point x, y
     * @param x current x coord
     * @param y current y coord
     * @return y coord after matrix application
     */
    int getY(int x, int y) {
        return x * c + y * d + yOff;
    }

    int getHeight(){
        return height;
    }

    int getWidth(){
        return width;
    }

}