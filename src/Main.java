import java.awt.Desktop;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1) Initialize databases (local preview only)
        MovieDatabase.initialize("ratedmoviesfull.csv");
        RaterDatabase.initialize("ratings.csv");

        // 2) Use a real rater id from ratings.csv for preview
        String testRaterId = "71"; // change if you like

        // 3) Where to write the HTML
        Path out = Paths.get(System.getProperty("user.dir"), "recommendations.html");
        System.out.println("Writing to: " + out.toAbsolutePath());

        // 4) Capture RecommendationRunner's System.out into the file
        try (PrintStream fileOut = new PrintStream(Files.newOutputStream(out), true, StandardCharsets.UTF_8)) {
            PrintStream old = System.out;
            System.setOut(fileOut);
            new RecommendationRunner().printRecommendationsFor(testRaterId);
            System.setOut(old);
        }

        // 5) Auto-open in your default browser
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(out.toUri());
            System.out.println("Opened in your browser.");
        } else {
            System.out.println("Wrote recommendations.html — open it in your browser.");
        }
    }
}
