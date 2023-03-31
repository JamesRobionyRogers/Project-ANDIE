package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;


public class ToolActions {
    
    /**
     * A list of actions for the Tool menu.
     */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Tool menu actions.
     * </p>
     */
    public ToolActions() {
        actions = new ArrayList<Action>();
        SetLanguage language = SetLanguage.getInstance();

        actions.add(new ResizeToolAction("Resize", null, "Resize the image", null));
        actions.add(new PixelPeekToolAction("Peek", null, "Peek the Pixel", null));
        actions.add(new RotateToolAction("Rotate", null, "Rotate image", null));
        //actions.add(new FlipImageActions(language.getTranslated("flip_image"), null, language.getTranslated("flip_image_desc"), null));
        actions.add(new FlipImageActions("Flip Image", null, "Flip the Image", null));
    }

    /**
     * <p>
     * Create a menu containing the list of Tool actions.
     * </p>
     * 
     * @return The Tool menu UI element.
     */
    public JMenu createMenu() {
        JMenu toolMenu = new JMenu("Tools");

        for (Action action: actions) {
            toolMenu.add(new JMenuItem(action));
        }

        return toolMenu;
    }

            /**
         * Create a new Image flip tool action
         * 
         * @param name The name of the action
         * @param icon An icon to use to represent the action
         * @param desc A brief description of the action
         * @param mnemonic A mnemonic key to use as a shortcut
         */
        public class FlipImageActions extends ImageAction{

            /**
             * Action to flip image horizontally or vertically
             * 
             * @see ImageFlip
             * 
             * @param e The event triggering the callback
             */

             //0 is true - horizontal
             //1 is false - vertical
            FlipImageActions(String name, ImageIcon icon, String desc, Integer mnemonic){
                super(name, icon, desc, mnemonic);
            }
    
        public void actionPerformed(ActionEvent e){
            int option; 
            Object[] flip = {"Horizontal",
                                "Vertical"};
            option = JOptionPane.showOptionDialog(null, "Which way do you want to flip the Image?",
            "Rotate",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            flip,
            flip[1]);

            if (option == 0){
                target.getImage().apply(new ImageFlip(true));
            } else if(option==1){
                target.getImage().apply(new ImageFlip(false));
            }else if(option==-1){
                return;
            }

            target.repaint();
            target.getParent().revalidate();
        }
    }

    
    public class ResizeToolAction extends ImageAction {

        public void actionPerformed(ActionEvent e){
            //Determine scale - ask user
            int scale = 100;

            SpinnerNumberModel radiusModel3 = new SpinnerNumberModel(100, 1, null, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel3);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter Resize %", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            
            if (option == JOptionPane.CANCEL_OPTION){
                return;
            } else if(option == JOptionPane.OK_OPTION){
                scale = radiusModel3.getNumber().intValue();
            }

            target.getImage().apply(new ResizeTool(scale));
            target.repaint();
            target.getParent().revalidate();
        }
        ResizeToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }
    public class RotateToolAction extends ImageAction {
        public void actionPerformed(ActionEvent e){
            int deg;

            //Custom button text
            Object[] options = {"270°/-90°",
                                "180°/-180°",
                                "90°/-270°"};
            deg = JOptionPane.showOptionDialog(null, "How do you want to rotate the image?",
            "Rotate",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2]);

            // -1 is cancel code for dialog option
            if (deg == -1) return;

            // set chosen option to corresponding degrees of rotation
            deg = (3-deg)*90;
            target.getImage().apply(new RotateTool(deg));
            target.repaint();
            target.getParent().revalidate();

        }

        RotateToolAction(String name, ImageIcon icon,
        String desc, Integer mnemonic) {
        super(name, icon, desc, mnemonic);
        }

    }

    public class PixelPeekToolAction extends ImageAction {

        public void actionPerformed(ActionEvent e){
            int x,y;
            x = y = 0;
            SpinnerNumberModel radiusModel3 = new SpinnerNumberModel(0, 0, null, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel3);
            JOptionPane.showOptionDialog(null, radiusSpinner, "x coord", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            x = radiusModel3.getNumber().intValue();
            JOptionPane.showOptionDialog(null, radiusSpinner, "y coord", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            y = radiusModel3.getNumber().intValue();

            target.getImage().apply(new PixelPeekTool(x,y));
            target.repaint();
            target.getParent().revalidate();
        }

        PixelPeekToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }
}
