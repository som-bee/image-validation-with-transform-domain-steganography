import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Graphics;

import javax.imageio.ImageIO;


public class Test {
    public static void main(String[] args) throws IOException {
        //System.out.println(Arrays.toString(ImgOperation.getBitArrayFromColor(128)));
        // System.out.println("FIGERPRINT IMAGE");
        // BufferedImage bufferedSecretImg = null;
        // try {
        //     bufferedSecretImg = ImageIO.read(new File("secret-images/thumb-32x32.png"));
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        // System.out.println(Arrays.asList(ImgOperation.generateFragmentCoordinates(bufferedSecretImg)));

        System.out.println("TESTING");
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("cover-img/cover-1024.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Graphics g = img.getGraphics();
        g.setFont(g.getFont().deriveFont(30f));
        g.drawString("Hello", 100, 100);
        g.dispose();
        ImageIO.write(img, "png", new File("test/test.png"));
        System.out.println("TESTING DONE...");

    }
}
