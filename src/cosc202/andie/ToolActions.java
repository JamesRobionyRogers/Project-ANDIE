package cosc202.andie;

import java.util.*;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.awt.GridLayout;

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

        actions.add(new ResizeToolAction(language.getTranslated("resize"), null, language.getTranslated("resize_desc"), null));
        actions.add(new PixelPeekToolAction("Peek [DNT]", null, "Peek the Pixel [DNT]", null));
        actions.add(new RotateToolAction(language.getTranslated("rotate"), null, language.getTranslated("rotate_desc"), null));
        actions.add(new FlipImageActions(language.getTranslated("flip_image"), null, language.getTranslated("flip_image_desc"), null));
        //actions.add(new FlipImageActions("Flip Image", null, "Flip the Image", null));
    }

    /**
     * <p>
     * Create a menu containing the list of Tool actions.
     * </p>
     * 
     * @return The Tool menu UI element.
     */
    public JMenu createMenu() {
        SetLanguage language = SetLanguage.getInstance();
        JMenu toolMenu = new JMenu(language.getTranslated("tools"));

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
            SetLanguage language = SetLanguage.getInstance();
            int option; 
            Object[] flip = {language.getTranslated("horizontal"),
                    language.getTranslated("vertical")};
            option = JOptionPane.showOptionDialog(null, language.getTranslated("flip_image_question"),
            language.getTranslated("flip"),
            JOptionPane.DEFAULT_OPTION,
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
            SetLanguage language = SetLanguage.getInstance();
            //Determine scale - ask user
            int scale = 100;

            Object[] options = {language.getTranslated("ok"), language.getTranslated("cancel")};

            SpinnerNumberModel radiusModel3 = new SpinnerNumberModel(100, 1, null, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel3);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner, language.getTranslated("resize_question"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
            if (option == 1){
                return;
            } else if(option == 0){
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
        SetLanguage language = SetLanguage.getInstance();
        public void actionPerformed(ActionEvent e){
            int deg;

            //Custom button text
            Object[] options = {language.getTranslated("rotate_270_-90"),
                                language.getTranslated("rotate_180_-180"),
                                language.getTranslated("rotate_90_-270")};
            deg = JOptionPane.showOptionDialog(null, language.getTranslated("rotate_image_question"),
            language.getTranslated("rotate"),
            JOptionPane.DEFAULT_OPTION,
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
    
            SpinnerNumberModel B = new SpinnerNumberModel(0, 0, null, 1);
            JSpinner brightnessSpinner = new JSpinner(B);
            SpinnerNumberModel C = new SpinnerNumberModel(0, 0, null, 1);
            JSpinner contrastSpinner = new JSpinner(C);
        
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(new JLabel("x:"));
            panel.add(brightnessSpinner);
            panel.add(new JLabel("y:"));
            panel.add(contrastSpinner);
        
            int option = JOptionPane.showOptionDialog(null, panel, "Enter a pixels coordinates [DNT]",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                x = B.getNumber().intValue();
                y = C.getNumber().intValue();
            }
            PixelPeek.pixelPeek(x,y,target.getImage().getCurrentImage());
        }
    
        PixelPeekToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }
}
