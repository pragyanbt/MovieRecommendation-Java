import java.util.*;

public class RaterDatabase {
    private static HashMap<String, Rater> ourRaters;
    private static String loadedFile = null;

    public static void initialize(String ratingsfile) {
        if (ourRaters == null || !ratingsfile.equals(loadedFile)) {
            loadRatings(ratingsfile);
            loadedFile = ratingsfile;
        }
    }

    private static void ensureInit() {
        if (ourRaters == null) loadRatings("ratings.csv");
    }

    private static void loadRatings(String filename) {
        ourRaters = new HashMap<>();
        FirstRatings fr = new FirstRatings();
        for (Rater r : fr.loadRaters(filename)) ourRaters.put(r.getID(), r);
    }

    public static int size() { ensureInit(); return ourRaters.size(); }
    public static Rater getRater(String id) { ensureInit(); return ourRaters.get(id); }
    public static ArrayList<Rater> getRaters() { ensureInit(); return new ArrayList<>(ourRaters.values()); }
}

