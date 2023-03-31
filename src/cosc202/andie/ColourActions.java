package cosc202.andie;

import java.util.*;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel directly 
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ColourActions {
    
    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        SetLanguage language = SetLanguage.getInstance();
        actions = new ArrayList<Action>();
<<<<<<< src/cosc202/andie/ColourActions.java
        actions.add(new ConvertToGreyAction(language.getTranslated("greyscale"), null, language.getTranslated("greyscale_desc"), Integer.valueOf(KeyEvent.VK_G)));
=======
        actions.add(new ChangeBrightnessAndContrast("Brightness&Contrast", null, "Change Brightness and Contrast", Integer.valueOf(KeyEvent.VK_B)));

>>>>>>> src/cosc202/andie/ColourActions.java
    }

    /**
     * <p>
     * Create a menu contianing the list of Colour actions.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        SetLanguage language = SetLanguage.getInstance();
        JMenu fileMenu = new JMenu(language.getTranslated("colour"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to convert an image to greyscale.
     * </p>
     * 
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new ConvertToGrey());
            target.repaint();
            target.getParent().revalidate();
        }

    }




    /**
     * <p>
     * Action to convert an images Brightness and Contrast.
     * </p>
     * 
     * @see BrightnessAndCotrast
     */
    public class ChangeBrightnessAndContrast extends ImageAction { 
        
        
        /**
         * <p>
         * Create a ChangeBrightnessAndContrast action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */

        ChangeBrightnessAndContrast(String name, ImageIcon icon, String desc, Integer mnemonic) { 
            super(name, icon, desc, mnemonic); 
        } 


         /**
         * <p>
         * Callback for when the Change-Brightness-And-Contrast action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ChangeBrightnessAndContrast is triggered.
         * It prompts the user for a Brightness percentage, and a Contrast Percentage, then applys {@link BrightnessAndContrast}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            int brightness = 0;
            int contrast = 0;
        
            SpinnerNumberModel B = new SpinnerNumberModel(0, -100, 100, 5);
            JSpinner brightnessSpinner = new JSpinner(B);
            SpinnerNumberModel C = new SpinnerNumberModel(0, -100, 100, 5);
            JSpinner contrastSpinner = new JSpinner(C);
        
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(new JLabel("Brightness:"));
            panel.add(brightnessSpinner);
            panel.add(new JLabel("Contrast:"));
            panel.add(contrastSpinner);
        
            int option = JOptionPane.showOptionDialog(null, panel, "Enter Brightness and Contrast",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                brightness = B.getNumber().intValue();
                contrast = C.getNumber().intValue();
            }
        
            target.getImage().apply(new BrightnessAndContrast(brightness, contrast));
            target.repaint();
            target.getParent().revalidate();
        }

    } 

}
