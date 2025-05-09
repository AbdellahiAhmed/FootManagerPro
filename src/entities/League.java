package entities;

public class League {
    private int id;
    private String name;
    private String country;
    private String season;
    private int numberOfTeams;
    private String startDate;
    private String endDate;

    public League() {
    }

    public League(int id, String name, String country, String season, int numberOfTeams, String startDate,
            String endDate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.season = season;
        this.numberOfTeams = numberOfTeams;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getSeason() {
        return season;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setNumberOfTeams(int numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "League{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", season='" + season + '\'' +
                ", numberOfTeams=" + numberOfTeams +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}