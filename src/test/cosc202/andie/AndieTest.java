package test.cosc202.andie;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JFrame;
import cosc202.andie.Andie;
import cosc202.andie.actions.ImageAction;

public class AndieTest {

    @Test
    public void testAndieStartup() {
        // Attempt to create and show the GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Andie.main(null);
            } catch (Exception e) {
                // If any exceptions occur, fail the test
                fail("ANDIE startup failed with an exception: " + e.getMessage());
            }
        });

        // Sleep for a short time to allow the GUI to initialize
        try {
            Thread.sleep(10000); // Adjust the sleep time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get the main JFrame instance
        JFrame frame = Andie.getJFrame();

        // Check if the frame is visible
        assertTrue(frame.isVisible(), "ANDIE GUI is not visible");

        // Check if the frame title is set correctly
        assertTrue(frame.getTitle().toLowerCase().contains("andie"), "ANDIE GUI has an incorrect title");

        // Check if the menu bar is set
        assertNotNull(Andie.getMenuBar(), "ANDIE GUI does not have a menu bar");

        // Check if the toolbar is set
        assertNotNull(Andie.getToolbar(), "ANDIE GUI does not have a toolbar");
    }

    @Test
    public void testOpenFile() {

        // Attempt to create and show the GUI which opens the file
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Andie.main(null);
            } catch (Exception e) {
                // If any exceptions occur, fail the test
                fail("ANDIE startup failed with an exception: " + e.getMessage());
            }
        });

        // Sleep for a short time to allow the tester to select the file to open
        try {
            Thread.sleep(10000); // Adjust the sleep time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if a file has been loaded successfully
        assertNotNull(ImageAction.getTarget().getImage().getCurrentImage(), "ANDIE GUI has not loaded a file");
    }
}
