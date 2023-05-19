package cosc202.andie.actions;

import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JMenu;

public interface ActionCollection {
    
    /**
     * <p>
     * Create a menu contianing the list of File actions.
     * </p>
     * 
     * @return The File menu UI element.
     */
    public JMenu createMenu();

    /**
     * <p>
     * Returns an <code>ArrayList&lt;Action></code> contianing a list of File
     * actions to be used in the toolbar.
     * </p>
     * 
     * @return The File toolbar actions.
     */
    public ArrayList<Action> getToolbarActions();
}
