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

        for (int reg = 1; reg < 4; reg++) {
            for (int seg = 1; seg <= 4; seg++) {
                System.out.println("region : " + reg + ", seg:" + seg);
                ArrayList<Integer[][]> pixelArr = getPixelArrayFromSegment(colorArrCover, coverCoordinates, reg, seg);
                System.out.println(pixelArr.size());

                 //hideBitArrInPixelArr(pixelArr, bitArrSecretImg, segmentsScrtImg);

                updateColorArrayFromPixelArray(pixelArr, colorArrCover, coverCoordinates, reg, seg);

            }
        }
        //

        // getting the bufferd image from color array
         bufferedCoverImage = ImgOperation.getImageFromColorArray(colorArrCover);
        // Saving the modified image
        try {
        ImageIO.write(bufferedCoverImage, "png", stegoCover);
        } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        System.out.println("Image Steganography Done...");

    }

    private static void updateColorArrayFromPixelArray(ArrayList<Integer[][]> pixelArr, Integer[][][] colorArrCover,
            HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> coverCoordinates, int reg,
            int seg) {

        int curColor = 0;

        // segment coordinates
        int xs = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("start").get("x");
        int ys = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("start").get("y");
        int xe = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("end").get("x");
        int ye = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("end").get("y");
        int width = xe - xs + 1;
        int curIndex=0;
        for (int y = ys; y <= ye && curIndex<pixelArr.size();) {
            for (int x = xs; x <= xe && curIndex<pixelArr.size();) {
                Integer[][] arr = pixelArr.get(curIndex++);
                colorArrCover[y][x][curColor]=arr[0][0];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor]=arr[0][1];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor]=arr[1][0];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor]=arr[1][1];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }

            }

        }
    }

    private static void hideBitArrInPixelArr(ArrayList<Integer[][]> pixelArr, boolean[][][][] bitArrSecretImg,
            HashMap<String, HashMap<String, HashMap<String, Integer>>> segmentsScrtImg) {

        int curBit = 0;


    }

    private static ArrayList<Integer[][]> getPixelArrayFromSegment(Integer[][][] colorArrCover,
            HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> coverCoordinates, int reg,
            int seg) {

        ArrayList<Integer[][]> pixelArr = new ArrayList<>();

        int curColor = 0;

        // segment coordinates
        int xs = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("start").get("x");
        int ys = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("start").get("y");
        int xe = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("end").get("x");
        int ye = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("end").get("y");
        int width = xe - xs + 1;

        for (int y = ys; y <= ye;) {
            for (int x = xs; x <= xe;) {
                int c1 = colorArrCover[y][x][curColor];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return pixelArr;
                }
                int c2 = colorArrCover[y][x][curColor];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return pixelArr;
                }
                int c3 = colorArrCover[y][x][curColor];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return pixelArr;
                }
                int c4 = colorArrCover[y][x][curColor];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return pixelArr;
                }
                Integer[][] arr = new Integer[][] { { c1, c2 }, { c3, c4 } };

                pixelArr.add(arr);

            }

        }

        return pixelArr;
    }

    private static void hideBitArrInColorArr(ArrayList<Integer[][]> pixelArray, boolean[][][][] bitArrSecretImg) {

    }

    // add separator in between console outputs
    private static void addSeparator() {
        System.out.println("#################################################################################");
    }

}
