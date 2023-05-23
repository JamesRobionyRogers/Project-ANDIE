package cosc202.andie;

import java.awt.*;
import java.awt.event.*;
import cosc202.andie.actions.tool.Selection;

/**
 * ClickListener is a class that tracks mouse activity and movement for selecting regions on an image.
 * When the mouse is pressed, the location point is stored as the start point. While the mouse is dragged, the location point is updated as the end point with each tracked location.
 * The ClickListener class applies the appropriate operations to the target image panel based on the mouse events, such as drawing focus rectangles or performing other selected actions.
 *
 * <p>
 * This class provides methods to activate and deactivate the ClickListener, set the target image panel, and specify the selection operations for live feedback and final image.
 * </p>
 *
 * <p>
 * Usage example:
 * </p>
 *
 * <pre>{@code
 * // Set the target image panel
 * ImagePanel imagePanel = new ImagePanel();
 * ClickListener.setTarget(imagePanel);
 *
 * // Create a selection operation for live feedback
 * Selection selectionOp = new RegionSelector();
 *
 * // Activate the ClickListener with the selection operation
 * ClickListener.activate(selectionOp);
 * }</pre>
 *
 * @see ImagePanel
 * @see Selection
 * @author Sola Woodhouse
 * @version 1.3
 */
public class ClickListener implements MouseListener, MouseMotionListener {

    /**
     * The ClickListener singleton instance.
     */
    private static ClickListener cl = new ClickListener();

    // The operation applied for visual feedback (e.g., a box for crop)
    private static Selection operation;

    // The operation applied when the user releases the click (e.g., a crop)
    private static Selection operationFinal;

    // The image panel
    private static ImagePanel target;

    // Indicates if draw is active or not
    private static boolean active;

    /**
     * Sets the target image panel.
     *
     * @param imPanel The target image panel.
     */
    public static void setTarget(ImagePanel imPanel) {
        target = imPanel;
    }

    /**
     * Activates the ClickListener with the specified selection operation for live feedback and final image.
     *
     * @param op The selection operation for live feedback.
     */
    public static void activate(Selection op) {
        operation = op;
        operationFinal = op;
        active = true;
    }

    /**
     * Activates the ClickListener with the specified selection operations for live feedback and final image.
     *
     * @param op      The selection operation for live feedback.
     * @param finalOp The selection operation for the final image.
     */
    public static void activate(Selection op, Selection finalOp) {
        operation = op;
        operationFinal = finalOp;
        active = true;
    }

    /**
     * Disables the ClickListener.
     */
    public static void disable() {
        active = false;
    }

    /**
     * Returns the ClickListener instance.
     *
     * @return The ClickListener instance.
     */
    public static ClickListener getInstance() {
        return cl;
    }

    /**
     * Stores the start location when the mouse is pressed and calls the RegionSelector apply method for live feedback.
     *
     * @param e The mouse event.
     */
    public void mousePressed(MouseEvent e) {
        if (!active)
            return;
        setStart(e.getPoint());
        setEnd(e.getPoint());
        target.getImage().applyTemp(operation);
        target.repaint();
        target.getParent().revalidate();
    }

    /**
     * Sets the end location when the mouse is released and applies the final operation to the target image.
     *
     * @param e The mouse event.
     */
    public void mouseReleased(MouseEvent e) {
        if (!active)
            return;
        setEnd(e.getPoint());
        target.getImage().apply(operationFinal);
        target.repaint();
        target.getParent().revalidate();
        active = false;
    }

    /**
     * Handles the mouseClicked event.
     *
     * @param e The mouse event.
     */
    public void mouseClicked(MouseEvent e) {
        if (!active)
            return;
        active = false;
    }

    /**
     * Handles the mouseEntered event.
     *
     * @param e The mouse event.
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * Handles the mouseExited event.
     *
     * @param e The mouse event.
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * Updates the end location when the mouse is dragged and shows live feedback if active.
     *
     * @param e The mouse event.
     */
    public void mouseDragged(MouseEvent e) {
        if (!active)
            return;
        setEnd(e.getPoint());
        target.getImage().applyTemp(operation);
        target.repaint();
        target.getParent().revalidate();
    }

    /**
     * Handles the mouseMoved event.
     *
     * @param e The mouse event.
     */
    public void mouseMoved(MouseEvent e) {}

    /**
     * Calculates the x-coordinate from a point considering the zoom level.
     *
     * @param p The point.
     * @return The calculated x-coordinate.
     */
    private int calcX(Point p) {
        return Math.min(Math.max(0, (int) (p.getX() / (target.getZoom() / 100))), target.getImage().getWidth() - 1);
    }

    /**
     * Calculates the y-coordinate from a point considering the zoom level.
     *
     * @param p The point.
     * @return The calculated y-coordinate.
     */
    private int calcY(Point p) {
        return Math.min(Math.max(0, (int) (p.getY() / (target.getZoom() / 100))), target.getImage().getHeight() - 1);
    }

    /**
     * Sets the end point of the selection based on the provided point.
     *
     * @param p The point representing the end location.
     */
    private void setEnd(Point p) {
        operation.setEnd(calcX(p), calcY(p));
        operationFinal.setEnd(calcX(p), calcY(p));
    }

    /**
     * Sets the start point of the selection based on the provided point.
     *
     * @param p The point representing the start location.
     */
    private void setStart(Point p) {
        operation.setStart(calcX(p), calcY(p));
        operationFinal.setStart(calcX(p), calcY(p));
    }
}