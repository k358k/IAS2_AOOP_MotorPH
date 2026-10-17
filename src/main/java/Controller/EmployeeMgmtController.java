/*



 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license


 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template


 */
package Controller;

import Util.AuditLogger;

import Model.*;

import View.EmployeeMgmtAddView;

import Service.EmployeeService;

import Service.HrService;

import Service.InputValidatorService;

import Util.PasswordUtil;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeParseException;

import java.util.Date;

import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionListener;

import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;

//import View.EmployeeAddView;
import View.EmployeeMgmtView;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.util.logging.Level;

import java.util.logging.Logger;

/**
 * Controller class for managing employee information and payroll operations.
 * Handles interactions between the views ({@link EmployeeMgmtView},  *
 * {@link EmployeeAddView}, and the HR service
 * layer ({@link HrService}
 *
 */
public class EmployeeMgmtController {

    private EmployeeMgmtView view;

    private EmployeeMgmtAddView viewAdd;

    private HrService userHr;

    /**
     *
     *
     * Constructs an EmployeeInfoManagementController with the specified views.
     *
     * @param view the main employee management view
     *
     *
     * @param viewAdd the employee add view
     *
     *
     */
    public EmployeeMgmtController(EmployeeMgmtView view, EmployeeMgmtAddView viewAdd) {

        this.view = view;

        this.viewAdd = viewAdd;

        this.userHr = new HrService(view.getUserHr());

        SwingUtilities.invokeLater(this::initController); // Ensure this runs on the EDT

    }

    /**
     *
     *
     * Initializes controller event listeners for both views.
     *
     */
    private void initController() {

        //Action listeners for EmployeeMgmtAddView
        viewAdd.getBtnAdd().addActionListener(e -> {

            addEmployee();

        });

        viewAdd.getBtnClear().addActionListener(e -> {

            clearAddTextFields();

        });

        // Action listeners for EmployeeMgmtView
        view.getBtnAdd().addActionListener(e -> {

            viewAdd.setVisible(true);

            viewAdd.setResizable(true);

            viewAdd.setTitle("Add New Employee");

            viewAdd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });

        view.getBtnDelete().addActionListener(e -> {

            try {

                deleteEmployee();

            } catch (SQLException ex) {

                view.showMessage("Error Deleting Employee: " + ex.getMessage());

                Logger.getLogger(EmployeeMgmtController.class.getName()).log(Level.SEVERE, null, ex);

            }

        });

        view.getBtnSave().addActionListener(e -> {

            updateEmployee();

        });

        view.getBtnSearch().addActionListener(e -> {
            searchEmployee();

        }
        );

    }

