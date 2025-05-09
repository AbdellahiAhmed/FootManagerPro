package gui;

import entities.Match;
import services.MatchService;
import utils.FootManagerException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MatchesPanel extends JPanel {
    private final MatchService matchService;
    private JTable matchesTable;
    private DefaultTableModel tableModel;
    private JTextField homeTeamIdField, awayTeamIdField, leagueIdField, matchDateField,
            matchTimeField, homeScoreField, awayScoreField, venueField;
    private JComboBox<String> statusComboBox;

    public MatchesPanel() throws SQLException {
        matchService = new MatchService();
        setLayout(new BorderLayout());
        initializeComponents();
        loadMatches();
    }

    private void initializeComponents() {
        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Match Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Initialize form fields
        homeTeamIdField = new JTextField(5);
        awayTeamIdField = new JTextField(5);
        leagueIdField = new JTextField(5);
        matchDateField = new JTextField(10);
        matchTimeField = new JTextField(8);
        homeScoreField = new JTextField(3);
        awayScoreField = new JTextField(3);
        venueField = new JTextField(20);
        statusComboBox = new JComboBox<>(new String[] { "SCHEDULED", "LIVE", "COMPLETED", "POSTPONED" });

        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Home Team ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(homeTeamIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Away Team ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(awayTeamIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("League ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(leagueIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Match Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        formPanel.add(matchDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Match Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        formPanel.add(matchTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Home Score:"), gbc);
        gbc.gridx = 1;
        formPanel.add(homeScoreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Away Score:"), gbc);
        gbc.gridx = 1;
        formPanel.add(awayScoreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Venue:"), gbc);
        gbc.gridx = 1;
        formPanel.add(venueField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusComboBox, gbc);

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
        addButton.addActionListener(e -> addMatch());
        updateButton.addActionListener(e -> updateMatch());
        deleteButton.addActionListener(e -> deleteMatch());
        clearButton.addActionListener(e -> clearForm());

        // Create table
        String[] columns = { "ID", "Home Team", "Away Team", "League", "Date", "Time",
                "Home Score", "Away Score", "Venue", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        matchesTable = new JTable(tableModel);
        matchesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matchesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedMatch();
            }
        });

        // Layout components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(matchesTable), BorderLayout.CENTER);
    }

    private void loadMatches() {
        try {
            List<Match> matches = matchService.getAllMatches();
            tableModel.setRowCount(0);
            for (Match match : matches) {
                Object[] row = {
                        match.getId(),
                        match.getHomeTeamId(),
                        match.getAwayTeamId(),
                        match.getLeagueId(),
                        match.getMatchDate(),
                        match.getMatchTime(),
                        match.getHomeTeamScore(),
                        match.getAwayTeamScore(),
                        match.getVenue(),
                        match.getStatus()
                };
                tableModel.addRow(row);
            }
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading matches: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedMatch() {
        int selectedRow = matchesTable.getSelectedRow();
        if (selectedRow >= 0) {
            homeTeamIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            awayTeamIdField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            leagueIdField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            matchDateField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            matchTimeField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            homeScoreField.setText(tableModel.getValueAt(selectedRow, 6).toString());
            awayScoreField.setText(tableModel.getValueAt(selectedRow, 7).toString());
            venueField.setText(tableModel.getValueAt(selectedRow, 8).toString());
            statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 9).toString());
        }
    }

    private void addMatch() {
        try {
            Match match = new Match();
            match.setHomeTeamId(Integer.parseInt(homeTeamIdField.getText()));
            match.setAwayTeamId(Integer.parseInt(awayTeamIdField.getText()));
            match.setLeagueId(Integer.parseInt(leagueIdField.getText()));
            match.setMatchDate(matchDateField.getText());
            match.setMatchTime(matchTimeField.getText());
            match.setHomeTeamScore(Integer.parseInt(homeScoreField.getText()));
            match.setAwayTeamScore(Integer.parseInt(awayScoreField.getText()));
            match.setVenue(venueField.getText());
            match.setStatus(statusComboBox.getSelectedItem().toString());

            matchService.addMatch(match);
            clearForm();
            loadMatches();
            JOptionPane.showMessageDialog(this, "Match added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for team IDs and scores",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding match: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMatch() {
        int selectedRow = matchesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a match to update");
            return;
        }

        try {
            Match match = new Match();
            match.setId((Integer) tableModel.getValueAt(selectedRow, 0));
            match.setHomeTeamId(Integer.parseInt(homeTeamIdField.getText()));
            match.setAwayTeamId(Integer.parseInt(awayTeamIdField.getText()));
            match.setLeagueId(Integer.parseInt(leagueIdField.getText()));
            match.setMatchDate(matchDateField.getText());
            match.setMatchTime(matchTimeField.getText());
            match.setHomeTeamScore(Integer.parseInt(homeScoreField.getText()));
            match.setAwayTeamScore(Integer.parseInt(awayScoreField.getText()));
            match.setVenue(venueField.getText());
            match.setStatus(statusComboBox.getSelectedItem().toString());

            matchService.updateMatch(match);
            clearForm();
            loadMatches();
            JOptionPane.showMessageDialog(this, "Match updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for team IDs and scores",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating match: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMatch() {
        int selectedRow = matchesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a match to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this match?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int matchId = (Integer) tableModel.getValueAt(selectedRow, 0);
                matchService.deleteMatch(matchId);
                clearForm();
                loadMatches();
                JOptionPane.showMessageDialog(this, "Match deleted successfully!");
            } catch (FootManagerException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting match: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        homeTeamIdField.setText("");
        awayTeamIdField.setText("");
        leagueIdField.setText("");
        matchDateField.setText("");
        matchTimeField.setText("");
        homeScoreField.setText("");
        awayScoreField.setText("");
        venueField.setText("");
        statusComboBox.setSelectedIndex(0);
        matchesTable.clearSelection();
    }
}