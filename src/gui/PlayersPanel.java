package gui;

import entities.Player;
import services.PlayerService;
import utils.FootManagerException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PlayersPanel extends JPanel {
    private final PlayerService playerService;
    private JTable playersTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, ageField, positionField, teamIdField, nationalityField, jerseyNumberField;

    public PlayersPanel() throws SQLException {
        playerService = new PlayerService();
        setLayout(new BorderLayout());
        initializeComponents();
        loadPlayers();
    }

    private void initializeComponents() {
        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Player Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Initialize form fields
        nameField = new JTextField(20);
        ageField = new JTextField(5);
        positionField = new JTextField(10);
        teamIdField = new JTextField(5);
        nationalityField = new JTextField(15);
        jerseyNumberField = new JTextField(5);

        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1;
        formPanel.add(positionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Team ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(teamIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Nationality:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nationalityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Jersey Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(jerseyNumberField, gbc);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);

        // Add action listeners
        addButton.addActionListener(e -> addPlayer());
        updateButton.addActionListener(e -> updatePlayer());
        deleteButton.addActionListener(e -> deletePlayer());
        clearButton.addActionListener(e -> clearForm());

        // Create table
        String[] columns = { "ID", "Name", "Age", "Position", "Team ID", "Nationality", "Jersey Number" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        playersTable = new JTable(tableModel);
        playersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPlayer();
            }
        });

        // Layout components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(playersTable), BorderLayout.CENTER);
    }

    private void loadPlayers() {
        try {
            List<Player> players = playerService.getAllPlayers();
            tableModel.setRowCount(0);
            for (Player player : players) {
                Object[] row = {
                        player.getId(),
                        player.getName(),
                        player.getAge(),
                        player.getPosition(),
                        player.getTeamId(),
                        player.getNationality(),
                        player.getJerseyNumber()
                };
                tableModel.addRow(row);
            }
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading players: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedPlayer() {
        int selectedRow = playersTable.getSelectedRow();
        if (selectedRow >= 0) {
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            ageField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            positionField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            teamIdField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            nationalityField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            jerseyNumberField.setText(tableModel.getValueAt(selectedRow, 6).toString());
        }
    }

    private void addPlayer() {
        try {
            Player player = new Player();
            player.setName(nameField.getText());
            player.setAge(Integer.parseInt(ageField.getText()));
            player.setPosition(positionField.getText());
            player.setTeamId(Integer.parseInt(teamIdField.getText()));
            player.setNationality(nationalityField.getText());
            player.setJerseyNumber(Integer.parseInt(jerseyNumberField.getText()));

            playerService.addPlayer(player);
            clearForm();
            loadPlayers();
            JOptionPane.showMessageDialog(this, "Player added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for Age, Team ID, and Jersey Number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding player: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePlayer() {
        int selectedRow = playersTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a player to update");
            return;
        }

        try {
            Player player = new Player();
            player.setId((Integer) tableModel.getValueAt(selectedRow, 0));
            player.setName(nameField.getText());
            player.setAge(Integer.parseInt(ageField.getText()));
            player.setPosition(positionField.getText());
            player.setTeamId(Integer.parseInt(teamIdField.getText()));
            player.setNationality(nationalityField.getText());
            player.setJerseyNumber(Integer.parseInt(jerseyNumberField.getText()));

            playerService.updatePlayer(player);
            clearForm();
            loadPlayers();
            JOptionPane.showMessageDialog(this, "Player updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for Age, Team ID, and Jersey Number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating player: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePlayer() {
        int selectedRow = playersTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a player to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this player?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int playerId = (Integer) tableModel.getValueAt(selectedRow, 0);
                playerService.deletePlayer(playerId);
                clearForm();
                loadPlayers();
                JOptionPane.showMessageDialog(this, "Player deleted successfully!");
            } catch (FootManagerException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting player: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        ageField.setText("");
        positionField.setText("");
        teamIdField.setText("");
        nationalityField.setText("");
        jerseyNumberField.setText("");
        playersTable.clearSelection();
    }
}