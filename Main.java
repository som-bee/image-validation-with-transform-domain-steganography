import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // cover image file
        File coverImg = new File("cover-img/cover-e1-1024.png");

        // fingerprint file
        int size=32;
        File tPrint = new File("secret-images/thumb-"+size+"x"+size+".png");


        //Output File
        File stegoCover = new File("stego-output/stego-cover.png");


        //hiding the fingerprint file as a secret inside the cover image file using transform domain steganography
        TDS.hideSecretImage(coverImg, tPrint, stegoCover);


        //extracting the fingerprint from the stego image file using transform domain steganography
        TDS.extractSecretImage(stegoCover, size);

    }

}
