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

    public static void main(String[] args) {
        String movies  = "ratedmoviesfull.csv";
        String ratings = "ratings.csv";

        MovieDatabase.initialize(movies);
        RaterDatabase.initialize(ratings);
        FourthRatings fr = new FourthRatings();

        // Q6
        printTop("Q6", fr.getSimilarRatings("337", 10, 3));

        // Q7
        printTop("Q7", fr.getSimilarRatingsByFilter("964", 20, 5, new GenreFilter("Mystery")));

        // Q8
        printTop("Q8", fr.getSimilarRatings("71", 20, 5));

        // Q9
        String dirs = "Clint Eastwood,J.J. Abrams,Alfred Hitchcock,Sydney Pollack,David Cronenberg,Oliver Stone,Mike Leigh";
        printTop("Q9", fr.getSimilarRatingsByFilter("120", 10, 2, new DirectorsFilter(dirs)));

        // Q10
        AllFilters f10 = new AllFilters();
        f10.addFilter(new GenreFilter("Drama"));
        f10.addFilter(new MinutesFilter(80, 160));
        printTop("Q10", fr.getSimilarRatingsByFilter("168", 10, 3, f10));

        // Q11
        AllFilters f11 = new AllFilters();
        f11.addFilter(new YearAfterFilter(1975));
        f11.addFilter(new MinutesFilter(70, 200));
        printTop("Q11", fr.getSimilarRatingsByFilter("314", 10, 5, f11));
    }
}

