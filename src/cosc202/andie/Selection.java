package cosc202.andie;

/**Method to select a rectangular region with a mouse. It will get the coordinates, then a separate method will make the rectangle border. 
 * Abilities: 
 * - Makes a rectangle - mouse pressed and released to get area/ co-ords
 * Planned abilities:
 * - Area is selectable as separate layer
 * - Can make multiple selections, and return to old one if shape if made - mouse enter and exit events over the areas, then mouse clicked event to select it
 * - While selection is made, editing options to appear - location tbd, can be up in regular menubar for now? refresh of GUI to appear
 * - Can choose colours
 * - Can choose thickness of borders
 * - Can choose to fill, to clear, or to crop
*/

//Get first and last coords, and store them
public abstract class Selection implements ImageOperation {

    protected int x1, x2, y1, y2;

    public void setPoint(double x, double y){
        setPoint((int) x, (int)y);
    }

    public void setPoint(int x, int y){
        this.x1 = x;
        this.y1 = y;
        this.x2 = x;
        this.y2 = y;
    }



    public void setStart(int x, int y){
        this.x1 = x;
        this.y1 = y;
    }
    public void setStart(double x, double y){
        setStart((int)x , (int) y);
    }
    
    public void setEnd(int x, int y){
        this.x2 = x;
        this.y2 = y;
    }

    public void setEnd(double x, double y){
        setEnd((int)x , (int) y);
    }
}