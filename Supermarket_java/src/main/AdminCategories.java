package main;

// Autor java code
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminCategories extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;

    private JLabel lblTitle;
    private JLabel lblName;
    private JLabel lblDescription;

    private JTextField txtName;
    private JTextArea txtDescription;

    private JTable tblCategories;
    private JScrollPane tableScroll;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnBack;

    private DefaultTableModel tableModel;
    private int selectedId = -1; // tracks the real DB ID

    public AdminCategories(AdminFrame parent) {
        initComponents();
        initEvents();
        loadData();
    }

    private void initComponents() {
        setTitle("Category Management - La Torre");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        // â”€â”€ HEADER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(130, 0, 130));
        headerPanel.setBounds(0, 0, 750, 50);

        lblTitle = new JLabel("CATEGORY MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 12, 400, 28);

        headerPanel.add(lblTitle);

        // â”€â”€ FORM â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        formPanel.setBounds(20, 70, 710, 120);

        lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 12));
        lblName.setBounds(20, 15, 80, 25);

        txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 12));
        txtName.setBounds(100, 15, 600, 25);

        lblDescription = new JLabel("Description:");
        lblDescription.setFont(new Font("Arial", Font.BOLD, 12));
        lblDescription.setBounds(20, 50, 80, 25);

        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setBounds(100, 50, 600, 60);

        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblDescription);
        formPanel.add(scrollDesc);

        // â”€â”€ BUTTONS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBounds(20, 200, 710, 45);

        btnAdd    = createButton("Add",    new Color(76, 175, 80),   0);
        btnUpdate = createButton("Update", new Color(33, 150, 243), 110);
        btnDelete = createButton("Delete", new Color(244, 67, 54),  220);
        btnClear  = createButton("Clear",  new Color(158, 158, 158),330);
        btnBack   = createButton("Back",   new Color(96, 125, 139), 610);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        // â”€â”€ TABLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBounds(20, 260, 710, 310);

        String[] columns = {"ID", "Name", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCategories = new JTable(tableModel);
        tblCategories.setFont(new Font("Arial", Font.PLAIN, 12));
        tblCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCategories.getSelectionModel().addListSelectionListener(e -> loadSelectedData());

        tableScroll = new JScrollPane(tblCategories);
        tableScroll.setBounds(0, 0, 710, 310);

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

    // â”€â”€ LOAD from DB â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadData() {
        tableModel.setRowCount(0);
        selectedId = -1;

        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql = "SELECT id_categoria, nombre, descripcion FROM categorias ORDER BY id_categoria";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_categoria"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                });
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading categories:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // â”€â”€ Load selected row into form â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadSelectedData() {
        int row = tblCategories.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtDescription.setText((String) tableModel.getValueAt(row, 2));
        }
    }

    private void initEvents() {

        // â”€â”€ ADD â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnAdd.addActionListener(e -> {
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtName.getText().trim());
                ps.setString(2, txtDescription.getText().trim());
                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this, "Category added successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error adding category:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // â”€â”€ UPDATE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnUpdate.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a category from the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "UPDATE categorias SET nombre=?, descripcion=? WHERE id_categoria=?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtName.getText().trim());
                ps.setString(2, txtDescription.getText().trim());
                ps.setInt(3, selectedId);
                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this, "Category updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating category:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // â”€â”€ DELETE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnDelete.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a category from the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int answer = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this category?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                try {
                    Connection cn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM categorias WHERE id_categoria = ?";
                    PreparedStatement ps = cn.prepareStatement(sql);
                    ps.setInt(1, selectedId);
                    ps.executeUpdate();
                    ps.close();
                    cn.close();

                    JOptionPane.showMessageDialog(this, "Category deleted successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();

                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 1451) {
                        JOptionPane.showMessageDialog(this,
                                "Cannot delete: the category has associated products.",
                                "FK Error", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error deleting category:\n" + ex.getMessage(),
                                "DB Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> this.dispose());
    }

    private boolean validateFields() {
        if (txtName.getText().trim().isEmpty()
                || txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in all fields.",
                    "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtName.setText("");
        txtDescription.setText("");
        tblCategories.clearSelection();
        selectedId = -1;
    }
}

