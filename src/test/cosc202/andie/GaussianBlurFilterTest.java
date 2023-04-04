package test.cosc202.andie;

import org.junit.*;
import static org.junit.Assert.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import cosc202.andie.GaussianBlurFilter; 

public class GaussianBlurFilterTest {

    @Test
    public void testApply() throws IOException {
        // Load test image      
        File file = new File("/Users/jamesrobiony-rogers/Library/CloudStorage/OneDrive-UniversityofOtago/COSC202/ProjectANDIE/project-andie/src/test/cosc202/andie/test_images/ANDIE-TestImage.jpg");
        BufferedImage testImage = ImageIO.read(file);  

        // Apply Gaussian blur filter to test image
        GaussianBlurFilter filter = new GaussianBlurFilter(2);
        BufferedImage resultImage = filter.apply(testImage);

        // Save result image to file
        File outputFile = new File("result_image.jpg");
        ImageIO.write(resultImage, "jpg", outputFile);

        // Check that result image exists and is not empty
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    // @Test
    // public void testPopulateKernalArray() {
    //     // Create a new Gaussian blur filter with radius 1
    //     GaussianBlurFilter filter = new GaussianBlurFilter(1);

    //     // Create a new array to hold the kernel
    //     float[] kernel = new float[9];

    //     // Call populateKernalArray with the new array
    //     filter.populateKernalArray(kernel);

    //     // Check that the array is populated with expected values
    //     assertArrayEquals(new float[] { 0.077847f, 0.123317f, 0.077847f,
    //             0.123317f, 0.195346f, 0.123317f,
    //             0.077847f, 0.123317f, 0.077847f }, kernel, 0.0001f);
    // }

    // @Test
    // public void testGaussianEquation() {
    //     // Create a new Gaussian blur filter with radius 1
    //     GaussianBlurFilter filter = new GaussianBlurFilter(1);

    //     // Test some values of the Gaussian equation
    //     assertEquals(0.000123f, filter.gaussianEquation(0, 0), 0.000001f);
    //     assertEquals(0.000936f, filter.gaussianEquation(1, 0), 0.000001f);
    //     assertEquals(0.000936f, filter.gaussianEquation(0, 1), 0.000001f);
    //     assertEquals(0.007125f, filter.gaussianEquation(1, 1), 0.000001f);
    // }
}
