package Controller;

import View.AttendanceMgmtView;
import View.AttendanceAddView;
import Service.FinanceService;
import Util.DatabaseConnectionUtil;
import Util.ReportGeneratorUtil;
import View.EmployeeReportView;
import View.PayslipAddView;
import View.PayslipMgmtView;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Controller class for managing attendance operations in the application.
 * Handles interactions between the attendance views ({@link AttendanceMgmtView},
 * {@link AttendanceAddView}) and the HR service layer ({@link HrService}).
 */
public class PayslipMgmtController {

    private final PayslipMgmtView view;
    private final PayslipAddView viewAdd;
    private final FinanceService userFinance;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructs an AttendanceManagementController with the specified views.
     * Initializes the HR service and sets up event listeners.
     *
     * @param view the main attendance management view
     * @param viewAdd the attendance add view
     */
    public PayslipMgmtController(PayslipMgmtView view, PayslipAddView viewAdd) {
        this.view = view;
        this.viewAdd = viewAdd;
        this.userFinance = new FinanceService(view.getUserFinance());

        attachManagementViewListeners();
        attachAddViewListeners();
    }

    /**
     * Attaches event listeners to the management view components. 
     */
    private void attachManagementViewListeners() {
        view.getBtnAdd().addActionListener(e -> openAddView());
        view.getBtnDelete().addActionListener(e -> deletePayslip());
        view.getBtnSearch().addActionListener(e -> searchPayslip());
        view.getBtnPrint().addActionListener(e -> printPayslip());
    }

    /**
     * Attaches event listeners to the add view components. Sets up handler for
     * the add attendance button.
     */
    private void attachAddViewListeners() {
        viewAdd.getBtnAdd().addActionListener(e -> addRecord());
    }

    /**
     * Opens the add attendance view. Makes the add view visible and
     * non-resizable.
     */
    private void openAddView() {
        viewAdd.setVisible(true);
        viewAdd.setResizable(false);
        viewAdd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Handles the attendance addition process.
     */
    private void addRecord() {
        try {
            // Get and validate inputs
            String empIdText = viewAdd.getFieldEmployeeId().getText().trim();
            String monthText = viewAdd.getFieldMonth().getText().trim();
            String yearText = viewAdd.getFieldYear().getText().trim();

            if (empIdText.isEmpty() || monthText.isEmpty() || yearText.isEmpty()) {
                viewAdd.showMessage("Please fill in all fields.");
                return;
            }

            int empId;
            int month;
            int year;

            // 1. Validate String formats before parsing to catch non-numeric input specifically
            try {
                empId = Integer.parseInt(empIdText);
            } catch (NumberFormatException e) {
                viewAdd.showMessage("Employee ID must be a valid whole number.");
                return;
            }

            try {
                month = Integer.parseInt(monthText);
            } catch (NumberFormatException e) {
                viewAdd.showMessage("Month must be a valid whole number.");
                return;
            }

            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException e) {
                viewAdd.showMessage("Year must be a valid whole number.");
                return;
            }

            // 2. Validate parsed numeric values and specific string patterns
            if (empId <= 0) {
                viewAdd.showMessage("Employee ID must be a positive number.");
                return;
            }
            if (month < 1 || month > 12) {
                viewAdd.showMessage("Month must be a number between 1 and 12.");
                return;
            }
            // Re-checking yearText here ensures the format even if parsing succeeded for something like "123"
            if (!yearText.matches("^\\d{4}$")) {
                viewAdd.showMessage("Year must be a four-digit number (e.g., 2023).");
                return;
            }

            // Add to database through service layer
            boolean success = userFinance.createPayslip(empId, month, year);

            if (success) {
                viewAdd.showMessage("Payslip record added successfully.");
                view.populatePayslipTable(null);
                // addView.dispose();
            } else {
                viewAdd.showMessage("Failed to add payslip record. Please check for duplicate entries or server issues.");
            }

        } catch (IllegalArgumentException e) {
            viewAdd.showMessage("Error: " + e.getMessage());
        } catch (SQLException e) {
            viewAdd.showMessage("Database Error: " + e.getMessage());
        } catch (Exception e) {
            viewAdd.showMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Handles the attendance deletion process.
     */
    private void deletePayslip() {
        int selectedRow = view.getTblPayslip().getSelectedRow();
        int recordId = 0;

        if (selectedRow != -1) { // Check if a row is selected
            recordId = (Integer) view.getTblPayslip().getValueAt(selectedRow, view.getTblPayslip().getColumn("Payslip No.").getModelIndex());
        } else {
            System.out.println("Please select a record to delete.");
        }

        int response = JOptionPane.showConfirmDialog(
                view,
                "Are you sure you want to delete this payslip record?\nThis action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            try {
                boolean success = userFinance.deletePayslip(recordId);
                if (success) {
                    view.populatePayslipTable(null);
                    view.showMessage("Payslip record deleted successfully");
                } else {
                    view.showMessage("Failed to delete payslip record");
                }
            } catch (SQLException e) {
                view.showMessage("Database Error: " + e.getMessage());
            } catch (Exception e) {
                view.showMessage("Error deleting record: " + e.getMessage());
            }
        }
    }

    private void searchPayslip() {
        try {
            String searchIdText = view.getFieldSearchEmployeeId().getText();

            // If search input is empty, display all records
            if (searchIdText.isBlank()) {
                view.populatePayslipTable(null); // Pass null to show all records
            } else {
                // If search input is not empty, search
                Integer empId = Integer.valueOf(searchIdText); // Parse the employee ID

                // Attempt to find the employee by ID. Returns a List<Employee>.
                var foundEmployees = userFinance.getEmployeeById(empId);

                // Check if the list is not empty (meaning an employee was found)
                if (!foundEmployees.isEmpty()) {
                    System.out.println("Search Employee : " + foundEmployees.get(0).toString());
                    System.out.println("Search Parameters : Employee ID[" + empId + "]");
                    view.populatePayslipTable(empId); // Populate table with seacrh result
                } else {
                    view.populatePayslipTable(null); // No employee found, show all
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

    private void printPayslip() {
        String employeeIdStr = view.getFieldEmployeeId().getText();
        String startDateStr = view.getFieldPeriodStartDate().getText();

        try {
            int employeeId = Integer.parseInt(employeeIdStr);
            LocalDate startDate = LocalDate.parse(startDateStr); 

            int month = startDate.getMonthValue();
            int year = startDate.getYear();

            try (Connection conn = DatabaseConnectionUtil.getConnection()) {
                ReportGeneratorUtil generator = new ReportGeneratorUtil();
                generator.generateReport(conn, employeeId, month, year);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Something went wrong: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(EmployeeReportView.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
}
