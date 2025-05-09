package entities;

public class Match {
    private int id;
    private int homeTeamId;
    private int awayTeamId;
    private int leagueId;
    private String matchDate;
    private String matchTime;
    private int homeTeamScore;
    private int awayTeamScore;
    private String venue;
    private String status; // SCHEDULED, LIVE, COMPLETED, POSTPONED

    public Match() {
    }

    public Match(int id, int homeTeamId, int awayTeamId, int leagueId, String matchDate,
            String matchTime, int homeTeamScore, int awayTeamScore, String venue, String status) {
        this.id = id;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.leagueId = leagueId;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.venue = venue;
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public String getVenue() {
        return venue;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", homeTeamId=" + homeTeamId +
                ", awayTeamId=" + awayTeamId +
                ", leagueId=" + leagueId +
                ", matchDate='" + matchDate + '\'' +
                ", matchTime='" + matchTime + '\'' +
                ", homeTeamScore=" + homeTeamScore +
                ", awayTeamScore=" + awayTeamScore +
                ", venue='" + venue + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}