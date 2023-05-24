package test.cosc202.andie;

import org.junit.*;

import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import cosc202.andie.actions.FileActions;

public class FileActionsTest {

    // private Andie andie = new Andie(); 
    private ArrayList<Action> actions; 

    private FileActions fileActions;

    @Before
    public void setUp() {
        fileActions = new FileActions();

        // Creating a list of actions
        actions = fileActions.getAllActions(); 
        // actions = new Action[] {
        //     fileActions.new FileOpenAction("open", Icons.FILE_OPEN, "open_desc", KeyboardShortcut.FILE_OPEN),
        //     fileActions.new FileSaveAction("save", Icons.FILE_SAVE, "save_desc", KeyboardShortcut.FILE_SAVE),
        //     fileActions.new FileSaveAsAction("save_as", Icons.FILE_SAVE_AS, "save_as_desc", KeyboardShortcut.FILE_SAVE_AS),
        //     fileActions.new FileExportAction("export", Icons.FILE_EXPORT, "export_desc", KeyboardShortcut.FILE_EXPORT),
        //     fileActions.new ImportAction("import", Icons.FILE_IMPORT, "import_desc", KeyboardShortcut.FILE_IMPORT),
        //     fileActions.new FileExitAction("exit", Icons.FILE_EXIT, "exit_desc", KeyboardShortcut.FILE_EXIT)
        // };
    }

    @Test
    public void testFileActionIcons() {
        // Iterating over the actions 
        for (Action action : actions) {
            // Checking if the action is not null
            Assert.assertNotNull(action);

            // Storing the nessacery icons for a FileAction (MenuIcon & ToolbarIcon)
            ImageIcon[] icons = new ImageIcon[] {
                (ImageIcon) action.getValue(Action.SMALL_ICON), 
                (ImageIcon) action.getValue("ToolbarIcon"),
                // (ImageIcon) action.getValue("WindowIcon")
            };
            
            // Checking if the action contains all three icons
            for (ImageIcon icon : icons) {
                Assert.assertNotNull(icon);
            }
        }
    }

    @Test
    public void testFileActionKeyboardShortcuts() {
        // Iterating over the actions
        for (Action action : actions) {
            // Checking if the action is not null
            Assert.assertNotNull(action);

            // Storing the nessacery icons for a FileAction (MenuIcon & ToolbarIcon)
            KeyStroke keyboardShortcut = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);

            // Checking if the action contains a shortcut 
            String message = "Action " + action.getValue(Action.NAME) + " does not have a keyboard shortcut";
            Assert.assertNotNull(message, keyboardShortcut);
        }
    }

    // Add more tests for other actions if needed

}
