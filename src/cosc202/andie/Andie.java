package cosc202.andie;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import cosc202.andie.actions.*;

import javax.imageio.*;

/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 * 
 * <p>
 * This class is the entry point for the program.
 * It creates a Graphical User Interface (GUI) that provides access to various image editing and processing operations.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class Andie {

    /**
     * <p>
     * Launches the main GUI for the ANDIE program.
     * </p>
     * 
     * <p>
     * This method sets up an interface consisting of an active image (an {@code ImagePanel})
     * and various menus which can be used to trigger operations to load, save, edit, etc. 
     * These operations are implemented {@link ImageOperation}s and triggerd via 
     * {@code ImageAction}s grouped by their general purpose into menus.
     * </p>
     * 
     *
     *  
     */
    private static JFrame frame;
    private static boolean frameSet = false;
    private static boolean firstRun = true;

    private static JMenuBar menuBar;
    private static Toolbar toolbar;

    private static void createAndShowGUI() {
        SetLanguage language = SetLanguage.getInstance();
        // Set up the main GUI frame
        frame = new JFrame(language.getTranslated("andie"));
        Image image;
        try {
            image = ImageIO.read(Andie.class.getClassLoader().getResource("icon.png"));
            frame.setIconImage(image);
        } catch (IOException e) {
            ExceptionHandler.displayWarning(language.getTranslated("icon_warning"));
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // The main content area is an ImagePanel
        ImagePanel imagePanel = new ImagePanel();
        ImageAction.setTarget(imagePanel);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        ClickListener.setTarget(imagePanel);
        ClickListener cl = ClickListener.getInstance();
        imagePanel.addMouseListener(cl);
        imagePanel.addMouseMotionListener(cl);
        
        //setBar();
        
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setLocationRelativeTo(null);      // Centering the ANDIE window on screen
        frame.setVisible(true); 
    }

    /**
     * Make the menuBar
     * 
     * Makes accessible for refreshing the GUI
     * 
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see ColourActions
     * 
     * @param args
     */

     public static void setMenuBar(){
                // Add in menus for various types of action the user may perform.
                menuBar = new JMenuBar();

                // File menus are pretty standard, so things that usually go in File menus go here.
                FileActions fileActions = new FileActions();
                menuBar.add(fileActions.createMenu());
        
                // Likewise Edit menus are very common, so should be clear what might go here.
                EditActions editActions = new EditActions();
                menuBar.add(editActions.createMenu());
        
                // View actions control how the image is displayed, but do not alter its actual content
                ViewActions viewActions = new ViewActions();
                menuBar.add(viewActions.createMenu());
        
                // Filters apply a per-pixel operation to the image, generally based on a local window
                FilterActions filterActions = new FilterActions();
                menuBar.add(filterActions.createMenu());
        
                // Actions that affect the representation of colour in the image
                ColourActions colourActions = new ColourActions();
                menuBar.add(colourActions.createMenu());

                //Actions to do toolactions
                ToolActions toolActions = new ToolActions();
                menuBar.add(toolActions.createMenu());

                //Actions to add multilingual support
                MultilingualSupport multilingual = new MultilingualSupport();
                menuBar.add(multilingual.createMenu());
    }

    /**
     * Make the toolbar and add it to the frame 
     * @see Toolbar
     */
    public static void setToolbar() {
        // Check if there is a image 
        boolean enableButtons = ImageAction.getTarget().getImage().getCurrentImage() == null ? false : true;

        // Create a new toolbar if it doesn't exist, or update the existing toolbar
        if (toolbar == null) {
            toolbar = new Toolbar(enableButtons);
            frame.add(toolbar, BorderLayout.WEST);
        } else {
            toolbar.updateToolbar(enableButtons);
        }
    }

    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     * 
     * <p>
     * Creates and launches the main GUI in a separate thread.
     * As a result, this is essentially a wrapper around {@code createAndShowGUI()}.
     * </p>
     * 
     * @param args Command line arguments, not currently used
     * @see #createAndShowGUI()
     */
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    if(!frameSet){
                        createAndShowGUI();
                        frameSet = true;
                    }
                    setMenuBar();
                    setToolbar(); 
                    SetLanguage language = SetLanguage.getInstance();
                    frame.setJMenuBar(menuBar);
                    frame.setTitle(language.getTranslated("andie"));
                    frame.revalidate();
                    if (firstRun){
                        // hack to find open action
                        for (Component comp : menuBar.getComponents()){
                            JMenu test = (JMenu)comp;
                            for (Component comp2 : test.getMenuComponents()){
                                JMenuItem test2 = (JMenuItem)comp2; 
                                if (test2.getAction().getClass().getName().equals("cosc202.andie.actions.FileActions$FileOpenAction")){
                                    // trigger open
                                    test2.doClick();
                                }
                            }
                            
                        }
                    }
                    firstRun = false;
                    
                } catch (Exception e) {
                    // Error's should not be caught here, so if they are they need to be addressed 
                    ExceptionHandler.debugException(e);
                }
            }
        });
    }

    /**
     * Returns ANDIE's JFrame. Inteded for use in the @see ExceptionHandler class.
     * 
     * @return ANDIE's JFrame
     */
    public static JFrame getJFrame() {
        return frame;
    }

    /**
     * Returns ANDIE's JMenuBar. 
     * 
     * @return ANDIE's JMenuBar
     */
    public static JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Returns ANDIE's Toolbar.
     * 
     * @return ANDIE's Toolbar
     */
    public static Toolbar getToolbar () {
        return toolbar;
    }
}
