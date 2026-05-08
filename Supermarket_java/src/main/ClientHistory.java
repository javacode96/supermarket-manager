package main;

// Autor java code
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ClientHistory extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel filterPanel;
    private JPanel tablePanel;

    private JLabel lblTitle;
    private JLabel lblDateFrom;
    private JLabel lblDateTo;

    private JTextField txtDateFrom;
    private JTextField txtDateTo;
    private JButton btnFilter;
    private JButton btnViewDetail;
    private JButton btnBack;

    private JTable tblHistory;
    private JScrollPane tableScroll;
    private DefaultTableModel tableModel;

    private int clientId;

    public ClientHistory(ClientFrame parent, int clientId) {
        this.clientId = clientId;
        initComponents();
        initEvents();
        loadData(null, null);
    }

    private void initComponents() {
        setTitle("Purchase History - La Torre");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        // â”€â”€ HEADER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(230, 81, 0)); // Orange
        headerPanel.setBounds(0, 0, 950, 50);

        lblTitle = new JLabel("PURCHASE HISTORY");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 12, 400, 28);

        headerPanel.add(lblTitle);

        // â”€â”€ FILTERS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        filterPanel = new JPanel();
        filterPanel.setLayout(null);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        filterPanel.setBounds(20, 70, 910, 60);

        lblDateFrom = new JLabel("From:");
        lblDateFrom.setFont(new Font("Arial", Font.BOLD, 12));
        lblDateFrom.setBounds(20, 18, 60, 25);

        txtDateFrom = new JTextField("2024-01-01");
        txtDateFrom.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDateFrom.setBounds(80, 18, 120, 25);

        lblDateTo = new JLabel("To:");
        lblDateTo.setFont(new Font("Arial", Font.BOLD, 12));
        lblDateTo.setBounds(220, 18, 60, 25);

        txtDateTo = new JTextField("2026-12-31");
        txtDateTo.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDateTo.setBounds(280, 18, 120, 25);

        JLabel lblFormat = new JLabel("(Format: YYYY-MM-DD)");
        lblFormat.setFont(new Font("Arial", Font.ITALIC, 10));
        lblFormat.setForeground(new Color(120, 120, 120));
        lblFormat.setBounds(415, 22, 140, 18);

        btnFilter     = createButton("Filter",      new Color(33, 150, 243), 560, 18, 25);
        btnViewDetail = createButton("View Detail", new Color(76, 175, 80),  680, 18, 25);

        filterPanel.add(lblDateFrom);
        filterPanel.add(txtDateFrom);
        filterPanel.add(lblDateTo);
        filterPanel.add(txtDateTo);
        filterPanel.add(lblFormat);
        filterPanel.add(btnFilter);
        filterPanel.add(btnViewDetail);

        // â”€â”€ TABLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBounds(20, 145, 910, 410);

        String[] columns = {"Sale ID", "Date", "Product", "Quantity", "Unit Price", "Subtotal"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHistory = new JTable(tableModel);
        tblHistory.setFont(new Font("Arial", Font.PLAIN, 12));
        tblHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableScroll = new JScrollPane(tblHistory);
        tableScroll.setBounds(0, 0, 910, 410);

        tablePanel.add(tableScroll);

        btnBack = createButton("Back", new Color(96, 125, 139), 820, 0, 35);

        mainPanel.add(headerPanel);
        mainPanel.add(filterPanel);
        mainPanel.add(tablePanel);
        mainPanel.add(btnBack);

        add(mainPanel);
    }

    private JButton createButton(String text, Color color, int x, int y, int h) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBounds(x, y, 110, h);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }

    // â”€â”€ LOAD history from DB â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadData(String from, String to) {
        tableModel.setRowCount(0);

        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql;
            PreparedStatement ps;

            if (from != null && to != null) {
                sql = "SELECT v.id_venta, v.fecha, p.nombre, dv.cantidad, " +
                      "       dv.precio_unitario, dv.subtotal " +
                      "FROM ventas v " +
                      "JOIN detalle_venta dv ON v.id_venta = dv.id_venta " +
                      "JOIN productos p      ON dv.id_producto = p.id_producto " +
                      "WHERE v.id_cliente = ? " +
                      "  AND DATE(v.fecha) BETWEEN ? AND ? " +
                      "ORDER BY v.fecha DESC";
                ps = cn.prepareStatement(sql);
                ps.setInt(1, clientId);
                ps.setString(2, from);
                ps.setString(3, to);
            } else {
                sql = "SELECT v.id_venta, v.fecha, p.nombre, dv.cantidad, " +
                      "       dv.precio_unitario, dv.subtotal " +
                      "FROM ventas v " +
                      "JOIN detalle_venta dv ON v.id_venta = dv.id_venta " +
                      "JOIN productos p      ON dv.id_producto = p.id_producto " +
                      "WHERE v.id_cliente = ? " +
                      "ORDER BY v.fecha DESC";
                ps = cn.prepareStatement(sql);
                ps.setInt(1, clientId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_venta"),
                    rs.getString("fecha"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    String.format("Q %.2f", rs.getDouble("precio_unitario")),
                    String.format("Q %.2f", rs.getDouble("subtotal"))
                });
            }

            rs.close();
            ps.close();
            cn.close();

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No purchases found in that date range.",
                        "No Results", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading history:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initEvents() {

        btnFilter.addActionListener(e -> {
            String from = txtDateFrom.getText().trim();
            String to   = txtDateTo.getText().trim();

            if (from.isEmpty() || to.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Enter both dates to filter.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            loadData(from, to);
        });

        btnViewDetail.addActionListener(e -> {
            int row = tblHistory.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a purchase.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int    saleId    = (int)    tableModel.getValueAt(row, 0);
            String date      = (String) tableModel.getValueAt(row, 1);
            String product   = (String) tableModel.getValueAt(row, 2);
            int    quantity  = (int)    tableModel.getValueAt(row, 3);
            String unitPrice = (String) tableModel.getValueAt(row, 4);
            String subtotal  = (String) tableModel.getValueAt(row, 5);

            String saleTotal = "N/A";
            try {
                Connection cn = DatabaseConnection.getConnection();
                PreparedStatement ps = cn.prepareStatement(
                        "SELECT total FROM ventas WHERE id_venta = ?");
                ps.setInt(1, saleId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    saleTotal = String.format("Q %.2f", rs.getDouble("total"));
                }
                rs.close();
                ps.close();
                cn.close();
            } catch (SQLException ex) {
                // show N/A if query fails
            }

            String detail = String.format(
                "Sale ID:            %d\n" +
                "Date:               %s\n" +
                "--------------------------------\n" +
                "Product:            %s\n" +
                "Quantity:           %d units\n" +
                "Unit price:         %s\n" +
                "Subtotal:           %s\n" +
                "--------------------------------\n" +
                "TOTAL SALE:         %s",
                saleId, date, product, quantity,
                unitPrice, subtotal, saleTotal
            );

            JOptionPane.showMessageDialog(this, detail,
                    "Purchase Detail", JOptionPane.INFORMATION_MESSAGE);
        });

        btnBack.addActionListener(e -> this.dispose());
    }
}

