import java.awt.Desktop;
import java.io.PrintStream;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws Exception {
        MovieDatabase.initialize("ratedmoviesfull.csv");
        RaterDatabase.initialize("ratings.csv");
        String testRater = "71"; // any existing rater id works

        Path out = Paths.get("recommendations.html");
        try (PrintStream fileOut = new PrintStream(Files.newOutputStream(out))) {
            PrintStream old = System.out;
            System.setOut(fileOut);
            new RecommendationRunner().printRecommendationsFor(testRater);
            System.setOut(old);
        }
        if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(out.toUri());
        System.out.println("Opened: " + out.toAbsolutePath());
    }
}
