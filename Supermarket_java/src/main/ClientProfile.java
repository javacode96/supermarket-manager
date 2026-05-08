package main;

// Autor java code
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ClientProfile extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel dataPanel;

    private JLabel lblTitle;
    private JLabel lblName;
    private JLabel lblEmail;
    private JLabel lblPhone;
    private JLabel lblPassword;
    private JLabel lblCurrentPassword;

    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;

    private JButton btnUpdate;
    private JButton btnCancel;
    private JButton btnBack;

    private int userId;

    public ClientProfile(ClientFrame parent, int userId) {
        this.userId = userId;
        initComponents();
        initEvents();
        loadData();
    }

    private void initComponents() {
        setTitle("My Profile - La Torre");
        setSize(700, 600);
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
        headerPanel.setBounds(0, 0, 700, 50);

        lblTitle = new JLabel("MY PROFILE");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 12, 400, 28);

        headerPanel.add(lblTitle);

        // â”€â”€ DATA PANEL â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        dataPanel = new JPanel();
        dataPanel.setLayout(null);
        dataPanel.setBackground(Color.WHITE);
        dataPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        dataPanel.setBounds(20, 70, 560, 420);

        JLabel lblPersonalSection = new JLabel("Personal Information");
        lblPersonalSection.setFont(new Font("Arial", Font.BOLD, 13));
        lblPersonalSection.setForeground(new Color(26, 82, 118));
        lblPersonalSection.setBounds(20, 15, 150, 25);

        JSeparator sep1 = new JSeparator();
        sep1.setBounds(20, 42, 520, 1);
        sep1.setForeground(new Color(200, 200, 200));

        lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 12));
        lblName.setBounds(20, 55, 100, 25);

        txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 12));
        txtName.setBounds(120, 55, 400, 25);

        lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        lblEmail.setBounds(20, 90, 100, 25);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setBounds(120, 90, 400, 25);

        lblPhone = new JLabel("Phone:");
        lblPhone.setFont(new Font("Arial", Font.BOLD, 12));
        lblPhone.setBounds(20, 125, 100, 25);

        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPhone.setBounds(120, 125, 400, 25);

        JLabel lblPasswordSection = new JLabel("Change Password");
        lblPasswordSection.setFont(new Font("Arial", Font.BOLD, 13));
        lblPasswordSection.setForeground(new Color(26, 82, 118));
        lblPasswordSection.setBounds(20, 165, 200, 25);

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(20, 192, 520, 1);
        sep2.setForeground(new Color(200, 200, 200));

        lblCurrentPassword = new JLabel("Current Password:");
        lblCurrentPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblCurrentPassword.setBounds(20, 205, 150, 25);

        txtCurrentPassword = new JPasswordField();
        txtCurrentPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCurrentPassword.setBounds(180, 205, 340, 25);

        lblPassword = new JLabel("New Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblPassword.setBounds(20, 240, 150, 25);

        txtNewPassword = new JPasswordField();
        txtNewPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNewPassword.setBounds(180, 240, 340, 25);

        JLabel lblConfirm = new JLabel("Confirm Password:");
        lblConfirm.setFont(new Font("Arial", Font.BOLD, 12));
        lblConfirm.setBounds(20, 275, 150, 25);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtConfirmPassword.setBounds(180, 275, 340, 25);

        JLabel lblInfo = new JLabel("(Leave blank if you do not want to change the password)");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 10));
        lblInfo.setForeground(new Color(120, 120, 120));
        lblInfo.setBounds(180, 305, 340, 15);

        dataPanel.add(lblPersonalSection);
        dataPanel.add(sep1);
        dataPanel.add(lblName);
        dataPanel.add(txtName);
        dataPanel.add(lblEmail);
        dataPanel.add(txtEmail);
        dataPanel.add(lblPhone);
        dataPanel.add(txtPhone);
        dataPanel.add(lblPasswordSection);
        dataPanel.add(sep2);
        dataPanel.add(lblCurrentPassword);
        dataPanel.add(txtCurrentPassword);
        dataPanel.add(lblPassword);
        dataPanel.add(txtNewPassword);
        dataPanel.add(lblConfirm);
        dataPanel.add(txtConfirmPassword);
        dataPanel.add(lblInfo);

        btnUpdate = createButton("Save Changes", new Color(76, 175, 80),   50, 0);
        btnCancel = createButton("Cancel",       new Color(158, 158, 158), 280, 0);
        btnBack   = createButton("Back",         new Color(96, 125, 139),  480, 0);

        mainPanel.add(headerPanel);
        mainPanel.add(dataPanel);
        mainPanel.add(btnUpdate);
        mainPanel.add(btnCancel);
        mainPanel.add(btnBack);

        add(mainPanel);
    }

    private JButton createButton(String text, Color color, int x, int y) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBounds(20 + x, 505 + y, 150, 35);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }

    private void loadData() {
        try {
            Connection cn = DatabaseConnection.getConnection();
            String sql = "SELECT nombre, email, telefono FROM usuarios WHERE id_usuario = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("nombre"));
                txtEmail.setText(rs.getString("email"));
                txtPhone.setText(rs.getString("telefono") != null ? rs.getString("telefono") : "");
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading profile data:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initEvents() {

        btnUpdate.addActionListener(e -> {
            if (!validateFields()) return;

            try {
                Connection cn = DatabaseConnection.getConnection();
                String sql;
                PreparedStatement ps;

                String newPass = new String(txtNewPassword.getPassword()).trim();

                if (newPass.isEmpty()) {
                    sql = "UPDATE usuarios SET nombre=?, email=?, telefono=? WHERE id_usuario=?";
                    ps = cn.prepareStatement(sql);
                    ps.setString(1, txtName.getText().trim());
                    ps.setString(2, txtEmail.getText().trim());
                    ps.setString(3, txtPhone.getText().trim());
                    ps.setInt(4, userId);
                } else {
                    String currentPass = new String(txtCurrentPassword.getPassword()).trim();
                    PreparedStatement psVerify = cn.prepareStatement(
                        "SELECT id_usuario FROM usuarios WHERE id_usuario=? AND contrasena=?");
                    psVerify.setInt(1, userId);
                    psVerify.setString(2, currentPass);
                    ResultSet rsVerify = psVerify.executeQuery();

                    if (!rsVerify.next()) {
                        rsVerify.close();
                        psVerify.close();
                        cn.close();
                        JOptionPane.showMessageDialog(this,
                                "The current password is incorrect.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    rsVerify.close();
                    psVerify.close();

                    sql = "UPDATE usuarios SET nombre=?, email=?, telefono=?, contrasena=? WHERE id_usuario=?";
                    ps = cn.prepareStatement(sql);
                    ps.setString(1, txtName.getText().trim());
                    ps.setString(2, txtEmail.getText().trim());
                    ps.setString(3, txtPhone.getText().trim());
                    ps.setString(4, newPass);
                    ps.setInt(5, userId);
                }

                ps.executeUpdate();
                ps.close();
                cn.close();

                JOptionPane.showMessageDialog(this,
                        "Profile updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                txtCurrentPassword.setText("");
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating profile:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> {
            loadData();
            txtCurrentPassword.setText("");
            txtNewPassword.setText("");
            txtConfirmPassword.setText("");
        });

        btnBack.addActionListener(e -> this.dispose());
    }

    private boolean validateFields() {
        if (txtName.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name and email are required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!txtEmail.getText().contains("@")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid email.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String newPass  = new String(txtNewPassword.getPassword());
        String confirm  = new String(txtConfirmPassword.getPassword());

        if (!newPass.isEmpty() || !confirm.isEmpty()) {
            if (new String(txtCurrentPassword.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Enter the current password to change it.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this,
                        "The new password must have at least 6 characters.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (!newPass.equals(confirm)) {
                JOptionPane.showMessageDialog(this,
                        "Passwords do not match.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }
}

