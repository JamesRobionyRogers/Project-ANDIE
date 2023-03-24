package cosc202.andie;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FlipImageActions {
       
    /**
     * A list of actions for the flip image menu.
     */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Tool menu actions.
     * </p>
     */
    public FlipImageActions() {
        actions = new ArrayList<Action>();
        actions.add(new HorizontalFlipAction("Horizontal", null, "Flip image horizontally", Integer.valueOf(KeyEvent.VK_M)));
        actions.add(new VerticalFlipAction("Vertical", null, "Flip image vertically", Integer.valueOf(KeyEvent.VK_M)));
    }

    /**
     * <p>
     * Create a menu containing the list of Tool actions.
     * </p>
     * 
     * @return The Tool menu UI element.
     */
    public JMenu createMenu() {
        JMenu flipMenu = new JMenu("Flip Image");

        for (Action action: actions) {
            flipMenu.add(new JMenuItem(action));
        }

        return flipMenu;
    }

        /**
         * Create a new Image flip tool action
         * 
         * @param name The name of the action
         * @param icon An icon to use to represent the action
         * @param desc A brief description of the action
         * @param mnemonic A mnemonic key to use as a shortcut
         */
        public class HorizontalFlipAction extends ImageAction{

            /**
             * Action to flip image horizontally or vertically
             * 
             * @see ImageFlip
             * 
             * @param e The event triggering the callback
             */
            HorizontalFlipAction(String name, ImageIcon icon, String desc, Integer mnemonic){
                super(name, icon, desc, mnemonic);
            }
    
        public void actionPerformed(ActionEvent e){
    
            target.getImage().apply(new ImageFlip(true));
            target.repaint();
            target.getParent().revalidate();
        }
    }

            /**
         * Create a new Image flip tool action
         * 
         * @param name The name of the action
         * @param icon An icon to use to represent the action
         * @param desc A brief description of the action
         * @param mnemonic A mnemonic key to use as a shortcut
         */
        public class VerticalFlipAction extends ImageAction{

        /**
         * Action to flip image vertically
         * 
         * @see ImageFlip
         * 
         * @param e The event triggering the callback
        */
        VerticalFlipAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

    public void actionPerformed(ActionEvent e){
        //System.out.println("action performed");
        target.getImage().apply(new ImageFlip(false));
        target.repaint();
        target.getParent().revalidate();
    }
}
    
}
