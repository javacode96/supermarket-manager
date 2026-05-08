package main;

// Autor java code
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeeSales extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel detailPanel;
    private JPanel buttonPanel;

    private JLabel lblTitle;
    private JLabel lblClient;
    private JLabel lblProduct;
    private JLabel lblQuantity;
    private JLabel lblSubtotal;
    private JLabel lblTax;
    private JLabel lblTotal;

    private JComboBox<String> cmbClient;
    private JComboBox<String> cmbProduct;
    private JTextField txtQuantity;
    private JTextField txtSubtotal;
    private JTextField txtTax;
    private JTextField txtTotal;

    private JButton btnAddProduct;
    private JButton btnRemoveProduct;
    private JButton btnRegisterSale;
    private JButton btnClear;
    private JButton btnBack;

    private JTable tblDetail;
    private JScrollPane tableScroll;
    private DefaultTableModel tableModel;

    // IDs for client and product combos
    private java.util.List<Integer> clientIds  = new java.util.ArrayList<>();
    private java.util.List<Integer> productIds = new java.util.ArrayList<>();

    // ID of the logged-in employee
    private int employeeId;

    private static final double TAX_RATE = 0.12;

    public EmployeeSales(EmployeeFrame parent, int employeeId) {
        this.employeeId = employeeId;
        initComponents();
        initEvents();
        loadClients();
        loadProducts();
    }

    private void initComponents() {
        setTitle("Sales Registry - La Torre");
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

        lblTitle = new JLabel("SALES REGISTRY");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 12, 400, 28);

        headerPanel.add(lblTitle);

        // â”€â”€ FORM â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        formPanel.setBounds(20, 70, 960, 100);

        lblClient = new JLabel("Client:");
        lblClient.setFont(new Font("Arial", Font.BOLD, 12));
        lblClient.setBounds(20, 15, 80, 25);

        cmbClient = new JComboBox<>();
        cmbClient.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbClient.setBounds(100, 15, 280, 25);

        lblProduct = new JLabel("Product:");
        lblProduct.setFont(new Font("Arial", Font.BOLD, 12));
        lblProduct.setBounds(400, 15, 80, 25);

        cmbProduct = new JComboBox<>();
        cmbProduct.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbProduct.setBounds(480, 15, 320, 25);

        lblQuantity = new JLabel("Quantity:");
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 12));
        lblQuantity.setBounds(820, 15, 70, 25);

        txtQuantity = new JTextField("1");
        txtQuantity.setFont(new Font("Arial", Font.PLAIN, 12));
        txtQuantity.setBounds(890, 15, 60, 25);

        btnAddProduct = new JButton("+ Add");
        btnAddProduct.setFont(new Font("Arial", Font.BOLD, 12));
        btnAddProduct.setBackground(new Color(76, 175, 80));
        btnAddProduct.setForeground(Color.WHITE);
        btnAddProduct.setBounds(20, 55, 120, 32);
        btnAddProduct.setFocusPainted(false);
        btnAddProduct.setBorder(BorderFactory.createEmptyBorder());
        btnAddProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRemoveProduct = new JButton("- Remove");
        btnRemoveProduct.setFont(new Font("Arial", Font.BOLD, 12));
        btnRemoveProduct.setBackground(new Color(244, 67, 54));
        btnRemoveProduct.setForeground(Color.WHITE);
        btnRemoveProduct.setBounds(150, 55, 120, 32);
        btnRemoveProduct.setFocusPainted(false);
        btnRemoveProduct.setBorder(BorderFactory.createEmptyBorder());
        btnRemoveProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formPanel.add(lblClient);
        formPanel.add(cmbClient);
        formPanel.add(lblProduct);
        formPanel.add(cmbProduct);
        formPanel.add(lblQuantity);
        formPanel.add(txtQuantity);
        formPanel.add(btnAddProduct);
        formPanel.add(btnRemoveProduct);

        // â”€â”€ DETAIL TABLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        detailPanel = new JPanel();
        detailPanel.setLayout(null);
        detailPanel.setBackground(new Color(245, 245, 245));
        detailPanel.setBounds(20, 185, 960, 380);

        String[] columns = {"Product ID", "Name", "Quantity", "Unit Price", "Subtotal"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDetail = new JTable(tableModel);
        tblDetail.setFont(new Font("Arial", Font.PLAIN, 12));
        tblDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableScroll = new JScrollPane(tblDetail);
        tableScroll.setBounds(0, 0, 960, 280);

        // Totals
        lblSubtotal = new JLabel("Subtotal:");
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 13));
        lblSubtotal.setBounds(620, 295, 80, 25);

        txtSubtotal = new JTextField("Q 0.00");
        txtSubtotal.setFont(new Font("Arial", Font.BOLD, 13));
        txtSubtotal.setEditable(false);
        txtSubtotal.setBackground(new Color(240, 240, 240));
        txtSubtotal.setBounds(700, 295, 120, 25);

        lblTax = new JLabel("Tax (12%):");
        lblTax.setFont(new Font("Arial", Font.BOLD, 13));
        lblTax.setBounds(620, 330, 80, 25);

        txtTax = new JTextField("Q 0.00");
        txtTax.setFont(new Font("Arial", Font.BOLD, 13));
        txtTax.setEditable(false);
        txtTax.setBackground(new Color(240, 240, 240));
        txtTax.setBounds(700, 330, 120, 25);

        lblTotal = new JLabel("TOTAL:");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotal.setForeground(new Color(0, 121, 107));
        lblTotal.setBounds(620, 365, 80, 25);

        txtTotal = new JTextField("Q 0.00");
        txtTotal.setFont(new Font("Arial", Font.BOLD, 15));
        txtTotal.setForeground(new Color(0, 121, 107));
        txtTotal.setEditable(false);
        txtTotal.setBackground(new Color(240, 240, 240));
        txtTotal.setBounds(700, 365, 120, 25);

        detailPanel.add(tableScroll);
        detailPanel.add(lblSubtotal);
        detailPanel.add(txtSubtotal);
        detailPanel.add(lblTax);
        detailPanel.add(txtTax);
        detailPanel.add(lblTotal);
        detailPanel.add(txtTotal);

        // â”€â”€ BUTTONS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBounds(20, 175, 960, 5);

        btnRegisterSale = createButton("Register Sale", new Color(0, 121, 107), 20);
        btnClear        = createButton("Clear",         new Color(158, 158, 158), 180);
        btnBack         = createButton("Back",          new Color(96, 125, 139), 860);

        mainPanel.add(headerPanel);
        mainPanel.add(formPanel);
        mainPanel.add(detailPanel);
        mainPanel.add(btnRegisterSale);
        mainPanel.add(btnClear);
        mainPanel.add(btnBack);

        add(mainPanel);
    }

    private JButton createButton(String text, Color color, int x) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBounds(x, 635, 150, 38);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }

    // â”€â”€ LOAD clients into combo â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadClients() {
        cmbClient.removeAllItems();
        clientIds.clear();

        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql = "SELECT id_usuario, nombre FROM usuarios " +
                         "WHERE rol = 'Cliente' AND estado = 'activo' ORDER BY nombre";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                clientIds.add(rs.getInt("id_usuario"));
                cmbClient.addItem(rs.getString("nombre"));
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading clients:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // â”€â”€ LOAD products into combo â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadProducts() {
        cmbProduct.removeAllItems();
        productIds.clear();

        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql = "SELECT id_producto, nombre, precio, stock FROM productos " +
                         "WHERE stock > 0 ORDER BY nombre";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productIds.add(rs.getInt("id_producto"));
                cmbProduct.addItem(rs.getString("nombre") +
                    " - Q" + String.format("%.2f", rs.getDouble("precio")) +
                    " (Stock: " + rs.getInt("stock") + ")");
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

    // â”€â”€ CALCULATE totals from table â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void calculateTotals() {
        double subtotal = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            subtotal += (double) tableModel.getValueAt(i, 4);
        }
        double tax   = subtotal * TAX_RATE;
        double total = subtotal + tax;

        txtSubtotal.setText(String.format("Q %.2f", subtotal));
        txtTax.setText(String.format("Q %.2f", tax));
        txtTotal.setText(String.format("Q %.2f", total));
    }

    private void initEvents() {

        // â”€â”€ ADD PRODUCT TO DETAIL â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnAddProduct.addActionListener(e -> {
            if (cmbProduct.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "Select a product.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(txtQuantity.getText().trim());
                if (quantity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "The quantity must be a positive number.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int productId = productIds.get(cmbProduct.getSelectedIndex());

            // Verify available stock and get price
            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql = "SELECT nombre, precio, stock FROM productos WHERE id_producto = ?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String name      = rs.getString("nombre");
                    double price     = rs.getDouble("precio");
                    int    available = rs.getInt("stock");

                    if (quantity > available) {
                        JOptionPane.showMessageDialog(this,
                                "Insufficient stock. Available: " + available + " units.",
                                "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                        rs.close(); ps.close(); cn.close();
                        return;
                    }

                    // Check if product is already in the table
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if ((int) tableModel.getValueAt(i, 0) == productId) {
                            JOptionPane.showMessageDialog(this,
                                    "This product is already in the sale.\nRemove it and add it again with the correct quantity.",
                                    "Duplicate", JOptionPane.WARNING_MESSAGE);
                            rs.close(); ps.close(); cn.close();
                            return;
                        }
                    }

                    double subtotal = price * quantity;
                    tableModel.addRow(new Object[]{
                        productId, name, quantity, price, subtotal
                    });

                    calculateTotals();
                }

                rs.close();
                ps.close();
                cn.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error verifying product:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // â”€â”€ REMOVE PRODUCT FROM DETAIL â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnRemoveProduct.addActionListener(e -> {
            int row = tblDetail.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a product from the list.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            tableModel.removeRow(row);
            calculateTotals();
        });

        // â”€â”€ REGISTER SALE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnRegisterSale.addActionListener(e -> {
            if (cmbClient.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "Select a client.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Add at least one product to the sale.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int answer = JOptionPane.showConfirmDialog(this,
                    "Confirm sale for " + txtTotal.getText() + "?",
                    "Confirm Sale", JOptionPane.YES_NO_OPTION);

            if (answer != JOptionPane.YES_OPTION) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                cn.setAutoCommit(false); // start transaction

                // Calculate amounts
                double subtotal = 0;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    subtotal += (double) tableModel.getValueAt(i, 4);
                }
                double tax   = subtotal * TAX_RATE;
                double total = subtotal + tax;

                int clientId = clientIds.get(cmbClient.getSelectedIndex());

                // 1. Insert sale header
                String sqlSale = "INSERT INTO ventas (id_cliente, id_empleado, subtotal, iva, total) " +
                                  "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psSale = cn.prepareStatement(sqlSale,
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                psSale.setInt(1, clientId);
                psSale.setInt(2, employeeId);
                psSale.setDouble(3, subtotal);
                psSale.setDouble(4, tax);
                psSale.setDouble(5, total);
                psSale.executeUpdate();

                // Get generated sale ID
                ResultSet rsKeys = psSale.getGeneratedKeys();
                int saleId = 0;
                if (rsKeys.next()) {
                    saleId = rsKeys.getInt(1);
                }
                rsKeys.close();
                psSale.close();

                // 2. Insert each detail line and update stock
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int    productId  = (int)    tableModel.getValueAt(i, 0);
                    int    quantity   = (int)    tableModel.getValueAt(i, 2);
                    double unitPrice  = (double) tableModel.getValueAt(i, 3);
                    double lineTotal  = (double) tableModel.getValueAt(i, 4);

                    // Insert detail
                    String sqlDetail = "INSERT INTO detalle_venta " +
                                        "(id_venta, id_producto, cantidad, precio_unitario, subtotal) " +
                                        "VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement psDetail = cn.prepareStatement(sqlDetail);
                    psDetail.setInt(1, saleId);
                    psDetail.setInt(2, productId);
                    psDetail.setInt(3, quantity);
                    psDetail.setDouble(4, unitPrice);
                    psDetail.setDouble(5, lineTotal);
                    psDetail.executeUpdate();
                    psDetail.close();

                    // Deduct stock
                    String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";
                    PreparedStatement psStock = cn.prepareStatement(sqlStock);
                    psStock.setInt(1, quantity);
                    psStock.setInt(2, productId);
                    psStock.executeUpdate();
                    psStock.close();
                }

                cn.commit(); // confirm transaction
                cn.setAutoCommit(true);
                cn.close();

                JOptionPane.showMessageDialog(this,
                        "Sale registered successfully!\n" +
                        "Sale ID: " + saleId + "\n" +
                        "Total charged: " + String.format("Q %.2f", total),
                        "Successful Sale", JOptionPane.INFORMATION_MESSAGE);

                clearSale();
                loadProducts(); // refresh stock in combo

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error registering sale:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
                try {
                    Connection cn = DatabaseConnection.getConnection();
                    cn.rollback();
                    cn.close();
                } catch (SQLException ignored) {}
            }
        });

        btnClear.addActionListener(e -> clearSale());
        btnBack.addActionListener(e -> this.dispose());
    }

    private void clearSale() {
        tableModel.setRowCount(0);
        txtQuantity.setText("1");
        txtSubtotal.setText("Q 0.00");
        txtTax.setText("Q 0.00");
        txtTotal.setText("Q 0.00");
        if (cmbClient.getItemCount() > 0) cmbClient.setSelectedIndex(0);
        if (cmbProduct.getItemCount() > 0) cmbProduct.setSelectedIndex(0);
    }
}

