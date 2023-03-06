import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // cover image file
        File coverImg = new File("cover-img/cover-enlarged.png");

        // fingerprint file
        File tPrint = new File("thumb-images/thumb-shorten.png");


        //Output File
        File stegoCover = new File("stego-output/stego-cover.png");

        TDS.addWatermark(coverImg, tPrint, stegoCover);

    }

}
