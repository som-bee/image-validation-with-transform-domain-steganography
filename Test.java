import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.Color;
import javax.imageio.ImageIO;


public class Test {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(ImgOperation.getBitArrayFromColor(128)));
        System.out.println("FIGERPRINT IMAGE");
        BufferedImage bufferedSecretImg = null;
        try {
            bufferedSecretImg = ImageIO.read(new File("secret-images/thumb-32x32.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.asList(GenerateFragments.generateFragmentCoordinates(bufferedSecretImg)));
    }
}
