import java.util.*;

public class MovieDatabase {
    private static HashMap<String,Movie> ourMovies;
    private static String loadedFile = null;

    public static void initialize(String moviefile) {
        if (ourMovies == null || !moviefile.equals(loadedFile)) {
            loadMovies(moviefile);
            loadedFile = moviefile;
        }
    }

    private static void initialize() {
        if (ourMovies == null) loadMovies("ratedmoviesfull.csv");
    }

    private static void loadMovies(String filename) {
        ourMovies = new HashMap<>();
        FirstRatings fr = new FirstRatings();
        for (Movie m : fr.loadMovies(filename)) {
            ourMovies.put(m.getID(), m);
        }
    }

    public static boolean containsID(String id) { initialize(); return ourMovies.containsKey(id); }
    public static int size() { initialize(); return ourMovies.size(); }
    public static String getTitle(String id) { initialize(); return ourMovies.get(id).getTitle(); }
    public static int getYear(String id) { initialize(); return ourMovies.get(id).getYear(); }
    public static String getGenres(String id) { initialize(); return ourMovies.get(id).getGenres(); }
    public static String getDirector(String id) { initialize(); return ourMovies.get(id).getDirector(); }
    public static String getCountry(String id) { initialize(); return ourMovies.get(id).getCountry(); }
    public static int getMinutes(String id) { initialize(); return ourMovies.get(id).getMinutes(); }
    public static Movie getMovie(String id) { initialize(); return ourMovies.get(id); }

    public static ArrayList<String> filterBy(Filter f) {
        initialize();
        ArrayList<String> res = new ArrayList<>();
        for (String id : ourMovies.keySet()) if (f.satisfies(id)) res.add(id);
        return res;
    }
}

