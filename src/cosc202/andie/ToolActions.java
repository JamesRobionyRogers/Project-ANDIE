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
        actions.add(new ResizeToolAction("Resize", null, "Resize the image", null));
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

        ResizeToolAction(String name, ImageIcon icon,
        String desc, Integer mnemonic) {
        super(name, icon, desc, mnemonic);
        }
    }
}
