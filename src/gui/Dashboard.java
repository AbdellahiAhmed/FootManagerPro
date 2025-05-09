package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Dashboard extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public Dashboard() throws SQLException {
        setTitle("FootManagerPro - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create main split pane
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(200); // Set initial divider location

        // Create and setup navigation panel
        JPanel navPanel = createNavigationPanel();
        splitPane.setLeftComponent(navPanel);

        // Create and setup content panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add implemented panels
        contentPanel.add(new PlayersPanel(), "PLAYERS");
        contentPanel.add(new TeamsPanel(), "TEAMS");
        contentPanel.add(new LeaguesPanel(), "LEAGUES");
        contentPanel.add(new MatchesPanel(), "MATCHES");

        splitPane.setRightComponent(contentPanel);

        // Add split pane to frame
        add(splitPane);
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new GridBagLayout());
        navPanel.setBackground(new Color(51, 51, 51));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add title
        JLabel titleLabel = new JLabel("FootManagerPro");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        navPanel.add(titleLabel, gbc);

        // Add navigation buttons
        String[] navItems = { "Players", "Teams", "Leagues", "Matches" };
        gbc.gridy = 1;
        gbc.weighty = 0;

        for (String item : navItems) {
            JButton navButton = createNavButton(item);
            navPanel.add(navButton, gbc);
            gbc.gridy++;
        }

        // Add logout button at bottom
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weighty = 1.0; // Push logout button to bottom
        JButton logoutButton = createNavButton("Logout");
        logoutButton.addActionListener(e -> logout());
        navPanel.add(logoutButton, gbc);

        return navPanel;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(51, 51, 51));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 40));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(75, 75, 75));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 51, 51));
            }
        });

        // Add action listener for navigation
        if (!text.equals("Logout")) {
            button.addActionListener(e -> cardLayout.show(contentPanel, text.toUpperCase()));
        }

        return button;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
            });
        }
    }
}