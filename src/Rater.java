import java.util.ArrayList;

public class Rater {
    private final String myID;
    private final ArrayList<Rating> myRatings = new ArrayList<>();

    public Rater(String id) { myID = id; }

    public void addRating(String item, double rating) {
        for (int i = 0; i < myRatings.size(); i++) {
            if (myRatings.get(i).getItem().equals(item)) {
                myRatings.set(i, new Rating(item, rating));
                return;
            }
        }
        myRatings.add(new Rating(item, rating));
    }

    public String getID() { return myID; }

    /** returns -1 if no rating for this item */
    public double getRating(String item) {
        for (Rating r : myRatings) if (r.getItem().equals(item)) return r.getValue();
        return -1;
    }

    public boolean hasRating(String item) { return getRating(item) != -1; }

    public int numRatings() { return myRatings.size(); }

    public ArrayList<String> getItemsRated() {
        ArrayList<String> items = new ArrayList<>();
        for (Rating r : myRatings) items.add(r.getItem());
        return items;
    }
}
