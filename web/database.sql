-- Database: home_of_hope

CREATE DATABASE IF NOT EXISTS home_of_hope;
USE home_of_hope;

-- Contact Messages Table
CREATE TABLE IF NOT EXISTS contact_messages (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  subject VARCHAR(200) NOT NULL,
  message TEXT NOT NULL,
  submitted_at DATETIME NOT NULL,
  notes TEXT,
  response_sent_at DATETIME
);

-- Donations Table
CREATE TABLE IF NOT EXISTS donations (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  donated_at DATETIME NOT NULL,
  status ENUM('pending','verified') DEFAULT 'pending',
  notes TEXT
);

-- Newsletter Subscribers Table
CREATE TABLE IF NOT EXISTS newsletter_subscribers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(150) NOT NULL UNIQUE,
  subscribed_at DATETIME NOT NULL
);

-- Volunteers Table
CREATE TABLE IF NOT EXISTS volunteers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  reason TEXT NOT NULL,
  signed_up_at DATETIME NOT NULL,
  status ENUM('new','contacted','accepted','declined') DEFAULT 'new',
  notes TEXT
);

-- Admin Users Table
CREATE TABLE IF NOT EXISTS admin_users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Default Admin User (password: admin@123)
-- Note: In a real scenario, use password_hash() in PHP to generate this.
-- This is just for schema reference.
-- INSERT INTO admin_users (username, password_hash) VALUES ('admin', '$2y$10$wU0O8/rV9Vf1.o5B6A7A... (example hash)');
