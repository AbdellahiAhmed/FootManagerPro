package main;

import gui.LoginWindow;

public class MainApp {
    public static void main(String[] args) {
        // Start the application with the login window
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.setVisible(true);
    }
}