import java.util.*;

public class AllFilters implements Filter {
    private final ArrayList<Filter> filters = new ArrayList<>();
    public void addFilter(Filter f) { filters.add(f); }
    @Override public boolean satisfies(String id) {
        for (Filter f : filters) if (!f.satisfies(id)) return false;
        return true;
    }
}

