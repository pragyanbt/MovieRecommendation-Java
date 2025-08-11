import java.util.*;

public class MovieRunnerAnswers {
    

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

