package services;

import database.JDBCConnection;
import entities.Player;
import utils.FootManagerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {
    private Connection connection;

    public PlayerService() throws SQLException {
        this.connection = JDBCConnection.getConnection();
    }

    public List<Player> getAllPlayers() throws FootManagerException {
        List<Player> players = new ArrayList<>();
        String query = "SELECT * FROM players";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Player player = new Player(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("position"),
                    rs.getInt("team_id"),
                    rs.getString("nationality"),
                    rs.getInt("jersey_number")
                );
                players.add(player);
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching players: " + e.getMessage());
        }
        return players;
    }

    public void addPlayer(Player player) throws FootManagerException {
        String query = "INSERT INTO players (name, age, position, team_id, nationality, jersey_number) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, player.getName());
            pstmt.setInt(2, player.getAge());
            pstmt.setString(3, player.getPosition());
            pstmt.setInt(4, player.getTeamId());
            pstmt.setString(5, player.getNationality());
            pstmt.setInt(6, player.getJerseyNumber());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Creating player failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    player.setId(generatedKeys.getInt(1));
                } else {
                    throw new FootManagerException("Creating player failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error adding player: " + e.getMessage());
        }
    }

    public void updatePlayer(Player player) throws FootManagerException {
        String query = "UPDATE players SET name = ?, age = ?, position = ?, team_id = ?, nationality = ?, jersey_number = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, player.getName());
            pstmt.setInt(2, player.getAge());
            pstmt.setString(3, player.getPosition());
            pstmt.setInt(4, player.getTeamId());
            pstmt.setString(5, player.getNationality());
            pstmt.setInt(6, player.getJerseyNumber());
            pstmt.setInt(7, player.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Updating player failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error updating player: " + e.getMessage());
        }
    }

    public void deletePlayer(int playerId) throws FootManagerException {
        String query = "DELETE FROM players WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, playerId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new FootManagerException("Deleting player failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error deleting player: " + e.getMessage());
        }
    }

    public Player getPlayerById(int playerId) throws FootManagerException {
        String query = "SELECT * FROM players WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, playerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Player(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("position"),
                        rs.getInt("team_id"),
                        rs.getString("nationality"),
                        rs.getInt("jersey_number")
                    );
                } else {
                    throw new FootManagerException("Player not found with ID: " + playerId);
                }
            }
        } catch (SQLException e) {
            throw new FootManagerException("Error fetching player: " + e.getMessage());
        }
    }
}