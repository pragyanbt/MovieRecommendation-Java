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
        initDBs(ratingsfile, moviefile);
        ArrayList<Rating> ar = fr.getAverageRatingsByFilter(minimalRaters, filterCriteria);
        System.out.println("found " + ar.size() + " movies");
        Collections.sort(ar);
        for (Rating r : ar) {
            System.out.println(r.getValue() + "\t" + MovieDatabase.getYear(r.getItem())
                    + "\t" + MovieDatabase.getTitle(r.getItem()) + "\n\t"
                    + MovieDatabase.getGenres(r.getItem()));
            break;
        }
    }

    public void printSimilarRatings(String moviefile, String ratingsfile,
                                    String raterID, int numSimilar, int minimalRaters) {
        FourthRatings fr = new FourthRatings();
        initDBs(ratingsfile, moviefile);
        ArrayList<Rating> ratings = fr.getSimilarRatings(raterID, numSimilar, minimalRaters);
        printTopOrNone(ratings);
    }

    public void printSimilarRatingsByGenre(String moviefile, String ratingsfile,
                                           String raterID, String genre,
                                           int numSimilar, int minimalRaters) {
        FourthRatings fr = new FourthRatings();
        initDBs(ratingsfile, moviefile);
        ArrayList<Rating> ratings = fr.getSimilarRatingsByFilter(
                raterID, numSimilar, minimalRaters, new GenreFilter(genre));
        if (ratings == null || ratings.isEmpty()) {
            System.out.println("(no result)");
            return;
        }
        Rating r = ratings.get(0);
        System.out.println(MovieDatabase.getTitle(r.getItem()) + "\t" + r.getValue()
                + "\n" + MovieDatabase.getGenres(r.getItem()));
    }

    public void printSimilarRatingsByDirector(String moviefile, String ratingsfile,
                                              String raterID, String directorsCsv,
                                              int numSimilar, int minimalRaters) {
        FourthRatings fr = new FourthRatings();
        initDBs(ratingsfile, moviefile);
        ArrayList<Rating> ratings = fr.getSimilarRatingsByFilter(
                raterID, numSimilar, minimalRaters, new DirectorsFilter(directorsCsv));
        if (ratings == null || ratings.isEmpty()) {
            System.out.println("(no result)");
            return;
        }
        Rating r = ratings.get(0);
        System.out.println(MovieDatabase.getTitle(r.getItem()) + "\t" + r.getValue()
                + "\n" + MovieDatabase.getDirector(r.getItem()));
    }

    public void printSimilarRatingsByGenreAndMinutes(String moviefile, String ratingsfile,
                                                     String raterID, String genre,
                                                     int minMinutes, int maxMinutes,
                                                     int numSimilar, int minimalRaters) {
        FourthRatings fr = new FourthRatings();
        initDBs(ratingsfile, moviefile);
        AllFilters f = new AllFilters();
        f.addFilter(new GenreFilter(genre));
        f.addFilter(new MinutesFilter(minMinutes, maxMinutes)); // inclusive
        ArrayList<Rating> ratings = fr.getSimilarRatingsByFilter(
                raterID, numSimilar, minimalRaters, f);
        if (ratings == null || ratings.isEmpty()) {
            System.out.println("(no result)");
            return;
        }
        Rating r = ratings.get(0);
        System.out.println(MovieDatabase.getTitle(r.getItem()) + "\t"
                + MovieDatabase.getMinutes(r.getItem()) + "min\t" + r.getValue()
                + "\n" + MovieDatabase.getGenres(r.getItem()));
    }

    public void printSimilarRatingsByYearAfterAndMinutes(String moviefile, String ratingsfile,
                                                         String raterID, int yearAfter,
                                                         int minMinutes, int maxMinutes,
                                                         int numSimilar, int minimalRaters) {
        FourthRatings fr = new FourthRatings();
        initDBs(ratingsfile, moviefile);
        AllFilters f = new AllFilters();
        f.addFilter(new YearAfterFilter(yearAfter));           // inclusive
        f.addFilter(new MinutesFilter(minMinutes, maxMinutes)); // inclusive
        ArrayList<Rating> ratings = fr.getSimilarRatingsByFilter(
                raterID, numSimilar, minimalRaters, f);
        if (ratings == null || ratings.isEmpty()) {
            System.out.println("(no result)");
            return;
        }
        Rating r = ratings.get(0);
        System.out.println(MovieDatabase.getTitle(r.getItem()) + "\t"
                + MovieDatabase.getMinutes(r.getItem()) + "min\t" + r.getValue()
                + "\t" + MovieDatabase.getYear(r.getItem()));
    }
}
