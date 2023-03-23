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
        actions.add(new PixelPeekToolAction("Peek", null, "Peek the Pixel", null));
        actions.add(new RotateToolAction("Rotate", null, "Rotate image", null));

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
    public class RotateToolAction extends ImageAction {
        public void actionPerformed(ActionEvent e){
            target.getImage().apply(new RotateTool(360));
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

        PixelPeekToolAction(String name, ImageIcon icon,
        String desc, Integer mnemonic) {
        super(name, icon, desc, mnemonic);
        }
    }
}
