package cosc202.andie.actions.tool;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import cosc202.andie.ClickListener;


/**
 * <p>
 * ImageOperation to draw a shape.
 * </p>
 * 
 * <p>
 * The regionselector stores the location, shape, and colour of a shape to be drawn. The location of Points is obtained from ClickListener
 * </p>
 * 
 * @see ClickListener
 * @author Sola Woodhouse
 * @version 1.0
 */
public class RegionSelector extends Selection{
    
    /**Stores the shape to be drawn */
    private String shape;

    /**Stores the colour for the shape to be drawn */
    private Color colour;

    /**Stores whether the shape will be filled or not */
    private boolean fill;

    private int strokeSize;

    /**Constructor for unfilled shapes */
    public RegionSelector(String shapeIn, Color colourIn){
        shape = shapeIn;
        fill = false;
        colour = colourIn;
    }

    /**Constructor for filled shapes */
    public RegionSelector(String shapeIn, Color colourIn, boolean fillIn){
        shape = shapeIn;
        colour = colourIn;
        fill = fillIn;
        strokeSize = 1;
    }

        /**Constructor for filled shapes */
        public RegionSelector(String shapeIn, Color colourIn, boolean fillIn, int strokeSizeIn){
            shape = shapeIn;
            colour = colourIn;
            fill = fillIn;
            strokeSize = strokeSizeIn;
        }
    
    /**Apply method
     * <p>
     * Draws the shape made by @see drawShape onto the buffered image. There is live feedback to see the shape before drawing it
     * @param input The image to draw the shape on.
     * @return The resulting image with a shape drawn on.
     */
    public BufferedImage apply(BufferedImage input){
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        Graphics2D g2d = output.createGraphics();
        int stroke = (int) (Math.sqrt(Math.max(input.getHeight(), input.getWidth()))/10)*strokeSize;
        g2d.setStroke(new BasicStroke(stroke));
        g2d.setColor(colour);
        if(shape=="line"){
            g2d.drawLine(getX1(), getY1(), getX2(), getY2());
        }else{
            if(fill){
                g2d.fill(drawShape());
            }else{
            g2d.draw(drawShape());
            }
        }
        g2d.dispose();
        return output;
    }

    /**DrawShape method
     * <p>
     * @return The shape to be drawn by the apply method
     */
    private Shape drawShape(){
        Shape draw = null;
        if(shape=="oval"){
            draw = new Ellipse2D.Double(Math.min(getX1(),getX2()), Math.min(getY1(),getY2()), Math.max(getX2(),getX1())-Math.min(getX2(),getX1()), Math.max(getY2(),getY1())-Math.min(getY1(),getY2()));
        }if(shape == "rectangle"){
            draw = new Rectangle (Math.min(getX1(),getX2()), Math.min(getY1(),getY2()), Math.max(getX2(),getX1())-Math.min(getX2(),getX1()), Math.max(getY2(),getY1())-Math.min(getY1(),getY2()));
        }
        return draw;
    }

}
