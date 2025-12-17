package view;

import controller.IncidentController;
import model.Incident;
import model.IncidentModel;
import dao.IncidentDAOImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private final IncidentController controller;
    private JTable incidentTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    // Palette de couleurs moderne
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color HEADER_COLOR = new Color(44, 62, 80);

    public MainWindow() {
        // Initialize controller
        IncidentModel model = new IncidentModel(new IncidentDAOImpl());
        controller = new IncidentController(model);

        setTitle(" Incident Management System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup UI
        initUI();

        // Load data
        loadTableData(controller.getAllIncidents());
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // HEADER
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // CENTER (Table + Buttons)
        JPanel centerContainer = new JPanel(new BorderLayout(0, 15));
        centerContainer.setBackground(BACKGROUND_COLOR);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel tablePanel = createTablePanel();
        centerContainer.add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        centerContainer.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerContainer, BorderLayout.CENTER);

        // STATUS BAR
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(" Incident Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Monitor and manage security incidents efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(189, 195, 199));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(HEADER_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        headerPanel.add(textPanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        "📊 Incidents List",
                        0,
                        0,
                        new Font("Segoe UI", Font.BOLD, 14),
                        PRIMARY_COLOR
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String[] columns = {"ID", "Title", "Type", "Risk", "Status", "Reported Date", "Assigned To"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        incidentTable = new JTable(tableModel);
        incidentTable.setAutoCreateRowSorter(true);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(incidentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void styleTable() {
        incidentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        incidentTable.setRowHeight(35);
        incidentTable.setShowGrid(true);
        incidentTable.setGridColor(new Color(220, 220, 220));
        incidentTable.setSelectionBackground(new Color(52, 152, 219, 100));
        incidentTable.setSelectionForeground(Color.BLACK);
        incidentTable.setIntercellSpacing(new Dimension(10, 5));

        JTableHeader header = incidentTable.getTableHeader();
        header.setBackground(SECONDARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        incidentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        incidentTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        incidentTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        incidentTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        incidentTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                if (!isSelected && value != null) {
                    String risk = value.toString();
                    switch (risk) {
                        case "CRITICAL":
                            c.setBackground(new Color(231, 76, 60, 50));
                            setForeground(DANGER_COLOR);
                            break;
                        case "HIGH":
                            c.setBackground(new Color(230, 126, 34, 50));
                            setForeground(new Color(230, 126, 34));
                            break;
                        case "MEDIUM":
                            c.setBackground(new Color(241, 196, 15, 50));
                            setForeground(new Color(243, 156, 18));
                            break;
                        case "LOW":
                            c.setBackground(new Color(46, 204, 113, 50));
                            setForeground(ACCENT_COLOR);
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        incidentTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        incidentTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        incidentTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        incidentTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        incidentTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        incidentTable.getColumnModel().getColumn(5).setPreferredWidth(180);
        incidentTable.getColumnModel().getColumn(6).setPreferredWidth(150);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addBtn = createStyledButton(" Add Incident", ACCENT_COLOR);
        JButton updateBtn = createStyledButton(" Update", PRIMARY_COLOR);
        JButton deleteBtn = createStyledButton(" Delete", DANGER_COLOR);
        JButton searchBtn = createStyledButton(" Search", new Color(155, 89, 182));
        JButton refreshBtn = createStyledButton(" Refresh", SECONDARY_COLOR);

        addBtn.addActionListener(e -> openAddDialog());
        updateBtn.addActionListener(e -> openUpdateDialog());
        deleteBtn.addActionListener(e -> deleteSelectedIncident());
        searchBtn.addActionListener(e -> openSearchDialog());
        refreshBtn.addActionListener(e -> {
            loadTableData(controller.getAllIncidents());
            JOptionPane.showMessageDialog(this, "✅ Data refreshed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(refreshBtn);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker().darker(), 1),
                        BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker(), 1),
                        BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
        });

        return button;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(HEADER_COLOR);
        statusBar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        statusLabel = new JLabel("Ready | Total Incidents: 0");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        statusBar.add(statusLabel, BorderLayout.WEST);

        return statusBar;
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
        updateStatusBar(incidents.size());
    }

    private void updateStatusBar(int count) {
        if (statusLabel != null) {
            statusLabel.setText(" Total Incidents: " + count);
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
            JOptionPane.showMessageDialog(this,
                    "⚠️ Please select an incident to update.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this,
                    "⚠️ Please select an incident to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this incident?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Long incidentId = (Long) tableModel.getValueAt(selectedRow, 0);
            controller.deleteIncident(incidentId);
            loadTableData(controller.getAllIncidents());
            JOptionPane.showMessageDialog(this, "✅ Incident deleted successfully!");
        }
    }

    private void openSearchDialog() {
        String keyword = JOptionPane.showInputDialog(this,
                "🔍 Enter keyword to search:",
                "Search Incidents",
                JOptionPane.QUESTION_MESSAGE);

        if (keyword != null && !keyword.isEmpty()) {
            List<Incident> results = controller.searchIncidents(keyword);
            loadTableData(results);
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No incidents found matching: " + keyword,
                        "Search Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }
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