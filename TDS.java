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

    //

    // Taking cover image and the image file that will be hidden
    public static void hideSecretImage(File coverImage, File fPrint, File stegoCover) {

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

                hideBitArrInPixelArr(pixelArr, bitArrSecretImg, segmentsScrtImg);

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
        int curIndex = 0;
        for (int y = ys; y <= ye && curIndex < pixelArr.size();) {
            for (int x = xs; x <= xe && curIndex < pixelArr.size();) {
                Integer[][] arr = pixelArr.get(curIndex++);
                colorArrCover[y][x][curColor] = arr[0][0];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor] = arr[0][1];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor] = arr[1][0];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor] = arr[1][1];
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

        int[][][] bitMatrixArr = new int[(bitArrSecretImg.length * bitArrSecretImg[0].length * 3 * 8) / 4][2][2];

        for (int frag = 1; frag <= 4; frag++) {
            int xs = segmentsScrtImg.get("fragment-" + frag).get("start").get("x");
            int ys = segmentsScrtImg.get("fragment-" + frag).get("start").get("y");
            int xe = segmentsScrtImg.get("fragment-" + frag).get("end").get("x");
            int ye = segmentsScrtImg.get("fragment-" + frag).get("end").get("y");
            int curPosition = 0;
            for (int y = ys; y <= ye && curPosition < bitMatrixArr.length; y++) {
                for (int x = xs; x <= xe && curPosition < bitMatrixArr.length; x++) {
                    for (int color = 0; color < 3; color++) {
                        for (int bit = 0; bit < 8; bit++) {

                            bitMatrixArr[curPosition++][(frag - 1) / 2][(frag - 1)
                                    % 2] = (bitArrSecretImg[y][x][color][bit]) ? 1 : 0;
                            // color manupulation using lsb
                            // int curBit = (bitArrSecretImg[y][x][color][bit]) ? 1 : 0;
                            // int curColor = pixelArr.get(curPixel)[(frag - 1) / 2][(frag - 1) % 2];

                            // // lsb
                            // if (curBit == 1) {
                            // curColor = curColor | 1;
                            // } else {
                            // curColor = curColor & 254;
                            // }
                            // // test
                            // // curColor=curBit;

                            // pixelArr.get(curPixel)[(frag - 1) / 2][(frag - 1) % 2] = curColor;
                            // curPixel += 1;
                        }
                    }

                }
            }

        }

        performTDS(bitMatrixArr, pixelArr);

    }

    /*
     * 1.
     * A1’ = (a1+a2)/2 A2’ = (a1-a2)/2
     * A3’ = (a3-a4) A4’ = a4
     * 
     * 2.
     * A1’ = (a1+a4)/2 A2’ =(a1-a4)/2
     * A3’ = (a2+a3)/2 A4’ =(a2-a3)/2
     * 
     * 3.
     * A1’ = (a1+a3)/2 A2’ =(a2+a4)/2
     * A3’ = (a1-a3)/2 A4’ =(a2-a4)/2
     * 
     */

    private static void performTDS(int[][][] bitMatrixArr, ArrayList<Integer[][]> pixelArr) {
        int curPixel = 0;

        for (int curSquare = 0; curSquare < bitMatrixArr.length; curSquare++) {

            int a1, a2, a3, a4;
            a1 = formatColor(pixelArr.get(curPixel)[0][0]);
            a2 = formatColor(pixelArr.get(curPixel)[0][1]);
            a3 = formatColor(pixelArr.get(curPixel)[1][0]);
            a4 = formatColor(pixelArr.get(curPixel)[1][1]);



            // Use equations to transform a1, a2, a3, a4 to A1’, A2’, A3’ and A4’:
            // A1’ = (a1+a3)/2 A2’ =(a2+a4)/2
            // A3’ = (a1-a3)/2 A4’ =(a2-a4)/2
            int A1, A2, A3, A4;
     
            A1 = (a1 + a2) / 2;
            A2 = a2;
            A3 = (a3 + a4) / 2;
            A4 = a4;

            A1 = getAvgOfBounds(A1, 4);
            A2 = getAvgOfBounds(A2, 4);
            A3 = getAvgOfBounds(A3, 4);
            A4 = getAvgOfBounds(A4, 4);

            int R1, R2, R3, R4;
            R1 = hideBitInColor(bitMatrixArr[curSquare][0][0],A1);
            R2 = hideBitInColor(bitMatrixArr[curSquare][0][1],A2);
            R3 = hideBitInColor(bitMatrixArr[curSquare][1][0],A3);
            R4 = hideBitInColor(bitMatrixArr[curSquare][1][1],A4);

            // without forward transformation
            // R1 = hideBitInColor(bitMatrixArr[curSquare][0][0],a1);
            // R2 = hideBitInColor(bitMatrixArr[curSquare][0][1],a2);
            // R3 = hideBitInColor(bitMatrixArr[curSquare][1][0],a3);
            // R4 = hideBitInColor(bitMatrixArr[curSquare][1][1],a4);

            pixelArr.get(curPixel)[0][0] = R1;
            pixelArr.get(curPixel)[0][1] = R2;
            pixelArr.get(curPixel)[1][0] = R3;
            pixelArr.get(curPixel)[1][1] = R4;

            //modifyng this will act as matrix interval
            curPixel += 2;
        }
    }

    private static int formatColor(Integer color) {
        if(color<1){
            color += 1;
        }
        else if(color>254){
            color-=1;
        }
        return color;
    }

    private static int hideBitInColor(int bit, int r) {
        if(bit==1){
            r+=1;
        }
        else{
            r-=1;
        }
        return r;
    }

    private static int getAvgOfBounds(int A1, int val) {
        int left = (A1 / val) * val;
        int right = (A1 / val + 1) * val;

        return (left + right) / 2;
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

  

    // add separator in between console outputs
    private static void addSeparator() {
        System.out.println("#################################################################################");
    }

    public static void extractSecretImage(File stegoCover, int size) {


        // SECRET IMAGE
        addSeparator();
        System.out.println("FIGERPRINT IMAGE");
        System.out.println("width: " + size + ", height: " + size);

        BufferedImage bufferedSecretImg = new BufferedImage(size, size,
       BufferedImage.TYPE_INT_RGB);
        
        
        

        boolean[][][][] bitArrSecretImg = new boolean[size][size][3][8];
        System.out.println(bitArrSecretImg.length * bitArrSecretImg[0].length * 3 + " bytes");

        // segmenting the secret image
        HashMap<String, HashMap<String, HashMap<String, Integer>>> segmentsScrtImg = GenerateFragments
                .generateFragmentCoordinates(bufferedSecretImg);
        System.out.println(Arrays.asList(segmentsScrtImg));
        addSeparator();

         // COVER IAMGE
         BufferedImage bufferedCoverImage = null;
         try {
             bufferedCoverImage = ImageIO.read(stegoCover);
         } catch (IOException e) {
             e.printStackTrace();
         }
 
         int widthCover = bufferedCoverImage.getWidth();
         int heightCover = bufferedCoverImage.getHeight();
 
         System.out.println("STEGO COVER IMAGE");
         System.out.println("width: " + widthCover + ", height: " + heightCover);
         HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> coverCoordinates = GenerateCoordinates
                 .getCoordinatesFromCoverImg(new int[] { 0, 0, widthCover - 1, heightCover - 1 });
         System.out.println(Arrays.asList(coverCoordinates));
         Integer[][][] colorArrCover = ImgOperation.getColorArrayFromImage(bufferedCoverImage);
         // System.out.println(colorArrCover.length+" , "+colorArrCover[0].length);
 
         addSeparator();
 
         System.out.println("TRANSFORM DOMAIN STEGANOGRAPHY");
         // PERFORMING STEGANOGRAPHY (EXTRACTING)
         // colorArrCover --> bitArrSecretImg
 
         for (int reg = 1; reg < 4; reg++) {
             for (int seg = 1; seg <= 4; seg++) {
                 System.out.println("region : " + reg + ", seg:" + seg);
                 ArrayList<Integer[][]> pixelArr = getPixelArrayFromSegment(colorArrCover, coverCoordinates, reg, seg);
                 System.out.println(pixelArr.size());

                extractBitArrFromPixelArray(pixelArr,bitArrSecretImg, segmentsScrtImg);
                 //hideBitArrInPixelArr(pixelArr, bitArrSecretImg, segmentsScrtImg);
 
                 //updateColorArrayFromPixelArray(pixelArr, colorArrCover, coverCoordinates, reg, seg);
 
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

    private static void extractBitArrFromPixelArray(ArrayList<Integer[][]> pixelArr, boolean[][][][] bitArrSecretImg,
            HashMap<String, HashMap<String, HashMap<String, Integer>>> segmentsScrtImg) {
    }

}
