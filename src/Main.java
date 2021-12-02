import java.io.File;

/**
 * Runs the project code.
 */
public class Main {

   public static void main(String[] args)
   {
      // The shortcut to the file containing the images
      File fileList = new File("C:\\Users\\reece\\Desktop\\EditImages");

      // The images in the file
      File[] fileArray = fileList.listFiles();

      // Applies the filter to all images in the file that holds them.
      for (File f : fileArray)
      {
            ConvertToStipple.blackFigureOnWhite(f, "png", 4);
      }
   }
}
