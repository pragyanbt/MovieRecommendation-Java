public class YearAfterFilter implements Filter {
    private final int year;
    public YearAfterFilter(int year) { this.year = year; }
    @Override public boolean satisfies(String id) {
        return MovieDatabase.getYear(id) >= year;
    }
}

