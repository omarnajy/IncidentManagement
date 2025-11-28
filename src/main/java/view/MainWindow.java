package view;

import controller.IncidentController;
import model.Incident;
import model.IncidentModel;
import dao.IncidentDAOImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainWindow extends JFrame {

    private IncidentController controller;
    private JTable incidentTable;
    private DefaultTableModel tableModel;

    public MainWindow() {
        // Initialize controller
        IncidentModel model = new IncidentModel(new IncidentDAOImpl());
        controller = new IncidentController(model);

        setTitle("Incident Management System");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup UI
        initUI();

        // Load data
        loadTableData(controller.getAllIncidents());
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Table
        String[] columns = {"ID", "Title", "Type", "Risk", "Status", "Reported Date", "Assigned To"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        incidentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(incidentTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Incident");
        JButton updateBtn = new JButton("Update Incident");
        JButton deleteBtn = new JButton("Delete Incident");
        JButton searchBtn = new JButton("Search");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button actions
        addBtn.addActionListener(e -> openAddDialog());
        updateBtn.addActionListener(e -> openUpdateDialog());
        deleteBtn.addActionListener(e -> deleteSelectedIncident());
        searchBtn.addActionListener(e -> openSearchDialog());
    }

    private void loadTableData(List<Incident> incidents) {
        tableModel.setRowCount(0);
        for (Incident i : incidents) {
            tableModel.addRow(new Object[]{
                    i.getIncidentId(),
                    i.getTitle(),
                    i.getType(),
                    i.getRisk(),
                    i.getStatus(),
                    i.getReportedDate(),
                    i.getAssignedTo()
            });
        }
    }

    private void openAddDialog() {
        IncidentDialog dialog = new IncidentDialog(this, controller, null);
        dialog.setVisible(true);
        loadTableData(controller.getAllIncidents());
    }

    private void openUpdateDialog() {
        int selectedRow = incidentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an incident to update.");
            return;
        }
        Long incidentId = (Long) tableModel.getValueAt(selectedRow, 0);
        Incident incident = controller.getIncidentById(incidentId);
        IncidentDialog dialog = new IncidentDialog(this, controller, incident);
        dialog.setVisible(true);
        loadTableData(controller.getAllIncidents());
    }

    private void deleteSelectedIncident() {
        int selectedRow = incidentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an incident to delete.");
            return;
        }
        Long incidentId = (Long) tableModel.getValueAt(selectedRow, 0);
        controller.deleteIncident(incidentId);
        loadTableData(controller.getAllIncidents());
    }

    private void openSearchDialog() {
        String keyword = JOptionPane.showInputDialog(this, "Enter keyword to search:");
        if (keyword != null && !keyword.isEmpty()) {
            List<Incident> results = controller.searchIncidents(keyword);
            loadTableData(results);
        } else {
            loadTableData(controller.getAllIncidents());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }


}
