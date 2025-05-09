-- Create database if not exists
CREATE DATABASE IF NOT EXISTS foot_manager_pro;
USE foot_manager_pro;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create leagues table
CREATE TABLE IF NOT EXISTS leagues (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    season VARCHAR(20) NOT NULL,
    number_of_teams INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

-- Create teams table
CREATE TABLE IF NOT EXISTS teams (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    league_id INT,
    coach VARCHAR(100),
    founded_year INT,
    home_stadium VARCHAR(100),
    FOREIGN KEY (league_id) REFERENCES leagues(id)
);

-- Create players table
CREATE TABLE IF NOT EXISTS players (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    position VARCHAR(50) NOT NULL,
    team_id INT,
    nationality VARCHAR(100) NOT NULL,
    jersey_number INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams(id)
);

-- Create matches table
CREATE TABLE IF NOT EXISTS matches (
    id INT PRIMARY KEY AUTO_INCREMENT,
    home_team_id INT NOT NULL,
    away_team_id INT NOT NULL,
    league_id INT NOT NULL,
    match_date DATE NOT NULL,
    match_time TIME NOT NULL,
    home_team_score INT DEFAULT 0,
    away_team_score INT DEFAULT 0,
    venue VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (home_team_id) REFERENCES teams(id),
    FOREIGN KEY (away_team_id) REFERENCES teams(id),
    FOREIGN KEY (league_id) REFERENCES leagues(id)
);

-- Insert default admin user
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');