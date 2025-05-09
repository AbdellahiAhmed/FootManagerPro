package services;

import database.JDBCConnection;
import entities.Team;
import utils.FootManagerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamService {
    private Connection connection;

    public TeamService() throws SQLException {
        this.connection = JDBCConnection.getConnection();
    }

    public List<Team> getAllTeams() throws FootManagerException {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM teams";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Team team = new Team(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getInt("league_id"),
                        rs.getString("coach"),
                        rs.getInt("founded_year"),
                        rs.getString("home_stadium"));
                teams.add(team);
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching teams: " + e.getMessage());
        }
        return teams;
    }

    public void addTeam(Team team) throws FootManagerException {
        String query = "INSERT INTO teams (name, city, league_id, coach, founded_year, home_stadium) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, team.getName());
            pstmt.setString(2, team.getCity());
            pstmt.setInt(3, team.getLeagueId());
            pstmt.setString(4, team.getCoach());
            pstmt.setInt(5, team.getFoundedYear());
            pstmt.setString(6, team.getHomeStadium());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Creating team failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    team.setId(generatedKeys.getInt(1));
                } else {
                    throw new FootManagerException("Creating team failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error adding team: " + e.getMessage());
        }
    }

    public void updateTeam(Team team) throws FootManagerException {
        String query = "UPDATE teams SET name = ?, city = ?, league_id = ?, coach = ?, founded_year = ?, home_stadium = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, team.getName());
            pstmt.setString(2, team.getCity());
            pstmt.setInt(3, team.getLeagueId());
            pstmt.setString(4, team.getCoach());
            pstmt.setInt(5, team.getFoundedYear());
            pstmt.setString(6, team.getHomeStadium());
            pstmt.setInt(7, team.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Updating team failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error updating team: " + e.getMessage());
        }
    }

    public void deleteTeam(int teamId) throws FootManagerException {
        String query = "DELETE FROM teams WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, teamId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Deleting team failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error deleting team: " + e.getMessage());
        }
    }

    public Team getTeamById(int teamId) throws FootManagerException {
        String query = "SELECT * FROM teams WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Team(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("city"),
                            rs.getInt("league_id"),
                            rs.getString("coach"),
                            rs.getInt("founded_year"),
                            rs.getString("home_stadium"));
                } else {
                    throw new FootManagerException("Team not found with ID: " + teamId);
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching team: " + e.getMessage());
        }
    }

    public List<Team> getTeamsByLeague(int leagueId) throws FootManagerException {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM teams WHERE league_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, leagueId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("city"),
                            rs.getInt("league_id"),
                            rs.getString("coach"),
                            rs.getInt("founded_year"),
                            rs.getString("home_stadium"));
                    teams.add(team);
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching teams by league: " + e.getMessage());
        }
        return teams;
    }
}