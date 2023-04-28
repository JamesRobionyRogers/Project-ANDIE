package cosc202.andie;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ExceptionHandler {
    
    /** The parent frame for the error messages to apperar inside of */
    private static JFrame parentFrame = Andie.getJFrame(); 

    
    /**
     * <p>
     * Creates a new ExceptionHandler object.
     * </p>
     */
    public ExceptionHandler() {
    }

    /**
     * <p>
     * Displays an error message to the user.
     * </p>
     * 
     * @param message The message to display to the user.
     */
    public static void displayError(String message) {
        if (parentFrame == null) {
            System.out.println(message);
        }

        SetLanguage language = SetLanguage.getInstance();
        String paneTitle = language.getTranslated("error_string");

        // Creating the JOptionPane and JDialog manually allows us to set location of the dialog
        JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null);
        JDialog dialog = pane.createDialog(parentFrame, paneTitle);

        // Setting location
        int x = (parentFrame.getWidth() - pane.getWidth() - 2); 
        int y = (parentFrame.getHeight() - pane.getHeight() - 3);
        dialog.setLocation(x, y);
        
        // Setting visibility
        dialog.setVisible(true);
    }

    /**
     * <p>
     * Displays an warning message to the user.
     * </p>
     * 
     * @param message The message to display to the user.
     */
    public static void displayWarning(String message) {
        if (parentFrame == null) {
            System.out.println(message);
        }

        SetLanguage language = SetLanguage.getInstance();
        String paneTitle = language.getTranslated("warning_string");

        // Creating the JOptionPane and JDialog manually allows us to set location of the dialog
        JOptionPane pane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null);
        JDialog dialog = pane.createDialog(parentFrame, paneTitle);

        // Setting location
        int x = (parentFrame.getWidth() - pane.getWidth() - 2); 
        int y = (parentFrame.getHeight() - pane.getHeight() - 3);
        dialog.setLocation(x, y);
        
        // Setting visibility
        dialog.setVisible(true);
    }

    /**
     * <p>
     * Displays an error message for developers.
     * </p>
     * 
     * @param message The message to display to the user.
     * @param title The title of the error message.
     */
    public static void debugException(Exception exception) {

        // Setting the title as the name of the exception
        String title = exception.getClass().getName();

        // Setting the message as the stack trace
        String localisedMessage = exception.getLocalizedMessage();
        
        // Printing the info to the console for debugging
        System.out.println("[EXCEPTION] " + title); 
        System.out.println(localisedMessage);
        exception.printStackTrace();

        // Printing the first point of error in the andie package 
        StackTraceElement[] stackTrace = exception.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().startsWith("cosc202.andie")) {
                System.out.println("\nFirst point of error: " + stackTraceElement);
                break;
            }
        }

        // // Creating the JOptionPane and JDialog manually allows us to set location of the dialog
        // JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null);
        // JDialog dialog = pane.createDialog(parentFrame, title);

        // // Setting location
        // int x = (parentFrame.getWidth() - pane.getWidth() - 2); 
        // int y = (parentFrame.getHeight() - pane.getHeight() - 3);
        // dialog.setLocation(x, y);
        
        // // Setting visibility
        // dialog.setVisible(true);
    }

}
