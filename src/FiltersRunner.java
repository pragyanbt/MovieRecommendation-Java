public class FiltersRunner {
    public static void main(String[] args) {
        System.out.println("WD: " + new java.io.File(".").getAbsolutePath());
        String movies = "ratedmoviesfull.csv";
        String ratings = "ratings.csv";
        MovieRunnerWithFilters mr = new MovieRunnerWithFilters();

        System.out.print("Q4 plain >=35 -> "); // (your first question here)
        mr.printAverageRatings(movies, ratings, 35);

        System.out.print("Q5 year>=2000, >=20 -> ");
        mr.printAverageRatingsByYearAfter(movies, ratings, 20, 2000);

        System.out.print("Q6 genre=Comedy, >=20 -> ");
        mr.printAverageRatingsByGenre(movies, ratings, 20, "Comedy");

        System.out.print("Q7 minutes 105..135, >=5 -> ");
        mr.printAverageRatingsByMinutes(movies, ratings, 5, 105, 135);

        System.out.print("Q8 directors list, >=4 -> ");
        mr.printAverageRatingsByDirectors(
                movies, ratings, 4,
                "Clint Eastwood,Joel Coen,Martin Scorsese,Roman Polanski,Nora Ephron,Ridley Scott,Sydney Pollack"
        );

        System.out.print("Q9 year>=1990 & Drama, >=8 -> ");
        mr.printAverageRatingsByYearAfterAndGenre(movies, ratings, 8, 1990, "Drama");

        System.out.print("Q10 minutes 90..180 & directors list, >=3 -> ");
        mr.printAverageRatingsByDirectorsAndMinutes(
                movies, ratings, 3, 90, 180,
                "Clint Eastwood,Joel Coen,Tim Burton,Ron Howard,Nora Ephron,Sydney Pollack"
        );
    }
}

