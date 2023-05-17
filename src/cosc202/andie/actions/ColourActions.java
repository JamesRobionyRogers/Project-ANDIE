package cosc202.andie.actions;

import cosc202.andie.*;
import cosc202.andie.actions.colour.*;
import java.util.*;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
//import javax.swing.plaf.OptionPaneUI;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel
 * directly
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations
 * will need to be added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ColourActions {

    private SetLanguage language = SetLanguage.getInstance();

    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        actions.add(new ConvertToGreyAction(language.getTranslated("greyscale"), Icons.COLOUR_GREYSCALE, language.getTranslated("greyscale_desc"), null));
        actions.add(new ChangeBrightnessAndContrast(language.getTranslated("brightness_contrast"), Icons.COLOUR_ADJUSTMENTS ,language.getTranslated("brightness_contrast_desc"), KeyboardShortcut.COLOUR_BRIGHTNESS_CONTRAST));
        actions.add(new InvertColourAction("[TRANSLATE] Invert", Icons.COLOUR_INVERT, "[TRANSLATE] Invert the colour of an image", KeyboardShortcut.COLOUR_INVERT));
        actions.add(new AlphaMaskAction("[TRANSLATE] Alpha Mask", Icons.COLOUR_MASK, "[TRANSLATE] Apply an alpha mask to the image",null));
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

        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }

        if (ImageAction.getTarget().getImage().getCurrentImage() == null) {
            fileMenu.setEnabled(false);
        }
        return fileMenu;
    }

    /**
     * <p>
     * Changes the alpha of the image following a users mask.
     * </p>
     * 
     * @see AlphaMask
     */
    public class AlphaMaskAction extends ImageAction {

        /**
         * <p>
         * Create a new alpha-mask action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        AlphaMaskAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the alpha-mask action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the AlphaMaskAction is triggered.
         * It changes the images alpha channel following a mask.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent evt) {

            BufferedImage mask = null;
            JFileChooser fileChooser = new JFileChooser();
            FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(imageFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            int result = fileChooser.showOpenDialog(Andie.getJFrame());

            if (result != JFileChooser.APPROVE_OPTION)
                return;

            try {
                mask = ImageIO.read(fileChooser.getSelectedFile());

                target.getImage().apply(new AlphaMask(mask));
                target.repaint();
                target.getParent().revalidate();

            } catch (Exception ex) {
                ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("save_file_io_excepton"));
            }

        }
        

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
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
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
     * Action to invert the colour on an image.
     * </p>
     * 
     * @see InverColour
     */
    public class InvertColourAction extends ImageAction {

        /**
         * <p>
         * Create a new colour inversion action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        InvertColourAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the invert-colour action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the InvertColourAction is triggered.
         * Inverts the images colour.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new InvertColour());
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
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */

        ChangeBrightnessAndContrast(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Change-Brightness-And-Contrast action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ChangeBrightnessAndContrast is triggered.
         * It prompts the user for a Brightness percentage, and a Contrast Percentage,
         * then applys {@link BrightnessAndContrast}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            SetLanguage language = SetLanguage.getInstance();

            JSlider brightnessSlider = new JSlider(-100, 100);
            brightnessSlider.setMajorTickSpacing(25);
            brightnessSlider.setMinorTickSpacing(5);
            brightnessSlider.setPaintTicks(true);
            JSlider contrastSlider = new JSlider(-100, 100);
            contrastSlider.setMajorTickSpacing(20);
            contrastSlider.setMinorTickSpacing(5);
            contrastSlider.setPaintTicks(true);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(new JLabel(language.getTranslated("brightness")));
            panel.add(brightnessSlider);
            panel.add(new JLabel(language.getTranslated("contrast")));
            panel.add(contrastSlider);

            Object[] options = { language.getTranslated("ok"), language.getTranslated("cancel") };

            // ChangeListener that is notified every time the value in the jslider is
            // updated by the user
            brightnessSlider.addChangeListener(new ChangeListener() {
                @Override
                // is called when state changes, and updates image shown behind the
                // SpinnerNumberModel
                public void stateChanged(ChangeEvent e) {
                    if (brightnessSlider.getValueIsAdjusting())
                        return;
                    // if this is the first time number is altered, change to show it has been
                    // altered and then apply filter

                    target.getImage()
                            .applyTemp(new BrightnessAndContrast(brightnessSlider.getValue(), contrastSlider.getValue()));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            // ChangeListener that is notified every time the value in the jslider is
            // updated by the user
            contrastSlider.addChangeListener(new ChangeListener() {
                @Override
                // is called when state changes, and updates image shown behind the
                // SpinnerNumberModel
                public void stateChanged(ChangeEvent e) {
                    if (contrastSlider.getValueIsAdjusting())
                        return;
                    // if this is the first time number is altered, change to show it has been
                    // altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter

                    target.getImage()
                            .applyTemp(new BrightnessAndContrast(brightnessSlider.getValue(), contrastSlider.getValue()));
                    target.repaint();
                    target.getParent().revalidate();
                }

            });

            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                panel, 
                language.getTranslated("b_c_question"),
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                Icons.COLOUR_ADJUSTMENTS_WINDOW, 
                options, 
                options[0]
            );

            target.getImage().revert();
            if (option == 0){
                target.getImage().apply(new BrightnessAndContrast(brightnessSlider.getValue(), contrastSlider.getValue()));
            }
            target.repaint();
            target.getParent().revalidate();

        }

    }

}
