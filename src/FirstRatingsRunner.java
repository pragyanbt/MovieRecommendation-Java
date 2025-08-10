public class FirstRatingsRunner {
    public static void main(String[] args) {
        System.out.println("Working dir: " + new java.io.File(".").getAbsolutePath());
        FirstRatings fr = new FirstRatings();

        System.out.println("---- Movies (ratedmoviesfull.csv) ----");
        fr.testLoadMovies();

        System.out.println("---- Raters (ratings.csv) ----");
        fr.testLoadRaters();
    }
}
