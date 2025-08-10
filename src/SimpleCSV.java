import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SimpleCSV implements Closeable, Iterable<Map<String,String>> {
    private final BufferedReader br;
    private final List<String> headers;

    public SimpleCSV(String filename) {
        try {
            br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8);
            String headerLine = br.readLine();
            if (headerLine == null) throw new RuntimeException("Empty CSV: " + filename);
            headers = parseLine(headerLine);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override public void close() throws IOException { br.close(); }

    @Override public Iterator<Map<String,String>> iterator() {
        return new Iterator<>() {
            String nextLine = readNext();
            private String readNext() {
                try { return br.readLine(); } catch (IOException e) { throw new RuntimeException(e); }
            }
            @Override public boolean hasNext() { return nextLine != null; }
            @Override public Map<String,String> next() {
                if (nextLine == null) throw new NoSuchElementException();
                List<String> vals = parseLine(nextLine);
                Map<String,String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    String key = headers.get(i);
                    String val = i < vals.size() ? vals.get(i) : "";
                    row.put(key, val);
                }
                nextLine = readNext();
                return row;
            }
        };
    }

    private static List<String> parseLine(String line) {
        ArrayList<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"'); i++;               // escaped quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString()); cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out;
    }
}

