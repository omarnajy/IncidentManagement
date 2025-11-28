# IncidentManagementSystem — DB-generated IDs

This small Java Swing app manages 'incidents' and persists them in MySQL. The project now uses database-generated numeric IDs for `incident_id` (BIGINT AUTO_INCREMENT) and the application expects the DB to create the ID on INSERT.

## SQL schema (create DB + table)
Run these statements in MySQL (adjust user/permission as needed):

```sql
CREATE DATABASE IF NOT EXISTS gestion_employee;
USE gestion_employee;

CREATE TABLE IF NOT EXISTS incidents (
  incident_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  type VARCHAR(50),
  risk VARCHAR(50),
  status VARCHAR(50),
  reported_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  assigned_to VARCHAR(255),
  resolution_notes TEXT
);
```

If you already have an `incidents` table using VARCHAR `incident_id`, convert it to auto-increment numeric ID like this **(make a backup first)**:

```sql
ALTER TABLE incidents DROP PRIMARY KEY;
ALTER TABLE incidents MODIFY COLUMN incident_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY;
```

> Note: Adjust statements per your current schema and MySQL version. Back up data before running destructive changes.

## What changed in code
- `model.Incident` now uses `Long incidentId` instead of `String`.
- `dao.GenericDAO<T>`: `add(...)` returns `Long` generated id; `findById` and `delete` now accept `Long` IDs.
- `dao.IncidentDAOImpl` uses `Statement.RETURN_GENERATED_KEYS` on `INSERT` and sets the generated ID on the entity.
- `controller.IncidentController.addIncident` no longer requires an ID (DB generates it).
- UI (`view.IncidentDialog` and `MainWindow`) no longer ask users to enter an ID when creating a new incident; shows "(auto-generated)".

## Running
1. Create the database/table as shown above and update `dao/DBconnection.java` with the right JDBC URL and credentials.
2. Build/run from your IDE or add a Maven/Gradle wrapper to compile and run.

If you'd like, I can also:
- Add a `pom.xml` so the project is buildable via Maven,
- Add migration tooling (Flyway), or
- Add unit/integration tests for these changes.
