package controller;

import java.util.Date;
import java.util.List;
import model.Incident;
import model.IncidentModel;

public class IncidentController {

    private final IncidentModel model;

    public IncidentController(IncidentModel model) {
        this.model = model;
    }

    // ADD INCIDENT
    public void addIncident(String title, String description,
                            Incident.IncidentType type, Incident.Risk risk,
                            Incident.Status status, Date reportedDate,
                            String assignedTo, String resolutionNotes) {

        // Basic validation
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be empty");

        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty");

        if (reportedDate == null) reportedDate = new Date(); // default to now

        model.addIncident(title, description, type, risk, status, reportedDate, assignedTo, resolutionNotes);
    }

    // UPDATE INCIDENT
    public void updateIncident(Incident incident) {
        if (incident.getIncidentId() == null)
            throw new IllegalArgumentException("Incident ID cannot be empty");

        model.updateIncident(incident);
    }

    // DELETE INCIDENT
    public void deleteIncident(Long incidentId) {
        if (incidentId == null)
            throw new IllegalArgumentException("Incident ID cannot be empty");

        model.deleteIncident(incidentId);
    }

    // GET INCIDENT BY ID
    public Incident getIncidentById(Long incidentId) {
        return model.getIncidentById(incidentId);
    }

    // GET ALL INCIDENTS
    public List<Incident> getAllIncidents() {
        return model.getAllIncidents();
    }


    // SEARCH INCIDENTS BY KEYWORD
    public List<Incident> searchIncidents(String keyword) {
        return model.searchIncidents(keyword);
    }


    // FILTER INCIDENTS
    public List<Incident> filterIncidents(Incident.Status status,
                                          Incident.Risk risk,
                                          Incident.IncidentType type) {
        return model.filterIncidents(status, risk, type);
    }
}
