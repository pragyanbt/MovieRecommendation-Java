import java.util.*;

/**
 * Plug-in for the course website. Implements Recommender.
 * - getItemsToRate(): returns ~20 recent, normal-length movies to rate
 * - printRecommendationsFor(): prints an HTML table with top recommendations
 *
 * Works with your existing classes: MovieDatabase, RaterDatabase, FourthRatings,
 * and filters (AllFilters, YearAfterFilter, MinutesFilter).
 */
public class RecommendationRunner implements Recommender {

    /** Choose ~20 movies that are recent and reasonable length. */
    @Override
    public ArrayList<String> getItemsToRate() {
        // Build a pool: year >= 2000 AND 80..180 minutes (inclusive)
        AllFilters poolFilter = new AllFilters();
        poolFilter.addFilter(new YearAfterFilter(2000));    // inclusive
        poolFilter.addFilter(new MinutesFilter(80, 180));   // inclusive

        ArrayList<String> pool = MovieDatabase.filterBy(poolFilter);
        // Shuffle so users don't all see the same titles
        Collections.shuffle(pool, new Random()); // you can pass a fixed seed for determinism

        // Pick up to 20 from the pool
        int howMany = Math.min(20, pool.size());
        ArrayList<String> toRate = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) toRate.add(pool.get(i));
        return toRate;
    }

    /** Print an HTML table of recommendations for the given web rater id. */
    @Override
    public void printRecommendationsFor(String webRaterID) {
        // DO NOT call RaterDatabase.initialize(...) here; the site already added the web user.
        // MovieDatabase is also initialized by the site, but calling initialize again is harmless.
        // MovieDatabase.initialize("ratedmoviesfull.csv"); // not needed on the site

        FourthRatings fr = new FourthRatings();

        // Tunables: you can tweak these if you like
        final int NUM_SIMILAR   = 20; // how many neighbors to use
        final int MIN_RATERS    = 5;  // minimal neighbor count per movie
        final int MAX_RESULTS   = 20; // how many rows to display

        ArrayList<Rating> recs = fr.getSimilarRatings(webRaterID, NUM_SIMILAR, MIN_RATERS);

        // ---- HTML OUTPUT ----
        System.out.println("<!DOCTYPE html>");
        System.out.println("<html><head><meta charset='utf-8'>");
        System.out.println("<title>Your Movie Recommendations</title>");
        System.out.println("<style>");
        System.out.println("body{font-family:system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;margin:24px;color:#111;}");
        System.out.println(".wrap{max-width:1024px;margin:0 auto;}");
        System.out.println("h1{font-size:24px;margin:0 0 12px;}");
        System.out.println("p.note{color:#555;margin:4px 0 16px;}");
        System.out.println("table{border-collapse:collapse;width:100%;box-shadow:0 1px 4px rgba(0,0,0,.06)}");
        System.out.println("th,td{border-bottom:1px solid #eee;padding:10px 12px;text-align:left;vertical-align:top}");
        System.out.println("th{background:#fafafa;font-weight:600}");
        System.out.println("tr:hover{background:#f9fbff}");
        System.out.println(".rating{font-variant-numeric:tabular-nums;font-weight:600}");
        System.out.println("</style></head><body><div class='wrap'>");

        if (recs == null || recs.isEmpty()) {
            System.out.println("<h1>No recommendations yet</h1>");
            System.out.println("<p class='note'>Try rating a few more movies so we can learn your taste.</p>");
            System.out.println("</div></body></html>");
            return;
        }

        System.out.println("<h1>Recommended for you</h1>");
        System.out.println("<p class='note'>Based on people with similar taste. Showing up to " + Math.min(MAX_RESULTS, recs.size()) + " movies.</p>");
        System.out.println("<table>");
        System.out.println("<thead><tr>"
                + "<th>#</th>"
                + "<th>Title</th>"
                + "<th>Year</th>"
                + "<th>Minutes</th>"
                + "<th>Genres</th>"
                + "<th>Directors</th>"
                + "<th>Score</th>"
                + "</tr></thead><tbody>");

        int shown = 0;
        for (Rating r : recs) {
            if (shown >= MAX_RESULTS) break;
            String id = r.getItem();
            String title = escape(MovieDatabase.getTitle(id));
            int year = MovieDatabase.getYear(id);
            int mins = MovieDatabase.getMinutes(id);
            String genres = escape(MovieDatabase.getGenres(id));
            String dirs = escape(MovieDatabase.getDirector(id));
            String score = String.format(java.util.Locale.US, "%.3f", r.getValue());

            System.out.println("<tr>"
                    + "<td>" + (shown + 1) + "</td>"
                    + "<td>" + title + "</td>"
                    + "<td>" + year + "</td>"
                    + "<td>" + mins + "</td>"
                    + "<td>" + genres + "</td>"
                    + "<td>" + dirs + "</td>"
                    + "<td class='rating'>" + score + "</td>"
                    + "</tr>");
            shown++;
        }

        System.out.println("</tbody></table>");
        System.out.println("</div></body></html>");
    }

    // Minimal HTML escape for plain text fields (avoid breaking the table if titles have & or <)
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}

