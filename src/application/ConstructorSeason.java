package application;

public class ConstructorSeason {
    private int year;
    private String constructorName;
    private String constructorNationality;
    private int wins;
    private double totalPoints;
    private int seasonRank;

    public ConstructorSeason(int year, String constructorName, String constructorNationality, int wins, double totalPoints, int seasonRank) {
        this.year = year;
        this.constructorName = constructorName;
        this.constructorNationality = constructorNationality;
        this.wins = wins;
        this.totalPoints = totalPoints;
        this.seasonRank = seasonRank;
    }

    // Getters
    public int getYear() { return year; }
    public String getConstructorName() { return constructorName; }
    public String getConstructorNationality() { return constructorNationality; }
    public int getWins() { return wins; }
    public double getTotalPoints() { return totalPoints; }
    public int getSeasonRank() { return seasonRank; }
}