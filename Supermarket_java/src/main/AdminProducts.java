package main;

// Autor java code
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminProducts extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;

    private JLabel lblTitle;
    private JLabel lblCode;
    private JLabel lblName;
    private JLabel lblDescription;
    private JLabel lblPrice;
    private JLabel lblStock;

    private JTextField txtCode;
    private JTextField txtName;
    private JTextArea txtDescription;
    private JTextField txtPrice;
    private JTextField txtStock;

    private JTable tblProducts;
    private JScrollPane tableScroll;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnBack;

    private DefaultTableModel tableModel;
    private int selectedId = -1;

    public AdminProducts(AdminFrame parent) {
        initComponents();
        initEvents();
        loadData();
    }

    private void initComponents() {
        setTitle("Product Management - La Torre");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        // â”€â”€ HEADER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(0, 121, 107)); // Teal green
        headerPanel.setBounds(0, 0, 1000, 50);

        lblTitle = new JLabel("PRODUCT MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 12, 400, 28);

        headerPanel.add(lblTitle);

        // â”€â”€ FORM â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        formPanel.setBounds(20, 70, 960, 150);

        lblCode = new JLabel("Code:");
        lblCode.setFont(new Font("Arial", Font.BOLD, 12));
        lblCode.setBounds(20, 15, 80, 25);

        txtCode = new JTextField();
        txtCode.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCode.setBounds(100, 15, 150, 25);

        lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 12));
        lblName.setBounds(270, 15, 80, 25);

        txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 12));
        txtName.setBounds(350, 15, 250, 25);

        lblPrice = new JLabel("Price:");
        lblPrice.setFont(new Font("Arial", Font.BOLD, 12));
        lblPrice.setBounds(620, 15, 80, 25);

        txtPrice = new JTextField();
        txtPrice.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPrice.setBounds(700, 15, 130, 25);

        lblStock = new JLabel("Stock:");
        lblStock.setFont(new Font("Arial", Font.BOLD, 12));
        lblStock.setBounds(850, 15, 60, 25);

        txtStock = new JTextField();
        txtStock.setFont(new Font("Arial", Font.PLAIN, 12));
        txtStock.setBounds(910, 15, 40, 25);

        lblDescription = new JLabel("Description:");
        lblDescription.setFont(new Font("Arial", Font.BOLD, 12));
        lblDescription.setBounds(20, 55, 80, 25);

        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setBounds(100, 55, 850, 70);

        formPanel.add(lblCode);
        formPanel.add(txtCode);
        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblPrice);
        formPanel.add(txtPrice);
        formPanel.add(lblStock);
        formPanel.add(txtStock);
        formPanel.add(lblDescription);
        formPanel.add(scrollDesc);

        // â”€â”€ BUTTONS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBounds(20, 230, 960, 45);

        btnAdd    = createButton("Add",    new Color(76, 175, 80),   0);
        btnUpdate = createButton("Update", new Color(33, 150, 243), 110);
        btnDelete = createButton("Delete", new Color(244, 67, 54),  220);
        btnClear  = createButton("Clear",  new Color(158, 158, 158),330);
        btnBack   = createButton("Back",   new Color(96, 125, 139), 860);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        // â”€â”€ TABLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBounds(20, 290, 960, 390);

        String[] columns = {"ID", "Code", "Name", "Price", "Stock"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProducts = new JTable(tableModel);
        tblProducts.setFont(new Font("Arial", Font.PLAIN, 12));
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProducts.getSelectionModel().addListSelectionListener(e -> loadSelectedData());

        tableScroll = new JScrollPane(tblProducts);
        tableScroll.setBounds(0, 0, 960, 390);

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

    // â”€â”€ LOAD products from DB â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadData() {
        tableModel.setRowCount(0);
        selectedId = -1;

        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql = "SELECT id_producto, codigo, nombre, precio, stock " +
                         "FROM productos ORDER BY id_producto";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_producto"),
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                });
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading products:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // â”€â”€ Load selected row into form â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadSelectedData() {
        int row = tblProducts.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtCode.setText((String) tableModel.getValueAt(row, 1));
            txtName.setText((String) tableModel.getValueAt(row, 2));
            txtPrice.setText(String.valueOf(tableModel.getValueAt(row, 3)));
            txtStock.setText(String.valueOf(tableModel.getValueAt(row, 4)));

            // Load description from DB (not shown in the table)
            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "SELECT descripcion FROM productos WHERE id_producto = ?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setInt(1, selectedId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    txtDescription.setText(rs.getString("descripcion"));
                }
                rs.close();
                ps.close();
                cn.close();
            } catch (SQLException ex) {
                txtDescription.setText("");
            }
        }
    }

    private void initEvents() {

        // â”€â”€ ADD â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnAdd.addActionListener(e -> {
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "INSERT INTO productos (codigo, nombre, descripcion, precio, stock) " +
                             "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtCode.getText().trim());
                ps.setString(2, txtName.getText().trim());
                ps.setString(3, txtDescription.getText().trim());
                ps.setDouble(4, Double.parseDouble(txtPrice.getText().trim()));
                ps.setInt(5, Integer.parseInt(txtStock.getText().trim()));
                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this, "Product added successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();

            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1062) {
                    JOptionPane.showMessageDialog(this,
                            "A product with that code already exists.",
                            "Duplicate Code", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error adding product:\n" + ex.getMessage(),
                            "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // â”€â”€ UPDATE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnUpdate.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a product from the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "UPDATE productos SET codigo=?, nombre=?, descripcion=?, " +
                             "precio=?, stock=? WHERE id_producto=?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtCode.getText().trim());
                ps.setString(2, txtName.getText().trim());
                ps.setString(3, txtDescription.getText().trim());
                ps.setDouble(4, Double.parseDouble(txtPrice.getText().trim()));
                ps.setInt(5, Integer.parseInt(txtStock.getText().trim()));
                ps.setInt(6, selectedId);
                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this, "Product updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating product:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // â”€â”€ DELETE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnDelete.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a product from the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int answer = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this product?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                try {
                    Connection cn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM productos WHERE id_producto = ?";
                    PreparedStatement ps = cn.prepareStatement(sql);
                    ps.setInt(1, selectedId);
                    ps.executeUpdate();
                    ps.close();
                    cn.close();

                    JOptionPane.showMessageDialog(this, "Product deleted successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();

                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 1451) {
                        JOptionPane.showMessageDialog(this,
                                "Cannot delete: the product has registered sales.",
                                "FK Error", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error deleting product:\n" + ex.getMessage(),
                                "DB Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> this.dispose());
    }

    private boolean validateFields() {
        if (txtCode.getText().trim().isEmpty()
                || txtName.getText().trim().isEmpty()
                || txtPrice.getText().trim().isEmpty()
                || txtStock.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in all required fields.",
                    "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            double price = Double.parseDouble(txtPrice.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            if (price < 0 || stock < 0) {
                JOptionPane.showMessageDialog(this, "Price and stock cannot be negative.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price and stock must be valid numbers.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtCode.setText("");
        txtName.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        tblProducts.clearSelection();
        selectedId = -1;
    }
}

