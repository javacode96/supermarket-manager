package main;

// Autor java code
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeeSearch extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel searchPanel;
    private JPanel tablePanel;

    private JLabel lblTitle;
    private JLabel lblSearch;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnView;
    private JButton btnBack;

    private JTable tblProducts;
    private JScrollPane tableScroll;
    private DefaultTableModel tableModel;

    public EmployeeSearch(EmployeeFrame parent) {
        initComponents();
        initEvents();
        loadData("");
    }

    private void initComponents() {
        setTitle("Product Search - La Torre");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        // â”€â”€ HEADER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(21, 101, 192));
        headerPanel.setBounds(0, 0, 900, 50);

        lblTitle = new JLabel("PRODUCT CATALOG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 12, 400, 28);

        headerPanel.add(lblTitle);

        // â”€â”€ SEARCH â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        searchPanel.setBounds(20, 70, 860, 60);

        lblSearch = new JLabel("Search by name or code:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
        lblSearch.setBounds(20, 15, 200, 25);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 12));
        txtSearch.setBounds(220, 15, 400, 25);

        btnSearch = createButton("Search",      new Color(33, 150, 243), 630, 17, 25);
        btnView   = createButton("View Detail", new Color(76, 175, 80),  745, 17, 25);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnView);

        // â”€â”€ TABLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBounds(20, 145, 860, 410);

        String[] columns = {"ID", "Code", "Name", "Category", "Price", "Stock"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProducts = new JTable(tableModel);
        tblProducts.setFont(new Font("Arial", Font.PLAIN, 12));
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableScroll = new JScrollPane(tblProducts);
        tableScroll.setBounds(0, 0, 860, 410);

        tablePanel.add(tableScroll);

        btnBack = createButton("Back", new Color(96, 125, 139), 760, 565, 30);

        mainPanel.add(headerPanel);
        mainPanel.add(searchPanel);
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

    // â”€â”€ LOAD products from DB â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private void loadData(String term) {
        tableModel.setRowCount(0);

        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql;
            PreparedStatement ps;

            if (term.isEmpty()) {
                sql = "SELECT p.id_producto, p.codigo, p.nombre, " +
                      "       COALESCE(c.nombre, 'No category') AS categoria, " +
                      "       p.precio, p.stock " +
                      "FROM productos p " +
                      "LEFT JOIN categorias c ON p.id_categoria = c.id_categoria " +
                      "ORDER BY p.nombre";
                ps = cn.prepareStatement(sql);
            } else {
                sql = "SELECT p.id_producto, p.codigo, p.nombre, " +
                      "       COALESCE(c.nombre, 'No category') AS categoria, " +
                      "       p.precio, p.stock " +
                      "FROM productos p " +
                      "LEFT JOIN categorias c ON p.id_categoria = c.id_categoria " +
                      "WHERE p.nombre LIKE ? OR p.codigo LIKE ? " +
                      "ORDER BY p.nombre";
                ps = cn.prepareStatement(sql);
                ps.setString(1, "%" + term + "%");
                ps.setString(2, "%" + term + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_producto"),
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                });
            }

            rs.close();
            ps.close();
            cn.close();

            if (tableModel.getRowCount() == 0 && !term.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No products found with that criteria.",
                        "No Results", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error querying products:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initEvents() {

        btnSearch.addActionListener(e -> performSearch());

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });

        btnView.addActionListener(e -> {
            int row = tblProducts.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a product.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int    id          = (int)    tableModel.getValueAt(row, 0);
            String code        = (String) tableModel.getValueAt(row, 1);
            String name        = (String) tableModel.getValueAt(row, 2);
            String category    = (String) tableModel.getValueAt(row, 3);
            double price       = (double) tableModel.getValueAt(row, 4);
            int    stock       = (int)    tableModel.getValueAt(row, 5);

            // Fetch description from DB
            String description = "";
            try {
                Connection cn = DatabaseConnection.getConnection();
                PreparedStatement ps = cn.prepareStatement(
                    "SELECT descripcion FROM productos WHERE id_producto = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    description = rs.getString("descripcion") != null
                                  ? rs.getString("descripcion") : "No description";
                }
                rs.close();
                ps.close();
                cn.close();
            } catch (SQLException ex) {
                description = "Not available";
            }

            String detail = String.format(
                "Code:             %s\n" +
                "Name:             %s\n" +
                "Category:         %s\n" +
                "Description:      %s\n" +
                "--------------------------------\n" +
                "Price:            Q %.2f\n" +
                "Available Stock:  %d units",
                code, name, category, description,
                price, stock
            );

            JOptionPane.showMessageDialog(this, detail,
                    "Product Detail", JOptionPane.INFORMATION_MESSAGE);
        });

        btnBack.addActionListener(e -> this.dispose());
    }

    private void performSearch() {
        String term = txtSearch.getText().trim();
        loadData(term);
    }
}

