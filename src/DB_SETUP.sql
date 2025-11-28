-- DB setup for IncidentManagementSystem
-- Database name: Incidents

-- Create database and table
CREATE DATABASE IF NOT EXISTS Incidents;
USE Incidents;

CREATE TABLE IF NOT EXISTS incidents (
  incident_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  type VARCHAR(50),
  risk VARCHAR(50),
  status VARCHAR(50),
  reported_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  assigned_to VARCHAR(255),
  resolution_notes TEXT
);

-- Sample seed rows
INSERT INTO incidents (title, description, type, risk, status, reported_date, assigned_to, resolution_notes)
VALUES
('Email phishing attempt', 'Phishing email directed at finance team', 'PHISHING', 'HIGH', 'NEW', NOW(), 'alice@example.com', NULL),
('Suspected malware', 'Workstation shows unsigned process', 'MALWARE', 'CRITICAL', 'IN_PROGRESS', NOW(), 'bob@example.com', 'Investigating');
