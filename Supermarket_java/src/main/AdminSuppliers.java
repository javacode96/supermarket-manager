package main;

// Autor java code
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminSuppliers extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;

    private JLabel lblTitle;
    private JLabel lblNIT;
    private JLabel lblName;
    private JLabel lblPhone;
    private JLabel lblEmail;

    private JTextField txtNIT;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;

    private JTable tblSuppliers;
    private JScrollPane tableScroll;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnBack;

    private DefaultTableModel tableModel;
    private int selectedId = -1;

    public AdminSuppliers(AdminFrame parent) {
        initComponents();
        initEvents();
        loadData();
    }

    private void initComponents() {
        setTitle("Supplier Management - La Torre");
        setSize(900, 650);
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
        headerPanel.setBounds(0, 0, 900, 50);

        lblTitle = new JLabel("SUPPLIER MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 12, 400, 28);

        headerPanel.add(lblTitle);

        // â”€â”€ FORM â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        formPanel.setBounds(20, 70, 860, 100);

        lblNIT = new JLabel("NIT:");
        lblNIT.setFont(new Font("Arial", Font.BOLD, 12));
        lblNIT.setBounds(20, 15, 80, 25);

        txtNIT = new JTextField();
        txtNIT.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNIT.setBounds(100, 15, 200, 25);

        lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 12));
        lblName.setBounds(320, 15, 80, 25);

        txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 12));
        txtName.setBounds(400, 15, 250, 25);

        lblPhone = new JLabel("Phone:");
        lblPhone.setFont(new Font("Arial", Font.BOLD, 12));
        lblPhone.setBounds(20, 55, 80, 25);

        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPhone.setBounds(100, 55, 200, 25);

        lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        lblEmail.setBounds(320, 55, 80, 25);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setBounds(400, 55, 250, 25);

        formPanel.add(lblNIT);
        formPanel.add(txtNIT);
        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblPhone);
        formPanel.add(txtPhone);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);

        // â”€â”€ BUTTONS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBounds(20, 180, 860, 45);

        btnAdd    = createButton("Add",    new Color(76, 175, 80),   0);
        btnUpdate = createButton("Update", new Color(33, 150, 243), 110);
        btnDelete = createButton("Delete", new Color(244, 67, 54),  220);
        btnClear  = createButton("Clear",  new Color(158, 158, 158),330);
        btnBack   = createButton("Back",   new Color(96, 125, 139), 760);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        // â”€â”€ TABLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBounds(20, 240, 860, 380);

        String[] columns = {"ID", "NIT", "Name", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblSuppliers = new JTable(tableModel);
        tblSuppliers.setFont(new Font("Arial", Font.PLAIN, 12));
        tblSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSuppliers.getSelectionModel().addListSelectionListener(e -> loadSelectedData());

        tableScroll = new JScrollPane(tblSuppliers);
        tableScroll.setBounds(0, 0, 860, 380);

        tablePanel.add(tableScroll);

        mainPanel.add(headerPanel);
        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(tablePanel);

        add(mainPanel);
    }

    private JButton createButton(String text, Color color, int x) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBounds(x, 0, 100, 38);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }

    // â”€â”€ LOAD suppliers from DB â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadData() {
        tableModel.setRowCount(0);
        selectedId = -1;

        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql = "SELECT id_proveedor, nit, nombre, telefono, email " +
                         "FROM proveedores ORDER BY id_proveedor";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_proveedor"),
                    rs.getString("nit"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("email")
                });
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading suppliers:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // â”€â”€ Load selected row into form â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadSelectedData() {
        int row = tblSuppliers.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtNIT.setText((String) tableModel.getValueAt(row, 1));
            txtName.setText((String) tableModel.getValueAt(row, 2));
            txtPhone.setText((String) tableModel.getValueAt(row, 3));
            txtEmail.setText((String) tableModel.getValueAt(row, 4));
        }
    }

    private void initEvents() {

        // â”€â”€ ADD â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnAdd.addActionListener(e -> {
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "INSERT INTO proveedores (nit, nombre, telefono, email) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtNIT.getText().trim());
                ps.setString(2, txtName.getText().trim());
                ps.setString(3, txtPhone.getText().trim());
                ps.setString(4, txtEmail.getText().trim());
                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this, "Supplier added successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();

            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1062) {
                    JOptionPane.showMessageDialog(this,
                            "A supplier with that NIT already exists.",
                            "Duplicate NIT", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error adding supplier:\n" + ex.getMessage(),
                            "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // â”€â”€ UPDATE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnUpdate.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a supplier from the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "UPDATE proveedores SET nit=?, nombre=?, telefono=?, email=? WHERE id_proveedor=?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtNIT.getText().trim());
                ps.setString(2, txtName.getText().trim());
                ps.setString(3, txtPhone.getText().trim());
                ps.setString(4, txtEmail.getText().trim());
                ps.setInt(5, selectedId);
                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this, "Supplier updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating supplier:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // â”€â”€ DELETE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnDelete.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a supplier from the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int answer = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this supplier?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                try {
                    Connection cn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM proveedores WHERE id_proveedor = ?";
                    PreparedStatement ps = cn.prepareStatement(sql);
                    ps.setInt(1, selectedId);
                    ps.executeUpdate();
                    ps.close();
                    cn.close();

                    JOptionPane.showMessageDialog(this, "Supplier deleted successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting supplier:\n" + ex.getMessage(),
                            "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> this.dispose());
    }

    private boolean validateFields() {
        if (txtNIT.getText().trim().isEmpty()
                || txtName.getText().trim().isEmpty()
                || txtPhone.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in all fields.",
                    "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!txtEmail.getText().contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtNIT.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        tblSuppliers.clearSelection();
        selectedId = -1;
    }
}

