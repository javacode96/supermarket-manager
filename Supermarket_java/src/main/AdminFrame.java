package main;

// Autor java code
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminFrame extends JFrame {

    private String currentUser;

    // Components
    private JPanel headerPanel;
    private JPanel centerPanel;
    private JLabel lblWelcome;
    private JLabel lblRole;

    private JButton btnUsers;
    private JButton btnProducts;
    private JButton btnCategories;
    private JButton btnSuppliers;
    private JButton btnLogout;

    // Constructor
    public AdminFrame(String user) {
        this.currentUser = user;
        initComponents();
        initEvents();
    }

    private void initComponents() {
        setTitle("La Torre Supermarket - Admin Panel");
        setSize(600, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        // â”€â”€ HEADER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(26, 82, 118)); // Dark blue
        headerPanel.setBounds(0, 0, 600, 80);

        lblWelcome = new JLabel("Welcome, " + currentUser);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBounds(20, 12, 400, 28);

        lblRole = new JLabel("Role: Administrator");
        lblRole.setFont(new Font("Arial", Font.PLAIN, 13));
        lblRole.setForeground(new Color(180, 210, 230));
        lblRole.setBounds(20, 42, 200, 20);

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setBackground(new Color(198, 40, 40));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBounds(460, 22, 120, 35);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(BorderFactory.createEmptyBorder());

        headerPanel.add(lblWelcome);
        headerPanel.add(lblRole);
        headerPanel.add(btnLogout);

        // â”€â”€ MODULES TITLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        JLabel lblModules = new JLabel("AVAILABLE MODULES");
        lblModules.setFont(new Font("Arial", Font.BOLD, 14));
        lblModules.setForeground(new Color(60, 60, 60));
        lblModules.setHorizontalAlignment(SwingConstants.CENTER);
        lblModules.setBounds(0, 100, 600, 25);

        JSeparator sep = new JSeparator();
        sep.setBounds(50, 130, 500, 2);
        sep.setForeground(new Color(200, 200, 200));

        // â”€â”€ BUTTON PANEL â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBounds(60, 145, 480, 240);

        btnUsers      = createModuleButton("User Management",     new Color(21, 101, 192));
        btnProducts   = createModuleButton("Product Management",  new Color(0, 121, 107));
        btnCategories = createModuleButton("Category Management", new Color(130, 0, 130));
        btnSuppliers  = createModuleButton("Supplier Management", new Color(230, 81, 0));

        btnUsers.setBounds(0, 0, 225, 95);
        btnProducts.setBounds(255, 0, 225, 95);
        btnCategories.setBounds(0, 120, 225, 95);
        btnSuppliers.setBounds(255, 120, 225, 95);

        centerPanel.add(btnUsers);
        centerPanel.add(btnProducts);
        centerPanel.add(btnCategories);
        centerPanel.add(btnSuppliers);

        // â”€â”€ FOOTER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        JLabel lblFooter = new JLabel("La Torre Supermarket 2026 - Management System");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(150, 150, 150));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        lblFooter.setBounds(0, 420, 600, 20);

        mainPanel.add(headerPanel);
        mainPanel.add(lblModules);
        mainPanel.add(sep);
        mainPanel.add(centerPanel);
        mainPanel.add(lblFooter);

        add(mainPanel);
    }

    private void initEvents() {
        btnUsers.addActionListener(e ->
            new AdminUsers(this).setVisible(true));

        btnProducts.addActionListener(e ->
            new AdminProducts(this).setVisible(true));

        btnCategories.addActionListener(e ->
            new AdminCategories(this).setVisible(true));

        btnSuppliers.addActionListener(e ->
            new AdminSuppliers(this).setVisible(true));

        btnLogout.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                this.dispose();
                new Login().setVisible(true);
            }
        });
    }

    private JButton createModuleButton(String text, Color color) {
        JButton btn = new JButton("<html><center>" + text + "</center></html>");
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());

        btn.addMouseListener(new MouseAdapter() {
            Color original = color;
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(original.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }
}

