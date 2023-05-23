package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cosc202.andie.*;

 /**
 * <p>
 * Actions provided by the Edit menu.
 * </p>
 * 
 * <p>
 * The Edit menu is very common across a wide range of applications.
 * There are a lot of operations that a user might expect to see here.
 * In the sample code there are Undo and Redo actions, but more may need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class EditActions implements ActionCollection {
    
    /** A list of actions for the Edit menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Edit menu actions.
     * </p>
     */
    public EditActions() {
        actions = new ArrayList<Action>();

        actions.add(new UndoAction("undo", Icons.EDIT_UNDO, "undo", KeyboardShortcut.EDIT_UNDO));
        actions.add(new RedoAction("redo", Icons.EDIT_REDO, "redo", KeyboardShortcut.EDIT_REDO));
        actions.add(new RecordAction("record", Icons.EDIT_RECORD, "record", KeyboardShortcut.EDIT_RECORD));
        actions.add(new StopRecordAction("stop_record", Icons.EDIT_STOP_RECORD, "stop_record", KeyboardShortcut.EDIT_STOP_RECORD));
        actions.add(new RevertAction("revert", Icons.EDIT_REVERT, "revert_desc", null));

    }

    /**
     * <p>
     * Create a menu contianing the list of Edit actions.
     * </p>
     * 
     * @return The edit menu UI element.
     */
    public JMenu createMenu() {
        SetLanguage language = SetLanguage.getInstance();
        JMenu editMenu = new JMenu(language.getTranslated("edit"));

        for (Action action: actions) {
            editMenu.add(new JMenuItem(action));
            
        }
        if (ImageAction.getTarget().getImage().getCurrentImage() == null){
            editMenu.setEnabled(false);
        }
        
        return editMenu;
    }

    @Override
    public ArrayList<Action> getToolbarActions() {
        ArrayList<Action> toolbarActions = new ArrayList<Action>();

        toolbarActions.add(actions.get(0));
        toolbarActions.add(actions.get(1));

        return toolbarActions;
    }

    /**
     * <p>
     * Action to undo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#undo()
     */
    public class UndoAction extends ImageAction {

        /**
         * <p>
         * Create a new undo action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        UndoAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the undo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAction is triggered.
         * It undoes the most recently applied operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().undo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

     /**
     * <p>
     * Action to redo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#redo()
     */   
    public class RedoAction extends ImageAction {

        /**
         * <p>
         * Create a new redo action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RedoAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        
        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RedoAction is triggered.
         * It redoes the most recently undone operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().redo();
            target.repaint();
            target.getParent().revalidate();
        }
    }



    /**
     * <p>
     * Action to record a series of {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#record()
     */   
    public class RecordAction extends ImageAction {

        /**
         * <p>
         * Create a new record action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RecordAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        
        /**
         * <p>
         * Callback for when the record action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RecordAction is triggered.
         * It begins recording the following operations
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
           target.getImage().record(); 
           
        }
    }
    /**
     * <p>
     * Action to stop recording {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#stoprecord()
     */   
    public class StopRecordAction extends ImageAction {

        /**
         * <p>
         * Create a new stop record action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        StopRecordAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }


        /**
         * <p>
         * Callback for when the stop recording action is triggered.
         * </p>
         * 
         * 
         * 
         * <p>
         * This method is called whenever the StopRecordAction is triggered.
         * It stops recording the operations
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if(target.getImage().isRecording()){
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter ops = new FileNameExtensionFilter("ops", ".ops");
            fileChooser.addChoosableFileFilter(ops);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setDialogTitle("Export operations");
            int result = fileChooser.showSaveDialog(target);
    
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().stoprecord(imageFilepath, fileChooser.getFileFilter().getDescription());
    
                } catch (Exception ex) {
                    ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("save_file_io_excepton"));
                }
            }}
          
           
        }
    }

    /**
     * <p>
     * Action to revert all changes {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#revert()
     */   
    public class RevertAction extends ImageAction {

        /**
         * <p>
         * Create a new revert action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RevertAction(String name, String icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * <p>
         * Callback for when the stop recording action is triggered.
         * </p>
         * 
         * 
         * 
         * <p>
         * This method is called whenever the RevertAction is triggered.
         * It reverts the image contained within the ImagePanel to the original opened image
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().revert();
            target.repaint();
            target.getParent().revalidate();
        }
    }

}