    /**
     *
     *
     * Handles the employee addition process.
     *

     */
    private void updateEmployee() {

        String empId = view.getFieldEmployeeId().getText();

        // Check if a record is selected
        if (empId == null || empId.isBlank()) {

            view.showMessage("Please select a record to update.");

            return;

        }

        try {

            Employee employee = validateEmployeeUpdateInput(view);

            System.out.println("Running EmployeeMgmtController.updateEmployee()...");
            System.out.println("Employee Object Parameter: " + employee.toString());

            boolean isSuccessful = userHr.updateEmployee(employee);

            if (isSuccessful) {

                AuditLogger.logActivity(
                    "EMPLOYEE_UPDATED",
                    String.valueOf(employee.getEmpId(()
                );                                     
                
                view.showMessage("Employee record updated successfully!");

                view.populateEmployeeTable(null);

            } else {

                view.showMessage("Failed to update employee record. Please try again.");

            }

        } catch (IllegalArgumentException e) {

            // Validation errors from our validation methods
            view.showMessage("Validation Error: " + e.getMessage());

        } catch (DateTimeParseException e) {

            view.showMessage("Invalid date format. Please enter birthday in YYYY-MM-DD format.");

        } catch (SQLException e) {

            // Database-specific errors - preserve the SQL exception details
            String errorMessage = "Database Error: " + e.getMessage();

            if (e.getSQLState() != null) {

                errorMessage += " (SQL State: " + e.getSQLState() + ")";

            }

            view.showMessage(errorMessage);

            e.printStackTrace(); // Log the full stack trace for debugging

        } catch (Exception e) {

            // Catch-all for unexpected errors
            view.showMessage("An unexpected error occurred: " + e.getMessage());

            e.printStackTrace();

        }

    }

    private void addEmployee() {

        try {

            Employee employee = validateEmployeeAddInput(viewAdd);

            boolean isSuccessful = userHr.addEmployee(employee);

            if (isSuccessful) {

                AuditLogger.logActivity(
                    "EMPLOYEE_CREATED",
                    String.valueOf(employee.getEmpId(()
                );                                     
                
                viewAdd.showMessage("Employee record added successfully!");
                viewAdd.dispose();
                view.populateEmployeeTable(null);

            } else {

                view.showMessage("Failed to update employee record. Please try again.");

            }

        } catch (IllegalArgumentException e) {

            // Validation errors from our validation methods
            viewAdd.showMessage("Validation Error: " + e.getMessage());

        } catch (DateTimeParseException e) {

            viewAdd.showMessage("Invalid date format. Please enter birthday in YYYY-MM-DD format.");

        } catch (SQLException ex) {

            // Database-specific errors
            String errorMessage = "Database Error: " + ex.getMessage();

            if (ex.getSQLState() != null) {

                errorMessage += " (SQL State: " + ex.getSQLState() + ")";

            }
            viewAdd.showMessage(errorMessage);

        } catch (RuntimeException e) {

            // Handle specific business logic errors
            if (e.getMessage().contains("does not exist")) {

                viewAdd.showMessage("Error: Supervisor does not exist. Please check the Supervisor ID.");

            } else if (e.getMessage().contains("duplicate")) {

                viewAdd.showMessage("Error: Employee with this information already exists.");

            } else {

                viewAdd.showMessage("Business Logic Error: " + e.getMessage());

            }

        } catch (Exception e) {

            // Catch-all for unexpected errors
            viewAdd.showMessage("An unexpected error occurred: " + e.getMessage());

        }

    }

    private void deleteEmployee() throws SQLException {

        String empIdText = view.getFieldEmployeeId().getText();

        // Check if a record is selected
        if (empIdText == null || empIdText.isBlank()) {

            view.showMessage("Please select a record to delete.");

            return;

        }

        try {

            int empId = Integer.parseInt(empIdText);

            int response = JOptionPane.showConfirmDialog(
                    view,
                    "Are you sure you want to delete this employee record?\nThis action cannot be undone.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {

                boolean isSuccessful = userHr.deleteEmployee(empId);

                if (isSuccessful) {

                    view.showMessage("Employee record deleted successfully!");

                    view.populateEmployeeTable(null);

                } else {

                    view.showMessage("Failed to delete employee record. Please try again.");

                }

            }

        } catch (NumberFormatException e) {

            view.showMessage("Invalid Employee ID format. Please select a valid record.");

        } // Database-specific errors
        catch (Exception e) {

            view.showMessage("An unexpected error occurred while deleting the employee record: " + e.getMessage());

            e.printStackTrace();

        }

    }

    private void searchEmployee() {
        try {
            String searchIdText = view.getFieldSearchEmployeeId().getText();

            // If search input is empty, display all employees
            if (searchIdText == null || searchIdText.isBlank()) {
                view.populateEmployeeTable(null); // Pass null to show all employees
            } else {
                // If search input is not empty, parse ID and search for a specific employee
                Integer empId = Integer.parseInt(searchIdText); // Parse the employee ID

                // Attempt to find the employee by ID
                var foundEmployees = userHr.getEmployeeById(empId);

                // Check if the list is not empty (meaning an employee was found)
                if (!foundEmployees.isEmpty()) {
                    System.out.println("Search Employee : " + foundEmployees.get(0).toString());
                    view.populateEmployeeTable(empId); // Populate table with just that employee
                } else {
                    view.populateEmployeeTable(null); // No employee found, show all
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
            e.printStackTrace();
        } catch (Exception e) {
            view.showMessage("An unexpected error occurred during search: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Employee validateEmployeeUpdateInput(EmployeeMgmtView view) throws SQLException, IllegalArgumentException {

        try {

            // Extract and trim string fields
            String firstName = view.getFieldFirstName().getText().trim();

            String lastName = view.getFieldLastName().getText().trim();

            String birthdayString = view.getFieldBirthday().getText().trim();

            String phoneNumber = view.getFieldPhoneNumber().getText().trim();

            String sssNumber = view.getFieldSssNumber().getText().trim();

            String philhealthNumber = view.getFieldPhilhealthNumber().getText().trim();

            String tinNumber = view.getFieldTin().getText().trim();

            String pagibigNumber = view.getFieldPagibigNumber().getText().trim();

            String jobPositionTitle = view.getFieldJobTitle().getText().trim();

            // Extract enum selections
            Gender gender = (Gender) view.getComboBoxGender().getSelectedItem();

            EmploymentStatus employmentStatus = (EmploymentStatus) view.getComboBoxEmploymentStatus().getSelectedItem();

            Department department = (Department) view.getComboBoxDepartment().getSelectedItem();

            // Extract optional address fields (null if empty)
            String buildingStreet = getOptionalField(view.getFieldBuildingStreet().getText().trim());

            String barangay = getOptionalField(view.getFieldBarangay().getText().trim());

            String cityMunicipality = getOptionalField(view.getFieldCityMunicipality().getText().trim());

            String province = getOptionalField(view.getFieldProvince().getText().trim());

            String zipcode = getOptionalField(view.getFieldZipcode().getText().trim());

            // Extract optional supervisor ID
            Integer supervisorId = parseOptionalInteger(view.getFieldSupervisorId().getText().trim());

            // Extract numeric fields with validation
            double basicSalary = parseRequiredDouble(view.getFieldBasicSalary().getText().trim(), "Basic Salary");

            double riceSubsidy = parseRequiredDouble(view.getFieldRiceSubsidy().getText().trim(), "Rice Subsidy");

            double phoneAllowance = parseRequiredDouble(view.getFieldPhoneAllowance().getText().trim(), "Phone Allowance");

            double clothingAllowance = parseRequiredDouble(view.getFieldClothingAllowance().getText().trim(), "Clothing Allowance");

            // Optional numeric fields
            double hourlyRate = parseOptionalDouble(view.getFieldHourlyRate().getText().trim());

            double grossSemiMonthlyRate = parseOptionalDouble(view.getFieldGrossSemiMonthlyRate().getText().trim());

            // Validate required fields with specific error messages
            validateRequiredFields("placeholderPassword",
                    firstName, lastName, birthdayString, phoneNumber, sssNumber,
                    philhealthNumber, tinNumber, pagibigNumber, jobPositionTitle,
                    gender, employmentStatus, department);

            // Validate formats with specific error messages
            validateFormats(birthdayString, pagibigNumber, philhealthNumber, phoneNumber, sssNumber, tinNumber);

            // Get employee ID
            int empId = Integer.parseInt(view.getFieldEmployeeId().getText());

            // Create and return Employee object
            return new Employee(
                    firstName, lastName, gender, LocalDate.parse(birthdayString), phoneNumber,
                    sssNumber, philhealthNumber, tinNumber, pagibigNumber, employmentStatus,
                    department, supervisorId,
                    new PositionSalaryHistory(empId, new JobPosition(jobPositionTitle, ""),
                            basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, LocalDate.now(), null),
                    new EmployeeAddress(empId, new Address(1, buildingStreet, barangay, cityMunicipality, province, zipcode),
                            AddressType.HOME),
                    empId, "def", UserRole.EMPLOYEE
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Invalid Employee ID format.");

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format for birthday.");

        }

    }

    private Employee validateEmployeeAddInput(EmployeeMgmtAddView viewAdd) throws SQLException, IllegalArgumentException {

        try {

            // Extract and trim string fields
            String firstName = viewAdd.getFieldFirstName().getText().trim();

            String lastName = viewAdd.getFieldLastName().getText().trim();

            String birthdayString = viewAdd.getFieldBirthday().getText().trim();

            String phoneNumber = viewAdd.getFieldPhoneNumber().getText().trim();

            String sssNumber = viewAdd.getFieldSssNumber().getText().trim();

            String philhealthNumber = viewAdd.getFieldPhilhealthNumber().getText().trim();

            String tinNumber = viewAdd.getFieldTin().getText().trim();

            String pagibigNumber = viewAdd.getFieldPagibigNumber().getText().trim();

            String jobPositionTitle = viewAdd.getFieldJobTitle().getText().trim();

            // Extract password field
            String passwordString = viewAdd.getFieldPassword().getText();

            // Extract enum selections
            Gender gender = (Gender) viewAdd.getComboBoxGender().getSelectedItem();

            EmploymentStatus employmentStatus = (EmploymentStatus) viewAdd.getComboBoxEmploymentStatus().getSelectedItem();

            Department department = (Department) viewAdd.getComboBoxDepartment().getSelectedItem();

            // Extract optional address fields (null if empty)
            String buildingStreet = getOptionalField(viewAdd.getFieldBuildingStreet().getText().trim());

            String barangay = getOptionalField(viewAdd.getFieldBarangay().getText().trim());

            String cityMunicipality = getOptionalField(viewAdd.getFieldCityMunicipality().getText().trim());

            String province = getOptionalField(viewAdd.getFieldProvince().getText().trim());

            String zipcode = getOptionalField(viewAdd.getFieldZipcode().getText().trim());

            // Extract optional supervisor ID
            Integer supervisorId = parseOptionalInteger(viewAdd.getFieldSupervisorId().getText().trim());

            // Extract numeric fields with validation
            double basicSalary = parseRequiredDouble(viewAdd.getFieldBasicSalary().getText().trim(), "Basic Salary");

            double riceSubsidy = parseRequiredDouble(viewAdd.getFieldRiceSubsidy().getText().trim(), "Rice Subsidy");

            double phoneAllowance = parseRequiredDouble(viewAdd.getFieldPhoneAllowance().getText().trim(), "Phone Allowance");

            double clothingAllowance = parseRequiredDouble(viewAdd.getFieldClothingAllowance().getText().trim(), "Clothing Allowance");

            // Validate required fields with specific error messages
            validateRequiredFields(passwordString, firstName, lastName, birthdayString, phoneNumber, sssNumber,
                    philhealthNumber, tinNumber, pagibigNumber, jobPositionTitle,
                    gender, employmentStatus, department);

            // Validate formats with specific error messages
            validateFormats(birthdayString, pagibigNumber, philhealthNumber, phoneNumber, sssNumber, tinNumber);

            // Create and return Employee object
            return new Employee(
                    firstName,
                    lastName,
                    gender,
                    LocalDate.parse(birthdayString),
                    phoneNumber,
                    sssNumber,
                    philhealthNumber,
                    tinNumber,
                    pagibigNumber,
                    employmentStatus,
                    department,
                    supervisorId,
                    new PositionSalaryHistory(
                            0, // Placeholder

                            new JobPosition(jobPositionTitle, ""),
                            basicSalary,
                            riceSubsidy,
                            phoneAllowance,
                            clothingAllowance,
                            LocalDate.now(),
                            null
                    ),
                    new EmployeeAddress(
                            0,
                            new Address(1, buildingStreet, barangay, cityMunicipality, province, zipcode),
                            AddressType.HOME // Always set to HOME - for now

                    ),
                    0, // Employee ID Placeholder; Set to Auto-Increment in database

                    passwordString, // Plaintext password

                    UserRole.EMPLOYEE // Default User Role at record creation

            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Invalid number format in salary or allowance fields. Please enter valid numbers.");

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format for birthday.");

        } catch (Exception e) {

            throw new IllegalArgumentException("Error processing employee data: " + e.getMessage());

        }

    }

    // Helper method for required field validation with specific messages
    private void validateRequiredFields(String passwordString, String firstName, String lastName, String birthdayString,
            String phoneNumber, String sssNumber, String philhealthNumber,
            String tinNumber, String pagibigNumber, String jobPositionTitle,
            Gender gender, EmploymentStatus employmentStatus, Department department) {

        StringBuilder missingFields = new StringBuilder();

        if (passwordString.isBlank()) {
            missingFields.append("Password, ");
        }

        if (firstName.isBlank()) {
            missingFields.append("First Name, ");
        }

        if (lastName.isBlank()) {
            missingFields.append("Last Name, ");
        }

        if (birthdayString.isBlank()) {
            missingFields.append("Birthday, ");
        }

        if (phoneNumber.isBlank()) {
            missingFields.append("Phone Number, ");
        }

        if (sssNumber.isBlank()) {
            missingFields.append("SSS Number, ");
        }

        if (philhealthNumber.isBlank()) {
            missingFields.append("PhilHealth Number, ");
        }

        if (tinNumber.isBlank()) {
            missingFields.append("TIN Number, ");
        }

        if (pagibigNumber.isBlank()) {
            missingFields.append("Pag-IBIG Number, ");
        }

        if (jobPositionTitle.isBlank()) {
            missingFields.append("Job Position, ");
        }

        if (gender == null) {
            missingFields.append("Gender, ");
        }

        if (employmentStatus == null) {
            missingFields.append("Employment Status, ");
        }

        if (department == null) {
            missingFields.append("Department, ");
        }

        if (missingFields.length() > 0) {

            String missing = missingFields.toString();

            missing = missing.substring(0, missing.length() - 2); // Remove trailing comma and space

            throw new IllegalArgumentException("The following required fields are missing: " + missing);

        }

    }

    // Helper method for format validation with specific messages
    private void validateFormats(String birthdayString, String pagibigNumber, String philhealthNumber,
            String phoneNumber, String sssNumber, String tinNumber) {

        StringBuilder invalidFormats = new StringBuilder();

        if (!InputValidatorService.validateDateFormat(birthdayString)) {

            invalidFormats.append("Birthday (use YYYY-MM-DD format), ");

        }

        if (!InputValidatorService.validatePagibigFormat(pagibigNumber)) {

            invalidFormats.append("Pag-IBIG Number (use 12 digits), ");

        }

        if (!InputValidatorService.validatePhilhealthFormat(philhealthNumber)) {

            invalidFormats.append("PhilHealth Number (use 12 digits), ");

        }

        if (!InputValidatorService.validatePhoneNumberFormat(phoneNumber)) {

            invalidFormats.append("Phone Number (use XXX-XXX-XXX format), ");

        }

        if (!InputValidatorService.validateSssFormat(sssNumber)) {

            invalidFormats.append("SSS Number (use XX-XXXXXXX-X format), ");

        }

        if (!InputValidatorService.validateTinFormat(tinNumber)) {

            invalidFormats.append("TIN (use XXX-XXX-XXX-XXX format), ");

        }

        if (invalidFormats.length() > 0) {

            String invalid = invalidFormats.toString();

            invalid = invalid.substring(0, invalid.length() - 2); // Remove trailing comma and space

            throw new IllegalArgumentException("The following fields have invalid formats: " + invalid);

        }

    }

    private void clearAddTextFields() {

        viewAdd.getFieldFirstName().setText("");

        viewAdd.getFieldLastName().setText("");

        viewAdd.getFieldBirthday().setText("");

        viewAdd.getFieldPhoneNumber().setText("");

        viewAdd.getFieldSssNumber().setText("");

        viewAdd.getFieldPhilhealthNumber().setText("");

        viewAdd.getFieldTin().setText("");

        viewAdd.getFieldPagibigNumber().setText("");

        viewAdd.getFieldJobTitle().setText("");

        viewAdd.getFieldBuildingStreet().setText("");

        viewAdd.getFieldBarangay().setText("");

        viewAdd.getFieldCityMunicipality().setText("");

        viewAdd.getFieldProvince().setText("");

        viewAdd.getFieldZipcode().setText("");

        viewAdd.getFieldSupervisorId().setText("");

        viewAdd.getFieldBasicSalary().setText("");

        viewAdd.getFieldRiceSubsidy().setText("");

        viewAdd.getFieldPhoneAllowance().setText("");

        viewAdd.getFieldClothingAllowance().setText("");

        viewAdd.getFieldPassword().setText("");

        // Reset combo boxes to default
        view.getComboBoxGender().setSelectedIndex(0);

        view.getComboBoxEmploymentStatus().setSelectedIndex(0);

        view.getComboBoxDepartment().setSelectedIndex(0);

    }

    // Helper methods
    private String getOptionalField(String field) {

        return field.isEmpty() ? null : field;

    }

    private Integer parseOptionalInteger(String text) {

        if (text.isEmpty()) {
            return null;
        }

        try {

            return Integer.parseInt(text);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Invalid Supervisor ID. Please enter a valid number or leave it blank.");

        }

    }

    private double parseRequiredDouble(String text, String fieldName) {

        if (text.isEmpty()) {

            throw new IllegalArgumentException(fieldName + " is required.");

        }

        try {

            return Double.parseDouble(text);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Invalid " + fieldName + ". Please enter a valid number.");

        }

    }

    private double parseOptionalDouble(String text) {

        if (text.isEmpty()) {
            return 0.0;
        }

        try {

            return Double.parseDouble(text);

        } catch (NumberFormatException e) {

            return 0.0; // Default value for optional fields

        }

    }

    private boolean isAnyBlank(String... strings) {

        for (String str : strings) {

            if (str.isBlank()) {
                return true;
            }

        }

        return false;

    }
}
