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

    public static boolean[][][][] getBitArrayFromImage(BufferedImage bufferedImage) {
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
                bitArray[y][x][0] = getBitArrayFromColor(red);
                bitArray[y][x][1] = getBitArrayFromColor(green);
                bitArray[y][x][2] = getBitArrayFromColor(blue);

            }
        }

        return bitArray;
    }

    public static boolean[] getBitArrayFromColor(int color) {
        boolean[] bitArray = new boolean[8];
        String colorString = Integer.toBinaryString(color);
        int index = 7;
        for (int i = colorString.length() - 1; i >= 0; i--) {
            bitArray[index] = ('1' == colorString.charAt(i));
            // System.out.println(colorArr[index]+" "+colorString.charAt(i));
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

        int color = 0;
        for (int i = 0; i < bitArray.length; i++) {
            if (bitArray[i]) {
                color += (int) Math.pow(2, 7 - i);
            }
        }

        return color;
    }

    // getiing all regions
    public static HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> getCoordinatesFromCoverImg(
            int[] imgCoordinates) {

        // region no -> segment no -> start/end -> x/y coordinates
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> regions = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>>();

        int xs = imgCoordinates[0];
        int ys = imgCoordinates[1];
        int xe = imgCoordinates[2];
        int ye = imgCoordinates[3];

        regions.put("region-1", getRegionCoordinates(new int[] { xs, ys, xe / 2, ye / 2 }));
        regions.put("region-2", getRegionCoordinates(new int[] { xe / 2 + 1, ys, xe, ye / 2 }));
        regions.put("region-3", getRegionCoordinates(new int[] { xs, ye / 2 + 1, xe / 2, ye }));
        regions.put("region-4", getRegionCoordinates(new int[] { xe / 2 + 1, ye / 2 + 1, xe, ye }));

        return regions;
    }

    // getting all segments
    private static HashMap<String, HashMap<String, HashMap<String, Integer>>> getRegionCoordinates(
            int[] regionCoordinates) {
        HashMap<String, HashMap<String, HashMap<String, Integer>>> region = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();

        int xs = regionCoordinates[0];
        int ys = regionCoordinates[1];
        int xe = regionCoordinates[2];
        int ye = regionCoordinates[3];

        region.put("segment-1", getSegmentCoordinates(new int[] { xs, ys, xe, ys + (ye - ys) / 4 }));
        region.put("segment-2",
                getSegmentCoordinates(new int[] { xs, ys + (ye - ys) / 4 + 1, xe, ys + (ye - ys) / 2 }));
        region.put("segment-3",
                getSegmentCoordinates(new int[] { xs, ys + (ye - ys) / 2 + 1, xe, ys + ((ye - ys) * 3) / 4 }));
        region.put("segment-4", getSegmentCoordinates(new int[] { xs, ys + ((ye - ys) * 3) / 4 + 1, xe, ye }));

        return region;
    }

    // getting start and end coordinates
    private static HashMap<String, HashMap<String, Integer>> getSegmentCoordinates(int[] segmentCoordinates) {

        HashMap<String, HashMap<String, Integer>> segment = new HashMap<String, HashMap<String, Integer>>();

        int xs = segmentCoordinates[0];
        int ys = segmentCoordinates[1];
        int xe = segmentCoordinates[2];
        int ye = segmentCoordinates[3];

        HashMap<String, Integer> start = new HashMap<String, Integer>();
        start.put("x", xs);
        start.put("y", ys);

        HashMap<String, Integer> end = new HashMap<String, Integer>();
        end.put("x", xe);
        end.put("y", ye);

        segment.put("start", start);
        segment.put("end", end);

        return segment;
    }

    // getting the fragment coordinates of an image
    // fragment no -> start/end -> x/y coordinates
    public static HashMap<String, HashMap<String, HashMap<String, Integer>>> generateFragmentCoordinates(
            BufferedImage bufferedImage) {
        HashMap<String, HashMap<String, HashMap<String, Integer>>> fragCoordinates = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
        int xs = 0;
        int ys = 0;
        int xe = bufferedImage.getWidth() - 1;
        int ye = bufferedImage.getHeight() - 1;

        fragCoordinates.put("fragment-1", getFragmentCoordinates(new int[] { xs, ys, xe / 2, ye / 2 }));
        fragCoordinates.put("fragment-2", getFragmentCoordinates(new int[] { xe / 2 + 1, ys, xe, ye / 2 }));
        fragCoordinates.put("fragment-3", getFragmentCoordinates(new int[] { xs, ye / 2 + 1, xe / 2, ye }));
        fragCoordinates.put("fragment-4", getFragmentCoordinates(new int[] { xe / 2 + 1, ye / 2 + 1, xe, ye }));

        return fragCoordinates;
    }

    private static HashMap<String, HashMap<String, Integer>> getFragmentCoordinates(int[] fragCord) {
        HashMap<String, HashMap<String, Integer>> fragment = new HashMap<String, HashMap<String, Integer>>();

        int xs = fragCord[0];
        int ys = fragCord[1];
        int xe = fragCord[2];
        int ye = fragCord[3];

        HashMap<String, Integer> start = new HashMap<String, Integer>();
        start.put("x", xs);
        start.put("y", ys);

        HashMap<String, Integer> end = new HashMap<String, Integer>();
        end.put("x", xe);
        end.put("y", ye);

        fragment.put("start", start);
        fragment.put("end", end);

        return fragment;
    }
}
