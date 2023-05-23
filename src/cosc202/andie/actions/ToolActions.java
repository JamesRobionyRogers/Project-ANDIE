package cosc202.andie.actions;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.JDialog;

import cosc202.andie.*;
import cosc202.andie.actions.tool.*;

public class ToolActions implements ActionCollection {

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

        actions.add(new ResizeToolAction("resize", Icons.TOOLS_RESIZE, "resize_desc", KeyboardShortcut.TOOLS_RESIZE));
        actions.add(new RotateToolAction("rotate", Icons.TOOLS_ROTATE, "rotate_desc", KeyboardShortcut.TOOLS_ROTATE));
        actions.add(new FlipImageActions("flip_image", Icons.TOOLS_FLIP_HORIZONTAL, "flip_image_desc",
                KeyboardShortcut.TOOLS_FLIP));
        actions.add(new CropAction("crop", Icons.TOOLS_CROP, "crop_desc", KeyboardShortcut.TOOLS_CROP));
        actions.add(new DrawShapeAction("draw", Icons.TOOLS_DRAW, "draw_shape", null));
        actions.add(new RollAction("roll", null, "roll_desc", null));

        // Testing feature - not for production
        // actions.add(new PixelPeekToolAction("peek", null, "peek_desc", null));
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

        for (Action action : actions) {
            toolMenu.add(new JMenuItem(action));

        }
        if (ImageAction.getTarget().getImage().getCurrentImage() == null) {
            toolMenu.setEnabled(false);
        }
        return toolMenu;
    }

    @Override
    public ArrayList<Action> getToolbarActions() {
        ArrayList<Action> toolbarActions = new ArrayList<Action>();

        // Adding Crop, Rotate, Resize
        toolbarActions.add(actions.get(3));
        toolbarActions.add(actions.get(0));
        toolbarActions.add(actions.get(2));

        return toolbarActions;
    }

    /**
     * Create a new Image flip tool action
     * 
     * @param name     The name of the action
     * @param icon     An icon to use to represent the action
     * @param desc     A brief description of the action
     * @param mnemonic A mnemonic key to use as a shortcut
     */
    public class FlipImageActions extends ImageAction {

        /**
         * Action to flip image horizontally or vertically
         * 
         * @see ImageFlip
         * 
         * @param e The event triggering the callback
         */

        // 0 is true - horizontal
        // 1 is false - vertical
        FlipImageActions(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage language = SetLanguage.getInstance();
            int option;
            Object[] flip = { language.getTranslated("horizontal"), language.getTranslated("vertical") };

            option = JOptionPane.showOptionDialog(
                    Andie.getJFrame(),
                    language.getTranslated("flip_image_question"),
                    language.getTranslated("flip"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    (Icon) this.getValue("WindowIcon"),
                    flip,
                    flip[1]);

            if (option == 0) {
                target.getImage().apply(new ImageFlip(true));
            } else if (option == 1) {
                target.getImage().apply(new ImageFlip(false));
            } else if (option == -1) {
                return;
            }

            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class ResizeToolAction extends ImageAction {

        public void actionPerformed(ActionEvent e) {
            SetLanguage language = SetLanguage.getInstance();
            // Determine scale - ask user
            int scale = 100;

            Object[] options = { language.getTranslated("ok"), language.getTranslated("cancel") };

            SpinnerNumberModel radiusModel3 = new SpinnerNumberModel(100, 1, 65535, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel3);
            int option = JOptionPane.showOptionDialog(
                    Andie.getJFrame(),
                    radiusSpinner,
                    language.getTranslated("resize_question"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    (Icon) this.getValue("WindowIcon"),
                    options,
                    options[0]);

            if (option == 1) {
                return;
            } else if (option == 0) {
                scale = radiusModel3.getNumber().intValue();
            }
            // didn't scale don't perform ops
            if (scale == 100)
                return;

            try {
                // check image area is less than MAX_INTEGER
                int width = (int) (target.getImage().getCurrentImage().getWidth() * (double) scale / 100);
                int height = (int) (target.getImage().getCurrentImage().getHeight() * (double) scale / 100);

                // Image larger than max image size
                int size = Math.multiplyExact(width, height);

                if (size == 0)
                    throw (new ArithmeticException("Area was 0"));
                if (width > 65535)
                    throw (new ArithmeticException("Width too large"));
                if (height > 65535)
                    throw (new ArithmeticException("Height too large"));
            } catch (ArithmeticException ex) {
                ExceptionHandler.displayError(language.getTranslated("resize_warning").replace((CharSequence) "#",
                        (CharSequence) Integer.toString(scale)));
                return;
            }

            target.getImage().apply(new ResizeTool(scale));
            target.repaint();
            target.getParent().revalidate();
        }

        ResizeToolAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }

    public class RotateToolAction extends ImageAction {
        SetLanguage language = SetLanguage.getInstance();

        public void actionPerformed(ActionEvent e) {
            int deg;

            // Custom button text
            Object[] options = {
                    language.getTranslated("rotate_270_-90"),
                    language.getTranslated("rotate_180_-180"),
                    language.getTranslated("rotate_90_-270")
            };

            deg = JOptionPane.showOptionDialog(
                    Andie.getJFrame(),
                    language.getTranslated("rotate_image_question"),
                    language.getTranslated("rotate"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    (Icon) this.getValue("WindowIcon"),
                    options,
                    options[2]);

            // -1 is cancel code for dialog option
            if (deg == -1)
                return;

            // set chosen option to corresponding degrees of rotation
            deg = (3 - deg) * 90;
            target.getImage().apply(new RotateTool(deg));
            target.repaint();
            target.getParent().revalidate();

        }

        RotateToolAction(String name, String icon,
                String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

    }

    public class PixelPeekToolAction extends ImageAction {

        public void actionPerformed(ActionEvent e) {
            int x, y;
            x = y = 0;

            SpinnerNumberModel B = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
            JSpinner brightnessSpinner = new JSpinner(B);
            SpinnerNumberModel C = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
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
            PixelPeek.pixelPeek(x, y, target.getImage().getCurrentImage());
        }

        PixelPeekToolAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }

    /**
     * Create a new Crop tool action
     * 
     * @param name     The name of the action
     * @param icon     An icon to use to represent the action
     * @param desc     A brief description of the action
     * @param mnemonic A mnemonic key to use as a shortcut
     */

    public class CropAction extends ImageAction {

        public void actionPerformed(ActionEvent e) {
            Selection operation = new RegionSelector("rectangle", Color.WHITE);
            Selection operationFinal = new Crop();
            ClickListener.activate(operation, operationFinal);
            ClickListener.setTarget(target);
        }

        CropAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }

    public class DrawShapeAction extends ImageAction {

        boolean fill = false;
        Color colour = Color.BLUE;
        int strokeSize = 1;

        DrawShapeAction(String name, String icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {

            int shape;
            SetLanguage language = SetLanguage.getInstance();


                JButton colourPicker = new JButton(language.getTranslated("colour_chooser"));
                colourPicker.setBounds(200, 250, 100, 30);
                colourPicker.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource()==colourPicker){
                            ColorChooser color = new ColorChooser();
                            color.showDialog(null,language.getTranslated("choose_colour"));
                            colour = color.getColor();
                
                        }
                    }
                });

                JSlider thicknessSlider = new JSlider(1, 10);
                thicknessSlider.setMajorTickSpacing(9);
                thicknessSlider.setMinorTickSpacing(2);
                thicknessSlider.setPaintTicks(true);
                thicknessSlider.setPaintLabels(true);
                thicknessSlider.setValue(strokeSize);
                JPanel strokePanel = new JPanel();
                strokePanel.setLayout(new GridLayout(2, 2));
                //panel.add(new JLabel(language.getTranslated("thickness")));
                strokePanel.add(new JLabel(language.getTranslated("thickness")));
                strokePanel.add(thicknessSlider);
                


            JRadioButton fillButton = new JRadioButton(language.getTranslated("fill_shape"));
            fillButton.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    Object source = e.getItemSelectable();

                    if (source == fillButton) {
                        fill = true;
                    }
                    if (e.getStateChange() == ItemEvent.DESELECTED) {
                        fill = false;
                    }
                }
            });
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 1));
            panel.add(fillButton);
            panel.add(strokePanel);
            panel.add(colourPicker);
            // Option button text
            Object[] options = {
                language.getTranslated("rectangle"),
                language.getTranslated("oval"),
                language.getTranslated("line"),
            };

            // Creating the dialog box that returns the shape the user wants to draw
            shape = JOptionPane.showOptionDialog(
                Andie.getJFrame(), panel, 
                language.getTranslated("draw_question"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                (Icon) this.getValue("WindowIcon"),
                options,
                options[2]);
                strokeSize = thicknessSlider.getValue();

                if (shape == -1) {
                    fill = false;
                    return;
                }            
            
            // Assigning the ClickListener to correct shape
            switch (shape) {
                // -1 is cancel code for dialog option
                case -1 : 
                    break;

                case 0: 
                    ClickListener.activate(new RegionSelector("rectangle", colour, fill, strokeSize));
                    break;
                case 1: 
                    ClickListener.activate(new RegionSelector("oval", colour, fill, strokeSize));
                    break;
                case 2:
                    ClickListener.activate(new RegionSelector("line", colour, fill, strokeSize));
                    break;
                
                default:
                    break;
            }

            fill = false;
        }

    }


        /**
         * <p>
         * Action to open users default browser and..
         * </p>
         * 
         */
        public class RollAction extends ImageAction {

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
            RollAction(String name, String icon, String desc, Integer mnemonic) {
                super(name, icon, desc, mnemonic);
            }

            /**
             * <p>
             * Callback for when the roll action is triggered.
             * </p>
             * 
             * <p>
             * This method is called whenever the roll action is triggered.
             * It prompts the user for nothing, then pranks them.
             * </p>
             * 
             * @param e The event triggering this callback.
             */
            public void actionPerformed(ActionEvent e) {
                Roll r = new Roll();
                r.applyRoll();

            }
        }

}
