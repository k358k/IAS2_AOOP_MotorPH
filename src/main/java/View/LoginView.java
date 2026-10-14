package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JPasswordField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import Service.AuthenticationService;
import View.EmployeeDashboardView;

public class LoginView extends JFrame {

    private AuthenticationService authenticationService = new AuthenticationService();
    private static final long serialVersionUID = 1L;
    private JPanel contentPane, panelImage, panelTitle;
    private JLayeredPane loginPane, innerLoginPane, imagePane;
    private JTextField textEmpID_login;
    private JPasswordField passwordField;
    private JLabel lblMotorPh, lblEmployeeID, lblPassword, lblLogin;
    private JButton btnLogin;
    private JLabel lblImage;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginView frame = new LoginView(); // Corrected class name
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginView() {
        initialize();
    }
     

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(140, 10, 1300, 830);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 252, 251));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setResizable(false);

        // Initialize panels
        panelImage = new JPanel();
        imagePane = new JLayeredPane();

        loginPane = new JLayeredPane();
        innerLoginPane = new JLayeredPane();
        panelTitle = new JPanel();

        // RIGHT SIDE
        // Image Pane
        imagePane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        imagePane.setBounds(516, 0, 768, 791);
        contentPane.add(imagePane);
        imagePane.setLayout(new GridLayout(0, 1, 0, 0));

        lblImage = new JLabel("");
        // lblImage.setIcon(DefaultTheme.monoMotorcycleImage);
        imagePane.add(lblImage);

        // Image Panel
        panelImage.setBackground(new Color(41, 43, 44));
        panelImage.setBounds(516, 0, 768, 791);
        contentPane.add(panelImage);

        // LEFT SIDE
        // Login Pane
        loginPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        loginPane.setBounds(0, 0, 513, 791);
        contentPane.add(loginPane);
        SpringLayout sl_loginPane = new SpringLayout();
        loginPane.setLayout(sl_loginPane);

        // Inner Login Pane
        sl_loginPane.putConstraint(SpringLayout.NORTH, innerLoginPane, 61, SpringLayout.NORTH, loginPane);
        sl_loginPane.putConstraint(SpringLayout.WEST, innerLoginPane, 51, SpringLayout.WEST, loginPane);
        sl_loginPane.putConstraint(SpringLayout.SOUTH, innerLoginPane, 693, SpringLayout.NORTH, loginPane);
        sl_loginPane.putConstraint(SpringLayout.EAST, innerLoginPane, 439, SpringLayout.WEST, loginPane);
        innerLoginPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        loginPane.add(innerLoginPane);
        SpringLayout sl_innerLoginPane = new SpringLayout();
        innerLoginPane.setLayout(sl_innerLoginPane);

        // Title Panel
        sl_loginPane.putConstraint(SpringLayout.NORTH, panelTitle, 52, SpringLayout.NORTH, loginPane);
        sl_loginPane.putConstraint(SpringLayout.WEST, panelTitle, 36, SpringLayout.WEST, loginPane);
        sl_loginPane.putConstraint(SpringLayout.SOUTH, panelTitle, 109, SpringLayout.NORTH, loginPane);
        sl_loginPane.putConstraint(SpringLayout.EAST, panelTitle, 199, SpringLayout.WEST, loginPane);
        loginPane.add(panelTitle);
        panelTitle.setBackground(new Color(238, 68, 68));

        // Labels
        lblMotorPh = new JLabel("MotorPH");
        sl_innerLoginPane.putConstraint(SpringLayout.NORTH, lblMotorPh, 0, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.WEST, lblMotorPh, 0, SpringLayout.WEST, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.SOUTH, lblMotorPh, 49, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.EAST, lblMotorPh, 314, SpringLayout.WEST, innerLoginPane);
        lblMotorPh.setForeground(new Color(255, 252, 251));
        lblMotorPh.setBackground(new Color(255, 252, 251));
        lblMotorPh.setFont(new Font("Yu Gothic", Font.PLAIN, 23));
        innerLoginPane.add(lblMotorPh);

        lblEmployeeID = new JLabel("Employee ID:");
        sl_innerLoginPane.putConstraint(SpringLayout.NORTH, lblEmployeeID, 363, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.WEST, lblEmployeeID, 0, SpringLayout.WEST, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.EAST, lblEmployeeID, 114, SpringLayout.WEST, innerLoginPane);
        lblEmployeeID.setForeground(new Color(41, 43, 44));
        innerLoginPane.add(lblEmployeeID);
        lblEmployeeID.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 15));

        lblPassword = new JLabel("Password:");
        sl_innerLoginPane.putConstraint(SpringLayout.NORTH, lblPassword, 442, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.WEST, lblPassword, 0, SpringLayout.WEST, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.EAST, lblPassword, 114, SpringLayout.WEST, innerLoginPane);
        lblPassword.setForeground(new Color(41, 43, 44));
        lblPassword.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 15));
        innerLoginPane.add(lblPassword);

        lblLogin = new JLabel("LOG IN");
        sl_innerLoginPane.putConstraint(SpringLayout.NORTH, lblLogin, 230, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.WEST, lblLogin, 0, SpringLayout.WEST, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.EAST, lblLogin, 114, SpringLayout.WEST, innerLoginPane);
        lblLogin.setForeground(new Color(41, 43, 44));
        innerLoginPane.add(lblLogin);
        lblLogin.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 30));

        // Text Fields
        textEmpID_login = new JTextField();
        sl_innerLoginPane.putConstraint(SpringLayout.NORTH, textEmpID_login, 358, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.WEST, textEmpID_login, 117, SpringLayout.WEST, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.SOUTH, textEmpID_login, 388, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.EAST, textEmpID_login, 378, SpringLayout.WEST, innerLoginPane);
        innerLoginPane.add(textEmpID_login);
        textEmpID_login.setColumns(10);

        passwordField = new JPasswordField();
        sl_innerLoginPane.putConstraint(SpringLayout.NORTH, passwordField, 437, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.WEST, passwordField, 117, SpringLayout.WEST, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.SOUTH, passwordField, 467, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.EAST, passwordField, 378, SpringLayout.WEST, innerLoginPane);
        passwordField.setColumns(10);
        innerLoginPane.add(passwordField);

        // Login Button
        btnLogin = new JButton("Login");
        sl_innerLoginPane.putConstraint(SpringLayout.NORTH, btnLogin, 554, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.WEST, btnLogin, 0, SpringLayout.WEST, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.SOUTH, btnLogin, 584, SpringLayout.NORTH, innerLoginPane);
        sl_innerLoginPane.putConstraint(SpringLayout.EAST, btnLogin, 379, SpringLayout.WEST, innerLoginPane);
        innerLoginPane.add(btnLogin);

        // Add ActionListener to the Login Button
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String empIdStr = textEmpID_login.getText().trim();
                    System.out.println("Entered employee ID: " + empIdStr);

                    if (empIdStr.matches("\\d+")) {
                        System.out.println("Employee ID is valid (contains only digits)");
                        int empId = Integer.parseInt(empIdStr);
                        System.out.println("Parsed employee ID: " + empId);

                        String password = new String(passwordField.getPassword());
                        System.out.println("Entered password: " + password);

                        // Authenticate the user
                        boolean isAuthenticated = authenticationService.authenticate(empId, password);
                        System.out.println("Authentication result: " + isAuthenticated);

                        if (isAuthenticated) {
                            System.out.println("Authentication successful, navigating to EmployeeDashboardView");
                            JOptionPane.showMessageDialog(LoginView.this, "Login Successful", "Welcome!", JOptionPane.INFORMATION_MESSAGE);
                            // Navigate to the EmployeeView
                            EmployeeDashboardView employeeView = new EmployeeDashboardView(empId);
                            employeeView.setVisible(true);
                            dispose(); // Close the login window
                        } else {
                            System.out.println("Authentication failed, displaying error message");
                            JOptionPane.showMessageDialog(LoginView.this, "Invalid Employee ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        System.out.println("Employee ID is invalid (does not contain only digits)");
                        JOptionPane.showMessageDialog(LoginView.this, "Employee ID must be a number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    System.out.println("An exception occurred during login:");
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginView.this, "An error occurred during login :" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    /*
     * Getter Methods
     */
    public JTextField getEmpIDField_login() {
        return textEmpID_login;
    }

    public JPasswordField getPasswordField_login() {
        return passwordField;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }
}
