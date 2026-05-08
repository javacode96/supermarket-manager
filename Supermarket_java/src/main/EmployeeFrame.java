package main;

// Autor java code
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EmployeeFrame extends JFrame {

    private String currentUser;
    private int userId;

    private JPanel headerPanel;
    private JLabel lblWelcome;
    private JLabel lblRole;
    private JButton btnSales;
    private JButton btnProductSearch;
    private JButton btnLogout;

    public EmployeeFrame(String user, int userId) {
        this.currentUser = user;
        this.userId = userId;
        initComponents();
        initEvents();
    }

    private void initComponents() {
        setTitle("La Torre Supermarket - Employee Panel");
        setSize(520, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        // â”€â”€ HEADER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(26, 82, 118)); // La Torre blue
        headerPanel.setBounds(0, 0, 520, 80);

        lblWelcome = new JLabel("Welcome, " + currentUser);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBounds(20, 12, 350, 28);

        lblRole = new JLabel("Role: Employee");
        lblRole.setFont(new Font("Arial", Font.PLAIN, 13));
        lblRole.setForeground(new Color(180, 210, 230));
        lblRole.setBounds(20, 42, 200, 20);

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setBackground(new Color(198, 40, 40));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBounds(380, 22, 120, 35);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(BorderFactory.createEmptyBorder());

        headerPanel.add(lblWelcome);
        headerPanel.add(lblRole);
        headerPanel.add(btnLogout);

        // â”€â”€ TITLE â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        JLabel lblModules = new JLabel("AVAILABLE MODULES");
        lblModules.setFont(new Font("Arial", Font.BOLD, 14));
        lblModules.setForeground(new Color(60, 60, 60));
        lblModules.setHorizontalAlignment(SwingConstants.CENTER);
        lblModules.setBounds(0, 100, 520, 25);

        JSeparator sep = new JSeparator();
        sep.setBounds(50, 130, 420, 2);
        sep.setForeground(new Color(200, 200, 200));

        // â”€â”€ BUTTONS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        btnSales = createModuleButton("Sales Registry", new Color(0, 121, 107));
        btnSales.setBounds(60, 150, 180, 120);

        btnProductSearch = createModuleButton("Product Search", new Color(21, 101, 192));
        btnProductSearch.setBounds(280, 150, 180, 120);

        // â”€â”€ FOOTER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        JLabel lblFooter = new JLabel("La Torre Supermarket 2026 - Management System");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(150, 150, 150));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        lblFooter.setBounds(0, 330, 520, 20);

        mainPanel.add(headerPanel);
        mainPanel.add(lblModules);
        mainPanel.add(sep);
        mainPanel.add(btnSales);
        mainPanel.add(btnProductSearch);
        mainPanel.add(lblFooter);

        add(mainPanel);
    }

    private void initEvents() {
        btnSales.addActionListener(e ->
            new EmployeeSales(this, userId).setVisible(true));

        btnProductSearch.addActionListener(e ->
            new EmployeeSearch(this).setVisible(true));

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
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }
}

