package cosc202.andie;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import cosc202.andie.actions.*;


/**
 * <p>
 * {@code Toolbar} provides extra usibility to ANDIE, adding quick access to
 * commonly used actions.
 * </p>
 * 
 * <p>
 * The toolbar is a vertical toolbar that can be reposistioned to any other side
 * of the window or can even be detached from the window and moved around the
 * screen.
 * </p>
 * 
 * <p>
 * The toolbar is made up of a list of actions that are grouped by their general
 * purpose into menus.
 * </p>
 * 
 * @author James Robiony-Rogers
 * @version 1.0 - 17th May 2023
 */
public class Toolbar extends JToolBar {

    /** The height of the spacing between toolbar items */
    private static final int SPACING_HEIGHT = 8;

    /** A list of actions for the toolbar */
    private static ArrayList<Action> toolbarActions = new ArrayList<Action>() {{
        addAll(new FileActions().getToolbarActions()); 
        addAll(new EditActions().getToolbarActions());
        addAll(new ViewActions().getToolbarActions());
        addAll(new FilterActions().getToolbarActions());
        addAll(new ColourActions().getToolbarActions());
        addAll(new ToolActions().getToolbarActions());
    }};

    /**
     * Constructor to pass in Action objects to be displayed for the user 
     * 
     * @param actions the actions to be displayed
     */
    public Toolbar(boolean enableButtons) {
        super();

        // Creating a verticle toolbar, similar to Adobe Photoshop
        this.setOrientation(JToolBar.VERTICAL);

        // Iterating over the actions adding them to the toolbar
        Action prevAction = null;
        for (Action action : toolbarActions) {

            // If the action is null, skip it
            if (action == null) continue;

            // Getting the current actions icon 
            // Setting the toolbar action icon 
            ImageIcon actionIcon = (ImageIcon) action.getValue(Action.SMALL_ICON);
            action.putValue(Action.SMALL_ICON, Icons.setIconSize(actionIcon, Icons.type.toolbar));

            // Checking if the current instance outter class is different to the previous instance's outter class 
            // If so, add a separator to the toolbar
            if (prevAction != null && action.getClass().getEnclosingClass() != prevAction.getClass().getEnclosingClass()) {
                removeLastSpacing(); 
                this.addSeparator();
            }

            JButton toolbarItem = this.add(action);
            toolbarItem.setEnabled(enableButtons);
            toolbarItem.setFocusPainted(false); 
            toolbarItem.setBorderPainted(false);


            addSpacingBetweenToolbarItems();

            prevAction = action;
        }

        // Removes the last spacing between the last toolbar item and the bottom of the toolbar
        removeLastSpacing();
        this.addSeparator();

        // Enable the open file button if there is no image loaded
        this.getComponent(0).setEnabled(true);

    }

    /**
     * Replaces {@code toolbarActions} with a translated verson of the same
     * actions
     */
    private void translateToolbarItems() {
        ArrayList<Action> translatedActions = new ArrayList<Action>() {{
            addAll(new FileActions().getToolbarActions()); 
            addAll(new EditActions().getToolbarActions());
            addAll(new ViewActions().getToolbarActions());
            addAll(new FilterActions().getToolbarActions());
            addAll(new ColourActions().getToolbarActions());
            addAll(new ToolActions().getToolbarActions());
    
        }};

        toolbarActions = translatedActions;
    }

    /**
     * Updates the toolbar by enabling/disabling the buttons and updating their 
     * tooltips
     * 
     * @param enableButtons whether the buttons should be enabled or disabled
     */
    public void updateToolbar(boolean enableButtons) {
        translateToolbarItems();

        int buttonCount = 0; 
        for (Component component : this.getComponents()) {
            if (component instanceof JButton) {
                Action translatedAction = toolbarActions.get(buttonCount);

                JButton toolbarButton = (JButton) component;
                toolbarButton.setEnabled(enableButtons);
                
                // Updating the tooltip using the translated action's description
                String newToolTip = translatedAction.getValue(Action.SHORT_DESCRIPTION).toString();
                toolbarButton.setToolTipText(newToolTip);
                buttonCount++; 
            }
        }
    }


    /**
     * Adds spacing between toolbar items
     */
    private void addSpacingBetweenToolbarItems() {
        this.add(Box.createVerticalStrut(SPACING_HEIGHT));
    }

    /**
     * Removes the last spacing added by the loop between the last toolbar item 
     * and the bottom of the toolbar
     */
    private void removeLastSpacing() {
        this.remove(this.getComponentCount() - 1);
    }
}
