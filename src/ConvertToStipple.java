import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * A class of static methods.
 * Convert image on the machine into a "drawn" stipple art version of it. The
 * methods choose the images based on a file path.  Includes
 * blackFigureOnWhite() which converts to stipple art with black outlined
 * figures, fully black and white. blackFigureAged() which converts to stipple
 * art with black outlined figures, has a slight yellow hue.  whiteFigure()
 * converts to stipple art with white outlined figures. The clean value, cleans
 * up the background, the higher the values the more clean, but less detail on
 * the figures.  Each method has its recommended value.
 * (WARNING: will ruin non-image files if applied).
 */
public class ConvertToStipple {

   /**
    * Mutates the image input into a black figured image. Named "black
    * figure" based on outline color of the image figures. Clean value > 2
    * recommended.
    *
    * @param imageFile        A file path for the image.
    * @param fileType         The type of image file.
    */
   public static void blackFigureOnWhite
   (File imageFile, String fileType, int clean) throws IllegalArgumentException
   {
      // Check if the clean value is valid
      if (clean < 1 || clean > 10)
         throw new IllegalArgumentException("input value for clean is " +
                 "invalid, choose a value between 1 to 10. " +
                 "Values > 2 recommended");


      // Initialize the img variable
      BufferedImage img = null;

      // Read the image.
      try
      {
         img = ImageIO.read(imageFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      // convert the image data to greyScale, and create one that is G-blurred.
      img = convertToGreyScale(img);
      BufferedImage GreyGBlur =
              gaussianBlur(10, convertToGreyScale(imageFile));

      // Change the image data pixel by pixel.
      for (int y = 0; y < img.getHeight(); y++)
      {
         for (int x = 0; x < img.getWidth(); x++)
         {
            // Retrieve the RGB values of the two images.
            Color greyRGB = new Color(img.getRGB(x, y));
            Color GreyGBlurRGB = new Color(GreyGBlur.getRGB(x, y));

            // Subtract the pixels add a value to the right side to clean up the
            // image and convert to black figure.
            int alpha = (GreyGBlurRGB.getAlpha()) - (greyRGB.getAlpha() + clean);
            int red = (GreyGBlurRGB.getRed()) - (greyRGB.getRed() + clean);
            int green = (GreyGBlurRGB.getGreen()) - (greyRGB.getGreen() + clean);
            int blue = (GreyGBlurRGB.getBlue()) - (greyRGB.getBlue() + clean);

            // Convert the values into a single RBG value.
            int RGB = (alpha << 24) | (red << 16) | (green << 8) | blue;

            // Set the RGB data of the pixel.
            img.setRGB(x, y, RGB);
         }
      }

      // Remove the Yellow in the image "clean"
      img = convertToGreyScale(img);

      // Change the image file data.
      try
      {
         File file = new File(imageFile.getAbsolutePath());
         ImageIO.write(img, fileType, file);
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Mutates the image input into a black figured image. Named "black
    * figure" based on outline color of the image figures. Has a yellowish white
    * background. Clean value > 6 recommended.
    *
    * @param imageFile        A file path for the image.
    * @param fileType         The type of image file.
    */
   public static void blackFigureOnYellowishWhite
   (File imageFile, String fileType, int clean)
   {
      // Check if the clean value is valid.
      if (clean < 1 || clean > 10)
         throw new IllegalArgumentException("input value for clean is " +
                 "invalid, choose a value between 1 to 10. " +
                 "Values > 6 recommended");

      // Initialize the img variable
      BufferedImage img = null;

      // Read the image.
      try
      {
         img = ImageIO.read(imageFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      // convert the image data to greyScale, and create one that is G-blurred.
      img = convertToGreyScale(img);
      BufferedImage GreyGBlur =
              gaussianBlur(10, convertToGreyScale(imageFile));

      // Change the image data pixel by pixel.
      for (int y = 0; y < img.getHeight(); y++)
      {
         for (int x = 0; x < img.getWidth(); x++)
         {
            // Retrieve the RGB values of the two images.
            Color greyRGB = new Color(img.getRGB(x, y));
            Color GreyGBlurRGB = new Color(GreyGBlur.getRGB(x, y));

            // Subtract the pixels add an int to the right side to clean up the
            // image and convert to black figure.
            int alpha = (GreyGBlurRGB.getAlpha()) - (greyRGB.getAlpha() + clean);
            int red = (GreyGBlurRGB.getRed()) - (greyRGB.getRed() + clean);
            int green = (GreyGBlurRGB.getGreen()) - (greyRGB.getGreen() + clean);
            int blue = (GreyGBlurRGB.getBlue()) - (greyRGB.getBlue() + clean);

            // Convert the values into a single RBG value.
            int RGB = (alpha << 24) | (red << 16) | (green << 8) | blue;

            // Set the RGB data of the pixel.
            img.setRGB(x, y, RGB);
         }
      }

      // Change the image file data.
      try
      {
         File file = new File(imageFile.getAbsolutePath());
         ImageIO.write(img, fileType, file);
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Mutates the image input into a white figured image. Named "white
    * figure" based on outline color of the image figures. Clean value > 3
    * recommended.
    *
    * @param imageFile        A file path for the image.
    * @param fileType         The type of image file.
    */
   public static void whiteFigure(File imageFile, String fileType, int clean)
   {
      // Check if clean argument is valid.
      if (clean < 3 || clean > 10)
         throw new IllegalArgumentException("input value for clean is " +
                 "invalid, choose a value between 1 to 10. " +
                 "Values > 3 recommended");

      // Initialize the img variable.
      BufferedImage img = null;

      // Read the image.
      try
      {
         img = ImageIO.read(imageFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      // convert the image data to greyScale, and create one that is G-blurred.
      img = convertToGreyScale(img);
      BufferedImage GreyGBlur =
              gaussianBlur(10, convertToGreyScale(imageFile));

      // Change the image data pixel by pixel.
      for (int y = 0; y < img.getHeight(); y++)
      {
         for (int x = 0; x < img.getWidth(); x++)
         {
            // Retrieve the RGB values of the two images.
            Color greyRGB = new Color(img.getRGB(x, y));
            Color GreyGBlurRGB = new Color(GreyGBlur.getRGB(x, y));

            // Subtract the pixels add ten to the left side to clean up the
            // image and convert to white figure.
            int alpha = (GreyGBlurRGB.getAlpha() + clean) - (greyRGB.getAlpha());
            int red = (GreyGBlurRGB.getRed() + clean) - (greyRGB.getRed());
            int green = (GreyGBlurRGB.getGreen() + clean) - (greyRGB.getGreen());
            int blue = (GreyGBlurRGB.getBlue() + clean) - (greyRGB.getBlue());

            // Convert the values into a single RBG value.
            int RGB = (alpha << 24) | (red << 16) | (green << 8) | blue;

            // Set the RGB data of the pixel.
            img.setRGB(x, y, RGB);
         }
      }

      // Remove the Yellow in the image "clean"
      img = convertToGreyScale(img);

      // Change the image file data.
      try
      {
         File file = new File(imageFile.getAbsolutePath());
         ImageIO.write(img, fileType, file);
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * A private helper method that applies Gaussian blur to a BufferedImage.
    * The larger the radius the stronger the blur.
    *
    * @param radius        The radius of the gaussian kernel.
    * @param img           The BufferedImage to be processed.
    * @return              Returned a Gaussian Blurred BufferedImage.
    */
   private static BufferedImage gaussianBlur(int radius, BufferedImage img)
   {
      BufferedImage inputImg = img;
      BufferedImage result = img;

      // Set the sigma value and kernel width based on the radius input.
      double sigma = Math.max(radius / 2, 1);
      int kernelWidth = (2 * radius) + 1;

      // Initialize the 2D array which is the kernel.
      double[][] kernel = new double[kernelWidth][kernelWidth];
      double sum = 0;

      // Populate the kernel with the respective Gaussian distribution values.
      // The x and y values are how far they are from the center
      for (int y = -radius; y <= radius; y++)
      {
         for (int x = -radius; x <= radius; x++)
         {
            // Use the gaussian equation.
            double exponentNumerator = -(x * x + y * y);
            double exponentDenominator = (2 * sigma * sigma);

            double eExpression =
                    Math.pow(Math.E, exponentNumerator / exponentDenominator);
            double kernelValue = (eExpression / (2 * Math.PI * sigma * sigma));

            // Set the cell the to kernel value.
            kernel[x + radius][y + radius] = kernelValue;

            // The sum of all the kernel values, should be close to one.
            sum += kernelValue;
         }
      }

      // Normalize the kernel, ensuring all values add up to one.
      for (int y = 0; y < kernelWidth; y++)
      {
         for (int x = 0; x < kernelWidth; x++)
         {
            kernel[x][y] /= sum;
         }
      }

      // Set the new pixel values, ignore the edges.
      for (int y = radius; y < inputImg.getHeight() - radius; y++)
      {
         for (int x = radius; x < inputImg.getWidth() - radius; x++)
         {
            Color colorsOfPixel = new Color(inputImg.getRGB(x, y));

            // Initialize the RGB values.
            double redValue = 0;
            double greenValue = 0;
            double blueValue = 0;

            // Find the new RBG values from the kernel.
            for (int kernelY = -radius; kernelY <= radius; kernelY++)
            {
               for (int kernelX = -radius; kernelX <= radius; kernelX++)
               {
                  double kernelValue =
                          kernel[kernelX + radius][kernelY + radius];
                  Color colors = new Color
                          (inputImg.getRGB(x - kernelX, y - kernelY));

                  redValue += colors.getRed() * kernelValue;
                  greenValue += colors.getGreen() * kernelValue;
                  blueValue += colors.getBlue() * kernelValue;
               }
            }

            int RGB = (colorsOfPixel.getAlpha() << 24) |
                    ((int) redValue << 16) | ( (int) greenValue << 8) |
                    (int) blueValue;

            result.setRGB(x, y, RGB);
         }
      }

      return result;
   }

   /**
    * A private helper method that converts an image to GreyScale
    * (black and white) based on a file path.
    *
    * @param imageFile        The file path to for the image.
    * @return                 Returns the image as a GreyScale version.
    */
   private static BufferedImage convertToGreyScale(File imageFile)
   {
      BufferedImage result = null;

      // Read the contents of the image.
      try
      {
         result = ImageIO.read(imageFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      // Loop through each of the pixels.
      for (int y = 0; y < result.getHeight(); y++)
      {
         for (int x = 0; x < result.getWidth(); x++)
         {
            // Find the average of each of the colors
            Color colors = new Color(result.getRGB(x, y));
            int greyAverage = (colors.getRed() + colors.getBlue() +
                    colors.getGreen()) / 3;

            // Convert the averages into an RGB value.
            int RGB = (colors.getAlpha() << 24) | (greyAverage << 16) |
                    (greyAverage << 8) | greyAverage;

            // Set the RBG value of the individual pixel.
            result.setRGB(x, y, RGB);
         }
      }
      return result;
   }

   /**
    * A private helper method that converts of BufferedImage to GreyScale
    * (black and white).
    *
    * @param image        The BufferedImage to be converted.
    * @return             Returns a GreyScale version of the argument.
    */
   private static BufferedImage convertToGreyScale(BufferedImage image)
   {
      BufferedImage result = image;

      // Loop through each of the pixels.
      for (int y = 0; y < result.getHeight(); y++)
      {
         for (int x = 0; x < result.getWidth(); x++)
         {
            // Find the average of each of the colors
            Color colors = new Color(result.getRGB(x, y));
            int greyAverage = (colors.getRed() + colors.getBlue() +
                    colors.getGreen()) / 3;

            // Convert the averages into an RGB value.
            int RGB = (colors.getAlpha() << 24) | (greyAverage << 16) |
                    (greyAverage << 8) | greyAverage;

            // Set the RBG value of the individual pixel.
            result.setRGB(x, y, RGB);
         }
      }
      return result;
   }
}
