package cosc202.andie;

import java.util.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood. 
 * This includes a mean filter (a simple blur) in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class FilterActions{

    SetLanguage language = SetLanguage.getInstance();
    
    /** A list of actions for the Filter menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     */
    public FilterActions() {
        SetLanguage language = SetLanguage.getInstance();
        actions = new ArrayList<Action>();
        actions.add(new MeanFilterAction(language.getTranslated("mean_filter"), null, language.getTranslated("mean_filter_desc"), Integer.valueOf(KeyEvent.VK_M)));
        actions.add(new MedianFilterAction(language.getTranslated("median_filter"), null, language.getTranslated("median_filter_desc"), Integer.valueOf(KeyEvent.VK_M)));
        actions.add(new SharpenFilterAction(language.getTranslated("sharpen_filter"), null, language.getTranslated("sharpen_filter_desc"),Integer.valueOf(KeyEvent.VK_B)));
        actions.add(new SoftBlurAction(language.getTranslated("soft_blur_filter"), null, language.getTranslated("soft_blur_filter_desc"), Integer.valueOf(KeyEvent.VK_S))); 
        actions.add(new GaussianBlurAction(language.getTranslated("gaussian_blur_filter"), null, language.getTranslated("gaussian_blur_filter_desc"), Integer.valueOf(KeyEvent.VK_G)));
    }
    

    /**
     * <p>
     * Create a menu contianing the list of Filter actions.
     * </p>
     * 
     * @return The filter menu UI element.
     */
    public JMenu createMenu() {
        SetLanguage language = SetLanguage.getInstance();
        JMenu fileMenu = new JMenu(language.getTranslated("filter"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to blur an image with a mean filter.
     * </p>
     * 
     * @see MeanFilter
     */
    public class MeanFilterAction extends ImageAction {

        public boolean hasChanged = false;
        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MeanFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link MeanFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            SetLanguage language = SetLanguage.getInstance();

            Object[] options = {language.getTranslated("ok"), language.getTranslated("cancel")};

            // Pop-up dialog box to ask for the radius value.
           
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
          
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            
            //ChangeListener that is notified every time the value in the radiusspinner is updated by the user
            radiusModel.addChangeListener(new ChangeListener() {
                @Override
                // is called when state changes, and updates image shown behind the SpinnerNumberModel
                public void stateChanged(ChangeEvent e) {
                    //if this is the first time number is altered, change to show it has been altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter
                    if(!hasChanged){ hasChanged = true;
                    } else {
                        target.getImage().undo();
                        target.repaint();
                        target.getParent().revalidate();
                    }

                    SpinnerNumberModel spinner = (SpinnerNumberModel) e.getSource();
                    int radius = spinner.getNumber().intValue();
                    target.getImage().apply(new MeanFilter(radius));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });
                
            int option = JOptionPane.showOptionDialog(Andie.getJFrame(), radiusSpinner, language.getTranslated("enter_filter_radius"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
            // If the user cancels, undo last operation that was showing user effect of mean filter with given radius
            if (option == 1) {
                if(hasChanged){
                target.getImage().undo();
                target.repaint();
                target.getParent().revalidate();
                }
            } 
            if(option == -1){
                target.getImage().undo();
                target.repaint();
                target.getParent().revalidate();
            }
            //sets hasChanged back to false for if filter is used again
            hasChanged = false;
            
        }

    }

    /**
     * <p>
     * Action to blur an image with a soft blur filter.
     * </p>
     * 
     * @see SoftBlurFilter
     */
    public class SoftBlurAction extends ImageAction {

        /**
         * <p>
         * Create a new Soft Blur filter action
         * </p>
         * 
         * @param name     action name
         * @param icon     action icon
         * @param desc     action description
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        SoftBlurAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Create and apply the filter to the image
                target.getImage().apply(new SoftBlurFilter());
                target.repaint();
                target.getParent().revalidate();
            } catch (Exception ex) {
                ExceptionHandler.displayError(language.getTranslated("unapplied_filter_error"));
            }

        }

    }

    /**
     * <p>
     * Action to blur an image using the Gaussian Blur filter.
     * </p>
     * 
     * @see GaussianBlurFilter
     */
    public class GaussianBlurAction extends ImageAction {

        //boolean to keep track of whether this is first radius applied as gaussian blur
        public boolean hasChanged = false;
        /**
         * <p>
         * Create a new Gaussian Blur Filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        GaussianBlurAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * This method is called whenever the GaussianBlurAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized
         * {@link GaussianBlurFilter} to the image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            String[] options = { language.getTranslated("ok"), language.getTranslated("cancel") }; 

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);

            //ChangeListener that is notified every time the value in the radiusspinner is updated by the user
            radiusModel.addChangeListener(new ChangeListener() {
                @Override
                // is called when state changes, and updates image shown behind the SpinnerNumberModel
                public void stateChanged(ChangeEvent e) {
                    //if this is the first time number is altered, change to show it has been altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter
                    if(!hasChanged){ 
                        hasChanged = true;
                    } else {
                        target.getImage().undo();
                        target.repaint();
                        target.getParent().revalidate();
                    }

                    SpinnerNumberModel spinner = (SpinnerNumberModel) e.getSource();
                    int radius = spinner.getNumber().intValue();
                    target.getImage().apply(new GaussianBlurFilter(radius));
                    target.repaint();
                    target.getParent().revalidate();
                }});

            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                radiusSpinner,
                language.getTranslated("enter_filter_radius"), 
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]
            );

            // Checks if user cancelled- if so it undoes the previous action
            if (option == 1) {
                if(hasChanged){
                    target.getImage().undo();
                    target.repaint();
                    target.getParent().revalidate();
                    }
            }
            if(option == -1){
                target.getImage().undo();
                target.repaint();
                target.getParent().revalidate();
            }
            //sets hasChanged back to false for if filter is used again
            hasChanged = false;
        }
            
    }

    /**
     * Action to blur image with median filter
     * 
     * @see MedianFilter
     * 
     */
    public class MedianFilterAction extends ImageAction {

        //boolean to keep track of whether this is first radius applied as median filter
        public boolean hasChanged = false;
        /**
         * Create a new median-filter action
         * 
         * @param name     The name of the action
         * @param icon     An icon to use to represent the action
         * @param desc     A brief description of the action
         * @param mnemonic A mnemonic key to use as a shortcut
         */
        MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * This method is called whenever the MedianFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized
         * {@link MedianFilter} to the image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            SetLanguage language = SetLanguage.getInstance();

            Object[] options = { language.getTranslated("ok"), language.getTranslated("cancel") };

            SpinnerNumberModel radiusModel2 = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel2);

            //ChangeListener that is notified every time the value in the radiusspinner is updated by the user
            radiusModel2.addChangeListener(new ChangeListener() {
                @Override
                // is called when state changes, and updates image shown behind the SpinnerNumberModel
                public void stateChanged(ChangeEvent e) {
                    //if this is the first time number is altered, change to show it has been altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter
                    if(!hasChanged){ 
                        hasChanged = true;
                    } else {
                        target.getImage().undo();
                        target.repaint();
                        target.getParent().revalidate();
                    }

                    SpinnerNumberModel spinner = (SpinnerNumberModel) e.getSource();
                    int radius = spinner.getNumber().intValue();
                    target.getImage().apply(new MedianFilter(radius));
                    target.repaint();
                    target.getParent().revalidate();
                }});


            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    language.getTranslated("enter_filter_radius"), JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                // If the user cancels, undo last operation that was showing user effect of mean filter with given radius
                    if (option == 1) {
                        if(hasChanged){
                            target.getImage().undo();
                            target.repaint();
                            target.getParent().revalidate();
                            }
                    }
                    if(option == -1){
                        target.getImage().undo();
                        target.repaint();
                        target.getParent().revalidate();
                    }
            //sets hasChanged back to false for if filter is used again
            hasChanged = false;
        }

    }

    /**
     * Action to sharpen image with sharpen filter
     * 
     * @see SharpenFilter
     * 
     */
    public class SharpenFilterAction extends ImageAction {
        /**
         * Create a new sharpen filter action
         * 
         * @param name     The name of the action
         * @param icon     An icon to use to represent the action
         * @param desc     A brief description of the action
         * @param mnemonic A mnemonic key to use as a shortcut
         */
        SharpenFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);

        }
        /**
         * <p>
         * This method is called whenever the SharpenFilterAction is triggered.
         * {@link SharpenFilter} is applied to the image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            // Create and apply the filter
            target.getImage().apply(new SharpenFilter());
            target.repaint();
            target.getParent().revalidate();
        }
    }
}
