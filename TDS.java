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

    //Taking cover image and the image file that will be hidden
    public static void addWatermark(File coverImage, File fPrint, File stegoCover){




        addSeparator();
        System.out.println("FIGERPRINT IMAGE");
        //ArrayList<Boolean[][]> fPrintPixelArray = getPixelArray(fPrint);
        
        //System.out.println("fingerprint image pixel array size: " + fPrintPixelArray.size());
        addSeparator();



        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(coverImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        System.out.println("COVER IMAGE");
        System.out.println("width: " + width + ", height: " + height);


    }


    //add separator in between console outputs
    private static void addSeparator() {
        System.out.println("#################################################################################");
    }

}
