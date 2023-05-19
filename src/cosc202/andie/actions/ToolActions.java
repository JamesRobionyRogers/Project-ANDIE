package cosc202.andie.actions;

import cosc202.andie.*;
import cosc202.andie.actions.colour.AlphaMask;
import cosc202.andie.actions.tool.*;
import java.util.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;


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
        SetLanguage language = SetLanguage.getInstance();

        actions.add(new ResizeToolAction(language.getTranslated("resize"), Icons.TOOLS_RESIZE, language.getTranslated("resize_desc"), KeyboardShortcut.TOOLS_RESIZE));
        actions.add(new RotateToolAction(language.getTranslated("rotate"), Icons.TOOLS_ROTATE, language.getTranslated("rotate_desc"), KeyboardShortcut.TOOLS_ROTATE));
        actions.add(new FlipImageActions(language.getTranslated("flip_image"), Icons.TOOLS_FLIP_HORIZONTAL, language.getTranslated("flip_image_desc"), KeyboardShortcut.TOOLS_FLIP));
        actions.add(new CropAction("Crop", Icons.TOOLS_CROP, "Crop an image", KeyboardShortcut.TOOLS_CROP));
        actions.add(new DrawShapeAction(language.getTranslated("draw"), null, language.getTranslated("draw_shape"), null));
        //actions.add(new AlphaMaskAction());
        // Testing feature - not for production
        actions.add(new PixelPeekToolAction("Peek [DNT]", null, "Peek the Pixel [DNT]", null));
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
        if (ImageAction.getTarget().getImage().getCurrentImage() == null){
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
            FlipImageActions(String name, ImageIcon icon, String desc, KeyStroke mnemonic){
                super(name, icon, desc, mnemonic);
            }
    
        public void actionPerformed(ActionEvent e){
            SetLanguage language = SetLanguage.getInstance();
            int option; 
            Object[] flip = { language.getTranslated("horizontal"), language.getTranslated("vertical") };
            
            option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                language.getTranslated("flip_image_question"),
                language.getTranslated("flip"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                Icons.TOOLS_FLIP_HORIZONTAL_WINDOW,
                flip,
                flip[1]
            );

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

            SpinnerNumberModel radiusModel3 = new SpinnerNumberModel(100, 1, 65535, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel3);
            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                radiusSpinner, 
                language.getTranslated("resize_question"), 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                Icons.TOOLS_RESIZE_WINDOW, 
                options, 
                options[0]
            );
            
            if (option == 1){
                return;
            } else if(option == 0){
                scale = radiusModel3.getNumber().intValue();
            }
            // didn't scale don't perform ops
            if (scale == 100) return;

            try {
                // check image area is less than MAX_INTEGER
                int width = (int)(target.getImage().getCurrentImage().getWidth() * (double) scale / 100);
                int height = (int)(target.getImage().getCurrentImage().getHeight() * (double) scale / 100);

                // Image larger than max image size
                int size = Math.multiplyExact(width,height);
                
                if (size == 0) throw (new ArithmeticException("Area was 0"));
                if (width > 65535) throw (new ArithmeticException("Width too large"));
                if (height > 65535) throw (new ArithmeticException("Height too large"));
            } catch (ArithmeticException ex){
                ExceptionHandler.displayError(language.getTranslated("resize_warning").replace((CharSequence)"#",(CharSequence)Integer.toString(scale)));
                return;
            }


            target.getImage().apply(new ResizeTool(scale));
            target.repaint();
            target.getParent().revalidate();
        }
        ResizeToolAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }
    public class RotateToolAction extends ImageAction {
        SetLanguage language = SetLanguage.getInstance();
        public void actionPerformed(ActionEvent e){
            int deg;

            //Custom button text
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
                Icons.TOOLS_ROTATE_WINDOW,
                options,
                options[2]
            );

            // -1 is cancel code for dialog option
            if (deg == -1) return;

            // set chosen option to corresponding degrees of rotation
            deg = (3-deg)*90;
            target.getImage().apply(new RotateTool(deg));
            target.repaint();
            target.getParent().revalidate();

        }

        RotateToolAction(String name, ImageIcon icon,
        String desc, KeyStroke mnemonic) {
        super(name, icon, desc, mnemonic);
        }

    }

    public class PixelPeekToolAction extends ImageAction {

        public void actionPerformed(ActionEvent e){
            int x,y;
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
            PixelPeek.pixelPeek(x,y,target.getImage().getCurrentImage());
        }
    
        PixelPeekToolAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }

    public class CropAction extends ImageAction {

        public void actionPerformed(ActionEvent e){
            int x,y,width,height;
            x = y = width = height = 0;
    
            SpinnerNumberModel B = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
            JSpinner xSpinner = new JSpinner(B);
            SpinnerNumberModel C = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
            JSpinner ySpinner = new JSpinner(C);
            SpinnerNumberModel D = new SpinnerNumberModel(100, 0, Integer.MAX_VALUE, 1);
            JSpinner widthSpinner = new JSpinner(D);
            SpinnerNumberModel E = new SpinnerNumberModel(100, 0, Integer.MAX_VALUE, 1);
            JSpinner heightSpinner = new JSpinner(E);
        
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4, 2));
            panel.add(new JLabel("x:"));
            panel.add(xSpinner);
            panel.add(new JLabel("y:"));
            panel.add(ySpinner);
            panel.add(new JLabel("width:"));
            panel.add(widthSpinner);
            panel.add(new JLabel("height:"));
            panel.add(heightSpinner);

            int option = JOptionPane.showOptionDialog(
                Andie.getJFrame(), 
                panel, 
                "Crop the image [DNT]",
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                Icons.TOOLS_CROP_WINDOW, 
                null, 
                null
            );
        
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                x = B.getNumber().intValue();
                y = C.getNumber().intValue();
                width = D.getNumber().intValue();
                height = E.getNumber().intValue();
            }
            target.getImage().apply(new Crop(x,y,width,height));
            target.repaint();
            target.getParent().revalidate();
        }
    
        CropAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }
    }


