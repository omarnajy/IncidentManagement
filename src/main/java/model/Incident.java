package model;

import java.util.Date;

public class Incident {
    private Long incidentId;
    private String title;
    private String description;
    private IncidentType type;
    private Risk risk;
    private Status status;
    private Date reportedDate;
    private String assignedTo;
    private String resolutionNotes;

    public Incident(Long incidentId, String title,
                    String description, IncidentType type,
                    Risk risk, Status status,
                    Date reportedDate, String assignedTo,
                    String resolutionNotes) {

        this.incidentId = incidentId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.risk = risk;
        this.status = status;
        this.reportedDate = reportedDate;
        this.assignedTo = assignedTo;
        this.resolutionNotes = resolutionNotes;
    }

    public Incident() {}

    public Long getIncidentId() {
        return incidentId;
    }
    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public IncidentType getType() {
        return type;
    }
    public void setType(IncidentType type) {
        this.type = type;
    }

    public Risk getRisk() {
        return risk;
    }
    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getReportedDate() {
        return reportedDate;
    }
    public void setReportedDate(Date reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getAssignedTo() {
        return assignedTo;
    }
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }
    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public enum IncidentType {
        PHISHING,
        MALWARE,
        DATA_BREACH,
        UNAUTHORIZED_ACCESS,
        DDOS,
        OTHER
    }

    public enum Risk {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW
    }

    public enum Status {
        NEW,
        IN_PROGRESS,
        RESOLVED,
        CLOSED
    }

    @Override
    public String toString() {
        return "Incident{" +
                "incidentId='" + incidentId + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", risk=" + risk +
                ", status=" + status +
                ", reportedDate=" + reportedDate +
                '}';
    }
}