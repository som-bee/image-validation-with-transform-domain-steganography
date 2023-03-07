import java.io.File;
import java.util.HashMap;
import java.awt.image.BufferedImage;

public class GenerateFragments {
    //getting the fragment coordinates of an image
    // fragment no -> start/end -> x/y coordinates
    public static HashMap<String, HashMap<String, HashMap<String, Integer>>> generateFragmentCoordinates(BufferedImage bufferedImage) {
        HashMap<String, HashMap<String, HashMap<String, Integer>>> fragCoordinates = 
        new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
        int xs = 0;
        int ys = 0;
        int xe = bufferedImage.getWidth()-1;
        int ye = bufferedImage.getHeight()-1;

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

    public static void generateFragment(){

    } 
}
