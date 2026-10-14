package Controller;

import View.AttendanceMgmtView;
import View.AttendanceAddView;
import Model.ClockHour;
import Service.HrService;
import Service.InputValidatorService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Controller class for managing attendance operations in the application.
 * Handles interactions between the attendance views ({@link AttendanceMgmtView},
 * {@link AttendanceAddView}) and the HR service layer ({@link HrService}).
 */
public class AttendanceMgmtController {

    private final AttendanceMgmtView view;
    private final AttendanceAddView viewAdd;
    private final HrService userHr;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructs an AttendanceManagementController with the specified views.
     * Initializes the HR service and sets up event listeners.
     *
     * @param view the main attendance management view
     * @param viewAdd the attendance add view
     */
    public AttendanceMgmtController(AttendanceMgmtView view, AttendanceAddView viewAdd) {
        this.view = view;
        this.viewAdd = viewAdd;
        this.userHr = new HrService(view.getUserHr());

        attachManagementViewListeners();
        attachAddViewListeners();
    }

    /**
     * Attaches event listeners to the management view components.
     */
    private void attachManagementViewListeners() {
        view.getBtnAdd().addActionListener(e -> openAddView());
        view.getBtnDelete().addActionListener(e -> deleteAttendance());
        view.getBtnSearch().addActionListener(e -> searchAttendance());
    }

    /**
     * Attaches event listeners to the add view components. Sets up handler for
     * the add attendance button.
     */
    private void attachAddViewListeners() {
        viewAdd.getBtnAdd().addActionListener(e -> addAttendance());
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
    private void addAttendance() {
        try {
            // Get and validate inputs
            String empIdText = viewAdd.getFieldEmployeeId().getText().trim();
            String dateText = viewAdd.getFieldDate().getText().trim();
            String timeIn = viewAdd.getFieldTimeIn().getText().trim();
            String timeOut = viewAdd.getFieldTimeOut().getText().trim();

            if (empIdText.isEmpty() || dateText.isEmpty() || timeIn.isEmpty() || timeOut.isEmpty()) {
                viewAdd.showMessage("Please fill in all fields");
                return;
            }

            // Parse and validate employee ID
            int empId;
            try {
                empId = Integer.parseInt(empIdText);
                if (empId <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                viewAdd.showMessage("Invalid Employee ID. Please enter a valid number");
                return;
            }

            // Parse and validate date
            LocalDate date;
            try {
                date = LocalDate.parse(dateText, dateFormatter);
            } catch (DateTimeParseException e) {
                viewAdd.showMessage("Invalid date format. Please use yyyy-MM-dd");
                return;
            }

            // Validate time format (simple validation)
            if (!InputValidatorService.validateTimeFormat(timeIn)
                    || !InputValidatorService.validateTimeFormat(timeOut)) {
                viewAdd.showMessage("Invalid time format. Please use HH:MM:SS (24-hour format)");
                return;
            }

            // Combine date and time input to create LocalDateTime object
            LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.parse(timeIn));
            LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.parse(timeOut));

            // Create new attendance record
            ClockHour newAttendance = new ClockHour(empId, startDateTime, endDateTime);

            // Save to database through service layer
            boolean success = userHr.addAttendance(newAttendance);

            if (success) {
                viewAdd.showMessage("Attendance record added successfully");
                view.populateAttendanceTable(null, null, null);
//              addView.dispose();
            } else {
                viewAdd.showMessage("Failed to add attendance record");
            }

        } catch (IllegalArgumentException e) {
            viewAdd.showMessage("Error: " + e.getMessage());
        } catch (SQLException e) {
            viewAdd.showMessage("Error: " + e.getMessage());
        } catch (Exception e) {
            viewAdd.showMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the attendance deletion process.
     */
    private void deleteAttendance() {
        int selectedRow = view.getTableAttendance().getSelectedRow();
        int recordId = 0;

        if (selectedRow != -1) { // Check if a row is selected
            recordId = (Integer) view.getTableAttendance().getValueAt(selectedRow, view.getTableAttendance().getColumn("Record ID").getModelIndex());
        } else {
            System.out.println("Please select a record to delete.");
        }

        int response = JOptionPane.showConfirmDialog(
                view,
                "Are you sure you want to delete this employee record?\nThis action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            try {
                boolean success = userHr.deleteAttendance(recordId);
                if (success) {
                    view.populateAttendanceTable(null, null, null);
                    view.showMessage("Attendance record deleted successfully");
                } else {
                    view.showMessage("Failed to delete attendance record");
                }
            } catch (SQLException e) {
                view.showMessage("Database Error: " + e.getMessage());
            } catch (Exception e) {
                view.showMessage("Error deleting record: " + e.getMessage());
            }
        }
    }

    private void searchAttendance() {
        try {
            String searchIdText = view.getFieldSearchEmployeeId().getText();
            String startDateText = view.getFieldSearchPayPeriodStart().getText();
            String endDateText = view.getFieldSearchPayPeriodEnd().getText();

            // If search input is empty, display all records
            if (searchIdText.isBlank() || startDateText.isBlank() || endDateText.isBlank()) {
                view.populateAttendanceTable(null, null, null); // Pass null to show all records
            } else {
                // If search input is not empty, search
                Integer empId = Integer.parseInt(searchIdText); // Parse the employee ID
                LocalDate startDate = LocalDate.parse(startDateText);
                LocalDate endDate = LocalDate.parse(endDateText);

                // Attempt to find the employee by ID
                var foundEmployees = userHr.getEmployeeById(empId);

                // Check if the list is not empty (meaning an employee was found)
                if (!foundEmployees.isEmpty()) {
                    System.out.println("Search Employee : " + foundEmployees.get(0).toString());
                    System.out.println("Search Parameters : Employee ID[" + empId + "], Start Date[" + startDate.toString() + "], End Date[" + endDate.toString() + "]");
                    view.populateAttendanceTable(empId, startDate, endDate); // Populate table with seacrh result
                } else {
                    view.populateAttendanceTable(null, null, null); // No employee found, show all
                    view.showMessage("No employee found with ID: " + empId);
                }
            }
        } catch (NumberFormatException e) {
            view.showMessage("Invalid Employee ID format. Please enter a valid number.");
        } catch (DateTimeParseException ex) {
            view.showMessage("Error: Invalid date format. Please enter dates in YYYY-MM-DD format.");
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

}
