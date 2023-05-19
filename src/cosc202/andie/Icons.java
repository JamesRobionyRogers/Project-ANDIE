package cosc202.andie;

import java.awt.Image;
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
 * ImageIcon icon = Icons.FILE_OPEN_MENU;
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

    /** The path to the folder containing all the icons */
    private static String pathToIconsFolder = "./src/icons/"; 
    /** The dimensions for icons that appear in the pop-up boxes */
    private static int windowIconWidth = 40, windowIconHeight = 40;
    /** The dimensions for icons that appear as options under the menu bar */
    private static int menuIconWidth = 15, menuIconHeight = 15;   
    /** The dimensions for icons that appear in the toolbar */
    private static int toolbarIconWidth = 20, toolbarIconHeight = 20;
    /** Used to describe where the icon will used: menubar / window */
    public static enum type { menu, window, toolbar }; 
    
    // File menu icons see 
    public static final ImageIcon FILE_OPEN_MENU = assign("File-OpenFile.png", type.menu);
    public static final ImageIcon FILE_SAVE_MENU = assign("File-Save.png", type.menu);
    public static final ImageIcon FILE_SAVE_AS_MENU = assign("File-SaveAs.png", type.menu);
    public static final ImageIcon FILE_EXPORT_MENU = assign("File-Export.png", type.menu);
    public static final ImageIcon FILE_IMPORT_MENU = assign("File-Import.png", type.menu);
    public static final ImageIcon FILE_EXIT_MENU = assign("File-Exit.png", type.menu); 


    // Edit menu icons 
    public static final ImageIcon EDIT_UNDO = assign("Edit-Undo.png", type.menu);
    public static final ImageIcon EDIT_REDO = assign("Edit-Redo.png", type.menu);

    // View menu icons
    public static final ImageIcon VIEW_ZOOM_IN = assign("View-ZoomIn.png", type.menu);
    public static final ImageIcon VIEW_ZOOM_OUT = assign("View-ZoomOut.png", type.menu);
    public static final ImageIcon VIEW_ZOOM_RESET = assign("View-Reset.png", type.menu);

    // Filter menu icons
    public static final ImageIcon FILTER_BLUR = assign("Filter-Blur.png", type.menu);
    public static final ImageIcon FILTER_SHARPEN = assign("Filter-Sharpen.png", type.menu);
    public static final ImageIcon FILTER_CIRCLE_BLUR = assign("Filter-CircleBlur.png", type.menu);
    public static final ImageIcon FILTER_EMBOSS = assign("Filter-Emboss.png", type.menu);
    public static final ImageIcon FILTER_EDGE_DETECTION = assign("Filter-EdgeDetection.png", type.menu);

    public static final ImageIcon FILTER_BLUR_WINDOW = assign("Filter-Blur.png", type.window);
    public static final ImageIcon FILTER_SHARPEN_WINDOW = assign("Filter-Sharpen.png", type.window);
    public static final ImageIcon FILTER_CIRCLE_BLUR_WINDOW = assign("Filter-CircleBlur.png", type.window);
    public static final ImageIcon FILTER_EMBOSS_WINDOW = assign("Filter-Emboss.png", type.window);
    public static final ImageIcon FILTER_EDGE_DETECTION_WINDOW = assign("Filter-EdgeDetection.png", type.window);

    // Colour menu icons
    public static final ImageIcon COLOUR_GREYSCALE = assign("Colour-Greyscale.png", type.menu);
    public static final ImageIcon COLOUR_ADJUSTMENTS = assign("Colour-Adjustments.png", type.menu);
    public static final ImageIcon COLOUR_ADJUSTMENTS_WINDOW = assign("Colour-Adjustments.png", type.window);
    public static final ImageIcon COLOUR_INVERT = assign("Colour-Invert.png", type.menu);

    // Tools menu icons
    public static final ImageIcon TOOLS_FLIP_HORIZONTAL = assign("Tools-FlipHorizontal.png", type.menu);
    public static final ImageIcon TOOLS_FLIP_VERTICAL = assign("Tools-FlipVertical.png", type.menu);
    public static final ImageIcon TOOLS_ROTATE = assign("Tools-Rotate.png", type.menu);
    public static final ImageIcon TOOLS_RESIZE = assign("Tools-Resize.png", type.menu);
    public static final ImageIcon TOOLS_CROP = assign("Tools-Crop.png", type.menu);

    public static final ImageIcon TOOLS_FLIP_HORIZONTAL_WINDOW = assign("Tools-FlipHorizontal.png", type.window);
    public static final ImageIcon TOOLS_ROTATE_WINDOW = assign("Tools-Rotate.png", type.window);
    public static final ImageIcon TOOLS_RESIZE_WINDOW = assign("Tools-Resize.png", type.window);
    public static final ImageIcon TOOLS_CROP_WINDOW = assign("Tools-Crop.png", type.window);


    // Language menu icons
    public static final ImageIcon LANGUAGE_GLOBAL = assign("Language-Global-Large.png", type.menu);

    /**
     * <p>
     * Takes in a file name and a type (either {@code type.menu} or
     * {@code type.window})
     * and returns an {@code ImageIcon} object. It loads the image file from
     * the specified path, scales it to the appropriate size based on the type,
     * and returns a new {@code ImageIcon} object with the scaled image.
     * </p>
     * 
     * <p>
     * If there is an exception while loading the image, it returns
     * {@code null}.
     * </p>
     * 
     * <p>
     * This method is used to assign the appropriate icons to the various menu
     * items in the application. Solely used within the {@code Icons} class.
     * </p>
     * 
     * @param fileName The name of the file to be loaded
     * @param type     The type of icon to be loaded: {@code type.menu},
     *                 {@code type.window}
     */
    private static ImageIcon assign(String fileName, type type) {

        try {   
            // Loading high quality image in  
            ImageIcon iconImage = new ImageIcon(pathToIconsFolder + fileName);
            Image newImage = null; 
            
            // Scaling the image to the correct size based on the type of icon
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
        } catch (Exception e) {
            return null; 
        }
    }

    /**
     * <p>
     * Takes in an {@code ImageIcon} object and a type (either {@code type.menu}, 
     * {@code type.window} or {@code type.toolbar}) and returns an {@code ImageIcon}
     * object. 
     * </p><p>
     * The method scales the image to the appropriate size based on the type and
     * returns a new {@code ImageIcon} object with the scaled image.
     * </p>
     * 
     * <p>
     * This method is used to assign the appropriate icons to the various menu items in the application.
     * </p>
     * 
     * 
     * @param iconImage An IconImage object to be scaled
     * @param type The type of icon to be loaded: {@code type.menu}, {@code type.window} or {@code type.toolbar}
     * @return An ImageIcon object scaled to the given type (size)
     */
    public static ImageIcon setIconSize(ImageIcon iconImage, type type) {
        Image newImage = null; 
        // Scaling the image to the correct size based on the type of icon
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

    /**
     * Prevents instantiation of this class as it is a utility class, only meant 
     * to hold static constants.
     */ 
    private Icons() {}
}
