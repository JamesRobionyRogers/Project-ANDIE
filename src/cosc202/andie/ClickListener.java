package cosc202.andie;

import java.awt.*;
import java.awt.event.*;


/**
 * <p>
 * ClickListener to track the mouse activity and movement.
 * </p>
 * 
 * <p>
 * When the mouse is pressed, the location point is stored as start. While the mouse is dragged, the location point is stored as end with each tracked location. 
 * The mouse drag calls the apply method of the RegionSelector class to draw either a focus rectangle, or a different selected action. 
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Sola Woodhouse
 * @version 1.0
 */
public class ClickListener implements MouseListener, MouseMotionListener{

    /**
     * The ClickListener singleton.
     */
    private static ClickListener cl = new ClickListener();

    // the operation applied for visual feedback (e.g. a box for crop)
    private static Selection operation;

    // the operation applied when the user releases click (e.g. a crop)
    private static Selection operationFinal;

    // The image panel
    private static ImagePanel target;

    // If draw is active or not
    private static boolean active;

    /**Method to set the target image panel 
     * @parameter ImagePanel imPanel
    */
    public static void setTarget(ImagePanel imPanel){
        target = imPanel;
    }

    /** Method to set activae for live feedback and final image - drawing
     * @parameter selection op
    */
    public static void activate(Selection op) {
        operation = op;
        operationFinal = op;
        active = true;
    }

    /**Method to set active for live feedback and final image - crop
     * @parameter selection op
    */
    public static void activate(Selection op, Selection finalOp) {
        operation = op;
        operationFinal = finalOp;
        active = true;
    }

    /**Turn off active */
    public static void disable(){
        active = false;
    }

    /**
     * Implements the ImageAction interface - not used.
     */
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * Returns the ClickListener instance.
     */
    public static ClickListener getInstance(){
        return cl;
    }
    

    /** 
     * <p>
     * Stores the start and end location of when the mouse is pressed.
     * </p>
     * 
     * <p>
     * Calls the RegionSelector apply method with applyTemp for live feedback.
     * </p>
     */
    public void mousePressed(MouseEvent e){
        if (!active)
            return;
        setStart(e.getPoint());
        setEnd(e.getPoint());
        target.getImage().applyTemp(operation);
        target.repaint();
        target.getParent().revalidate();
    }

    /**When mouse is released, either draw the final, or clear the highlight */
   public void mouseReleased(MouseEvent e){
        if (!active) return;
        setEnd(e.getPoint());
        //target.getImage().revert();
        target.getImage().apply(operationFinal); 
        target.repaint();
        target.getParent().revalidate();

        active = false;
}
    public void mouseClicked(MouseEvent e){
        if (!active) return;
    }
    public void mouseEntered(MouseEvent e){
        if (!active) return;
    }
    public void mouseExited(MouseEvent e){
        if (!active) return;
    }

    /**When mouse is dragged, if active, live feedback is shown. */
    public void mouseDragged(MouseEvent e){
        if (!active) return;
        setEnd(e.getPoint());
        target.getImage().applyTemp(operation);
        target.repaint();
        target.getParent().revalidate();

    }

    public void mouseMoved(MouseEvent e){
        if (!active) return;
    }

    /**Calculate the x from a point considering zoom
     * @parameter point p
     * @return integer x
     */
    private int calcX(Point p){
        return Math.min(Math.max(0, (int)(p.getX() / (target.getZoom()/100))),target.getImage().getWidth()-1);
        
    }

    /**Calculate the y from a point considering zoom
     * @parameter point p
     * @return integer y
    */
    private int calcY(Point p){
        return Math.min(Math.max(0,(int)(p.getY() / (target.getZoom()/100))),target.getImage().getHeight()-1);
    }

    /**Set the end point
     * @parameter point p
     */
    private void setEnd(Point p){
        operation.setEnd(calcX(p), calcY(p));
        operationFinal.setEnd(calcX(p), calcY(p));
    }

    /**Set the start point 
    * @parameter point p
    */
    private void setStart(Point p){
        operation.setStart(calcX(p), calcY(p));
        operationFinal.setStart(calcX(p), calcY(p));
    }

    }
