package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class FileActions {

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
        actions.add(new FileOpenAction(language.getTranslated("open"), null, language.getTranslated("open_desc"), Integer.valueOf(KeyEvent.VK_O)));
        actions.add(new FileSaveAction(language.getTranslated("save"), null, language.getTranslated("save_desc"), Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new FileSaveAsAction(language.getTranslated("save_as"), null, language.getTranslated("save_as_desc"), Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExportAction(language.getTranslated("export"), null, language.getTranslated("export_desc"), Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExitAction(language.getTranslated("exit"), null, language.getTranslated("exit_desc"), Integer.valueOf(0)));
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
        //JMenu fileMenu = new JMenu("file");

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileOpenAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
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
                // Catching exceptions associated with opening a file Java doesn't have permission for 
                catch (SecurityException exception) {
                    ExceptionHandler.displayError(language.getTranslated("security_exception"));
                }
                
                catch (Exception ex) {
                    ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("open_file_io_excepton"));
                }
            }

            target.repaint();
            target.getParent().revalidate();
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileSaveAsAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
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
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().saveAs(imageFilepath);
                }
                // Catching Input/Output exceptions associated with opening a file
                catch (IOException ioException) {
                    ExceptionHandler.displayError(language.getTranslated("save_file_io_excepton"));
                }
                // Catching exceptions associated with opening a file Java doesn't have permission for
                catch (SecurityException exception) {
                    ExceptionHandler.displayError(language.getTranslated("security_exception"));
                }
                
                catch (Exception ex) {
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
    public class FileExitAction extends AbstractAction {

        /**
         * <p>
         * Create a new file-exit action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileExitAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
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
            FileNameExtensionFilter jpg = new FileNameExtensionFilter(".jpg", "jpg");
            FileNameExtensionFilter png = new FileNameExtensionFilter(".png", "png");
            FileNameExtensionFilter jpeg = new FileNameExtensionFilter(".jpeg", "jpeg");
            FileNameExtensionFilter tiff = new FileNameExtensionFilter(".tiff", "tiff");
            FileNameExtensionFilter gif = new FileNameExtensionFilter(".gif", "gif");
            
            // Adding the new extentions to the file chooser
            fileChooser.addChoosableFileFilter(jpg);
            fileChooser.addChoosableFileFilter(png);
            fileChooser.addChoosableFileFilter(jpeg);
            fileChooser.addChoosableFileFilter(gif);
            fileChooser.addChoosableFileFilter(tiff);

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

}
