import java.util.*;

public class RecommendationRunner implements Recommender {

    // Tunables (safe defaults for the course data)
    private static final int NUM_TO_RATE   = 18; // movies to present initially (site handles the UI)
    private static final int NUM_SIMILAR   = 30; // neighbors
    private static final int MIN_RATERS    = 3;  // minimal similar raters per movie
    private static final int MAX_RESULTS   = 20; // how many to render on the results page

    @Override
    public ArrayList<String> getItemsToRate() {
        // Provide ~18 recent, reasonable-length movies; the site renders its own rating UI
        AllFilters pool = new AllFilters();
        pool.addFilter(new YearAfterFilter(2000));
        pool.addFilter(new MinutesFilter(80, 180));
        ArrayList<String> ids = MovieDatabase.filterBy(pool);

        // Prefer posters so the first page looks nice
        ArrayList<String> withPoster = new ArrayList<>(), noPoster = new ArrayList<>();
        for (String id : ids) {
            String p = MovieDatabase.getPoster(id);
            if (p == null || p.isEmpty() || p.equalsIgnoreCase("N/A")) noPoster.add(id);
            else withPoster.add(id);
        }
        Collections.shuffle(withPoster, new Random());
        Collections.shuffle(noPoster,   new Random());

        ArrayList<String> pick = new ArrayList<>(NUM_TO_RATE);
        for (String id : withPoster) { if (pick.size() >= NUM_TO_RATE) break; pick.add(id); }
        for (String id : noPoster)  { if (pick.size() >= NUM_TO_RATE) break; pick.add(id); }
        return pick;
    }

