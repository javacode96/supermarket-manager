package main;

// Autor java code
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminUsers extends JFrame {

    // â”€â”€ Components â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;

    private JLabel lblTitle;
    private JLabel lblName;
    private JLabel lblEmail;
    private JLabel lblRole;
    private JLabel lblStatus;
    private JLabel lblPassword;

    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JComboBox<String> cmbStatus;

    private JTable tblUsers;
    private JScrollPane tableScroll;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnBack;

    private DefaultTableModel tableModel;
    private int selectedId = -1;

    public AdminUsers(AdminFrame parent) {
        initComponents();
        initEvents();
        loadData();
    }

    private void initComponents() {
        setTitle("User Management - La Torre");
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
        headerPanel.setBackground(new Color(26, 82, 118)); // La Torre blue
        headerPanel.setBounds(0, 0, 900, 50);

        lblTitle = new JLabel("USER MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 12, 400, 28);

        headerPanel.add(lblTitle);

        // â”€â”€ FORM â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        formPanel.setBounds(20, 70, 860, 130);

        lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 12));
        lblName.setBounds(20, 15, 80, 25);

        txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 12));
        txtName.setBounds(100, 15, 200, 25);

        lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        lblEmail.setBounds(320, 15, 80, 25);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setBounds(400, 15, 200, 25);

        lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblPassword.setBounds(620, 15, 90, 25);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setBounds(710, 15, 130, 25);

        lblRole = new JLabel("Role:");
        lblRole.setFont(new Font("Arial", Font.BOLD, 12));
        lblRole.setBounds(20, 60, 80, 25);

        // Role values match the SQL ENUM
        cmbRole = new JComboBox<>(new String[]{"Administrador", "Empleado", "Cliente"});
        cmbRole.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbRole.setBounds(100, 60, 200, 25);

        lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatus.setBounds(320, 60, 80, 25);

        cmbStatus = new JComboBox<>(new String[]{"activo", "inactivo"});
        cmbStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbStatus.setBounds(400, 60, 200, 25);

        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        formPanel.add(lblRole);
        formPanel.add(cmbRole);
        formPanel.add(lblStatus);
        formPanel.add(cmbStatus);

        // â”€â”€ BUTTONS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBounds(20, 210, 860, 45);

        btnAdd    = createButton("Add",    new Color(76, 175, 80),   0);
        btnUpdate = createButton("Update", new Color(33, 150, 243), 110);
        btnDelete = createButton("Delete", new Color(244, 67, 54),  220);
        btnClear  = createButton("Clear",  new Color(158, 158, 158),330);
        btnBack   = createButton("Back",   new Color(96, 125, 139), 750);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        // â”€â”€ TABLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBounds(20, 270, 860, 350);

        String[] columns = {"ID", "Name", "Email", "Role", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblUsers = new JTable(tableModel);
        tblUsers.setFont(new Font("Arial", Font.PLAIN, 12));
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsers.getSelectionModel().addListSelectionListener(e -> loadSelectedData());

        tableScroll = new JScrollPane(tblUsers);
        tableScroll.setBounds(0, 0, 860, 350);

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

    // â”€â”€ LOAD data from DB â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadData() {
        tableModel.setRowCount(0);
        selectedId = -1;

        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql = "SELECT id_usuario, nombre, email, rol, estado FROM usuarios ORDER BY id_usuario";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("rol"),
                    rs.getString("estado")
                });
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading users:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // â”€â”€ Load selected row into form â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadSelectedData() {
        int row = tblUsers.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtEmail.setText((String) tableModel.getValueAt(row, 2));
            cmbRole.setSelectedItem((String) tableModel.getValueAt(row, 3));
            cmbStatus.setSelectedItem((String) tableModel.getValueAt(row, 4));
            txtPassword.setText("");
        }
    }

    private void initEvents() {

        // â”€â”€ ADD â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnAdd.addActionListener(e -> {
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "INSERT INTO usuarios (nombre, email, contrasena, rol, estado) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtName.getText().trim());
                ps.setString(2, txtEmail.getText().trim());
                ps.setString(3, new String(txtPassword.getPassword()).trim());
                ps.setString(4, (String) cmbRole.getSelectedItem());
                ps.setString(5, (String) cmbStatus.getSelectedItem());
                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this, "User added successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error adding user:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // â”€â”€ UPDATE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnUpdate.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a user from the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql;
                PreparedStatement ps;

                String newPassword = new String(txtPassword.getPassword()).trim();
                if (newPassword.isEmpty()) {
                    sql = "UPDATE usuarios SET nombre=?, email=?, rol=?, estado=? WHERE id_usuario=?";
                    ps = cn.prepareStatement(sql);
                    ps.setString(1, txtName.getText().trim());
                    ps.setString(2, txtEmail.getText().trim());
                    ps.setString(3, (String) cmbRole.getSelectedItem());
                    ps.setString(4, (String) cmbStatus.getSelectedItem());
                    ps.setInt(5, selectedId);
                } else {
                    sql = "UPDATE usuarios SET nombre=?, email=?, contrasena=?, rol=?, estado=? WHERE id_usuario=?";
                    ps = cn.prepareStatement(sql);
                    ps.setString(1, txtName.getText().trim());
                    ps.setString(2, txtEmail.getText().trim());
                    ps.setString(3, newPassword);
                    ps.setString(4, (String) cmbRole.getSelectedItem());
                    ps.setString(5, (String) cmbStatus.getSelectedItem());
                    ps.setInt(6, selectedId);
                }

                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this, "User updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating user:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // â”€â”€ DELETE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnDelete.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a user from the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int answer = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this user?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                try {
                    Connection cn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
                    PreparedStatement ps = cn.prepareStatement(sql);
                    ps.setInt(1, selectedId);
                    ps.executeUpdate();
                    ps.close();
                    cn.close();

                    JOptionPane.showMessageDialog(this, "User deleted successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting user:\n" + ex.getMessage(),
                            "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> this.dispose());
    }

    private boolean validateFields() {
        if (txtName.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and email are required.",
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
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        tblUsers.clearSelection();
        selectedId = -1;
    }
}

