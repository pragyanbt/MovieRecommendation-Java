public class MovieRunnerAverageRunner {
    public static void main(String[] args) {
        System.out.println("WD: " + new java.io.File(".").getAbsolutePath());

        MovieRunnerAverage mra = new MovieRunnerAverage();

        // Demo with the short files (per spec example: prints two lines: 8.25 Her, 9.00 The Godfather)
        mra.printAverageRatings();

        // Show a single-movie lookup
        mra.getAverageRatingOneMovie();

        // === When you’re ready for full data, switch file names inside MovieRunnerAverage ===
        // e.g., new SecondRatings("ratedmoviesfull.csv", "ratings.csv") and pick your minimalRaters
        // Common choices: 12, 20, etc., to filter out sparsely rated movies.
    }
}

