import java.util.*;

public class DirectorsFilter implements Filter {
    private final HashSet<String> wanted = new HashSet<>();
    public DirectorsFilter(String directorsCsv) {
        for (String d : directorsCsv.split(",")) wanted.add(d.trim());
    }
    @Override public boolean satisfies(String id) {
        String[] dirs = MovieDatabase.getDirector(id).split(",");
        for (String d : dirs) if (wanted.contains(d.trim())) return true;
        return false;
    }
}