    @Override
    public void printRecommendationsFor(String webRaterID) {
        FourthRatings fr = new FourthRatings();
        ArrayList<Rating> recs = fr.getSimilarRatings(webRaterID, NUM_SIMILAR, MIN_RATERS);

        // Optional fallback if empty
        if (recs == null || recs.isEmpty()) {
            recs = fr.getSimilarRatings(webRaterID, /*neighbors*/50, /*min*/2);
        }

        // ---------- HTML with interactive UI (1–10 personal ratings) ----------
        println("<!doctype html><html><head><meta charset='utf-8'>");
        println("<title>Your Movie Recommendations</title>");
        println("<style>");
        println("body{font-family:system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;margin:24px;color:#111}");
        println(".wrap{max-width:1160px;margin:0 auto}");
        println("h1{font-size:24px;margin:0 0 6px}");
        println("p.note{color:#555;margin:0 0 16px}");
        println(".controls{display:flex;gap:12px;align-items:center;margin:12px 0 16px}");
        println("select,input[type=number]{padding:6px 8px;border:1px solid #ddd;border-radius:8px}");
        println("button{padding:8px 12px;border:1px solid #0ea5e9;background:#0ea5e9;color:#fff;border-radius:8px;cursor:pointer}");
        println("button.secondary{background:#fff;color:#0ea5e9}");
        println("table{border-collapse:collapse;width:100%;box-shadow:0 1px 4px rgba(0,0,0,.06)}");
        println("th,td{border-bottom:1px solid #eee;padding:10px 12px;text-align:left;vertical-align:top}");
        println("th{background:#fafafa;font-weight:600;position:sticky;top:0;z-index:1}");
        println("tr:hover{background:#f9fbff}");
        println(".poster{width:54px;height:80px;object-fit:cover;border-radius:6px;background:#f2f2f2}");
        println(".score{font-variant-numeric:tabular-nums;font-weight:700}");
        println(".my{display:flex;align-items:center;gap:8px}");
        println(".my input[type=range]{width:140px}");
        println(".pill{font-size:12px;padding:2px 8px;border-radius:999px;background:#eef;border:1px solid #dde;color:#334}");
        println("</style></head><body><div class='wrap'>");

        if (recs == null || recs.isEmpty()) {
            println("<h1>No recommendations yet</h1>");
            println("<p class='note'>Please rate a few more movies on the previous page.</p>");
            println("</div></body></html>");
            return;
        }

        int show = Math.min(MAX_RESULTS, recs.size());
        println("<h1>Recommended for you</h1>");
        println("<p class='note'>Based on people with similar taste. Use the personal rating (1–10) to reorder and filter locally.</p>");

        // Controls
        println("<div class='controls'>");
        println("<label>Sort by: <select id='sortBy'>"
                + "<option value='match'>Match Score (desc)</option>"
                + "<option value='my'>My Rating (desc)</option>"
                + "<option value='year'>Year (desc)</option>"
                + "<option value='minutes'>Minutes (asc)</option>"
                + "</select></label>");
        println("<label>Min my rating: <input id='minMy' type='number' min='1' max='10' step='1' value='1'></label>");
        println("<button id='apply'>Apply</button>");
        println("<button class='secondary' id='reset'>Reset My Ratings</button>");
        println("</div>");

        // Table header
        println("<table id='recs'><thead><tr>"
                + "<th>#</th>"
                + "<th>Poster</th>"
                + "<th>Title</th>"
                + "<th>Year</th>"
                + "<th>Minutes</th>"
                + "<th>Genres</th>"
                + "<th>Directors</th>"
                + "<th>Match<br>Score</th>"
                + "<th>My Rating<br>(1–10)</th>"
                + "</tr></thead><tbody>");

        // Rows
        for (int i = 0; i < show; i++) {
            Rating r = recs.get(i);
            String id = r.getItem();
            String title = esc(MovieDatabase.getTitle(id));
            String genres = esc(MovieDatabase.getGenres(id));
            String dirs = esc(MovieDatabase.getDirector(id));
            int year = MovieDatabase.getYear(id);
            int mins = MovieDatabase.getMinutes(id);
            String score = String.format(java.util.Locale.US, "%.3f", r.getValue());
            String poster = MovieDatabase.getPoster(id);
            if (poster == null || poster.isEmpty() || poster.equalsIgnoreCase("N/A")) {
                poster = "https://via.placeholder.com/108x160.png?text=No+Poster";
            }
            // Store data-* attributes for client-side sorting/filtering
            println("<tr data-id='" + esc(id) + "' data-score='" + score + "' data-year='" + year + "' data-minutes='" + mins + "'>");
            println("<td class='idx'>" + (i + 1) + "</td>");
            println("<td><img class='poster' src='" + esc(poster) + "' alt='Poster'></td>");
            println("<td>" + title + "</td>");
            println("<td>" + year + "</td>");
            println("<td>" + mins + "</td>");
            println("<td>" + genres + "</td>");
            println("<td>" + dirs + "</td>");
            println("<td class='score'>" + score + "</td>");
            // Personal 1–10 control (range + live badge)
            println("<td class='my'>"
                    + "<input class='myRange' type='range' min='1' max='10' step='1'>"
                    + "<span class='pill myVal'>–</span>"
                    + "</td>");
            println("</tr>");
        }
        println("</tbody></table>");

        // Footer tip
        println("<p class='note' style='margin-top:10px'>Your 1–10 ratings are stored in this browser only (localStorage). They do not change the server’s data.</p>");

        // -------- Client-side behavior --------
        println("<script>");
        println("(()=>{");
        println("const LSKEY='my-recs-ratings';");
        println("const table=document.getElementById('recs');");
        println("const tbody=table.querySelector('tbody');");
        println("const sortSel=document.getElementById('sortBy');");
        println("const minMy=document.getElementById('minMy');");
        println("const btnApply=document.getElementById('apply');");
        println("const btnReset=document.getElementById('reset');");
        println("let my=JSON.parse(localStorage.getItem(LSKEY)||'{}');");

        // initialize sliders from localStorage
        println("for(const tr of tbody.querySelectorAll('tr')){");
        println("  const id=tr.getAttribute('data-id');");
        println("  const slider=tr.querySelector('.myRange');");
        println("  const badge=tr.querySelector('.myVal');");
        println("  if(my[id]){ slider.value=my[id]; badge.textContent=my[id]; }");
        println("  slider.addEventListener('input',()=>{ badge.textContent=slider.value; });");
        println("  slider.addEventListener('change',()=>{ my[id]=parseInt(slider.value,10); localStorage.setItem(LSKEY,JSON.stringify(my)); });");
        println("}");

        // sorting & filtering
        println("function currentMy(tr){ const id=tr.getAttribute('data-id'); return my[id]||0; }");
        println("function cmp(a,b){");
        println("  const mode=sortSel.value;");
        println("  if(mode==='my'){ return (currentMy(b)-currentMy(a)) || (parseFloat(b.dataset.score)-parseFloat(a.dataset.score)); }");
        println("  if(mode==='year'){ return parseInt(b.dataset.year)-parseInt(a.dataset.year); }");
        println("  if(mode==='minutes'){ return parseInt(a.dataset.minutes)-parseInt(b.dataset.minutes); }");
        println("  return parseFloat(b.dataset.score)-parseFloat(a.dataset.score);");
        println("}");
        println("function apply(){");
        println("  const rows=Array.from(tbody.querySelectorAll('tr'));");
        println("  const min=parseInt(minMy.value||'1',10);");
        println("  const kept=rows.filter(tr=> currentMy(tr)>=min || (min<=1));");
        println("  kept.sort(cmp);");
        println("  tbody.innerHTML='';");
        println("  kept.forEach((tr,i)=>{ tr.querySelector('.idx').textContent=(i+1); tbody.appendChild(tr); });");
        println("}");
        println("btnApply.addEventListener('click',apply);");
        println("btnReset.addEventListener('click',()=>{ localStorage.removeItem(LSKEY); my={}; for(const tr of tbody.querySelectorAll('tr')){ tr.querySelector('.myRange').value='5'; tr.querySelector('.myVal').textContent='–'; } apply(); });");
        println("sortSel.addEventListener('change',apply);");
        println("})();");
        println("</script>");

        println("</div></body></html>");
    }

    // helpers
    private static void println(String s){ System.out.println(s); }
    private static String esc(String s){
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
