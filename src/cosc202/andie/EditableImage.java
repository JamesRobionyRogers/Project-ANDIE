package cosc202.andie;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 * 
 * <p>
 * The EditableImage represents an image with a series of operations applied to
 * it.
 * It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can
 * be undone.
 * THis is what is meant by "A Non-Destructive Image Editor" - you can always
 * undo back to the original image.
 * </p>
 * 
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original
 * image
 * and the result of applying the current set of operations to it.
 * The operations themselves are stored on a {@link Stack}, with a second
 * {@link Stack}
 * being used to allow undone operations to be redone.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class EditableImage {

    private SetLanguage language = SetLanguage.getInstance();

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /**
     * The current image, the result of applying {@link ops} to {@link original}.
     */
    private BufferedImage current;
    
    // a temporary buffered image for when dealing with visual changes
    private BufferedImage tempStore;
    private boolean editing;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    private loopStack<BufferedImage> undoImages;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    private loopStack<BufferedImage> redoImages;
    /** The file where the original image is stored/ */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;

    private boolean recording;

    private Stack<ImageOperation> recordedOps = new Stack<ImageOperation>();

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * 
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty stack
     * of operations.
     * </p>
     */
    public EditableImage() {
        original = null;
        current = null;
        ops = new Stack<ImageOperation>();
        undoImages = new loopStack<BufferedImage>();
        redoOps = new Stack<ImageOperation>();
        redoImages = new loopStack<BufferedImage>();
        imageFilename = null;
        opsFilename = null;
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return original != null;
    }

    public boolean hasAlpha(){

        /*
         * A wrapper for the hasAlpha boolean
         */
        final class Alpha {
            public static boolean hasAlpha = false;
        }

        /* looping over each pixel rather than getting the whole argbArray 
         * if it has alpha it'll probably be present early rather than late.
         * For images without alpha unlucky
        */
        ArrayDeque<Integer> horizontalOps = new ArrayDeque<Integer>(current.getWidth());
        for (int i = 0; i < current.getWidth(); horizontalOps.add(i++));

        ArrayDeque<Integer> verticalOps = new ArrayDeque<Integer>(current.getHeight());
        for (int i = 0; i < current.getHeight(); verticalOps.add(i++));

        // calculate if it has an alpha channel
        horizontalOps.parallelStream().takeWhile(i -> (!Alpha.hasAlpha)).forEach(x -> {
            verticalOps.parallelStream().takeWhile(i -> (!Alpha.hasAlpha)).forEach(y -> {
                if ((((current.getRGB(x, y) & 0xFF000000) >>> 24) != 255)){
                    Alpha.hasAlpha = true;            
                }
            });
        });

        return Alpha.hasAlpha;
    }

    private class loopStack<T> extends Stack<T> {
        private final int MAX_SIZE = 3;
        loopStack(){
            super();
        }

        public T push(T item){

            super.push(item);
            if (super.size() > MAX_SIZE){
                super.remove(0);
            }
            return item;
        }
    }
    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage.
     * </p>
     * 
     * <p>
     * Object instances in Java are accessed via references, which means that
     * assignment does
     * not copy an object, it merely makes another reference to the original.
     * In order to make an independent copy, the {@code clone()} method is generally
     * used.
     * {@link BufferedImage} does not implement {@link Cloneable} interface, and so
     * the
     * {@code clone()} method is not accessible.
     * </p>
     * 
     * <p>
     * This method makes a cloned copy of a BufferedImage.
     * This requires knoweldge of some details about the internals of the
     * BufferedImage,
     * but essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     * 
     * <p>
     * This code is taken from StackOverflow:
     * <a href=
     * "https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to
     * <a href=
     * "https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     * 
     * <p>
     * This method (only) is released under
     * <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
     * </p>
     * 
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) throws java.lang.NullPointerException {
        if (bi == null) {
            throw new java.lang.NullPointerException("Argument was null");
        }
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * <p>
     * Open an image from a file.
     * </p>
     * 
     * <p>
     * Opens an image from the specified file.
     * Also tries to open a set of operations from the file with <code>.ops</code>
     * added.
     * So if you open <code>some/path/to/image.png</code>, this method will also try
     * to
     * read the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param filePath The file to open the image from.
     */
    public void open(String filePath) {
        String imageFilename;
        String opsFilename;
        BufferedImage original;
        BufferedImage current;
        try {
            imageFilename = filePath;
            opsFilename = imageFilename + ".ops";
            File imageFile = new File(imageFilename);
            original = ImageIO.read(imageFile);
        } catch (IOException e) {
            ExceptionHandler.displayError(language.getTranslated("open_file_io_exception"));
            return;
        }

        try {
            current = deepCopy(original);
        } catch (java.lang.NullPointerException e) {
            // file wasn't an image
            ExceptionHandler.displayError(language.getTranslated("non_image_file"));
            return;
        }
        // image is correct
        this.imageFilename = imageFilename;
        this.opsFilename = opsFilename;
        this.original = original;
        this.current = current;

        try {
            FileInputStream fileIn = new FileInputStream(this.opsFilename);

            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.

            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();

            ops = opsFromFile;
            objIn.close();
            fileIn.close();
        } catch (Exception e) {
            // ops file didn't exist, image still loaded so clear application stack
            ops.clear();
            undoImages.clear();

            // Could be no file or something else. Carry on for now.
        } finally {
            // clear redo, recordingops and turn off recording either way
            redoOps.clear();
            recordedOps.clear();
            recording = false;
            redoImages.clear();
        }

        this.refresh();
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     * 
     * <p>
     * Saves an image to the file it was opened from, or the most recent file saved
     * as.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     */
    public void save() {
        try {
            if (this.opsFilename == null) {
                this.opsFilename = this.imageFilename + ".ops";
            }

            // just for testing
            if (!this.opsFilename.equals(this.imageFilename + ".ops")){
                System.out.println("OPS FILE IS INCORRECT FOR IMAGE FILE");
            }
            // Write operations file
            FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(this.ops);
            objOut.close();
            fileOut.close();
        } catch (FileNotFoundException fileException) {
            ExceptionHandler.displayError(language.getTranslated("file_not_found_exception"));

        } catch (IOException a) {
            System.out.println(a);
            ExceptionHandler.displayError(language.getTranslated("save_file_io_excepton"));
        }
    }

    /**
     * <p>
     * Save an image to a speficied file.
     * </p>
     * 
     * <p>
     * Saves an image to the file provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     */
    public void saveAs(String imageFilename, String extension) {
        try {
            // Write image file based on file extension
            exportSave(imageFilename, extension);

            // save ops file
            this.imageFilename = imageFilename + "." + extension;
            this.opsFilename = this.imageFilename + ".ops";
            save();
        }
        // Not sure when this would catch anything as exceptions should come from save()
        // method
        catch (Exception e) {
            ExceptionHandler.displayError(language.getTranslated("save_as_exception"));
        }
        
    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     * 
     * @param op The operation to apply.
     */
    public void apply(ImageOperation op) {
        
        // image isn't loaded
        if (current == null)
            return;
        if (editing){
            revert();
        }

        undoImages.push(current);
        
        current = op.apply(current);
        ops.add(op);
        if(recording) recordedOps.add(op);
        
        redoOps.clear();
        redoImages.clear();
    }

    public void applyTemp(ImageOperation op){
        if (current == null) return;
        if (!editing){
            tempStore = current;
            editing = true;
        }
        current = op.apply(tempStore);
    }

    public void revert(){
        if (editing){
            current = tempStore;
            editing = false;
        }
    }


    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo() {
        //System.out.println("triggered undo");
        if (!ops.isEmpty()) {
            //System.out.println("undo isn't empty");
            redoOps.push(ops.pop());
            if (!undoImages.isEmpty()){
                //System.out.println("Image stack isn't empty");
                //System.out.println(current);
                redoImages.push(current);
                current =  undoImages.pop();
                //System.out.println(current);
            } else {
                redoImages.push(current);
                refresh();
            }
        }
        if(recording && recordedOps.size() !=0) recordedOps.pop();
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     */
    public void redo() {
        //System.out.println("redo triggered");
        if (!redoOps.isEmpty()) {
            //System.out.println("redo isn't empty");
            
            if (!redoImages.isEmpty()){
                //System.out.println("image stack isn't empty");
                //System.out.println(current);
                undoImages.push(current);
                current = redoImages.pop();
                //System.out.println(current);
            } else {
                undoImages.push(current);
                current = redoOps.peek().apply(current);
            }
            
            ops.add(redoOps.pop());
            if (recording){
                recordedOps.add(ops.peek());
            }
        }

    }

    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     * 
     * @return The result of applying all of the current operations to the
     *         {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     * 
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to it in
     * sequence.
     * This is useful when undoing changes to the image, or in any other case where
     * {@link current}
     * cannot be easily incrementally updated.
     * </p>
     */
    private void refresh() {
        current = deepCopy(original);
        for (ImageOperation op : ops) {
            undoImages.push(current);
            current = op.apply(current);
        }
    }

    /**
     * <p>
     * Exports an edited image to new file with specified file name.
     * </p>
     * 
     * <p>
     * Saves edited image to new file with file name provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to export the image to.
     * @throws Exception If something goes wrong.
     */
    public void export(String imageFilename, String extension) throws Exception {
        
        String exportFilename = imageFilename + "." + extension;
        System.out.println(exportFilename);
        if (original == null) return;
        try {
            if(!testWrite(imageFilename)) throw (new java.lang.IllegalArgumentException("Cant write file"));
            ImageIO.write(this.current, extension, new File(exportFilename));
        } catch (Exception ex) {
            ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("save_file_io_excepton"));
            return;
        }
    }

    /**
     * <p>
     * Exports an edited image to new file with specified file name.
     * </p>
     * 
     * <p>
     * Saves edited image to new file with file name provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to export the image to.
     * @param extension the image file extension
     * @param ouput the image to write
     * @throws Exception If something goes wrong.
     */
    private void exportSave(String imageFilename, String extension) throws Exception {
        
        String exportFilename = imageFilename + "." + extension;
        System.out.println(exportFilename);
        try {
            String exten2 = extension.substring(1 + extension.lastIndexOf(".")).toLowerCase();
            if(!testWrite(imageFilename)) throw (new java.lang.IllegalArgumentException("Cant write file"));
            ImageIO.write(original, exten2, new File(exportFilename));
        } catch (Exception ex) {
            ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("save_file_io_excepton"));
            return;
        }
    }

   /**
     * <p>
     * Imports a .ops file that contains a series of operations 
     * </p>
     * 
     * <p>
     * Applies these operations to the currect image (ie adds them to the .ops stack)
     * </p>
     * 
     * 
     * @param filepath
     * @throws Exception If user attempts to open a file that isnt a .ops
     */


    public void importMacro(String filePath) {

        if (current == null) return;
        
        try {
            File macroFile = new File(filePath);
            FileInputStream fileIn = new FileInputStream(macroFile);

            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.

            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();

            for (ImageOperation op : opsFromFile){
                apply(op);
            }

            objIn.close();
            fileIn.close();

            this.refresh();
        } catch (Exception e) {
            // wasn't an ops file
        }

    }


    /** Tests a filename path to see if its valid
     * This uses regex to test the filename this is extremely restrictive
     * 
     * @param testFilename The filename path to test
     * @return If the filename is valid
     */
    private boolean testWrite(String testFilename){
        File f = new File(testFilename);
        // create regex pattern
        Pattern fileNames = Pattern.compile("^[A-Za-z0-9-_()]{1,50}$");
        // check that the filename matches
        Matcher test = fileNames.matcher(f.getName());
        // return result
        return test.matches();
    }

    public int getHeight(){
        return current.getHeight();
    }

    public int getWidth(){
        return current.getWidth();
    }
    /**
     * <p>
     * Method that is called when the user starts recording operations
     * <p>
     * 
     * <p>
     * Sets the tracking variable 'recording' to true 
     * <p>
     */

    public void record(){
        recording = true;
    }

     /**
     * <p>
     * Exports a series of recorded image operation to new .ops file with specified file name.
     * </p>
     * 
     * <p>
     * Saves image operations to new file with file name provided as a parameter.
     * 
     * 
     * 
     * @param imageFilename The file location to export the image to.
     * @param extension the image file extension
     * @param ouput the ops file to write
     * @throws Exception If something goes wrong.
     */


    public void stoprecord(String filepath, String extension){
        recording = false;
        try{
        FileOutputStream fileOut = new FileOutputStream(filepath + "." + extension);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(this.recordedOps);
            objOut.close();
            fileOut.close();
            recordedOps.clear();
        }
        catch (IOException a) {
            ExceptionHandler.displayError(language.getTranslated("save_file_io_excepton"));
        }
    }
/**
 * Method to se if the user is currently recording operations
 * 
 * Used in EditActions to stop fileChooser appearing if user clicks stop recording, but was
 * not recording 
 * 
 * 
 * @return if the user is recording operations
 */
    
    public boolean isRecording() {
        return recording;
    }
}
