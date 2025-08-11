import java.util.*;

/**
 * Runner for Step 4 (Similar Ratings).
 * - Keeps your original no-arg methods (same prints), but delegates to
 *   parameterized versions so SimilarRunner can call with filenames/ids.
 * - Uses inclusive filters and prints only the TOP recommendation (like yours).
 */
public class MovieRunnerSimilarRatings {

    // ---------- shared helpers ----------
    private void initDBs(String ratingsFile, String moviesFile) {
        RaterDatabase.initialize(ratingsFile);
        System.out.println("read data for " + RaterDatabase.size() + " raters");
        MovieDatabase.initialize(moviesFile);
        System.out.println("read data for " + MovieDatabase.size() + " movies");
    }

    private void printTopOrNone(ArrayList<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            System.out.println("(no result)");
            return;
        }
        Rating r = ratings.get(0);
        System.out.println(MovieDatabase.getTitle(r.getItem()) + "\t" + r.getValue());
    }

    // ===============================================================
    // Your original no-arg methods (now delegate to the parameterized)
    // ===============================================================

    public void printAverageRatings() {
        printAverageRatings("ratedmoviesfull.csv", "ratings.csv", 35);
    }

    public void printAverageRatingsByYearAfterAndGenre() {
        YearAfterFilter yaf = new YearAfterFilter(1990);
        GenreFilter gf = new GenreFilter("Drama");
        AllFilters af = new AllFilters();
        af.addFilter(yaf);
        af.addFilter(gf);
        printAverageRatingsByFilter("ratedmoviesfull.csv", "ratings.csv", 8, af);
    }

    public void printSimilarRatings() {
        printSimilarRatings("ratedmoviesfull.csv", "ratings.csv", "71", 20, 5);
    }

    public void printSimilarRatingsByGenre() {
        printSimilarRatingsByGenre("ratedmoviesfull.csv", "ratings.csv", "964", "Mystery", 20, 5);
    }

    public void printSimilarRatingsByDirector() {
        String dirs = "Clint Eastwood,J.J. Abrams,Alfred Hitchcock,Sydney Pollack,David Cronenberg,Oliver Stone,Mike Leigh";
        printSimilarRatingsByDirector("ratedmoviesfull.csv", "ratings.csv", "120", dirs, 10, 2);
    }

    public void printSimilarRatingsByGenreAndMinutes() {
        printSimilarRatingsByGenreAndMinutes("ratedmoviesfull.csv", "ratings.csv",
                "168", "Drama", 80, 160, 10, 3);
    }

    public void printSimilarRatingsByYearAfterAndMinutes() {
        printSimilarRatingsByYearAfterAndMinutes("ratedmoviesfull.csv", "ratings.csv",
                "314", 1975, 70, 200, 10, 5);
    }

    public void test() {
        // keep your stub
        FourthRatings fr = new FourthRatings();
        RaterDatabase.initialize("ratings_test.csv");
    }

    // ===============================================================
    // Parameterized versions (these are what your SimilarRunner calls)
    // ===============================================================

    public void printAverageRatings(String moviefile, String ratingsfile, int minimalRaters) {
        FourthRatings fr = new FourthRatings();
        initDBs(ratingsfile, moviefile);
        ArrayList<Rating> ar = fr.getAverageRatings(minimalRaters);
        System.out.println("found " + ar.size() + " movies");
        Collections.sort(ar);
        for (Rating r : ar) {
            System.out.println(r.getValue() + "\t" + MovieDatabase.getTitle(r.getItem()));
            break; // show top only (same as your style)
        }
    }

    public void printAverageRatingsByFilter(String moviefile, String ratingsfile,
                                            int minimalRaters, Filter filterCriteria) {
        FourthRatings fr = new FourthRatings();
        initDBs(ratingsfil