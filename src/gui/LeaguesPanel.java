package gui;

import entities.League;
import services.LeagueService;
import utils.FootManagerException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LeaguesPanel extends JPanel {
    private final LeagueService leagueService;
    private JTable leaguesTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, countryField, seasonField, numberOfTeamsField, startDateField, endDateField;

    public LeaguesPanel() throws SQLException {
        leagueService = new LeagueService();
        setLayout(new BorderLayout());
        initializeComponents();
        loadLeagues();
    }

    private void initializeComponents() {
        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("League Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Initialize form fields
        nameField = new JTextField(20);
        countryField = new JTextField(20);
        seasonField = new JTextField(10);
        numberOfTeamsField = new JTextField(5);
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);

        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Country:"), gbc);
        gbc.gridx = 1;
        formPanel.add(countryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Season:"), gbc);
        gbc.gridx = 1;
        formPanel.add(seasonField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Number of Teams:"), gbc);
        gbc.gridx = 1;
        formPanel.add(numberOfTeamsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        formPanel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        formPanel.add(endDateField, gbc);

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
        addButton.addActionListener(e -> addLeague());
        updateButton.addActionListener(e -> updateLeague());
        deleteButton.addActionListener(e -> deleteLeague());
        clearButton.addActionListener(e -> clearForm());

        // Create table
        String[] columns = { "ID", "Name", "Country", "Season", "Number of Teams", "Start Date", "End Date" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        leaguesTable = new JTable(tableModel);
        leaguesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaguesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedLeague();
            }
        });

        // Layout components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(leaguesTable), BorderLayout.CENTER);
    }

    private void loadLeagues() {
        try {
            List<League> leagues = leagueService.getAllLeagues();
            tableModel.setRowCount(0);
            for (League league : leagues) {
                Object[] row = {
                        league.getId(),
                        league.getName(),
                        league.getCountry(),
                        league.getSeason(),
                        league.getNumberOfTeams(),
                        league.getStartDate(),
                        league.getEndDate()
                };
                tableModel.addRow(row);
            }
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading leagues: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedLeague() {
        int selectedRow = leaguesTable.getSelectedRow();
        if (selectedRow >= 0) {
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            countryField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            seasonField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            numberOfTeamsField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            startDateField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            endDateField.setText(tableModel.getValueAt(selectedRow, 6).toString());
        }
    }

    private void addLeague() {
        try {
            League league = new League();
            league.setName(nameField.getText());
            league.setCountry(countryField.getText());
            league.setSeason(seasonField.getText());
            league.setNumberOfTeams(Integer.parseInt(numberOfTeamsField.getText()));
            league.setStartDate(startDateField.getText());
            league.setEndDate(endDateField.getText());

            leagueService.addLeague(league);
            clearForm();
            loadLeagues();
            JOptionPane.showMessageDialog(this, "League added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for Number of Teams",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding league: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLeague() {
        int selectedRow = leaguesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a league to update");
            return;
        }

        try {
            League league = new League();
            league.setId((Integer) tableModel.getValueAt(selectedRow, 0));
            league.setName(nameField.getText());
            league.setCountry(countryField.getText());
            league.setSeason(seasonField.getText());
            league.setNumberOfTeams(Integer.parseInt(numberOfTeamsField.getText()));
            league.setStartDate(startDateField.getText());
            league.setEndDate(endDateField.getText());

            leagueService.updateLeague(league);
            clearForm();
            loadLeagues();
            JOptionPane.showMessageDialog(this, "League updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for Number of Teams",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FootManagerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating league: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteLeague() {
        int selectedRow = leaguesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a league to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this league?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int leagueId = (Integer) tableModel.getValueAt(selectedRow, 0);
                leagueService.deleteLeague(leagueId);
                clearForm();
                loadLeagues();
                JOptionPane.showMessageDialog(this, "League deleted successfully!");
            } catch (FootManagerException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting league: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        countryField.setText("");
        seasonField.setText("");
        numberOfTeamsField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        leaguesTable.clearSelection();
    }
}