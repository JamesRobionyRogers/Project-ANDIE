package cosc202.andie;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

/**
 * <p>
 * KeyboardShortcuts is lookup class used for storing and retrieving the
 * keyboard shortcuts used in
 * ANDIE staticly.
 * </p>
 * 
 * <p>
 * Here is an example of how the class can be used to add an icon to a dialog
 * box:
 * 
 * <pre>
 * actions.add(new ZoomInAction(..., KeyboardShortcut.ZOOM_IN));
 * </pre>
 * 
 * or assigning it to a variable:
 * 
 * <pre>
 * KeyStroke shortcut = KeyboardShortcut.ZOOM_IN;
 * </pre>
 * </p>
 * 
 * <p>
 * The keyboard shortcuts in feature <code>Ctrl + ...</code> on Windows and
 * Linux and <code>Cmd + ...</code> on macOS and have been inspired by the 
 * </p>
 * 
 * @version 16th May 2023
 * @author James Robiony-Rogers
 */
public class KeyboardShortcut {

    // private static String OS = System.getProperty("os.name").toLowerCase();
    // private static int CTRL_OR_CMD = (OS.contains("mac")) ? KeyEvent.META_DOWN_MASK : KeyEvent.CTRL_DOWN_MASK;
    
    // File Menu Shortcuts
    public static final KeyStroke FILE_OPEN = assignCtrlCmd(KeyEvent.VK_O);
    public static final KeyStroke FILE_SAVE = assignCtrlCmd(KeyEvent.VK_S);
    public static final KeyStroke FILE_SAVE_AS = assignShift(KeyEvent.VK_S);
    public static final KeyStroke FILE_EXPORT = assignShift(KeyEvent.VK_E);
    public static final KeyStroke FILE_IMPORT = assignShift(KeyEvent.VK_I);
    public static final KeyStroke FILE_EXIT = assignCtrlCmd(KeyEvent.VK_Q);

    // Edit Menu Shortcuts
    public static final KeyStroke EDIT_UNDO = assignCtrlCmd(KeyEvent.VK_Z);
    public static final KeyStroke EDIT_REDO = assignShift(KeyEvent.VK_Z); 
    public static final KeyStroke EDIT_RECORD = assignCtrlCmd(KeyEvent.VK_R);
    public static final KeyStroke EDIT_STOP_RECORD = assignShift(KeyEvent.VK_R);

    // View Menu Shortcuts
    public static final KeyStroke VIEW_ZOOM_IN = assignCtrlCmd(KeyEvent.VK_EQUALS);
    public static final KeyStroke VIEW_ZOOM_OUT = assignCtrlCmd(KeyEvent.VK_MINUS);
    public static final KeyStroke VIEW_ZOOM_FULL = assignCtrlCmd(KeyEvent.VK_0);

    // Filter Menu Shortcuts 
    public static final KeyStroke FILTER_MEAN_BLUR = assignKey(KeyEvent.VK_M);
    public static final KeyStroke FILTER_SHARPEN = assignKey(KeyEvent.VK_S);
    public static final KeyStroke FILTER_GAUSSIAN_BLUR = assignKey(KeyEvent.VK_G); 
    public static final KeyStroke FILTER_EMBOSS = assignKey(KeyEvent.VK_E);

    // Colour Menu Shortcuts
    public static final KeyStroke COLOUR_BRIGHTNESS_CONTRAST = assignKey(KeyEvent.VK_B);
    public static final KeyStroke COLOUR_INVERT = assignCtrlCmd(KeyEvent.VK_I); 

    // Tools Menu Shortcuts
    public static final KeyStroke TOOLS_CROP = assignKey(KeyEvent.VK_C);
    public static final KeyStroke TOOLS_ROTATE = assignKey(KeyEvent.VK_R);
    public static final KeyStroke TOOLS_FLIP = assignKey(KeyEvent.VK_F);
    public static final KeyStroke TOOLS_RESIZE = assignCtrlCmd(KeyEvent.VK_T);



    /**
     * <p>
     * Assigns a keyboard shortcut, <code>Ctrl + ...</code> on Windows and
     * Linux and <code>Cmd + ...</code> on macOS, to a menu item.
     * </p>
     * 
     * @param shortcut The keyboard shortcut to assign to the menu item.
     * @return The keyboard shortcut to assign to the menu item.
     */
    private static KeyStroke assignCtrlCmd(int shortcut) {
        // Check what operating system is being used
        String os = System.getProperty("os.name").toLowerCase();

        int modifier = (os.contains("mac")) ? KeyEvent.META_DOWN_MASK : KeyEvent.CTRL_DOWN_MASK;

        return KeyStroke.getKeyStroke(shortcut, modifier);
    }


    /**
     * <p>
     * Assigns a keyboard shortcut, <code>Ctrl + Shift + ...</code> on Windows and
     * Linux and <code>Cmd + Shift + ...</code> on macOS, to a menu item.
     * </p>
     * 
     * @param shortcut  The keyboard shortcut to assign to the menu item.
     * @return The keyboard shortcut to assign to the menu item.
     */
    private static KeyStroke assignShift(int shortcut) {
        // Check what operating system is being used
        String os = System.getProperty("os.name").toLowerCase();

        int modifier = (os.contains("mac")) ? KeyEvent.META_DOWN_MASK : KeyEvent.CTRL_DOWN_MASK;
        modifier += KeyEvent.SHIFT_DOWN_MASK;

        return KeyStroke.getKeyStroke(shortcut, modifier);
    }

    /**
     * <p>
     * Assigns a single character keyboard shortcut, to a menu item.
     * </p>
     * 
     * @param shortcut The keyboard shortcut to assign to the menu item.
     * @return The keyboard shortcut to assign to the menu item.
     */
    private static KeyStroke assignKey(int shortcut) {
        int modifier = 0;
        return KeyStroke.getKeyStroke(shortcut, modifier);
    }

    /**
     * <p>
     * Returns a KeyStroke object that represents no keyboard shortcut.
     * </p>
     * 
     * @return A KeyStroke object that represents no keyboard shortcut.
     */
    public static KeyStroke noShortcut() {
        return null; 
        // return KeyStroke.getKeyStroke(KeyEvent.VK_UNDEFINED, 0);
    }

}

/**
 * Notes:
 * 
 *
 * https://www.codejava.net/java-se/swing/setting-shortcut-key-and-hotkey-for-menu-item-and-button-in-swing
 */

// Using the class KeyboardShotcuts, look up the keyboard shortcut that returns
// a KeyStroke object, do the os checking in the class
// (mnemonic, KeyboardShortcuts.getShortcut(name)
// (mnemonic, 0) means it will be strictly the mnemonic key e.g. 'O' for open
// (mnemonic, KeyEvent.CTRL_DOWN_MASK) means it will be ctrl + mnemonic key e.g.
// ctrl + 'O' for open
