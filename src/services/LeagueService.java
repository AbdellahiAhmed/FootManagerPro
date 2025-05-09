package services;

import database.JDBCConnection;
import entities.League;
import utils.FootManagerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeagueService {
    private Connection connection;

    public LeagueService() throws SQLException {
        this.connection = JDBCConnection.getConnection();
    }

    public List<League> getAllLeagues() throws FootManagerException {
        List<League> leagues = new ArrayList<>();
        String query = "SELECT * FROM leagues";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                League league = new League(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("season"),
                        rs.getInt("number_of_teams"),
                        rs.getString("start_date"),
                        rs.getString("end_date"));
                leagues.add(league);
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching leagues: " + e.getMessage());
        }
        return leagues;
    }

    public void addLeague(League league) throws FootManagerException {
        String query = "INSERT INTO leagues (name, country, season, number_of_teams, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, league.getName());
            pstmt.setString(2, league.getCountry());
            pstmt.setString(3, league.getSeason());
            pstmt.setInt(4, league.getNumberOfTeams());
            pstmt.setString(5, league.getStartDate());
            pstmt.setString(6, league.getEndDate());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Creating league failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    league.setId(generatedKeys.getInt(1));
                } else {
                    throw new FootManagerException("Creating league failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error adding league: " + e.getMessage());
        }
    }

    public void updateLeague(League league) throws FootManagerException {
        String query = "UPDATE leagues SET name = ?, country = ?, season = ?, number_of_teams = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, league.getName());
            pstmt.setString(2, league.getCountry());
            pstmt.setString(3, league.getSeason());
            pstmt.setInt(4, league.getNumberOfTeams());
            pstmt.setString(5, league.getStartDate());
            pstmt.setString(6, league.getEndDate());
            pstmt.setInt(7, league.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Updating league failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error updating league: " + e.getMessage());
        }
    }

    public void deleteLeague(int leagueId) throws FootManagerException {
        String query = "DELETE FROM leagues WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, leagueId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Deleting league failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error deleting league: " + e.getMessage());
        }
    }

    public League getLeagueById(int leagueId) throws FootManagerException {
        String query = "SELECT * FROM leagues WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, leagueId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new League(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("country"),
                            rs.getString("season"),
                            rs.getInt("number_of_teams"),
                            rs.getString("start_date"),
                            rs.getString("end_date"));
                } else {
                    throw new FootManagerException("League not found with ID: " + leagueId);
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching league: " + e.getMessage());
        }
    }
}