package cosc202.andie.actions.tool;

import cosc202.andie.ImageOperation;

/**
 * Abstract class that represents a selection made using mouse input.
 * It defines methods for setting and retrieving coordinates of the selection.
 * @author Xavier Nuttall
 * @see ImageOperation
 * @version 1.1
 */
public abstract class Selection implements ImageOperation, java.io.Serializable {

    private int x1;
    private int x2;
    private int y1;
    private int y2;

    /**
     * Sets the coordinates of the selected point.
     *
     * @param x The x-coordinate of the selected point.
     * @param y The y-coordinate of the selected point.
     */
    public void setPoint(double x, double y) {
        setPoint((int) x, (int) y);
    }

    /**
     * Sets the y-coordinate of the second point of the selection.
     *
     * @param y2 The y-coordinate of the second point.
     */
    public void setY2(int y2) {
        this.y2 = y2;
    }

    /**
     * Retrieves the y-coordinate of the second point of the selection.
     *
     * @return The y-coordinate of the second point.
     */
    public int getY2() {
        return y2;
    }

    /**
     * Sets the y-coordinate of the first point of the selection.
     *
     * @param y1 The y-coordinate of the first point.
     */
    public void setY1(int y1) {
        this.y1 = y1;
    }

    /**
     * Retrieves the y-coordinate of the first point of the selection.
     *
     * @return The y-coordinate of the first point.
     */
    public int getY1() {
        return y1;
    }

    /**
     * Sets the x-coordinate of the second point of the selection.
     *
     * @param x2 The x-coordinate of the second point.
     */
    public void setX2(int x2) {
        this.x2 = x2;
    }

    /**
     * Retrieves the x-coordinate of the second point of the selection.
     *
     * @return The x-coordinate of the second point.
     */
    public int getX2() {
        return x2;
    }

    /**
     * Sets the x-coordinate of the first point of the selection.
     *
     * @param x1 The x-coordinate of the first point.
     */
    public void setX1(int x1) {
        this.x1 = x1;
    }

    /**
     * Retrieves the x-coordinate of the first point of the selection.
     *
     * @return The x-coordinate of the first point.
     */
    public int getX1() {
        return x1;
    }

    /**
     * Sets the coordinates of the selected point.
     *
     * @param x The x-coordinate of the selected point.
     * @param y The y-coordinate of the selected point.
     */
    public void setPoint(int x, int y) {
        this.setX1(x);
        this.setY1(y);
        this.setX2(x);
        this.setY2(y);
    }

    /**
     * Sets the starting point of the selection.
     *
     * @param x The x-coordinate of the starting point.
     * @param y The y-coordinate of the starting point.
     */
    public void setStart(int x, int y) {
        this.setX1(x);
        this.setY1(y);
    }

    /**
     * Sets the starting point of the selection.
     *
     * @param x The x-coordinate of the starting point.
     * @param y The y-coordinate of the starting point.
     */
    public void setStart(double x, double y) {
        setStart((int) x, (int) y);
    }

    /**
     * Sets the ending point of the selection.
     *
     * @param x The x-coordinate of the ending point.
     * @param y The y-coordinate of the ending point.
     */
    public void setEnd(int x, int y) {
        this.setX2(x);
        this.setY2(y);
    }

    /**
     * Sets the ending point of the selection.
     *
     * @param x The x-coordinate of the ending point.
     * @param y The y-coordinate of the ending point.
     */
    public void setEnd(double x, double y) {
        setEnd((int) x, (int) y);
    }

    /**
     * Retrieves the width of the selection.
     *
     * @return The width of the selection.
     */
    public int getWidth() {
        return Math.max(x1, x2) - Math.min(x1, x2);
    }

    /**
     * Retrieves the height of the selection.
     *
     * @return The height of the selection.
     */
    public int getHeight() {
        return Math.max(y1, y2) - Math.min(y1, y2);
    }

    /**
     * Retrieves the x-coordinate of the top-left corner of the selection.
     *
     * @return The x-coordinate of the top-left corner.
     */
    public int getX() {
        return Math.min(x1, x2);
    }

    /**
     * Retrieves the y-coordinate of the top-left corner of the selection.
     *
     * @return The y-coordinate of the top-left corner.
     */
    public int getY() {
        return Math.min(y1, y2);
    }
}
