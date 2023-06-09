package cosc202.andie.actions;

import cosc202.andie.*;
import cosc202.andie.actions.filter.*;

import java.util.*;
import java.awt.GridLayout;
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
 * This includes a mean filter (a simple blur) in the sample code, but more
 * operations will need to be added.
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
public class FilterActions implements ActionCollection {

    SetLanguage language = SetLanguage.getInstance();

    /** A list of actions for the Filter menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     */
    public FilterActions() {
        actions = new ArrayList<Action>();
        actions.add(new MeanFilterAction("mean_filter", Icons.FILTER_BLUR, "mean_filter_desc", KeyboardShortcut.FILTER_MEAN_BLUR));
        actions.add(new MedianFilterAction("median_filter", Icons.FILTER_BLUR, "median_filter_desc", null));
        actions.add(new SharpenFilterAction("sharpen_filter", Icons.FILTER_SHARPEN, "sharpen_filter_desc", KeyboardShortcut.FILTER_SHARPEN));
        actions.add(new SoftBlurAction("soft_blur_filter", Icons.FILTER_CIRCLE_BLUR, "soft_blur_filter_desc", null));
        actions.add(new GaussianBlurAction("gaussian_blur_filter", Icons.FILTER_BLUR, "gaussian_blur_filter_desc", KeyboardShortcut.FILTER_GAUSSIAN_BLUR));
        actions.add(new EmbossFilterAction("emboss_filter", Icons.FILTER_EMBOSS, "emboss_filter_desc", KeyboardShortcut.FILTER_EMBOSS));
        actions.add(new EdgeDetectionAction("edge_filter", Icons.FILTER_EDGE_DETECTION, "edge_filter_desc", null));

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

        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));

        }
        if (ImageAction.getTarget().getImage().getCurrentImage() == null) {
            fileMenu.setEnabled(false);
        }

        return fileMenu;
    }

    @Override
    public ArrayList<Action> getToolbarActions() {
        ArrayList<Action> toolbarActions = new ArrayList<Action>();

        toolbarActions.add(actions.get(4));     // Gaussian Blur

        return toolbarActions;
    }

    /**
     * <p>
     * Action to blur an image with a mean filter.
     * </p>
     * 
     * @see MeanFilter
     */
    public class MeanFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        MeanFilterAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MeanFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized
         * {@link MeanFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            SetLanguage language = SetLanguage.getInstance();

            // Determine the radius - ask the user.

            Object[] options = { language.getTranslated("ok"), language.getTranslated("cancel") };

            // Pop-up dialog box to ask for the radius value.

            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);

            JSpinner radiusSpinner = new JSpinner(radiusModel);

            // ChangeListener that is notified every time the value in the radiusspinner is
            // updated by the user
            radiusModel.addChangeListener(new ChangeListener() {
                @Override
                // is called when state changes, and updates image shown behind the
                // SpinnerNumberModel
                public void stateChanged(ChangeEvent e) {
                    // if this is the first time number is altered, change to show it has been
                    // altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter

                    SpinnerNumberModel spinner = (SpinnerNumberModel) e.getSource();
                    int radius = spinner.getNumber().intValue();
                    target.getImage().applyTemp(new MeanFilter(radius));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                radiusSpinner,
                language.getTranslated("enter_filter_radius"), 
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, 
                (Icon) this.getValue("WindowIcon"), 
                options, 
                options[0]
            );

            // If the user cancels, undo last operation that was showing user effect of mean
            // filter with given radius
            target.getImage().revert();
            if (option == 0){
                target.getImage().apply(new MeanFilter(radiusModel.getNumber().intValue()));
            }
            target.repaint();
            target.getParent().revalidate();
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
        SoftBlurAction(String name, String icon, String desc, KeyStroke mnemonic) {
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
        GaussianBlurAction(String name, String icon, String desc, KeyStroke mnemonic) {
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

            // ChangeListener that is notified every time the value in the radiusspinner is
            // updated by the user
            radiusModel.addChangeListener(new ChangeListener() {
                @Override
                // is called when state changes, and updates image shown behind the
                // SpinnerNumberModel
                public void stateChanged(ChangeEvent e) {
                    // if this is the first time number is altered, change to show it has been
                    // altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter

                    SpinnerNumberModel spinner = (SpinnerNumberModel) e.getSource();
                    int radius = spinner.getNumber().intValue();
                    target.getImage().applyTemp(new GaussianBlurFilter(radius));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(),
                radiusSpinner,
                language.getTranslated("enter_filter_radius"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                (Icon) this.getValue("WindowIcon"),
                options,
                options[0]
            );
            target.getImage().revert();
            // Checks if user cancelled- if so it undoes the previous action
            if (option == 0) {
                target.getImage().apply(new GaussianBlurFilter(radiusModel.getNumber().intValue()));
            }
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * Action to blur image with median filter
     * 
     * @see MedianFilter
     * 
     */
    public class MedianFilterAction extends ImageAction {

        /**
         * Create a new median-filter action
         * 
         * @param name     The name of the action
         * @param icon     An icon to use to represent the action
         * @param desc     A brief description of the action
         * @param mnemonic A mnemonic key to use as a shortcut
         */
        MedianFilterAction(String name, String icon, String desc, KeyStroke mnemonic) {
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

            // ChangeListener that is notified every time the value in the radiusspinner is
            // updated by the user
            radiusModel2.addChangeListener(new ChangeListener() {
                @Override
                // is called when state changes, and updates image shown behind the
                // SpinnerNumberModel
                public void stateChanged(ChangeEvent e) {
                    // if this is the first time number is altered, change to show it has been
                    // altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter

                    SpinnerNumberModel spinner = (SpinnerNumberModel) e.getSource();
                    int radius = spinner.getNumber().intValue();
                    target.getImage().applyTemp(new MedianFilter(radius));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                radiusSpinner,
                language.getTranslated("enter_filter_radius"), 
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, 
                (Icon) this.getValue("WindowIcon"),
                options, 
                options[0]
                );

            // If the user cancels, undo last operation that was showing user effect of mean
            // filter with given radius
            target.getImage().revert();
            if (option == 0){
                int radius = radiusModel2.getNumber().intValue();
                target.getImage().apply(new MedianFilter(radius));

            }
            target.repaint();
            target.getParent().revalidate();
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
        SharpenFilterAction(String name, String icon, String desc, KeyStroke mnemonic) {
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

    /**
     * Action to emboss an image
     * 
     * @see EmbossEdgeDetection
     * 
     */
    public class EmbossFilterAction extends ImageAction {
        private String[] options = { language.getTranslated("emboss_left"), language.getTranslated("emboss_top_left"), language.getTranslated("emboss_top"),
        language.getTranslated("emboss_top_right"), language.getTranslated("emboss_right"), language.getTranslated("emboss_bottom_right"), language.getTranslated("emboss_bottom"),
        language.getTranslated("emboss_bottom_left") };

        /**
         * Create a new sharpen filter action
         * 
         * @param name     The name of the action
         * @param icon     An icon to use to represent the action
         * @param desc     A brief description of the action
         * @param mnemonic A mnemonic key to use as a shortcut
         */
        EmbossFilterAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);

        }

        public void actionPerformed(ActionEvent e) {

            JComboBox<String> jbox = new JComboBox<String>(options);
            // Create and apply the filter
            Object[] okClose = { language.getTranslated("ok"), language.getTranslated("cancel") };
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 2));
            panel.add(new JLabel(language.getTranslated("emboss_type")));
            panel.add(jbox);
            jbox.addItemListener(new ItemListener() {

                // is called when state changes, and updates image shown behind the
                // SpinnerNumberModel
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() != ItemEvent.SELECTED)
                        return;
                    // if this is the first time number is altered, change to show it has been
                    // altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter
                    int choice = jbox.getSelectedIndex();
                    target.getImage().applyTemp(new NegativeFilter(choice));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            // apply first selected option of emboss
            target.getImage().applyTemp(new NegativeFilter(0));
            target.repaint();
            target.getParent().revalidate();
            
            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                panel, 
                language.getTranslated("emboss_question"),
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                (Icon) this.getValue("WindowIcon"),
                okClose, 
                options[0]
            ); 
            target.getImage().revert();


            if (option == 0){
                target.getImage().apply(new NegativeFilter(jbox.getSelectedIndex()));
            }
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * Action to emboss an image
     * 
     * @see EmbossEdgeDetection
     * 
     */
    public class EdgeDetectionAction extends ImageAction {
        private String[] options = { language.getTranslated("edge_horizontal"), language.getTranslated("edge_vertical") };

        /**
         * Create a new sharpen filter action
         * 
         * @param name     The name of the action
         * @param icon     An icon to use to represent the action
         * @param desc     A brief description of the action
         * @param mnemonic A mnemonic key to use as a shortcut
         */
        EdgeDetectionAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);

        }

        public void actionPerformed(ActionEvent e) {

            JComboBox<String> jbox = new JComboBox<String>(options);
            // Create and apply the filter
            Object[] okClose = { language.getTranslated("ok"), language.getTranslated("cancel") };
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 2));
            panel.add(new JLabel(language.getTranslated("edge_type")));
            panel.add(jbox);
            jbox.addItemListener(new ItemListener() {

                // is called when state changes, and updates image shown behind the
                // SpinnerNumberModel
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() != ItemEvent.SELECTED)
                        return;
                    // if this is the first time number is altered, change to show it has been
                    // altered and then apply filter
                    // if number has already changed, undo last operation and then apply filter

                    int choice = jbox.getSelectedIndex() + 8;
                    target.getImage().applyTemp(new NegativeFilter(choice));
                    target.repaint();
                    target.getParent().revalidate();
                }
            });

            // apply the first selected option of edge detection
            target.getImage().applyTemp(new NegativeFilter(8));
            target.repaint();
            target.getParent().revalidate();

            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                panel, 
                language.getTranslated("edge_question"),
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                (Icon) this.getValue("WindowIcon"), 
                okClose, 
                options[0]
            );
            target.getImage().revert();

            if (option == 0){
                target.getImage().apply(new NegativeFilter(jbox.getSelectedIndex() + 8));
            }

            target.repaint();
            target.getParent().revalidate();
        }
    }
}
