import java.util.*;

/** Runner for Step Two: prints average ratings and looks up a single movie’s average. */
public class MovieRunnerAverage {

    /** Print all movies with >= minimalRaters ratings, sorted by average (low → high). */
    public void printAverageRatings() {
        // Change these to "ratedmoviesfull.csv" & "ratings.csv" when you’re ready
        SecondRatings sr = new SecondRatings("ratedmovies_short.csv", "ratings_short.csv");

        System.out.println("Movies: " + sr.getMovieSize());
        System.out.println("Raters: " + sr.getRaterSize());

        int minimalRaters = 3;  // per spec example, 3 for the short files
        ArrayList<Rating> avgs = sr.getAverageRatings(minimalRaters);
        Collections.sort(avgs); // Rating.compareTo sorts by value ascending

        for (Rating r : avgs) {
            String title = sr.getTitle(r.getItem());
            System.out.printf(java.util.Locale.US, "%.2f %s%n", r.getValue(), title);
        }
    }

    /** Print the average rating for exactly one movie title. */
    public void getAverageRatingOneMovie() {
        // Change to full files if you like: "ratedmoviesfull.csv", "ratings.csv"
        SecondRatings sr = new SecondRatings("ratedmovies_short.csv", "ratings_short.csv");

        String title = "The Godfather";  // you can change this to any exact title in your CSV
        int minimalRaters = 3;

        String id = sr.getID(title);
        if ("NO SUCH TITLE.".equals(id)) {
            System.out.println("NO SUCH TITLE.");
            return;
        }

        // Find the movie’s average from the list we’re allowed to use (>= minimalRaters)
        ArrayList<Rating> avgs = sr.getAverageRatings(minimalRaters);
        Double found = null;
        for (Rating r : avgs) {
            if (r.getItem().equals(id)) {
                found = r.getValue();
                break;
            }
        }

        if (found == null) {
            System.out.println("Not enough ratings for \"" + title + "\" (needs at least " + minimalRaters + ").");
        } else {
            System.out.printf(java.util.Locale.US, "Average for \"%s\": %.2f%n", title, found);
        }
    }
}

