import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.Color;
import javax.imageio.ImageIO;

// Transform Domain Steganography

public class TDS {

    // Taking cover image and the image file that will be hidden
    public static void addWatermark(File coverImage, File fPrint, File stegoCover) {

        // SECRET IMAGE
        addSeparator();
        System.out.println("FIGERPRINT IMAGE");
        BufferedImage bufferedSecretImg = null;
        try {
            bufferedSecretImg = ImageIO.read(fPrint);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int widthSecret = bufferedSecretImg.getWidth();
        int heightSecret = bufferedSecretImg.getHeight();
        System.out.println("width: " + widthSecret + ", height: " + heightSecret);

        boolean[][][][] bitArrSecretImg = ImgOperation.getBitArrayFromImage(bufferedSecretImg);
        System.out.println(bitArrSecretImg.length * bitArrSecretImg[0].length * 3 + " bytes");

        // segmenting the secret image
        HashMap<String, HashMap<String, HashMap<String, Integer>>> segmentsScrtImg = GenerateFragments
                .generateFragmentCoordinates(bufferedSecretImg);
        System.out.println(Arrays.asList(segmentsScrtImg));
        addSeparator();

        // COVER IAMGE
        BufferedImage bufferedCoverImage = null;
        try {
            bufferedCoverImage = ImageIO.read(coverImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int widthCover = bufferedCoverImage.getWidth();
        int heightCover = bufferedCoverImage.getHeight();

        System.out.println("COVER IMAGE");
        System.out.println("width: " + widthCover + ", height: " + heightCover);
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> coverCoordinates = GenerateCoordinates
                .getCoordinatesFromCoverImg(new int[] { 0, 0, widthCover - 1, heightCover - 1 });
        System.out.println(Arrays.asList(coverCoordinates));
        Integer[][][] colorArrCover = ImgOperation.getColorArrayFromImage(bufferedCoverImage);
        // System.out.println(colorArrCover.length+" , "+colorArrCover[0].length);

        addSeparator();

        System.out.println("TRANSFORM DOMAIN STEGANOGRAPHY"); 
        // PERFORMING STEGANOGRAPHY
        // colorArrCover <-- bitArrSecretImg
        hideBitArrInColorArr(colorArrCover, bitArrSecretImg);

    }

    private static void hideBitArrInColorArr(Integer[][][] colorArrCover, boolean[][][][] bitArrSecretImg) {


    }

    // add separator in between console outputs
    private static void addSeparator() {
        System.out.println("#################################################################################");
    }

}
