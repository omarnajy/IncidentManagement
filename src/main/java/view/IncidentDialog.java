package view;

import controller.IncidentController;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import model.Incident;

public class IncidentDialog extends JDialog {

    private JTextField idField, titleField, assignedToField;
    private JTextArea descriptionArea, resolutionArea;
    private JComboBox<Incident.IncidentType> typeBox;
    private JComboBox<Incident.Risk> riskBox;
    private JComboBox<Incident.Status> statusBox;
    private JFormattedTextField dateField;

    private IncidentController controller;
    private Incident incident;

    // Palette de couleurs
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);

    public IncidentDialog(JFrame parent, IncidentController controller, Incident incident) {
        super(parent, true);
        this.controller = controller;
        this.incident = incident;

        setTitle(incident == null ? "➕ Add New Incident" : "✏️ Update Incident");
        setSize(600, 750);
        setLocationRelativeTo(parent);
        setResizable(false);

        initUI();
        if (incident != null) loadIncidentData();
        else {
            idField.setText("(auto-generated)");
            idField.setEditable(false);
            idField.setEnabled(false);
        }
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //  HEADER 
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        //  FORM PANEL 
        JPanel formPanel = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        //  BUTTON PANEL 
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(SECONDARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        String emoji = incident == null ? "➕" : "";
        String title = incident == null ? "Create New Incident" : "Update Incident Details";

        JLabel titleLabel = new JLabel(emoji + " " + title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Fill in the incident information below");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(189, 195, 199));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(SECONDARY_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        headerPanel.add(textPanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Initialize fields
        idField = new JTextField();
        titleField = new JTextField();
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        resolutionArea = new JTextArea(4, 20);
        resolutionArea.setLineWrap(true);
        resolutionArea.setWrapStyleWord(true);

        assignedToField = new JTextField();
        typeBox = new JComboBox<>(Incident.IncidentType.values());
        riskBox = new JComboBox<>(Incident.Risk.values());
        statusBox = new JComboBox<>(Incident.Status.values());
        dateField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        dateField.setValue(new Date());

        // Style components
        styleTextField(idField);
        styleTextField(titleField);
        styleTextField(assignedToField);
        styleTextField(dateField);
        styleTextArea(descriptionArea);
        styleTextArea(resolutionArea);
        styleComboBox(typeBox);
        styleComboBox(riskBox);
        styleComboBox(statusBox);

        // Add sections
        formPanel.add(createSection("🆔 Identification",
                new Component[]{
                        createFieldPanel("Incident ID:", idField)
                }));

        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(createSection("📋 Basic Information",
                new Component[]{
                        createFieldPanel("Title:", titleField),
                        createFieldPanel("Description:", new JScrollPane(descriptionArea))
                }));

        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(createSection("🏷️ Classification",
                new Component[]{
                        createFieldPanel("Type:", typeBox),
                        createFieldPanel("Risk Level:", riskBox),
                        createFieldPanel("Status:", statusBox)
                }));

        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(createSection("📅 Assignment & Details",
                new Component[]{
                        createFieldPanel("Reported Date:", dateField),
                        createFieldPanel("Assigned To:", assignedToField),
                        createFieldPanel("Resolution Notes:", new JScrollPane(resolutionArea))
                }));

        return formPanel;
    }

    private JPanel createSection(String title, Component[] components) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                PRIMARY_COLOR
        );
        section.setBorder(BorderFactory.createCompoundBorder(
                border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        for (Component comp : components) {
            section.add(comp);
            section.add(Box.createVerticalStrut(10));
        }

        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, section.getPreferredSize().height));
        return section;
    }

    private JPanel createFieldPanel(String labelText, Component field) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(SECONDARY_COLOR);

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void styleTextArea(JTextArea area) {
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        box.setBackground(Color.WHITE);
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton saveBtn = createStyledButton(
                incident == null ? " Save Incident" : " Update Incident",
                ACCENT_COLOR
        );
        JButton cancelBtn = createStyledButton(" Cancel", DANGER_COLOR);

        saveBtn.addActionListener(e -> saveIncident());
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void loadIncidentData() {
        idField.setText(incident.getIncidentId() == null ? "" : incident.getIncidentId().toString());
        idField.setEditable(false);
        idField.setEnabled(false);
        titleField.setText(incident.getTitle());
        descriptionArea.setText(incident.getDescription());
        typeBox.setSelectedItem(incident.getType());
        riskBox.setSelectedItem(incident.getRisk());
        statusBox.setSelectedItem(incident.getStatus());
        dateField.setValue(incident.getReportedDate());
        assignedToField.setText(incident.getAssignedTo());
        resolutionArea.setText(incident.getResolutionNotes());
    }

    private void saveIncident() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        Incident.IncidentType type = (Incident.IncidentType) typeBox.getSelectedItem();
        Incident.Risk risk = (Incident.Risk) riskBox.getSelectedItem();
        Incident.Status status = (Incident.Status) statusBox.getSelectedItem();
        Date reportedDate = (Date) dateField.getValue();
        String assignedTo = assignedToField.getText().trim();
        String resolutionNotes = resolutionArea.getText().trim();

        // Validation
        if (title.isEmpty()) {
            showError("Title is required!");
            return;
        }
        if (description.isEmpty()) {
            showError("Description is required!");
            return;
        }

        try {
            if (incident == null) {
                controller.addIncident(title, description, type, risk, status,
                        reportedDate, assignedTo, resolutionNotes);
                showSuccess("Incident created successfully!");
            } else {
                incident.setTitle(title);
                incident.setDescription(description);
                incident.setType(type);
                incident.setRisk(risk);
                incident.setStatus(status);
                incident.setReportedDate(reportedDate);
                incident.setAssignedTo(assignedTo);
                incident.setResolutionNotes(resolutionNotes);
                controller.updateIncident(incident);
                showSuccess("Incident updated successfully!");
            }
            dispose();
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                "⚠️ " + message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
                "✅ " + message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}