import java.util.*;

public class RecommendationRunner implements Recommender {

    // Tunables — tweak if you want
    private static final int NUM_TO_RATE = 18;   // movies to show for rating (first page)
    private static final int NUM_SIMILAR = 30;   // neighbors to use
    private static final int MIN_RATERS  = 3;    // minimal similar raters per movie
    private static final int MAX_RESULTS = 24;   // cards to render

    // -------- First page: which movies to rate ----------
    @Override
    public ArrayList<String> getItemsToRate() {
        AllFilters pool = new AllFilters();
        pool.addFilter(new YearAfterFilter(2000));     // recent
        pool.addFilter(new MinutesFilter(80, 180));    // reasonable length
        ArrayList<String> ids = MovieDatabase.filterBy(pool);

        // Prefer items with a poster to make the UI look nice
        ArrayList<String> withPoster = new ArrayList<>();
        ArrayList<String> noPoster  = new ArrayList<>();
        for (String id : ids) {
            String p = safe(MovieDatabase.getPoster(id));
            if (p.equals("") || p.equalsIgnoreCase("N/A")) noPoster.add(id);
            else withPoster.add(id);
        }
        Collections.shuffle(withPoster, new Random());
        Collections.shuffle(noPoster,  new Random());

        ArrayList<String> pick = new ArrayList<>(NUM_TO_RATE);
        pick.addAll(withPoster.subList(0, Math.min(NUM_TO_RATE, withPoster.size())));
        for (String id : noPoster) { if (pick.size() >= NUM_TO_RATE) break; pick.add(id); }
        return pick;
    }

    // -------- Results page: pretty HTML UI ----------
    @Override
    public void printRecommendationsFor(String webRaterID) {
        FourthRatings fr = new FourthRatings();
        ArrayList<Rating> recs = fr.getSimilarRatings(webRaterID, NUM_SIMILAR, MIN_RATERS);

        println("<!doctype html><html><head><meta charset='utf-8'>");
        println("<title>Your Movie Picks</title>");
        println("<style>");
        println("body{margin:0;background:#0f172a;color:#e5e7eb;font-family:system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif}");
        println(".wrap{max-width:1200px;margin:32px auto;padding:0 20px}");
        println("h1{font-size:28px;margin:0 0 8px;font-weight:700}");
        println(".sub{color:#94a3b8;margin:0 0 24px}");
        println(".grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(220px,1fr));gap:16px}");
        println(".card{background:#111827;border:1px solid #1f2937;border-radius:14px;overflow:hidden;box-shadow:0 1px 6px rgba(0,0,0,.35)}");
        println(".poster{width:100%;height:320px;object-fit:cover;background:#0b1220}");
        println(".meta{padding:10px 12px}");
        println(".title{font-weight:700;line-height:1.25;margin:4px 0 6px;font-size:16px;color:#fff}");
        println(".dim{color:#9ca3af;font-size:12px}");
        println(".row{display:flex;justify-content:space-between;align-items:center;margin-top:8px}");
        println(".badge{background:#0ea5e9;color:#001018;padding:2px 8px;border-radius:999px;font-weight:700;font-size:12px}");
        println(".empty{background:#0b1220;border:1px dashed #334155;border-radius:12px;padding:28px;text-align:center;color:#93c5fd}");
        println("a{color:#93c5fd;text-decoration:none} a:hover{text-decoration:underline}");
        println("</style></head><body>");
        println("<div class='wrap'>");

        if (recs == null || recs.isEmpty()) {
            println("<h1>No recommendations yet</h1>");
            println("<p class='sub'>Try rating a few more movies so we can learn your taste.</p>");
            println("<div class='empty'>No results to show.</div>");
            println("</div></body></html>");
            return;
        }

        int show = Math.min(MAX_RESULTS, recs.size());
        println("<h1>Recommended for you</h1>");
        println("<p class='sub'>Based on people with similar taste. Showing " + show + " picks.</p>");
        println("<div class='grid'>");

        for (int i = 0; i < show; i++) {
            Rating r = recs.get(i);
            String id = r.getItem();

            String title = safe(MovieDatabase.getTitle(id));
            String poster = safe(MovieDatabase.getPoster(id));
            if (poster.equals("") || poster.equalsIgnoreCase("N/A"))
                poster = "https://via.placeholder.com/600x900.png?text=No+Poster";

            int year = MovieDatabase.getYear(id);
            int mins = MovieDatabase.getMinutes(id);
            String genres = safe(MovieDatabase.getGenres(id));
            String dirs = safe(MovieDatabase.getDirector(id));
            String score = String.format(java.util.Locale.US, "%.3f", r.getValue());

            println("<div class='card'>");
            println("<img class='poster' loading='lazy' src='" + poster + "' alt='Poster'>");
            println("<div class='meta'>");
            println("<div class='title'>" + title + "</div>");
            println("<div class='dim'>" + year + " • " + mins + " min</div>");
            println("<div class='dim'>" + genres + "</div>");
            println("<div class='dim'>Dir: " + dirs + "</div>");
            println("<div class='row'><span class='dim'>Match score</span><span class='badge'>" + score + "</span></div>");
            println("</div></div>");
        }

        println("</div></div></body></html>");
    }

    // -------- helpers --------
    private static void println(String s){ System.out.println(s); }
    private static String safe(String s){
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
