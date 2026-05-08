package main;

// Autor java code
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {

    // Components / global variables
    private JLabel lblTitle;
    private JLabel lblSubtitle;
    private JLabel lblUser;
    private JLabel lblPassword;
    private JTextField txtUser;
    private JButton btnLogin;
    private JButton btnClear;
    private JPasswordField txtPassword;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;

    // Constructor
    public Login() {
        initComponents();
        initEvents();
    }

    // Component initializer
    private void initComponents() {
        setTitle("La Torre Supermarket");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(26, 82, 118)); // Dark blue

        lblTitle = new JLabel("LA TORRE SUPERMARKET");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 20, 420, 35);

        lblSubtitle = new JLabel("Management System");
        lblSubtitle.setForeground(Color.WHITE);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitle.setBounds(0, 55, 420, 35);

        mainPanel.add(lblTitle);
        mainPanel.add(lblSubtitle);

        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(40, 95, 340, 180);

        lblUser = new JLabel("User (Email)");
        lblUser.setFont(new Font("Arial", Font.BOLD, 13));
        lblUser.setForeground(new Color(26, 82, 118));
        lblUser.setBounds(25, 20, 200, 25);

        txtUser = new JTextField();
        txtUser.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUser.setBounds(25, 45, 290, 30);

        lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 13));
        lblPassword.setForeground(new Color(26, 82, 118));
        lblPassword.setBounds(25, 85, 200, 25);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPassword.setBounds(25, 110, 290, 30);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(26, 82, 118));
        buttonPanel.setBounds(40, 290, 340, 45);

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setBackground(new Color(76, 175, 80));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(0, 0, 160, 38);
        btnLogin.setFocusable(false);

        btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Arial", Font.BOLD, 13));
        btnClear.setBackground(new Color(158, 158, 158));
        btnClear.setForeground(Color.WHITE);
        btnClear.setBounds(180, 0, 160, 38);
        btnClear.setFocusable(false);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnClear);

        formPanel.add(lblUser);
        formPanel.add(txtUser);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);

        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
        add(mainPanel);
    }

    private void initEvents() {
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    validateLogin();
                }
            }
        });

        btnLogin.addActionListener(e -> validateLogin());

        btnClear.addActionListener(e -> {
            txtUser.setText("");
            txtPassword.setText("");
            txtUser.requestFocus();
        });
    }

    private void validateLogin() {
        String email    = txtUser.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Empty Fields",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection cn = DatabaseConnection.getConnection();

        if (cn == null) {
            JOptionPane.showMessageDialog(this,
                    "Could not connect to the database.\nVerify that MySQL is running.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "SELECT id_usuario, nombre, rol FROM usuarios " +
                         "WHERE email = ? AND contrasena = ? AND estado = 'activo'";

            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int    userId   = rs.getInt("id_usuario");
                String name     = rs.getString("nombre");
                String role     = rs.getString("rol");

                rs.close();
                ps.close();
                cn.close();

                this.dispose();

                switch (role) {
                    case "Administrador":
                        new AdminFrame(name).setVisible(true);
                        break;
                    case "Empleado":
                        new EmployeeFrame(name, userId).setVisible(true);
                        break;
                    case "Cliente":
                        new ClientFrame(name, userId).setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this,
                                "Role not recognized.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                }

            } else {
                rs.close();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this,
                        "Incorrect email or password.\nVerify your credentials.",
                        "Authentication Error",
                        JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtUser.requestFocus();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error querying the database:\n" + ex.getMessage(),
                    "SQL Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

