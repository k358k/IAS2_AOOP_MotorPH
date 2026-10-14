package controller;

import Model.UserRole;
import Service.EmployeeService;
import Service.ItService;
import View.AccessControlMgmtView;
import View.PasswordUpdateView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 * Controller class for managing user access control operations. Handles
 * interactions between the {@link AccessControlMgmtView} and the IT service
 * layer ({@link ItService}).
 */
public class AccessControlMgmtController {

    private final AccessControlMgmtView view;
    private final PasswordUpdateView viewPassword;
    private ItService userIt;

    public AccessControlMgmtController(EmployeeService user, AccessControlMgmtView view, PasswordUpdateView viewPassword) {
        this.view = view;
        this.viewPassword = viewPassword;
        this.userIt = new ItService(user);
        initializeController();
    }

    /**
     * Initializes the controller by setting up event listeners.
     */
    private void initializeController() {
        setupEventListeners();
    }

    /**
     * Sets up event listeners for the view components.
     */
    private void setupEventListeners() {

        // Role button listeners - now perform immediate updates
        view.getBtnRoleEmployee().addActionListener(e -> updateRole(UserRole.EMPLOYEE));
        view.getBtnRoleHr().addActionListener(e -> updateRole(UserRole.HR));
        view.getBtnRoleFinance().addActionListener(e -> updateRole(UserRole.FINANCE));
        view.getBtnRoleIt().addActionListener(e -> updateRole(UserRole.IT));
        view.getBtnSearch().addActionListener(e -> searchEmployee());
        view.getBtnUpdatePassword().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPassword.setVisible(true);
                viewPassword.setResizable(true);
                viewPassword.setTitle("Update Password");
                viewPassword.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        viewPassword.getBtnSave().addActionListener(e -> updatePassword());
    }

    /**
     * Handles the user role update process.
     *
     * @param newRole the new {@link UserRole} to assign
     */
    private void updateRole(UserRole newRole) {

        String empIdText = view.getFieldEmployeeId().getText();
        if (empIdText.isEmpty()) {
            view.showMessage("Please select an employee first");
            return;
        }

        System.out.println("New Role: " + newRole.toString());

        try {
            int empId = Integer.parseInt(empIdText);

            // Confirm role change
            int confirm = JOptionPane.showConfirmDialog(view,
                    "Change this employee's role to " + newRole + "?",
                    "Confirm Role Change",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = userIt.updateUserRole(empId, newRole);
                if (success) {
                    view.showMessage("Role updated successfully to " + newRole);
                    view.getFieldUserRole().setText(newRole.toString());
                    view.populateAccessControlTable(null); //Refresh table

                } else {
                    view.showMessage("Failed to update role");
                }
            }
        } catch (Exception e) {
            view.showMessage("Error updating role: " + e.getMessage());
        }
    }

    /**
     * Handles the password update process.
     */
    private void updatePassword() {
        String empIdText = view.getFieldEmployeeId().getText();
        char[] passwordChars = viewPassword.getjPasswordField().getPassword();
        String newPassword = new String(passwordChars); // Convert char array to String

        if (empIdText.isEmpty()) {
            view.showMessage("Please select an employee first");
            return;
        }

        if (newPassword.isEmpty()) {
            viewPassword.showMessage("Please enter a new password");
            return;
        }

        try {
            int empId = Integer.parseInt(empIdText);

            // Confirm password change
            int confirm = JOptionPane.showConfirmDialog(viewPassword,
                    "Update this employee's password?",
                    "Confirm Password Change",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = userIt.updatePassword(empId, newPassword); // Hashed within the Service method
                if (success) {
                    viewPassword.showMessage("Password updated successfully");
                } else {
                    viewPassword.showMessage("Failed to update password");
                }
                viewPassword.getjPasswordField().setText(""); //Clear field
                viewPassword.dispose();
            }
        } catch (NumberFormatException e) {
            view.showMessage("Invalid Employee ID format");
        } catch (Exception e) {
            view.showMessage("Error updating password: " + e.getMessage());
        }
    }

    private void searchEmployee() {
        try {
            String searchIdText = view.getFieldSearchEmployeeId().getText();

            // If search input is empty, display all employees
            if (searchIdText == null || searchIdText.isBlank()) {
                view.populateAccessControlTable(null); // Pass null to show all employees
            } else {
                // If search input is not empty, parse ID and search for a specific employee
                Integer empId = Integer.valueOf(searchIdText); // Parse the employee ID

                // Attempt to find the employee by ID.
                var foundEmployees = userIt.getEmployeeById(empId);

                // Check if the list is not empty (meaning an employee was found)
                if (!foundEmployees.isEmpty()) {
                    System.out.println("Search Employee : " + foundEmployees.get(0).toString());
                    view.populateAccessControlTable(empId); // Populate table with just that employee
                } else {
                    view.populateAccessControlTable(null); // No employee found, show all
                    view.showMessage("No employee found with ID: " + empId);
                }
            }
        } catch (NumberFormatException e) {
            view.showMessage("Invalid Employee ID format. Please enter a valid number.");
        } catch (SQLException e) {
            String errorMessage = "Database Error: " + e.getMessage();
            if (e.getSQLState() != null) {
                errorMessage += " (SQL State: " + e.getSQLState() + ")";
            }
            view.showMessage(errorMessage);
        } catch (Exception e) {
            view.showMessage("An unexpected error occurred during search: " + e.getMessage());
        }
    }
}
