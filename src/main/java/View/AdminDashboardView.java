/*
 * Admin Dashboard View for MotorPH Payroll System
 * Displays different panels based on user role (Admin, HR, Finance, IT)
 */
package View;

import Config.ApplicationThemeUtility;
import Controller.AttendanceMgmtController;
import Controller.EmployeeMgmtController;
import Controller.LeaveMgmtController;
import Controller.PayslipMgmtController;
import Model.Employee;
import Model.UserRole;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Service.EmployeeService;
import controller.AccessControlMgmtController;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboardView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel lblDepartment, lblPosition, lblNameSideBar, lblProfileRound, lblId;
    private JLabel lblDate, lblDate_1, lblDate_2;

    private EmployeeService user;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
//                AdminDashboardView frame = new AdminDashboardView(new EmployeeService(new Employee()));
//                frame.setVisible(true);
//                frame.setResizable(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AdminDashboardView(EmployeeService user) {
        this.user = user;
        initialize();
        displayEmployee();
    }

    private void initialize() {
        // Frame setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 0, 1415, 834);

        // Content pane setup
        contentPane = new JPanel();
        contentPane.setBackground(ColorTheme.LIGHT_BG);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        createTopBar();
        createSideBar();
        createMainPanel();

        setupDateTimeDisplay();
    }

    private void createTopBar() {
        JPanel panelTopBar = new JPanel();
        panelTopBar.setBackground(ColorTheme.LIGHT_CONTAINER_BG);
        panelTopBar.setBounds(0, 0, 1399, 78);
        contentPane.add(panelTopBar);
        panelTopBar.setLayout(null);

        // Title
        JLabel lblTitle = new JLabel("Admin Dashboard");
        lblTitle.setForeground(ColorTheme.MAIN_COLOR);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 30));
        lblTitle.setBounds(41, 16, 1009, 48);
        panelTopBar.add(lblTitle);

        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> navigateToHome());
        btnBack.setForeground(ColorTheme.LIGHT_CONTAINER_BG);
        btnBack.setBackground(ColorTheme.LABEL_HIGHLIGHT);
        btnBack.setBounds(1058, 21, 144, 32);
        panelTopBar.add(btnBack);

        // Logout button
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());
        btnLogout.setForeground(new Color(250, 248, 247));
        btnLogout.setBackground(ColorTheme.LABEL_HIGHLIGHT);
        btnLogout.setBounds(1214, 21, 144, 32);

        panelTopBar.add(btnLogout);
    }

    private void createSideBar() {
        JPanel panelSideBar = new JPanel();
        panelSideBar.setBackground(ColorTheme.DARK);
        panelSideBar.setBounds(0, 76, 324, 719);
        contentPane.add(panelSideBar);
        panelSideBar.setLayout(null);

        // Profile components
        lblProfileRound = new JLabel("");
        lblProfileRound.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfileRound.setBounds(20, 75, 278, 139);
        panelSideBar.add(lblProfileRound);

        lblNameSideBar = new JLabel("*Full Name*");
        lblNameSideBar.setHorizontalAlignment(SwingConstants.CENTER);
        lblNameSideBar.setForeground(Color.WHITE);
        lblNameSideBar.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNameSideBar.setBounds(19, 232, 278, 26);
        panelSideBar.add(lblNameSideBar);

        lblId = new JLabel("*ID*");
        lblId.setHorizontalAlignment(SwingConstants.CENTER);
        lblId.setForeground(Color.WHITE);
        lblId.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblId.setBounds(22, 269, 278, 23);
        panelSideBar.add(lblId);

        // Department and position info
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(ColorTheme.DARK);
        infoPanel.setBounds(21, 620, 264, 56);
        panelSideBar.add(infoPanel);
        infoPanel.setLayout(new GridLayout(0, 1, 0, 15));

        lblDepartment = new JLabel("Department :");
        lblDepartment.setHorizontalAlignment(SwingConstants.CENTER);
        lblDepartment.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblDepartment.setForeground(Color.WHITE);
        infoPanel.add(lblDepartment);

        lblPosition = new JLabel("Position :");
        lblPosition.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPosition.setForeground(Color.WHITE);
        infoPanel.add(lblPosition);

        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(51, 51, 57));
        separator.setBackground(Color.WHITE);
        separator.setBounds(10, 578, 296, 4);
        panelSideBar.add(separator);

    }

    private void createMainPanel() {

        // Create the card panel
        cardPanel = new JPanel(new CardLayout());

        cardPanel.setBounds(323, 76, 1076, 719);

        contentPane.add(cardPanel);

        // Add role-specific panels
        cardPanel.add(createAdminPanel(), UserRole.ADMIN.toString());

        cardPanel.add(createHRPanel(), UserRole.HR.toString());

        cardPanel.add(createFinancePanel(), UserRole.FINANCE.toString());

        cardPanel.add(createITPanel(), UserRole.IT.toString());

        cardLayout = (CardLayout) cardPanel.getLayout();

        showPanel();

    }

    private void setupDateTimeDisplay() {

        Timer timer = new Timer(1000, e -> {

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

            Date now = new Date();

            String dateTimeString = dateFormat.format(now) + "     -     " + timeFormat.format(now);

            if (lblDate != null) {
                lblDate.setText(dateTimeString);
            }

            if (lblDate_1 != null) {
                lblDate_1.setText(dateTimeString);
            }

            if (lblDate_2 != null) {
                lblDate_2.setText(dateTimeString);
            }

        });

        timer.start();

    }

    // Navigation methods
    private void navigateToHome() {

        EmployeeDashboardView view = new EmployeeDashboardView(user.getEmpId());

        view.setVisible(true);

        dispose();

    }

    private void logout() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            this.dispose();

            new LoginView().setVisible(true);

            JOptionPane.showMessageDialog(null, "Logged out successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        }

    }

    // Panel creation methods
    private JPanel createHRPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(ColorTheme.LIGHT_BG);

        panel.setLayout(null);

        lblDate = createDateTimeLabel(panel);

        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setBackground(null);

        buttonsPanel.setBounds(162, 147, 784, 463);

        panel.add(buttonsPanel);

        buttonsPanel.setLayout(new GridLayout(0, 1, 20, 30));

        JButton btnEmployeeManagement = createButton("Employee Information Management",
                e -> openEmployeeManagement());

        buttonsPanel.add(btnEmployeeManagement);

        JButton btnLeaveManagement = createButton("Leave Management",
                e -> openLeaveManagement());

        buttonsPanel.add(btnLeaveManagement);

        JButton btnAttendanceManagement = createButton("Attendance Management",
                e -> openAttendanceManagement());

        buttonsPanel.add(btnAttendanceManagement);
        
        JButton btnEmployeeReports = createButton("Employee Reports",
                e -> openEmployeeReports());

        buttonsPanel.add(btnEmployeeReports);

        return panel;

    }

    private JPanel createFinancePanel() {

        JPanel panel = new JPanel();

        panel.setBackground(ColorTheme.LIGHT_BG);

        panel.setLayout(null);

        lblDate_1 = createDateTimeLabel(panel);

        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setBackground(null);

        buttonsPanel.setBounds(162, 147, 784, 463);

        panel.add(buttonsPanel);

        buttonsPanel.setLayout(new GridLayout(0, 1, 30, 30));

        JButton btnPayslipManagement = createButton("Payslip Management",
                e -> openPayslipManagement());

        buttonsPanel.add(btnPayslipManagement);

        JButton btnMonthlyPayrollSummary = createButton("Monthly Payroll Summary Records",
                e -> openMonthlyPayrollSummaryRecords());

        buttonsPanel.add(btnMonthlyPayrollSummary);

        return panel;

    }

    private JPanel createITPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(ColorTheme.LIGHT_BG);

        panel.setLayout(null);

        lblDate_2 = createDateTimeLabel(panel);

        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setBackground(null);

        buttonsPanel.setBounds(223, 221, 688, 168);

        panel.add(buttonsPanel);

        buttonsPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JButton btnAccessControl = createButton("Access Control Management",
                e -> openAccessControl());

        buttonsPanel.add(btnAccessControl);

        return panel;

    }

    private JPanel createAdminPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(ColorTheme.LIGHT_BG);

        panel.setLayout(null);

        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setBounds(138, 157, 799, 375);

        panel.add(buttonsPanel);

        buttonsPanel.setBackground(ColorTheme.LIGHT_BG);

        buttonsPanel.setLayout(new GridLayout(2, 2, 70, 140));

        JButton btnEmployeeInfo = createButton("Employee Information Management",
                e -> openEmployeeManagement());

        buttonsPanel.add(btnEmployeeInfo);

        JButton btnLeaveManagement = createButton("Leave Management",
                e -> openLeaveManagement());

        buttonsPanel.add(btnLeaveManagement);

        JButton btnAttendanceManagement = createButton("Attendance Management",
                e -> openAttendanceManagement());

        buttonsPanel.add(btnAttendanceManagement);

        JButton btnAccessControl = createButton("Access Control Management",
                e -> openAccessControl());

        buttonsPanel.add(btnAccessControl);

        return panel;

    }

    private JButton createButton(String text, ActionListener listener) {

        JButton button = new JButton(text);

        button.setFont(new Font("Tahoma", Font.PLAIN, 13));

        button.addActionListener(listener);

        button.setForeground(ColorTheme.LIGHT_CONTAINER_BG);

        button.setBackground(ColorTheme.DARK);

        return button;

    }

    private JLabel createDateTimeLabel(JPanel parentPanel) {

        JLabel label = new JLabel();

        label.setToolTipText("");

        label.setFont(new Font("Arial", Font.PLAIN, 25));

        label.setBounds(47, 19, 1017, 55);

        parentPanel.add(label);

        JSeparator separator = new JSeparator();

        separator.setForeground(Color.DARK_GRAY);

        separator.setBackground(Color.DARK_GRAY);

        separator.setBounds(10, 71, 1054, 3);

        parentPanel.add(separator);

        return label;

    }

    // Navigation methods for buttons
    private void openEmployeeManagement() {
        try {

            EmployeeMgmtView view = new EmployeeMgmtView(user);
            EmployeeMgmtAddView viewAdd = new EmployeeMgmtAddView();
            EmployeeMgmtController controller = new EmployeeMgmtController(view, viewAdd);

            view.setVisible(true);
            view.setResizable(true);
            view.setTitle("MotorPH Portal");

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openLeaveManagement() {

        try {

            LeaveMgmtView view = new LeaveMgmtView(user);
            LeaveMgmtController controller = new LeaveMgmtController(view);
            view.setVisible(true);
            view.setTitle("MotorPH Portal");
            dispose();
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void openAttendanceManagement() {

        try {

            AttendanceMgmtView view = new AttendanceMgmtView(user);
            AttendanceAddView viewAdd = new AttendanceAddView();
            AttendanceMgmtController controller = new AttendanceMgmtController(view, viewAdd);
            view.setVisible(true);
 
            view.setTitle("MotorPH Portal");
            
            dispose();
        } catch (Exception e) {

            e.printStackTrace();

        }

    }
    
        private void openEmployeeReports() {

        try {
            EmployeeReportView view = new EmployeeReportView(user);
            view.setVisible(true);
            view.setTitle("MotorPH Portal");
            
            dispose();
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void openPayslipManagement() {

        try {
            PayslipMgmtView view = new PayslipMgmtView(user);
            PayslipAddView viewAdd = new PayslipAddView();
            PayslipMgmtController controller = new PayslipMgmtController(view, viewAdd);
            view.setVisible(true);
            view.setResizable(true);
            view.setTitle("MotorPH Portal");
            dispose();
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void openMonthlyPayrollSummaryRecords() {

        try {

            MonthlyPayrollSummaryRecordsView view = new MonthlyPayrollSummaryRecordsView(user);
            view.setVisible(true);
            view.setResizable(true);
            view.setTitle("MotorPH Portal");
            dispose();
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void openAttendanceRecords() {

        try {

//            AttendanceRecordsView view = new AttendanceRecordsView(empId);
//            view.setVisible(true);
//            view.setResizable(false);
//            view.setTitle("MotorPH Portal");
//            dispose();
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void openAccessControl() {

        try {

            AccessControlMgmtView view = new AccessControlMgmtView(user);
            PasswordUpdateView viewPassword = new PasswordUpdateView();

            AccessControlMgmtController controller = new AccessControlMgmtController(user, view, viewPassword);

            view.setVisible(true);

            view.setResizable(true);

            view.setTitle("MotorPH Payroll");

            dispose();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void showPanel() {

        cardLayout.show(cardPanel, user.getUserRole().toString());

    }

    private void displayEmployee() {

        lblNameSideBar.setText(user.getFirstName() + " " + user.getLastName());

        lblPosition.setText(user.getPositionSalaryHistory().getJobPosition().getTitle());

        lblId.setText("Employee ID #" + user.getEmpId());

        lblDepartment.setText(user.getDepartment() + " Department");

    }

}
