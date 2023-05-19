package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.Roll;
import cosc202.andie.SetLanguage;

/**
 * <p>
 * Actions provided by the FunActions menu.
 * </p>
 * 
 * <p>
 * The funactions menu contains actions that are a surprise!
 * 
 * This includes things we won't tell you until you try it
 *
 * @author Jess Tyrrell
 * @version 1.0
 */

public class FunActions {
    
    SetLanguage language = SetLanguage.getInstance();
    
    /** A list of actions for the fun actions menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     */
    public FunActions() {
        SetLanguage language = SetLanguage.getInstance();
        actions = new ArrayList<Action>();
        actions.add(new RollAction(language.getTranslated("roll"), null, language.getTranslated("roll_desc"), Integer.valueOf(KeyEvent.VK_M)));
    }
    
    /**
     * <p>
     * Create a menu contianing the list of Fun actions.
     * </p>
     * 
     * @return The fun action menu UI element.
     */
    public JMenu createMenu() {
        SetLanguage language = SetLanguage.getInstance();
        JMenu fileMenu = new JMenu(language.getTranslated("fun"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to open users default browser and..
     * </p>
     * 
     */
    public class RollAction extends ImageAction{

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
        RollAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
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
        public void actionPerformed(ActionEvent e){
                Roll r = new Roll();
                r.applyRoll();
        
        }

    }

    
}


