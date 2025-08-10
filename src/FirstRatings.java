import java.io.IOException;
import java.util.*;

public class FirstRatings {

    // Change these if your CSVs are somewhere else:
    private static final String MOVIES_FILE  = "ratedmoviesfull.csv";
    private static final String RATINGS_FILE = "ratings.csv";

    public ArrayList<Movie> loadMovies(String filename) {
        ArrayList<Movie> movies = new ArrayList<>();
        try (SimpleCSV csv = new SimpleCSV(filename)) {
            for (var row : csv) {
                String id       = row.get("id").trim();
                String title    = row.get("title").trim();
                int year        = parseIntSafe(row.get("year"));
                String country  = row.get("country").trim();
                String genres   = row.containsKey("genre") ? row.get("genre").trim()
                        : row.getOrDefault("genres", "").trim(); // support either header
                String director = row.get("director").trim();
                int minutes     = parseIntSafe(row.get("minutes"));
                String poster   = row.get("poster").trim();
                movies.add(new Movie(id, title, year, genres, director, country, minutes, poster));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    public ArrayList<Rater> loadRaters(String filename) {
        Map<String,Rater> map = new HashMap<>();
        try (SimpleCSV csv = new SimpleCSV(filename)) {
            for (var row : csv) {
                String raterId = row.get("rater_id").trim();
                String movieId = row.get("movie_id").trim();
                double rating  = Double.parseDouble(row.get("rating").trim());
                Rater r = map.computeIfAbsent(raterId, Rater::new);
                r.addRating(movieId, rating);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(map.values());
    }

    // ====== Assignment "test" methods ======

    public void testLoadMovies() {
        ArrayList<Movie> movies = loadMovies(MOVIES_FILE);
        System.out.println("Total movies: " + movies.size()); // should be 3143

        int comedy = countGenre(movies, "Comedy");
        int over150 = countLongerThan(movies, 150);
        Map<String,Integer> dirCounts = directorCounts(movies);
        int maxDir = maxValue(dirCounts);
        ArrayList<String> topDirs = keysWithValue(dirCounts, maxDir);

        System.out.println("Comedy movies: " + comedy);            // 960
        System.out.println("Movies >150 minutes: " + over150);     // 132
        System.out.println("Max movies by one director: " + maxDir); // 23
        System.out.println("Director with most movies: " + topDirs.get(0)); // Woody Allen
    }

    public void testLoadRaters() {
        ArrayList<Rater> raters = loadRaters(RATINGS_FILE);
        System.out.println("Total raters: " + raters.size()); // should be 1048

        String targetRater = "193";
        String targetMovie = "1798709";

        int ratingsBy193 = 0, maxRatings = 0, ratingsForMovie = 0;
        ArrayList<String> topRaters = new ArrayList<>();
        Set<String> uniqueMovies = new HashSet<>();

        for (Rater r : raters) {
            if (r.getID().equals(targetRater)) ratingsBy193 = r.numRatings();
            if (r.numRatings() > maxRatings) { maxRatings = r.numRatings(); topRaters.clear(); topRaters.add(r.getID()); }
            else if (r.numRatings() == maxRatings) { topRaters.add(r.getID()); }
            if (r.hasRating(targetMovie)) ratingsForMovie++;
            uniqueMovies.addAll(r.getItemsRated());
        }

        System.out.println("Rater 193 ratings: " + ratingsBy193);            // 119
        System.out.println("Max ratings by any rater: " + maxRatings);       // 314
        System.out.println("Rater(s) with most ratings: " + topRaters);      // [735]
        System.out.println("Ratings for movie 1798709: " + ratingsForMovie); // 38
        System.out.println("Unique movies rated: " + uniqueMovies.size());   // 3143
    }

    // ====== helpers ======
    private int countGenre(ArrayList<Movie> movies, String token) {
        int c = 0; for (Movie m : movies) if (m.getGenres().contains(token)) c++; return c;
    }
    private int countLongerThan(ArrayList<Movie> movies, int min) {
        int c = 0; for (Movie m : movies) if (m.getMinutes() > min) c++; return c;
    }
    private Map<String,Integer> directorCounts(ArrayList<Movie> movies) {
        Map<String,Integer> counts = new HashMap<>();
        for (Movie m : movies) {
            for (String d : m.getDirector().split(",")) {
                String key = d.trim();
                if (!key.isEmpty()) counts.put(key, counts.getOrDefault(key, 0) + 1);
            }
        }
        return counts;
    }
    private int maxValue(Map<String,Integer> map) {
        int max = 0; for (int v : map.values()) max = Math.max(max, v); return max;
    }
    private ArrayList<String> keysWithValue(Map<String,Integer> map, int v) {
        ArrayList<String> out = new ArrayList<>();
        for (var e : map.entrySet()) if (e.getValue() == v) out.add(e.getKey());
        return out;
    }
    private int parseIntSafe(String s) { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; } }
}
