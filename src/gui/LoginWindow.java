package gui;

import database.JDBCConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginWindow() {
        setTitle("FootManagerPro - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("FootManagerPro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        // Username field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(loginButton, gbc);

        // Add action listener to login button
        loginButton.addActionListener(e -> attemptLogin());

        add(mainPanel);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = JDBCConnection.getConnection();
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // In real application, use password hashing

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Login successful
                    this.dispose(); // Close login window
                    SwingUtilities.invokeLater(() -> {
                        Dashboard dashboard = null;
                        try {
                            dashboard = new Dashboard();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        dashboard.setVisible(true);
                    });
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid username or password",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}