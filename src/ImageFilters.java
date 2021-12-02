import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A class of static methods. Convert images on the machine based on the
 * File path. Image filters include invertColors(), convertToGreyScale(), and
 * gaussianBlur().
 */
public class ImageFilters {

   private final static int MAX_RGB = 255;

   /**
    * Inverts the Colors of an image in the machine based on a file path.
    *
    * @param imageFile        The file path where the image is located.
    */
   public static void invertColors(File imageFile)
   {
      BufferedImage img = null;

      // Read the contents of the image.
      try
      {
         img = ImageIO.read(imageFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      // Loop through each of the pixels.
      for (int y = 0; y < img.getHeight(); y++)
      {
         for (int x = 0; x < img.getWidth(); x++)
         {
            // Find the integer values of the pixels.
            Color colors = new Color(img.getRGB(x, y));
            int alpha = MAX_RGB - colors.getAlpha();
            int red = MAX_RGB - colors.getRed();
            int blue = MAX_RGB - colors.getBlue();
            int green = MAX_RGB - colors.getGreen();

            // Convert the integer values into an RGB value.
            int RGB = (alpha << 24) | (red << 16) | (green << 8) | blue;

            // Set the RBG value of the individual pixel.
            img.setRGB(x, y, RGB);
         }
      }
      // Change the image file data.
      try
      {
         File file = new File(imageFile.getAbsolutePath());
         ImageIO.write(img, "png", file);
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Converts an image in the machine to GreyScale based on a file path.
    *
    * @param imageFile        The filePath where the image is located.
    */
   public static void convertToGreyScale(File imageFile)
   {
      BufferedImage img = null;

      // Read the contents of the image.
      try
      {
         img = ImageIO.read(imageFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      // Loop through each of the pixels.
      for (int y = 0; y < img.getHeight(); y++)
      {
         for (int x = 0; x < img.getWidth(); x++)
         {
            // Find the average of each of the colors
            Color colors = new Color(img.getRGB(x, y));
            int greyAverage = (colors.getRed() + colors.getBlue() +
                    colors.getGreen()) / 3;

            // Convert the averages into an RGB value.
            int RGB = (colors.getAlpha() << 24) | (greyAverage << 16) |
                    (greyAverage << 8) | greyAverage;

            // Set the RBG value of the individual pixel.
            img.setRGB(x, y, RGB);
         }
      }
      // Change the image file data.
      try
      {
         File file = new File(imageFile.getAbsolutePath());
         ImageIO.write(img, "png", file);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Applies Gaussian Blur to an image on the machine based on the file path
    * of the image. The larger the radius, the stronger the blur. Doesn't blur
    * the border of the image.
    *
    * @param radius           The radius of the Gaussian Kernel.
    * @param imageFile        The filePath where the image is located.
    */
   public static void gaussianBlur(int radius, File imageFile)
   {
      BufferedImage inputImg = null;
      BufferedImage outputImg = null;

      // Read the contents of the image.
      try
      {
         inputImg = ImageIO.read(imageFile);
         outputImg = ImageIO.read(imageFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

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

            outputImg.setRGB(x, y, RGB);
         }
      }

      // Change the image file data.
      try
      {
         File file = new File(imageFile.getAbsolutePath());
         ImageIO.write(outputImg, "png", file);
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
