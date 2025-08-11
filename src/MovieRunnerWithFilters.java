import java.util.*;

public class MovieRunnerWithFilters {

    private static void init(String moviefile, String ratingsfile) {
        MovieDatabase.initialize(moviefile);
        ThirdRatings tr = new ThirdRatings(ratingsfile);
        System.out.println("read data for " + tr.getRaterSize() + " raters");
        System.out.println("read data for " + MovieDatabase.size() + " movies");
    }

    public void printAverageRatings(String moviefile, String ratingsfile, int minimalRaters) {
        MovieDatabase.initialize(moviefile);
        ThirdRatings tr = new ThirdRatings(ratingsfile);
        ArrayList<Rating> list = tr.getAverageRatings(minimalRaters);
        System.out.println("found " + list.size() + " movies");
        Collections.sort(list);
        // Uncomment to see titles:
        // for (Rating r : list) System.out.printf(Locale.US, "%.2f %s%n", r.getValue(), MovieDatabase.getTitle(r.getItem()));
    }

    public void printAverageRatingsByYearAfter(String moviefile, String ratingsfile, int minimalRaters, int year) {
        MovieDatabase.initialize(moviefile);
        ThirdRatings tr = new ThirdRatings(ratingsfile);
        ArrayList<Rating> list = tr.getAverageRatingsByFilter(minimalRaters, new YearAfterFilter(year));
        System.out.println("found " + list.size() + " movies");
    }

    public void printAverageRatingsByGenre(String moviefile, String ratingsfile, int minimalRaters, String genre) {
        MovieDatabase.initialize(moviefile);
        ThirdRatings tr = new ThirdRatings(ratingsfile);
        ArrayList<Rating> list = tr.getAverageRatingsByFilter(minimalRaters, new GenreFilter(genre));
        System.out.println("found " + list.size() + " movies");
    }

    public void printAverageRatingsByMinutes(String moviefile, String ratingsfile, int minimalRaters, int min, int max) {
        MovieDatabase.initialize(moviefile);
        ThirdRatings tr = new ThirdRatings(ratingsfile);
        ArrayList<Rating> list = tr.getAverageRatingsByFilter(minimalRaters, new MinutesFilter(min, max));
        System.out.println("found " + list.size() + " movies");
    }

    public void printAverageRatingsByDirectors(String moviefile, String ratingsfile, int minimalRaters, String directorsCsv) {
        MovieDatabase.initialize(moviefile);
        ThirdRatings tr = new ThirdRatings(ratingsfile);
        ArrayList<Rating> list = tr.getAverageRatingsByFilter(minimalRaters, new DirectorsFilter(directorsCsv));
        System.out.println("found " + list.size() + " movies");
    }

    public void printAverageRatingsByYearAfterAndGenre(String moviefile, String ratingsfile, int minimalRaters, int year, String genre) {
        MovieDatabase.initialize(moviefile);
        ThirdRatings tr = new ThirdRatings(ratingsfile);
        AllFilters af = new AllFilters();
        af.addFilter(new YearAfterFilter(year));
        af.addFilter(new GenreFilter(genre));
        ArrayList<Rating> list = tr.getAverageRatingsByFilter(minimalRaters, af);
        System.out.println("found " + list.size() + " movies");
    }

    public void printAverageRatingsByDirectorsAndMinutes(String moviefile, String ratingsfile, int minimalRaters, int min, int max, String directorsCsv) {
        MovieDatabase.initialize(moviefile);
        ThirdRatings tr = new ThirdRatings(ratingsfile);
        AllFilters af = new AllFilters();
        af.addFilter(new MinutesFilter(min, max));
        af.addFilter(new DirectorsFilter(directorsCsv));
        ArrayList<Rating> list = tr.getAverageRatingsByFilter(minimalRaters, af);
        System.out.println("found " + list.size() + " movies");
    }
}

