package services;

import database.JDBCConnection;
import entities.Match;
import utils.FootManagerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchService {
    private Connection connection;

    public MatchService() throws SQLException {
        this.connection = JDBCConnection.getConnection();
    }

    public List<Match> getAllMatches() throws FootManagerException {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches ORDER BY match_date, match_time";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Match match = new Match(
                        rs.getInt("id"),
                        rs.getInt("home_team_id"),
                        rs.getInt("away_team_id"),
                        rs.getInt("league_id"),
                        rs.getString("match_date"),
                        rs.getString("match_time"),
                        rs.getInt("home_team_score"),
                        rs.getInt("away_team_score"),
                        rs.getString("venue"),
                        rs.getString("status"));
                matches.add(match);
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching matches: " + e.getMessage());
        }
        return matches;
    }

    public void addMatch(Match match) throws FootManagerException {
        String query = "INSERT INTO matches (home_team_id, away_team_id, league_id, match_date, match_time, venue, status) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, match.getHomeTeamId());
            pstmt.setInt(2, match.getAwayTeamId());
            pstmt.setInt(3, match.getLeagueId());
            pstmt.setString(4, match.getMatchDate());
            pstmt.setString(5, match.getMatchTime());
            pstmt.setString(6, match.getVenue());
            pstmt.setString(7, match.getStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Creating match failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    match.setId(generatedKeys.getInt(1));
                } else {
                    throw new FootManagerException("Creating match failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error adding match: " + e.getMessage());
        }
    }

    public void updateMatch(Match match) throws FootManagerException {
        String query = "UPDATE matches SET home_team_id = ?, away_team_id = ?, league_id = ?, " +
                "match_date = ?, match_time = ?, home_team_score = ?, away_team_score = ?, " +
                "venue = ?, status = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, match.getHomeTeamId());
            pstmt.setInt(2, match.getAwayTeamId());
            pstmt.setInt(3, match.getLeagueId());
            pstmt.setString(4, match.getMatchDate());
            pstmt.setString(5, match.getMatchTime());
            pstmt.setInt(6, match.getHomeTeamScore());
            pstmt.setInt(7, match.getAwayTeamScore());
            pstmt.setString(8, match.getVenue());
            pstmt.setString(9, match.getStatus());
            pstmt.setInt(10, match.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Updating match failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error updating match: " + e.getMessage());
        }
    }

    public void deleteMatch(int matchId) throws FootManagerException {
        String query = "DELETE FROM matches WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, matchId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Deleting match failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error deleting match: " + e.getMessage());
        }
    }

    public Match getMatchById(int matchId) throws FootManagerException {
        String query = "SELECT * FROM matches WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, matchId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Match(
                            rs.getInt("id"),
                            rs.getInt("home_team_id"),
                            rs.getInt("away_team_id"),
                            rs.getInt("league_id"),
                            rs.getString("match_date"),
                            rs.getString("match_time"),
                            rs.getInt("home_team_score"),
                            rs.getInt("away_team_score"),
                            rs.getString("venue"),
                            rs.getString("status"));
                } else {
                    throw new FootManagerException("Match not found with ID: " + matchId);
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching match: " + e.getMessage());
        }
    }

    public List<Match> getMatchesByLeague(int leagueId) throws FootManagerException {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches WHERE league_id = ? ORDER BY match_date, match_time";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, leagueId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match(
                            rs.getInt("id"),
                            rs.getInt("home_team_id"),
                            rs.getInt("away_team_id"),
                            rs.getInt("league_id"),
                            rs.getString("match_date"),
                            rs.getString("match_time"),
                            rs.getInt("home_team_score"),
                            rs.getInt("away_team_score"),
                            rs.getString("venue"),
                            rs.getString("status"));
                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching matches by league: " + e.getMessage());
        }
        return matches;
    }

    public List<Match> getMatchesByTeam(int teamId) throws FootManagerException {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches WHERE home_team_id = ? OR away_team_id = ? ORDER BY match_date, match_time";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, teamId);
            pstmt.setInt(2, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Match match = new Match(
                            rs.getInt("id"),
                            rs.getInt("home_team_id"),
                            rs.getInt("away_team_id"),
                            rs.getInt("league_id"),
                            rs.getString("match_date"),
                            rs.getString("match_time"),
                            rs.getInt("home_team_score"),
                            rs.getInt("away_team_score"),
                            rs.getString("venue"),
                            rs.getString("status"));
                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching matches by team: " + e.getMessage());
        }
        return matches;
    }
}