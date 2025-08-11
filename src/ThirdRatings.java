import java.util.*;

public class ThirdRatings {
    private final ArrayList<Rater> myRaters;

    public ThirdRatings() { this("ratings.csv"); }
    public ThirdRatings(String ratingsfile) {
        FirstRatings fr = new FirstRatings();
        myRaters = fr.loadRaters(ratingsfile);
    }

    public int getRaterSize() { return myRaters.size(); }

    private double getAverageByID(String id, int minimalRaters) {
        int count = 0; double sum = 0.0;
        for (Rater r : myRaters) {
            double v = r.getRating(id);
            if (v != -1) { sum += v; count++; }
        }
        return (count >= minimalRaters) ? sum / count : 0.0;
    }

    public ArrayList<Rating> getAverageRatings(int minimalRaters) {
        ArrayList<Rating> out = new ArrayList<>();
        for (String id : MovieDatabase.filterBy(new TrueFilter())) {
            double avg = getAverageByID(id, minimalRaters);
            if (avg > 0.0) out.add(new Rating(id, avg));
        }
        return out;
    }

    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) {
        ArrayList<Rating> out = new ArrayList<>();
        for (String id : MovieDatabase.filterBy(filterCriteria)) {
            double avg = getAverageByID(id, minimalRaters);
            if (avg > 0.0) out.add(new Rating(id, avg));
        }
        return out;
    }
}

