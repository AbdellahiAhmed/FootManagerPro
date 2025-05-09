package gui;

import entities.Team;
import services.TeamService;
import utils.FootManagerException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TeamsPanel extends JPanel {
    private final TeamService teamService;
    private JTable teamsTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, cityField, leagueIdField, coachField, foundedYearField, homeStadiumField;

    public TeamsPanel() throws SQLException {
        teamService = new TeamService();
        setLayout(new BorderLayout());
        initializeComponents();
        loadTeams();
    }

    private void initializeComponents() {
        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Team Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Initialize form fields
        nameField = new JTextField(20);
        cityField = new JTextField(20);
        leagueIdField = new JTextField(5);
        coachField = new JTextField(20);
        foundedYearField = new JTextField(5);
        homeStadiumField = new JTextField(20);

        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("City:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("League ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(leagueIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Coach:"), gbc);
        gbc.gridx = 1;
        formPanel.add(coachField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Founded Year:"), gbc);
        gbc.gridx = 1;
        formPanel.add(foundedYearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Home Stadium:"), gbc);
        gbc.gridx = 1;
        formPanel.add(homeStadiumField, gbc);

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
        addButton.addActionListener(e -> addTeam());
        updateButton.addActionListener(e -> updateTeam());
        deleteButton.addActionListener(e -> deleteTeam());
        clearButton.addActionListener(e -> clearForm());

        // Create table
        String[] columns = { "ID", "Name", "City", "League ID", "Coach", "Founded Year", "Home Stadium" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teamsTable = new JTable(tableModel);
        teamsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedTeam();
            }
        });

        // Layout components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(teamsTable), BorderLayout.CENTER);
    }

    private void loadTeams() {
        try {
            List<Team> teams = teamService.getAllTeams();
            tableModel.setRowCount(0);
            for (Team team : teams) {
                Object[] row = {
                        team.getId(),
                        team.getName(),
                        team.getCity(),
                        team.getLeagueId(),
                        team.getCoach(),
                        team.getFoundedYear(),
                        team.getHomeStadium()
                };
                tableModel.addRow(row);
            }
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading teams: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedTeam() {
        int selectedRow = teamsTable.getSelectedRow();
        if (selectedRow >= 0) {
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            cityField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            leagueIdField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            coachField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            foundedYearField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            homeStadiumField.setText(tableModel.getValueAt(selectedRow, 6).toString());
        }
    }

    private void addTeam() {
        try {
            Team team = new Team();
            team.setName(nameField.getText());
            team.setCity(cityField.getText());
            team.setLeagueId(Integer.parseInt(leagueIdField.getText()));
            team.setCoach(coachField.getText());
            team.setFoundedYear(Integer.parseInt(foundedYearField.getText()));
            team.setHomeStadium(homeStadiumField.getText());

            teamService.addTeam(team);
            clearForm();
            loadTeams();
            JOptionPane.showMessageDialog(this, "Team added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for League ID and Founded Year",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding team: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTeam() {
        int selectedRow = teamsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a team to update");
            return;
        }

        try {
            Team team = new Team();
            team.setId((Integer) tableModel.getValueAt(selectedRow, 0));
            team.setName(nameField.getText());
            team.setCity(cityField.getText());
            team.setLeagueId(Integer.parseInt(leagueIdField.getText()));
            team.setCoach(coachField.getText());
            team.setFoundedYear(Integer.parseInt(foundedYearField.getText()));
            team.setHomeStadium(homeStadiumField.getText());

            teamService.updateTeam(team);
            clearForm();
            loadTeams();
            JOptionPane.showMessageDialog(this, "Team updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for League ID and Founded Year",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating team: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTeam() {
        int selectedRow = teamsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a team to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this team?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int teamId = (Integer) tableModel.getValueAt(selectedRow, 0);
                teamService.deleteTeam(teamId);
                clearForm();
                loadTeams();
                JOptionPane.showMessageDialog(this, "Team deleted successfully!");
            } catch (FootManagerException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting team: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        cityField.setText("");
        leagueIdField.setText("");
        coachField.setText("");
        foundedYearField.setText("");
        homeStadiumField.setText("");
        teamsTable.clearSelection();
    }
}