import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // cover image file
        File coverImg = new File("cover-img/cover-2048.png");

        // fingerprint file
        File tPrint = new File("secret-images/thumb-32x32.png");


        //Output File
        File stegoCover = new File("stego-output/stego-cover.png");


        //hiding the fingerprint file as a secret inside the cover image file using transform domain steganography
        TDS.addWatermark(coverImg, tPrint, stegoCover);

    }

}
