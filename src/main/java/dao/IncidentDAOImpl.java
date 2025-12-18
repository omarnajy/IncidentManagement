package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Incident;
import model.Incident.IncidentType;
import model.Incident.Risk;
import model.Incident.Status;

public class IncidentDAOImpl implements GenericDAO<Incident> {


    public IncidentDAOImpl() {
    }

    // Helper method to get and close connection
    private Connection getConnection() {
        return DBconnection.getConnection();
    }

    // FIND ALL (Connection fixed)
    @Override
    public List<Incident> findAll() {
        List<Incident> list = new ArrayList<>();
        String sql = "SELECT * FROM incidents";

        // Connection is obtained and placed in the try-with-resources block
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToIncident(rs));
            }

        } catch (SQLException e) {
            // Improved error reporting (use a proper logging framework in production)
            System.err.println("Error while fetching all incidents: " + e.getMessage());
        }
        return list;
    }

    // FIND BY ID (Connection fixed)
    @Override
    public Incident findById(Long id) {
        String sql = "SELECT * FROM incidents WHERE incident_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToIncident(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching incident by ID: " + e.getMessage());
        }
        return null;
    }

    // INSERT (Connection fixed)
    @Override
    public Long add(Incident incident) {
        String sql = "INSERT INTO incidents " +
                "(title, description, type, risk, status, reported_date, assigned_to, resolution_notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, incident.getTitle());
            ps.setString(2, incident.getDescription());
            ps.setString(3, incident.getType().name());
            ps.setString(4, incident.getRisk().name());
            ps.setString(5, incident.getStatus().name());
            ps.setTimestamp(6, new Timestamp(incident.getReportedDate().getTime()));
            ps.setString(7, incident.getAssignedTo());
            ps.setString(8, incident.getResolutionNotes());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long generatedId = keys.getLong(1);
                    incident.setIncidentId(generatedId);
                    return generatedId;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while adding incident: " + e.getMessage());
        }
        return null;
    }

    // UPDATE (Signature and Connection fixed)
    @Override
    public void update(Incident incident) {
        String sql = "UPDATE incidents SET title=?, description=?, type=?, risk=?, status=?, " +
                "reported_date=?, assigned_to=?, resolution_notes=? WHERE incident_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, incident.getTitle());
            ps.setString(2, incident.getDescription());
            ps.setString(3, incident.getType().name());
            ps.setString(4, incident.getRisk().name());
            ps.setString(5, incident.getStatus().name());
            ps.setTimestamp(6, new Timestamp(incident.getReportedDate().getTime()));
            ps.setString(7, incident.getAssignedTo());
            ps.setString(8, incident.getResolutionNotes());
            ps.setLong(9, incident.getIncidentId()); // The ID is used for the WHERE clause

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating incident: " + e.getMessage());
        }
    }

    // DELETE (Connection fixed)
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM incidents WHERE incident_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while deleting incident: " + e.getMessage());
        }
    }

    // RESULTSET MAPPING (Unchanged, remains correct)
    private Incident mapResultSetToIncident(ResultSet rs) throws SQLException {
       
        return new Incident(
            rs.getLong("incident_id"),
                rs.getString("title"),
                rs.getString("description"),
                IncidentType.valueOf(rs.getString("type")),
                Risk.valueOf(rs.getString("risk")),
                Status.valueOf(rs.getString("status")),
                rs.getTimestamp("reported_date"),
                rs.getString("assigned_to"),
                rs.getString("resolution_notes")
        );
    }
}