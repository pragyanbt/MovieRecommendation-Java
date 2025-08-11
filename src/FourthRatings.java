import java.util.*;

/**
 * Step 4: similar-ratings using weighted averages and positive-only similarity.
 */
public class FourthRatings {

    // ---------- Averages (Step 2/3) ----------
    public double getAverageByID(String id, int minimalRaters) {
        int n = 0;
        double sum = 0.0;
        for (Rater r : RaterDatabase.getRaters()) {
            double v = r.getRating(id);
            if (v != -1) { n++; sum += v; }
        }
        return (n >= minimalRaters) ? (sum / n) : 0.0;
    }

    public ArrayList<Rating> getAverageRatings(int minimalRaters) {
        ArrayList<Rating> ar = new ArrayList<>();
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        for (String id : movies) {
            double avgr = getAverageByID(id, minimalRaters);
            if (avgr > 0.0) ar.add(new Rating(id, avgr));
        }
        return ar;
    }

    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) {
        ArrayList<Rating> ar = new ArrayList<>();
        ArrayList<String> movies = MovieDatabase.filterBy(filterCriteria);
        for (String id : movies) {
            double avgr = getAverageByID(id, minimalRaters);
            if (avgr > 0.0) ar.add(new Rating(id, avgr));
        }
        return ar;
    }

    // ---------- Similarities (Step 4) ----------
    private double dotProduct(Rater me, Rater r) {
        double product = 0.0;
        ArrayList<String> meIDs = me.getItemsRated();
        for (String meID : meIDs) {
            double other = r.getRating(meID);
            if (other != -1) {
                product += (me.getRating(meID) - 5.0) * (other - 5.0);
            }
        }
        return product;
    }

    /** POSITIVE similarities only, sorted high → low */
    private ArrayList<Rating> getSimilarities(String id) {
        ArrayList<Rating> similar = new ArrayList<>();
        Rater me = RaterDatabase.getRater(id);
        if (me == null) return similar;

        for (Rater r : RaterDatabase.getRaters()) {
            if (r == me) continue;
            double product = dotProduct(me, r);
            if (product > 0) { // strictly positive
                similar.add(new Rating(r.getID(), product));
            }
        }
        Collections.sort(similar, Collections.reverseOrder());
        return similar;
    }

    /** Weighted recommendations (no extra filter). Excludes movies already rated by target. */
    public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters) {
        ArrayList<Rating> ret = new ArrayList<>();
        ArrayList<Rating> sims = getSimilarities(id);
        Rater me = RaterDatabase.getRater(id);
        if (me == null) return ret;

        HashSet<String> already = new HashSet<>(me.getItemsRated());
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        int limit = Math.min(numSimilarRaters, sims.size());

        for (String mID : movies) {
            if (already.contains(mID)) continue; // don't recommend what they've already rated

            double weighted = 0.0, weightSum = 0.0;
            int contributors = 0;

            for (int k = 0; k < limit; k++) {
                Rating s = sims.get(k);               // s.getItem() is raterID; s.getValue() is similarity
                Rater r = RaterDatabase.getRater(s.getItem());
                if (r == null) continue;
                double v = r.getRating(mID);
                if (v == -1) continue;               // this similar rater didn't rate the movie

                weighted  += s.getValue() * v;
                weightSum += s.getValue();
                contributors++;
            }

            if (contributors >= minimalRaters && weightSum > 0) {
                ret.add(new Rating(mID, weighted / weightSum));
            }
        }
        Collections.sort(ret, Collections.reverseOrder());
        return ret;
    }

    /** Weighted recommendations with an extra filter. Excludes movies already rated by target. */
    public ArrayList<Rating> getSimilarRatingsByFilter(String id, int numSimilarRaters,
                                                       int minimalRaters, Filter filterCriteria) {
      