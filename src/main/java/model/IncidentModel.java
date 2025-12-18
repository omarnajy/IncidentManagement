package model;

import dao.IncidentDAOImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IncidentModel {

    private final IncidentDAOImpl dao;
    private List<Incident> incidents;

    public IncidentModel(IncidentDAOImpl dao) {
        this.dao = dao;
        this.incidents = dao.findAll(); // Load existing incidents from DB
    }

    // GET ALL INCIDENTS
    public List<Incident> getAllIncidents() {
        incidents = dao.findAll(); // refresh from DB
        return incidents;
    }

    // ADD INCIDENT
    public void addIncident(String title, String description,
                            Incident.IncidentType type, Incident.Risk risk,
                            Incident.Status status, Date reportedDate,
                            String assignedTo, String resolutionNotes) {
        Incident incident = new Incident(null, title, description, type, risk, status,
                reportedDate, assignedTo, resolutionNotes);

        // dao.add will return the generated database ID; the DAO implementation should set it on the entity
        Long generatedId = dao.add(incident);
        if (generatedId != null) incident.setIncidentId(generatedId);
        incidents.add(incident);
    }

    // UPDATE INCIDENT
    public void updateIncident(Incident incident) {
        // FIX: Removed the second parameter (idIgnored) to match the new DAO signature.
        dao.update(incident);

        // Refresh local list
        for (int i = 0; i < incidents.size(); i++) {
            if (incidents.get(i).getIncidentId().equals(incident.getIncidentId())) {
                incidents.set(i, incident);
                break;
            }
        }
    }

    // DELETE INCIDENT
    public void deleteIncident(Long incidentId) {
        dao.delete(incidentId);
        incidents.removeIf(i -> i.getIncidentId() != null && i.getIncidentId().equals(incidentId));
    }

    // GET INCIDENT BY ID
    public Incident getIncidentById(Long incidentId) {
        return dao.findById(incidentId);
    }

    // SEARCH INCIDENTS
    public List<Incident> searchIncidents(String keyword) {
        if (keyword == null || keyword.isEmpty()) return getAllIncidents();

        List<Incident> results = new ArrayList<>();
        for (Incident incident : incidents) {
                if ((incident.getIncidentId() != null && incident.getIncidentId().toString().contains(keyword)) ||
                    incident.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    incident.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                    (incident.getAssignedTo() != null &&
                            incident.getAssignedTo().toLowerCase().contains(keyword.toLowerCase()))) {

                results.add(incident);
            }
        }
        return results;
    }

    // FILTER INCIDENTS
    public List<Incident> filterIncidents(Incident.Status status,
                                          Incident.Risk risk,
                                          Incident.IncidentType type) {
        List<Incident> filtered = new ArrayList<>();
        for (Incident incident : incidents) {
            boolean matches = true;
            if (status != null && incident.getStatus() != status) matches = false;
            if (risk != null && incident.getRisk() != risk) matches = false;
            if (type != null && incident.getType() != type) matches = false;

            if (matches) filtered.add(incident);
        }
        return filtered;
    }
}