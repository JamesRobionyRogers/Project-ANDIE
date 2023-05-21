package cosc202.andie;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;


/**
 * <p>
 * Icons is lookup class used for storing and retrieving icons used in
 * ANDIE staticly.
 * </p>
 * 
 * <p>
 * Here is an example of how the class can be used to add an icon to a dialog
 * box:
 * 
 * <pre>
 * JOptionPane.showMessageDialog(... , Icons.FILE_OPEN_MENU, ...);
 * </pre>
 * 
 * or assigning it to a variable:
 * <pre>
 * String icon = Icons.FILE_OPEN_MENU;
 * </pre>
 * </p>
 * 
 * <p>
 * Icons have been sourced from <a href="https://mui.com/material-ui/materialicons/?theme=Outlined">mui.com</a>
 * and are licensed under the <a href="https://github.com/google/material-design-icons/blob/master/LICENSE">Apache License 2.0</a>
 * </p>
 * 
 * @version 3rd May 2023
 * @author James Robiony-Rogers
 */
public final class Icons {

    /** The folder containing all the icons (including the following '/')  */
    private static String folderContainingIcons = "icons/"; 
    /** The dimensions for icons that appear in the pop-up boxes */
    private static int windowIconWidth = 40, windowIconHeight = 40;
    /** The dimensions for icons that appear as options under the menu bar */
    private static int menuIconWidth = 15, menuIconHeight = 15;   
    /** The dimensions for icons that appear in the toolbar */
    private static int toolbarIconWidth = 20, toolbarIconHeight = 20;
    /** Used to describe where the icon will used: menubar / window */
    public static enum type { menu, window, toolbar }; 
    
    // File menu icons see 
    public static final String FILE_OPEN_MENU = "File-OpenFile.png";
    public static final String FILE_SAVE_MENU = "File-Save.png";
    public static final String FILE_SAVE_AS_MENU = "File-SaveAs.png";
    public static final String FILE_EXPORT_MENU = "File-Export.png";
    public static final String FILE_IMPORT_MENU = "File-Import.png";
    public static final String FILE_EXIT_MENU = "File-Exit.png"; 


    // Edit menu icons 
    public static final String EDIT_UNDO = "Edit-Undo.png";
    public static final String EDIT_REDO = "Edit-Redo.png";
    public static final String EDIT_RECORD = "Record.png";
    public static final String EDIT_STOP_RECORD = "Stop-Recording.png";


    // View menu icons
    public static final String VIEW_ZOOM_IN = "View-ZoomIn.png";
    public static final String VIEW_ZOOM_OUT = "View-ZoomOut.png";
    public static final String VIEW_ZOOM_RESET = "View-Reset.png";

    // Filter menu icons
    public static final String FILTER_BLUR = "Filter-Blur.png";
    public static final String FILTER_SHARPEN = "Filter-Sharpen.png";
    public static final String FILTER_CIRCLE_BLUR = "Filter-CircleBlur.png";
    public static final String FILTER_EMBOSS = "Filter-Emboss.png";
    public static final String FILTER_EDGE_DETECTION = "Filter-EdgeDetection.png";

    public static final String FILTER_BLUR_WINDOW = "Filter-Blur.png";
    public static final String FILTER_SHARPEN_WINDOW = "Filter-Sharpen.png";
    public static final String FILTER_CIRCLE_BLUR_WINDOW = "Filter-CircleBlur.png";
    public static final String FILTER_EMBOSS_WINDOW = "Filter-Emboss.png";
    public static final String FILTER_EDGE_DETECTION_WINDOW = "Filter-EdgeDetection.png";

    // Colour menu icons
    public static final String COLOUR_GREYSCALE = "Colour-Greyscale.png";
    public static final String COLOUR_ADJUSTMENTS = "Colour-Adjustments.png";
    public static final String COLOUR_ADJUSTMENTS_WINDOW = "Colour-Adjustments.png";
    public static final String COLOUR_INVERT = "Colour-Invert.png";
    public static final String COLOUR_MASK = "Colour-Mask.png";

    // Tools menu icons
    public static final String TOOLS_FLIP_HORIZONTAL = "Tools-FlipHorizontal.png";
    public static final String TOOLS_FLIP_VERTICAL = "Tools-FlipVertical.png";
    public static final String TOOLS_ROTATE = "Tools-Rotate.png";
    public static final String TOOLS_RESIZE = "Tools-Resize.png";
    public static final String TOOLS_CROP = "Tools-Crop.png";

    public static final String TOOLS_FLIP_HORIZONTAL_WINDOW = "Tools-FlipHorizontal.png";
    public static final String TOOLS_ROTATE_WINDOW = "Tools-Rotate.png";
    public static final String TOOLS_RESIZE_WINDOW = "Tools-Resize.png";
    public static final String TOOLS_CROP_WINDOW = "Tools-Crop.png";


    // Language menu icons
    public static final String LANGUAGE_GLOBAL = "Language-Global-Large.png";

    /**
     * <p>
     * Takes in an {@code Ions.CONSTANT} and a type (either {@code type.menu}, 
     * {@code type.window} or {@code type.toolbar}) and returns an {@code ImageIcon}
     * object. 
     * </p><p>
     * The method scales the image to the appropriate size based on the type and
     * returns a new {@code ImageIcon} object with the scaled image.
     * </p>
     * 
     * <p>
     * This method is used to assign icons of appropriate size to actions in the 
     * {@link ImageAction} class
     * </p>
     * 
     * 
     * @param iconImage An IconImage object to be scaled
     * @param type The type of icon to be loaded: {@code type.menu}, {@code type.window} or {@code type.toolbar}
     * @return An String object scaled to the given type (size)
     */
    public static ImageIcon getScaledIcon(String iconFilename, type type) {
        // Grab the path of the icon 
        URL iconURL = getIconURL(iconFilename);

        // Create an ImageIcon from the path URL using the original image size
        ImageIcon iconImage = new ImageIcon(iconURL);
        Image newImage = null; 

        // Scale the image to the appropriate size based on the type
        switch (type) {
            case menu: 
                // Transforming the image in a smooth fasion 
                newImage = iconImage.getImage().getScaledInstance(menuIconWidth, menuIconHeight, java.awt.Image.SCALE_SMOOTH); 
                break; 

            case window: 
                // Transforming the image in a smooth fasion 
                newImage = iconImage.getImage().getScaledInstance(windowIconWidth, windowIconHeight, java.awt.Image.SCALE_SMOOTH); 
                break; 

            case toolbar:
                // Transforming the image in a smooth fasion 
                newImage = iconImage.getImage().getScaledInstance(toolbarIconWidth, toolbarIconHeight, java.awt.Image.SCALE_SMOOTH); 
                break;
        }

        // Reassigning the transformed image
        iconImage = new ImageIcon(newImage); 


        return iconImage;
    }


    private static URL getIconURL(String filename) {
        String pathToIcon = folderContainingIcons + filename;
        URL path = ClassLoader.getSystemResource(pathToIcon);
        return path; 


    }

    /**
     * Prevents instantiation of this class as it is a utility class, only meant 
     * to hold static constants.
     */ 
    private Icons() {}
}
