import java.util.*;

public class SimilarRunner {

    private static void printTop(String label, ArrayList<Rating> recs) {
        if (recs == null || recs.isEmpty()) {
            System.out.println(label + ": (no result)");
            return;
        }
        Rating top = recs.get(0);
        System.out.println(label + ": " + MovieDatabase.getTitle(top.getItem()));
    }

    
}

