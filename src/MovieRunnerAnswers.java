import java.util.*;

public class MovieRunnerAnswers {
    public static void main(String[] args) {
        System.out.println("WD: " + new java.io.File(".").getAbsolutePath());

        // Use the FULL datasets:
        SecondRatings sr = new SecondRatings("ratedmoviesfull.csv", "ratings.csv");

        // --- Individual movie averages (4 decimals) ---
        printAvg(sr, "The Maze Runner", 1);
        printAvg(sr, "Moneyball", 1);
        printAvg(sr, "Vacation", 1);

        // --- How many movies have >= 50 ratings? ---
        int ge50 = sr.countMoviesWithAtLeast(50);
        System.out.println("Movies with >= 50 ratings: " + ge50);

        // --- Lowest rated among options with thresholds ---
        List<String> q9 = Arrays.asList("White House Down","The Interview","Elysium","The Purge","Riddick");
        String lowest20 = sr.lowestRatedAmongTitles(q9, 20);
        System.out.println("Lowest (>=20 ratings) among options: " + lowest20);

        List<String> q10 = Arrays.asList("The Purge","Spring Breakers","Identity Thief","Mama","The Hangover Part III");
        String lowest12 = sr.lowestRatedAmongTitles(q10, 12);
        System.out.println("Lowest (>=12 ratings) among options: " + lowest12);
    }

    private static void printAvg(SecondRatings sr, String title, int minimalRaters) {
        double avg = sr.getAverageRatingByTitle(title, minimalRaters);
        int n = sr.getNumRatingsForTitle(title);
        if (Double.isNaN(avg)) {
            System.out.println("Average for \"" + title + "\": N/A (needs at least " + minimalRaters + " ratings)");
        } else {
            System.out.printf(java.util.Locale.US, "Average for \"%s\": %.4f  (n=%d)%n", title, avg, n);
        }
    }
}

