public class Rating implements Comparable<Rating> {
    private final String item;
    private final double value;
    public Rating(String item, double value) { this.item = item; this.value = value; }
    public String getItem() { return item; }
    public double getValue() { return value; }
    @Override public int compareTo(Rating o) { return Double.compare(this.value, o.value); }
    @Override public String toString() { return "[" + item + " : " + value + "]"; }
}