public class DrawShapeAction extends ImageAction{

        
    boolean fill =  false;
    Color colour = Color.BLUE;

    DrawShapeAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
    }

        public void actionPerformed(ActionEvent e) {

            int shape;
            SetLanguage language = SetLanguage.getInstance();
            
                JButton colourPicker = new JButton(language.getTranslated("choose_colour"));
                colourPicker.setBounds(200, 250, 100, 30);
                colourPicker.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource()==colourPicker){
                            colour = JColorChooser.showDialog(null, language.getTranslated("choose_colour"), colour);
                        }
                    }
                });

                JRadioButton panel = new JRadioButton(language.getTranslated("fill_shape"));
                panel.addItemListener(new ItemListener(){

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        Object source = e.getItemSelectable();

                        if (source == panel){
                            fill = true;
                        }
                        if(e.getStateChange() == ItemEvent.DESELECTED){
                            fill = false;
                        }
                    }
                });
                //Custom button text
                Object[] options = {panel, colourPicker, language.getTranslated("rectangle"),
                                    language.getTranslated("oval"),
                                    language.getTranslated("line")};
                shape = JOptionPane.showOptionDialog(null, language.getTranslated("draw_question") ,
                language.getTranslated("draw"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[4]);

    
                // -1 is cancel code for dialog option
                if (shape == -1) {
                    fill = false;
                    return;
                }
                if(fill){
                    if (shape == 2){
                        ClickListener.activate(new RegionSelector("rectangle", colour,true));
                    }else if (shape == 3){
                        ClickListener.activate(new RegionSelector("oval", colour, true));
                    } else if (shape == 4){
                        ClickListener.activate(new RegionSelector("line", colour, true));
                    }
                }else{
                    if (shape == 2){
                    ClickListener.activate(new RegionSelector("rectangle", colour));
                }else if (shape == 3){
                    ClickListener.activate(new RegionSelector("oval", colour));
                } else if (shape == 4){
                    ClickListener.activate(new RegionSelector("line", colour));
                }
            }
            fill = false;
        }

    

}

}
