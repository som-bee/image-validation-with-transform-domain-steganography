import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.Color;
import javax.imageio.ImageIO;

public class ImgOperation {

    // getting color array from image -> [y][x][0/1/2] -> r/g/b
    public static Integer[][][] getColorArrayFromImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        Integer[][][] colorArray = new Integer[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Retrieving contents of a pixel
                int pixel = bufferedImage.getRGB(x, y);

                // Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                // Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                colorArray[y][x][0] = red;
                colorArray[y][x][1] = green;
                colorArray[y][x][2] = blue;

            }
        }

        return colorArray;

    }

    public static boolean[][][][] getBitArrayFromImage(BufferedImage bufferedImage){
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        boolean[][][][] bitArray = new boolean[height][width][3][8];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Retrieving contents of a pixel
                int pixel = bufferedImage.getRGB(x, y);

                // Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                // Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                bitArray[y][x][0]=getBitArrayFromColor(red);
                bitArray[y][x][1]=getBitArrayFromColor(green);
                bitArray[y][x][2]=getBitArrayFromColor(blue);
                

            }
        }

        return bitArray;
    }


    public static boolean[] getBitArrayFromColor(int color) {
        boolean[] bitArray = new boolean[8];
        String colorString =Integer.toBinaryString(color);
        int index = 7;
        for (int i=colorString.length()-1; i>=0; i--) {
            bitArray[index]=('1'==colorString.charAt(i));
            //System.out.println(colorArr[index]+" "+colorString.charAt(i));
            index--;
        }


        return bitArray;
    }

    public static BufferedImage getImageFromColorArray(Integer[][][] colorArray) {

        int width = colorArray[0].length;
        int height = colorArray.length;

        BufferedImage bufferedImage = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);
  
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Retrieving contents of a pixel
                int pixel = bufferedImage.getRGB(x, y);

                
                // Retrieving the R G B values
                int red = colorArray[y][x][0];
                int green = colorArray[y][x][1];
                int blue = colorArray[y][x][2];

                 // // Creating new Color object
                 Color color = new Color(red, green, blue);


                  // // Setting new Color object to the image
                  bufferedImage.setRGB(x, y, color.getRGB());


            }
        }

        
        return bufferedImage;
    }

    public static BufferedImage getBufferedImageFromBitArray(boolean[][][][] bitArray) {
        int width = bitArray.length;
        int height = bitArray[0].length;
        BufferedImage bufferedImage = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);
       
       

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
               

            
                // Retrieving the R G B values
                int red = getColorFromBitArray(bitArray[y][x][0]);
                int green = getColorFromBitArray(bitArray[y][x][1]);
                int blue = getColorFromBitArray(bitArray[y][x][2]);
                

                // // Creating new Color object
                Color color = new Color(red, green, blue);
                // // Setting new Color object to the image
                bufferedImage.setRGB(x, y, color.getRGB());

            }
        }

        return bufferedImage;
    }

    private static int getColorFromBitArray(boolean[] bitArray) {

       int color=0;
       for(int i = 0; i < bitArray.length; i++) {
        if(bitArray[i]){
            color += (int)Math.pow(2, 7-i);
        }
       }

        return color;
    }
}
