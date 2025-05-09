package entities;

public class Team {
    private int id;
    private String name;
    private String city;
    private int leagueId;
    private String coach;
    private int foundedYear;
    private String homeStadium;

    public Team() {
    }

    public Team(int id, String name, String city, int leagueId, String coach, int foundedYear, String homeStadium) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.leagueId = leagueId;
        this.coach = coach;
        this.foundedYear = foundedYear;
        this.homeStadium = homeStadium;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public String getCoach() {
        return coach;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public String getHomeStadium() {
        return homeStadium;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }

    public void setHomeStadium(String homeStadium) {
        this.homeStadium = homeStadium;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", leagueId=" + leagueId +
                ", coach='" + coach + '\'' +
                ", foundedYear=" + foundedYear +
                ", homeStadium='" + homeStadium + '\'' +
                '}';
    }
}