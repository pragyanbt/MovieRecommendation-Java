public class Movie {
    private final String id, title, genres, director, country, poster;
    private final int year, minutes;

    public Movie(String id, String title, int year, String genres, String director,
                 String country, int minutes, String poster) {
        this.id = id; this.title = title; this.year = year; this.genres = genres;
        this.director = director; this.country = country; this.minutes = minutes; this.poster = poster;
    }
    public String getID() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getGenres() { return genres; }
    public String getDirector() { return director; }
    public String getCountry() { return country; }
    public int getMinutes() { return minutes; }
    public String getPoster() { return poster; }
}
