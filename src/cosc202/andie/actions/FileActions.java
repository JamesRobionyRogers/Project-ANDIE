package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import cosc202.andie.Andie;
import cosc202.andie.ExceptionHandler;
import cosc202.andie.KeyboardShortcut;
import cosc202.andie.SetLanguage;
import cosc202.andie.Icons;

/**
 * <p>
 * Actions provided by the File menu.
 * </p>
 * 
 * <p>
 * The File menu is very common across applications,
 * and there are several items that the user will expect to find here.
 * Opening and saving files is an obvious one, but also exiting the program.
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
public class FileActions implements ActionCollection {

    SetLanguage language = SetLanguage.getInstance();

    /** A list of actions for the File menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of File menu actions.
     * </p>
     */
    public FileActions() {
        SetLanguage language = SetLanguage.getInstance();
        actions = new ArrayList<Action>();
        actions.add(new FileOpenAction(language.getTranslated("open"), Icons.FILE_OPEN_MENU, language.getTranslated("open_desc"), KeyboardShortcut.FILE_OPEN));
        actions.add(new FileSaveAction(language.getTranslated("save"), Icons.FILE_SAVE_MENU, language.getTranslated("save_desc"), KeyboardShortcut.FILE_SAVE));
        actions.add(new FileSaveAsAction(language.getTranslated("save_as"), Icons.FILE_SAVE_AS_MENU, language.getTranslated("save_as_desc"), KeyboardShortcut.FILE_SAVE_AS));
        actions.add(new FileExportAction(language.getTranslated("export"), Icons.FILE_EXPORT_MENU, language.getTranslated("export_desc"), KeyboardShortcut.FILE_EXPORT));
        actions.add(new ImportAction("Import", Icons.FILE_IMPORT_MENU, "Import an operations macro", KeyboardShortcut.FILE_IMPORT));
        actions.add(new FileExitAction(language.getTranslated("exit"), Icons.FILE_EXIT_MENU, language.getTranslated("exit_desc"), KeyboardShortcut.FILE_EXIT));
    }

    /**
     * <p>
     * Create a menu contianing the list of File actions.
     * </p>
     * 
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        SetLanguage language = SetLanguage.getInstance();
        JMenu fileMenu = new JMenu(language.getTranslated("file"));
        // JMenu fileMenu = new JMenu("file");
        int count = 0;
        for (Action action : actions) {
            JMenuItem item = new JMenuItem(action);
            if (count != 0 && count != 5 && ImageAction.getTarget().getImage().getCurrentImage() == null) {
                item.setEnabled(false);
            }
            count++;
            fileMenu.add(item);
        }
        return fileMenu;
    }

    /**
     * <p>
     * Returns an <code>ArrayList&lt;Action></code> contianing a list of File
     * actions to be used in the toolbar.
     * </p>
     * 
     * @return The File toolbar actions.
     */
    public ArrayList<Action> getToolbarActions() {
        ArrayList<Action> toolbarActions = new ArrayList<Action>();

        toolbarActions.add(actions.get(0));     // Open 
        toolbarActions.add(actions.get(1));     // Save

        return toolbarActions;
    }


    /**
     * <p>
     * Action to open an image from file.
     * </p>
     * 
     * @see EditableImage#open(String)
     */
    public class FileOpenAction extends ImageAction {

        /**
         * <p>
         * Create a new file-open action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileOpenAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-open action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileOpenAction is triggered.
         * It prompts the user to select a file and opens it as an image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(imageFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            int result = fileChooser.showOpenDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().open(imageFilepath);
                }
                // Catching Input/Output exceptions associated with opening a file
                catch (IOException ioException) {
                    ExceptionHandler.displayError(language.getTranslated("open_file_io_excepton"));
                }
                // Catching exceptions associated with opening a file Java doesn't have
                // permission for
                catch (SecurityException exception) {
                    ExceptionHandler.displayError(language.getTranslated("security_exception"));
                }

                catch (Exception ex) {
                    ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("open_file_io_excepton"));
                }
            }
            target.repaint();
            target.getParent().revalidate();

            //check if current image exists

            // Enabling the Menu Items if there is an image
            if (ImageAction.getTarget().getImage().getCurrentImage() != null){
                // go through each menu
                JMenuBar menuBar = Andie.getMenuBar();
                for (int i = 0; i < menuBar.getMenuCount(); i++){
                    JMenu menu = menuBar.getMenu(i);
                    menu.setEnabled(true);
                    // go through each item
                    for (int j = 0; j < menu.getItemCount(); j++){
                        menu.getItem(j).setEnabled(true);
                    }
                }

                // Enabling the Toolbar Items if there is an image
                Andie.getToolbar().updateToolbar(true);
            }
        }

    }

    /**
     * <p>
     * Action to save an image to its current file location.
     * </p>
     * 
     * @see EditableImage#save()
     */
    public class FileSaveAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileSaveAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-save action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAction is triggered.
         * It saves the image to its original filepath.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try {
                target.getImage().save();
            } catch (Exception ex) {
                ExceptionHandler.displayError(language.getTranslated("general_error"));
                ExceptionHandler.debugException(ex);

                ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("save_file_io_excepton"));
            }
        }

    }

    /**
     * <p>
     * Action to save an image to a new file location.
     * </p>
     * 
     * @see EditableImage#saveAs(String)
     */
    public class FileSaveAsAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save-as action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileSaveAsAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-save-as action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAsAction is triggered.
         * It prompts the user to select a file and saves the image to it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            // currently hardcoding the available file types, but need to change this
            JFileChooser fileChooser = new JFileChooser();

            // Creating new file extentions
            FileNameExtensionFilter jpg = new FileNameExtensionFilter("jpg", "jpg");
            FileNameExtensionFilter png = new FileNameExtensionFilter("png", "png");
            FileNameExtensionFilter jpeg = new FileNameExtensionFilter("jpeg", "jpeg");
            FileNameExtensionFilter tiff = new FileNameExtensionFilter("tiff", "tiff");
            FileNameExtensionFilter gif = new FileNameExtensionFilter("gif", "gif");

            // Adding the new extentions to the file chooser
            if (!target.getImage().hasAlpha()){
                fileChooser.addChoosableFileFilter(jpeg);
                fileChooser.addChoosableFileFilter(gif);
                fileChooser.addChoosableFileFilter(tiff);
                fileChooser.addChoosableFileFilter(jpg);
            }
            fileChooser.addChoosableFileFilter(png);


            // Hiding the "All Files" option
            fileChooser.setAcceptAllFileFilterUsed(false);

            // Setting the title of the dialog box
            fileChooser.setDialogTitle("Save a file");

            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    System.out.println(imageFilepath);
                    target.getImage().saveAs(imageFilepath, fileChooser.getFileFilter().getDescription());

                } catch (Exception ex) {
                    ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("save_file_io_excepton"));
                }
            }
        }

    }

    /**
     * <p>
     * Action to quit the ANDIE application.
     * </p>
     */
    public class FileExitAction extends ImageAction {

        /**
         * <p>
         * Create a new file-exit action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileExitAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-exit action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExitAction is triggered.
         * It quits the program.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }

    }

    /**
     * <p>
     * Action to export image to a new file.
     * </p>
     * 
     * @see EditableImage#export(String)
     */
    public class FileExportAction extends ImageAction {

        /**
         * <p>
         * Create a new file-export action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileExportAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-export action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExportAction is triggered.
         * It prompts the user for a filename, and then saves edited image to that file.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            // CHecks if image has been imported, shows error and returns if there is no file to export 
            if(!target.getImage().hasImage()){
                ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("save_file_io_excepton"));
                return;
            } 
           

            // currently hardcoding the available file types, but need to change this 
            JFileChooser fileChooser = new JFileChooser();

            // Creating new file extentions
            FileNameExtensionFilter jpg = new FileNameExtensionFilter("jpg", "jpg");
            FileNameExtensionFilter png = new FileNameExtensionFilter("png", "png");
            FileNameExtensionFilter jpeg = new FileNameExtensionFilter("jpeg", "jpeg");
            FileNameExtensionFilter tiff = new FileNameExtensionFilter("tiff", "tiff");
            FileNameExtensionFilter gif = new FileNameExtensionFilter("gif", "gif");

            // Adding the new extentions to the file chooser
            if (!target.getImage().hasAlpha()){
                fileChooser.addChoosableFileFilter(jpg);
                fileChooser.addChoosableFileFilter(jpeg);
                fileChooser.addChoosableFileFilter(gif);
                fileChooser.addChoosableFileFilter(tiff);
            }
            fileChooser.addChoosableFileFilter(png);

            // Hiding the "All Files" option
            fileChooser.setAcceptAllFileFilterUsed(false);

            // Setting the title of the dialog box
            fileChooser.setDialogTitle("Export a file");

            int result = fileChooser.showSaveDialog(target);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    System.out.println(imageFilepath);
                    target.getImage().export(imageFilepath, fileChooser.getFileFilter().getDescription());

                } catch (Exception ex) {
                    ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("save_file_io_excepton"));
                }
            }
        }

    }

    /**
     * <p>
     * Action to open an operations macro file
     * </p>
     * 
     * 
     */
    public class ImportAction extends ImageAction {

        /**
         * <p>
         * Create a new file-open action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        ImportAction(String name, ImageIcon icon, String desc, KeyStroke mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-open action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileOpenAction is triggered.
         * It prompts the user to select a file and opens it as an image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            // currently hardcoding the available file types, but need to change this
            JFileChooser fileChooser = new JFileChooser();

            // Creating new file extentions
            FileNameExtensionFilter ops = new FileNameExtensionFilter(".ops", "ops");

            // Adding the new extentions to the file chooser
            fileChooser.addChoosableFileFilter(ops);

            // Hiding the "All Files" option
            fileChooser.setAcceptAllFileFilterUsed(false);

            // Setting the title of the dialog box
            fileChooser.setDialogTitle("Import a macro");

            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().importMacro(imageFilepath);
                }
                // Catching Input/Output exceptions associated with opening a file
                catch (IOException ioException) {
                    ExceptionHandler.displayError(language.getTranslated("open_file_io_excepton"));
                }
                // Catching exceptions associated with opening a file Java doesn't have
                // permission for
                catch (SecurityException exception) {
                    ExceptionHandler.displayError(language.getTranslated("security_exception"));
                }

                catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("crashed here");
                    System.exit(1);
                }
            }

            target.repaint();
            target.getParent().revalidate();
        }

    }

}
