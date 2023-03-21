package test.cosc202.andie;

import org.junit.jupiter.api.*;
import cosc202.andie.ImagePanel;
import java.awt.Dimension;

public class ImagePanelTest {
    @Test
    void initialDummyTest(){

    }
    @Test
    void getZoomInitialValue() {
        ImagePanel testPanel = new ImagePanel();
        Assertions.assertEquals(100.0, testPanel.getZoom());
    }
    @Test 
    void getZoomAfterScale(){
        ImagePanel testPanel = new ImagePanel();
        testPanel.setZoom(70);
        Assertions.assertTrue(70.0 == testPanel.getZoom());
    }
   /**  @Test
    void givesException(){
        ImagePanel testPanel = new ImagePanel();
        Assertions.assertEquals(5, testPanel.getZoom());
    } */
    @Test 
    void getDimension(){
        ImagePanel testPanel = new ImagePanel();
        Assertions.assertTrue((new Dimension(450, 450)).equals(testPanel.getPreferredSize()));
    }
    }

