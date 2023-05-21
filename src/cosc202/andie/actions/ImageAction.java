package cosc202.andie.actions;

import javax.swing.*;

import cosc202.andie.Icons;
import cosc202.andie.ImagePanel;
import cosc202.andie.KeyboardShortcut;


/**
 * <p>
 * Abstract class representing actions the user might take in the interface.
 * </p>
 * 
 * <p>
 * This class uses Java's AbstractAction approach for Actions that can be applied to images.
 * The key difference from a generic AbstractAction is that an ImageAction contains a reference
 * to an image (via an ImagePanel interface element).
 * </p>
 * 
 * <p>
 * A distinction should be made between an ImageAction and an {@link ImageOperation}.
 * An ImageOperation is applied to an image in order to change it in some way.
 * An ImageAction provides support for the user doing something to an image in the user interface.
 * ImageActions may apply an ImageOperation, but do not have to do so. 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author James Robiony-Rogers
 * @version 1.0
 */
public abstract class ImageAction extends AbstractAction {
   
    /**
     * The user interface element containing the image upon which actions should be performed.
     * This is common to all ImageActions.
     */
    protected static ImagePanel target;

    /**
     * <p>
     * Constructor for ImageActions.
     * </p>
     * 
     * <p>
     * To construct an ImageAction you provide the information needed to integrate
     * it with the interface.
     * Note that the target is not specified per-action, but via the static member
     * {@link target}
     * via {@link setTarget}.
     * </p>
     * 
     * @param name     The name of the action (ignored if null).
     * @param icon     An icon to use to represent the action (ignored if null).
     * @param desc     A brief description of the action (ignored if null).
     * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
     */
    public ImageAction(String name, String icon, String desc, KeyStroke keyboardShortcut) {
        super(name);
        // super(name, icon);
        if (desc != null) {
            putValue(SHORT_DESCRIPTION, desc);
        }

        // Adding the keyboard shortcut to the action
        if (keyboardShortcut != null) {
            this.putValue(ACCELERATOR_KEY, keyboardShortcut);
        }
        if (icon != null) {
            assignIcons(icon);
        }
    }

    /**
     * <p>
     * Constructor for ImageActions.
     * </p>
     * 
     * <p>
     * To construct an ImageAction you provide the information needed to integrate it with the interface.
     * Note that the target is not specified per-action, but via the static member {@link target}
     * via {@link setTarget}.
     * </p>
     * 
     * @param name The name of the action (ignored if null).
     * @param icon An icon to use to represent the action (ignored if null).
     * @param desc A brief description of the action  (ignored if null).
     * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
     */
    public ImageAction(String name, String icon, String desc, Integer mnemonic) {
        this(name, icon, desc, KeyboardShortcut.noShortcut()); 
    }

    /**
     * <p>
     * Method to assign an icon to the action.
     * </p>
     * 
     * <p>
     * This method is called by the constructor to assign an icon to the action 
     * and assigns the {@code MenuIcon}, {@code ToolbarIcon}, and {@code WindowIcon} 
     * values to the {@code Action}.
     * </p>
     * 
     * @param icon The icon to assign to the action. 
     */
    private void assignIcons(String icon) {
        // Assinging the menu icons 
        this.putValue(Action.SMALL_ICON, Icons.getScaledIcon(icon, Icons.type.menu));
        this.putValue("ToolbarIcon", Icons.getScaledIcon(icon, Icons.type.toolbar));
        this.putValue("WindowIcon", Icons.getScaledIcon(icon, Icons.type.window));
    }

    /**
     * <p>
     * Set the target for ImageActions.
     * </p>
     * 
     * @param newTarget The ImagePanel to apply ImageActions to.
     */
    public static void setTarget(ImagePanel newTarget) {
        target = newTarget;
    } 

    /**
     * <p>
     * Get the target for ImageActions.
     * </p>
     * 
     * @return The ImagePanel to which ImageActions are currently being applied.
     */
    public static ImagePanel getTarget() {
        return target;
    }

}
