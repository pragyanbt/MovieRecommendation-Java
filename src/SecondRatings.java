import java.util.*;

/** Computes average ratings from the data loaded via FirstRatings. */
public class SecondRatings {
    private final ArrayList<Movie> myMovies;
    private final ArrayList<Rater> myRaters;

    /** Default to the short demo files (handy for quick checks). */
    public SecondRatings() {
        this("ratedmovies_short.csv", "ratings_short.csv");
    }

    /** Use your actual files (e.g., "ratedmoviesfull.csv", "ratings.csv"). */
    public SecondRatings(String moviefile, String ratingsfile) {
        FirstRatings fr = new FirstRatings();
        this.myMovies = fr.loadMovies(moviefile);
        this.myRaters = fr.loadRaters(ratingsfile);
    }

    public int getMovieSize() { return myMovies.size(); }
    public int getRaterSize() { return myRaters.size(); }

    /** Average rating for a movie ID if it has at least minimalRaters ratings; else 0.0 */
    private double getAverageByID(String id, int minimalRaters) {
        int count = 0;
        double sum = 0.0;
        for (Rater r : myRaters) {
            double v = r.getRating(id);
            if (v != -1) {
                sum += v;
                count++;
            }
        }
        return (count >= minimalRaters) ? (sum / count) : 0.0;
    }

    /** Average ratings for every movie with >= minimalRaters ratings (sorted later in runner). */
    public ArrayList<Rating> getAverageRatings(int minimalRaters) {
        ArrayList<Rating> list = new ArrayList<>();
        for (Movie m : myMovies) {
            double avg = getAverageByID(m.getID(), minimalRaters);
            if (avg > 0.0) {
                list.add(new Rating(m.getID(), avg));
            }
        }
        return list;
    }

    /** Get title from movie id, or a helpful not-found message. */
    public String getTitle(String id) {
        for (Movie m : myMovies) {
            if (m.getID().equals(id)) return m.getTitle();
        }
        return "ID not found: " + id;
    }

    /** Get id from exact movie title, or "NO SUCH TITLE". */
    public String getID(String title) {
        for (Movie m : myMovies) {
            if (m.getTitle().equals(title)) return m.getID();
        }
        return "NO SUCH TITLE.";
    }
    // ---- Add to SecondRatings ----

    /** Average rating for a title if it has at least minimalRaters ratings; else returns Double.NaN. */
    public double getAverageRatingByTitle(String title, int minimalRaters) {
        String id = getID(title);
        if ("NO SUCH TITLE.".equals(id)) return Double.NaN;
        int count = 0;
        double sum = 0.0;
        for (Rater r : myRaters) {
            double v = r.getRating(id);
            if (v != -1) { sum += v; count++; }
        }
        return (count >= minimalRaters) ? (sum / count) : Double.NaN;
    }

    /** Number of ratings the given title has. */
    public int getNumRatingsForTitle(String title) {
        String id = getID(title);
        if ("NO SUCH TITLE.".equals(id)) return 0;
        int c = 0;
        for (Rater r : myRaters) if (r.getRating(id) != -1) c++;
        return c;
    }

    /** Count how many movies have at least minimalRaters ratings. */
    public int countMoviesWithAtLeast(int minimalRaters) {
        int total = 0;
        for (Movie m : myMovies) {
            int c = 0;
            for (Rater r : myRaters) if (r.getRating(m.getID()) != -1) c++;
            if (c >= minimalRaters) total++;
        }
        return total;
    }

    /** Among the given titles, return the one with the lowest average with >= minimalRaters; null if none. */
    public String lowestRatedAmongTitles(java.util.List<String> titles, int minimalRaters) {
        String best = null;        // "best" here means lowest average
        double bestAvg = Double.POSITIVE_INFINITY;
        for (String t : titles) {
            double avg = getAverageRatingByTitle(t, minimalRaters);
            if (!Double.isNaN(avg) && avg < bestAvg) {
                bestAvg = avg;
                best = t;
            }
        }
        return best;
    }

}

