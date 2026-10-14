/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Util.TableModelUtil;
import Model.*;
import Model.UserRole;
import static Model.UserRole.EMPLOYEE;
import static Model.UserRole.FINANCE;
import static Model.UserRole.HR;
import static Model.UserRole.IT;
import Service.*;
import View.LoginView;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.awt.Color;
import Util.DatabaseConnectionUtil;
import Util.ReportGeneratorUtil;
import View.LoginView;
import java.sql.Connection;
import java.text.ParseException;
import java.time.format.DateTimeParseException;

public class EmployeeDashboardView extends javax.swing.JFrame {

    private AuthenticationService authenticationService = new AuthenticationService();
    private EmployeeService user;
    private List<String> availableLeaveTypeNames = new ArrayList<>();
    private List<LeaveRecord> leaveRecords;
    private List<LeaveEntitlement> leaveEntitlements;

    private List<ClockHour> attendanceRecords;
    private List<Payslip> payslipRecords;

    public EmployeeDashboardView(int empId) {
        try {
            this.user = new EmployeeService(authenticationService.loadUserDatafromDatabase(empId));
            this.leaveEntitlements = user.getMyLeaveEntitlements();
            initComponents();
            initCustomComponents();
            customizeViewBasedOnRole(); // Customize the view based on the user's role
            displayEmployeeDetails();
            startClock();

            populateAttendanceTable(null, null); // Initialize with all records

            populateLeaveTables();

            populatePayslipTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to clock in: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(EmployeeDashboardView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void customizeViewBasedOnRole() {
        switch (user.getUserRole()) {
            case HR:
                btnAdminLogin.setVisible(true);
                break;
            case IT:
                btnAdminLogin.setVisible(true);
                break;

            case FINANCE:
                btnAdminLogin.setVisible(true);
                break;

            case EMPLOYEE:
                btnAdminLogin.setVisible(false);
                break;
            default:
        }
    }

    private void displayEmployeeDetails() {
        try {

            // Assign values to variables
            String employeeId = user.getEmpId() + ""; // Get the employee ID
            String password = user.getPassword(); // Get the password
            String userRole = user.getUserRole() + ""; // Get the user role

            // Get personal details
            String firstName = user.getFirstName(); // Get the first name
            String lastName = user.getLastName(); // Get the last name
            String fullName = firstName + " " + lastName; // Concatenate first and last name
            String gender = user.getGender().toString(); // Get the gender
            String birthday = user.getBirthday().toString(); // Get the birthday
            String phoneNumber = user.getPhoneNumber(); // Get the phone number

            // Get address
            String addressType = user.getEmployeeAddress().getAddressType().toString(); // Get the address type
            String buildingStreet = (user.getEmployeeAddress().getAddressRecord().getBuildingStreet() == null) ? "N/A" : user.getEmployeeAddress().getAddressRecord().getBuildingStreet();// Get building-street and handle null values
            String barangay = (user.getEmployeeAddress().getAddressRecord().getBarangay() == null) ? "N/A" : user.getEmployeeAddress().getAddressRecord().getBarangay(); // Get barangay and handle null values
            String cityMunicipality = (user.getEmployeeAddress().getAddressRecord().getCityMunicipality() == null) ? "N/A" : user.getEmployeeAddress().getAddressRecord().getCityMunicipality(); // Get city-municipality and handle null values
            String province = (user.getEmployeeAddress().getAddressRecord().getProvince() == null) ? "N/A" : user.getEmployeeAddress().getAddressRecord().getCityMunicipality(); // Get province  and handle null values
            String zipcode = (user.getEmployeeAddress().getAddressRecord().getZipcode() == null) ? "N/A" : user.getEmployeeAddress().getAddressRecord().getZipcode(); // Get the zipcode and handle null values

            // Get identification details
            String sssNumber = user.getSssNumber(); // Get the SSS number
            String philhealthNumber = user.getPhilhealthNumber(); // Get the PhilHealth number
            String tinNumber = user.getTinNumber(); // Get the TIN number
            String pagibigNumber = user.getPagibigNumber(); // Get the Pag-IBIG number

            // Get employment details
            String department = user.getDepartment() + ""; // Get the department
            String status = user.getEmpStatus().toString(); // Get the employment status
            String position = user.getPositionSalaryHistory().getJobPosition().getTitle(); // Get the position
            String supervisorId = (user.getSupervisorId() == null || user.getSupervisorId() == 0) ? "N/A" : user.getSupervisorId() + ""; // Get the supervisor ID

            String basicSalary = String.valueOf(user.getPositionSalaryHistory().getBasicSalary()); // Get the basic salary
            String hourlyRate = String.valueOf(user.getPositionSalaryHistory().getHourlyRate()); // Get the hourly rate
            String grossSemiMonthlyRate = String.valueOf(user.getPositionSalaryHistory().getGrossSemiMonthlyRate()); // Get the gross semi-monthly rate
            String riceSubsidy = String.valueOf(user.getPositionSalaryHistory().getRiceSubsidy()); // Get the rice subsidy
            String phoneAllowance = String.valueOf(user.getPositionSalaryHistory().getPhoneAllowance()); // Get the phone allowance
            String clothingAllowance = String.valueOf(user.getPositionSalaryHistory().getClothingAllowance()); // Get the clothing allowance

            // Set the JLabels
            lblTopBarName.setText(fullName);
            lblLargeName.setText(fullName);
            lblLargePosition.setText(position);
            lblLargeStatus.setText(status);
            lblGreeting.setText("Good day, " + firstName);

            lblGender.setText("Gender: " + gender);
            lblAddress.setText(addressType + " ADDRESS");
            lblBuildingStreet.setText("Building/Street: " + buildingStreet);
            lblBarangay.setText("Barangay: " + barangay);
            lblCityMunicipality.setText("City/Municipality: " + cityMunicipality);
            lblProvince.setText("Province: " + province);

            lblBasicSalary.setText("Basic Salary: PHP" + basicSalary);
            lblBirthday.setText("Birthday: " + birthday);
            lblClothingAllowance.setText("Clothing Allowance: PHP" + clothingAllowance);
            lblDepartment.setText("Department: " + department);
            lblHourlyRate.setText("Hourly Rate: PHP" + hourlyRate);
            lblName.setText("Name: " + fullName);
            lblPagibig.setText("Pag-IBIG No. : " + pagibigNumber);
            lblPhilhealth.setText("PhilHealth No. : " + philhealthNumber);
            lblPhoneAllowance.setText("Phone Allowance: PHP" + phoneAllowance);
            lblPosition.setText("Position: " + position);
            lblRiceSubsidy.setText("Rice Subsidy: PHP" + riceSubsidy);
            lblSemiRate.setText("Gross Semi-monthly Rate: PHP" + grossSemiMonthlyRate);
            lblSss.setText("Social Security System (SSS) No. : " + sssNumber);
            lblStatus.setText("Employment Status: " + status);
            lblSupervisor.setText("Supervisor's ID: " + supervisorId);
            lblTin.setText("Tax Identification Number (TIN): " + tinNumber);
            lblPhone.setText("Phone Number: " + phoneNumber);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormatter dateFormatter = new DateFormatter(dateFormat);
            dateFormatter.setAllowsInvalid(false); // Prevents invalid input
            dateFormatter.setCommitsOnValidEdit(true); // Commits the value on valid edit

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startClock() {
        // Create a Timer to update the clock every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the current time
                Date now = new Date();
                // Format the time as HH:mm:ss
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
                String time = formatter.format(now);
                // Update the label with the current time
                lblClock.setText(time);
            }
        });
        timer.start(); // Start the timer
    }

    private String getLeaveTypeNameFromLeaveTypeId(int leaveTypeId) {
        String leaveTypeName = "Unknown"; // Initialize with a default "unknown" value

        try {
            List<LeaveEntitlement> entitlementRecords = user.getMyLeaveEntitlements();

            for (LeaveEntitlement entitlement : entitlementRecords) {
                // Check for null to prevent NullPointerException if getLeaveType() or getName() returns null
                if (entitlement != null && entitlement.getLeaveType() != null
                        && leaveTypeId == entitlement.getLeaveType().getLeaveTypeId()) {

                    leaveTypeName = entitlement.getLeaveType().getName();
                    break; // Exit the loop once the leave type is found
                }
            }

        } catch (SQLException ex) {
            // Log the exception
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error fetching leave entitlements: " + ex.getMessage(), ex);
            // If an SQL exception occurs, return a specific error message or default
            leaveTypeName = "Error fetching type (DB Error)";
        } catch (Exception ex) { // Catch other potential runtime exceptions like NullPointerException
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unexpected error in getLeaveTypeNameFromLeaveTypeId: " + ex.getMessage(), ex);
            leaveTypeName = "Error (Unexpected)";
        }

        return leaveTypeName;
    }

    private void populateLeaveTables() throws SQLException {

        // Leave Record
        leaveRecords = user.getMyLeaveRecords();

        String[] columnNamesLeave = {
            "Leave Record ID", "Employee ID", "Leave Type Name", "Application Date",
            "Start Date", "End Date", "Days Used", "Reason", "Leave Status",
            "Status Updated By", "Status Updated At", "Remarks"

        };

        DefaultTableModel tableModelLeave = TableModelUtil.createReadOnlyTableModel(
                leaveRecords,
                columnNamesLeave,
                record -> new Object[]{
                    record.getLeaveRecordId(),
                    record.getEmployeeId(),
                    getLeaveTypeNameFromLeaveTypeId(record.getLeaveTypeId()), // You need to implement this method

                    record.getApplicationDate(),
                    record.getAppliedStartDate(),
                    record.getAppliedEndDate(),
                    record.getDaysUsed(),
                    record.getReason(),
                    record.getStatus(),
                    record.getStatusUpdatedBy(),
                    record.getStatusUpdatedAt(),
                    record.getRemarks()

                }
        );

        tblLeave.setModel(tableModelLeave);

        // Apply the custom cell renderer to all columns
        tblLeave.setDefaultRenderer(Object.class, new ZebraStripingCellRenderer());

        // Customize the table header background
        tblLeave.getTableHeader().setBackground(ColorTheme.BLUE);

        tblLeave.getTableHeader().setForeground(Color.WHITE); // White text

        // Set Column Widths
        TableColumnModel columnModelLeave = tblLeave.getColumnModel();

        int[] columnWidthsLeave = {
            100, // "Leave Record ID"

            100, // "Employee ID"

            130, // "Leave Type Name"

            100, // "Application Date"

            100, // "Start Date"

            100, // "End Date"

            100, // "Days Used"

            150, // "Reason"

            120, // "Leave Status"

            130, // "Status Updated By"

            130, // "Status Updated At"

            200, // "Remarks"
        };

        // Loop through the columns and set their preferred widths
        for (int i = 0; i < columnWidthsLeave.length; i++) {

            if (i < columnModelLeave.getColumnCount()) { // Safety check in case widths array is shorter

                TableColumn column = columnModelLeave.getColumn(i);

                column.setPreferredWidth(columnWidthsLeave[i]);

            }
        }

// LEAVE ENTITLEMENT
        String[] columnNamesEntitlement = {
            "Leave Type ID", "Leave Type Name", "Entitlement Days",
            "Remaining Days"

        };

        DefaultTableModel tableModelEntitlement = TableModelUtil.createReadOnlyTableModel(
                leaveEntitlements,
                columnNamesEntitlement,
                record -> new Object[]{
                    record.getLeaveType().getLeaveTypeId(),
                    record.getLeaveType().getName(),
                    record.getEntitlementDays(),
                    record.getRemainingDays()

                }
        );

        tblLeaveEntitlement.setModel(tableModelEntitlement);

        // Apply the custom cell renderer to all columns
        tblLeaveEntitlement.setDefaultRenderer(Object.class, new ZebraStripingCellRenderer());

        // Customize the table header background
        tblLeaveEntitlement.getTableHeader().setBackground(ColorTheme.BLUE);

        tblLeaveEntitlement.getTableHeader().setForeground(Color.WHITE); // White text

        // SET COMBOBOX MODEL
        for (LeaveEntitlement record : leaveEntitlements) {

            availableLeaveTypeNames.add(record.getLeaveType().getName());

        }

        comboLeaveType.setModel(new DefaultComboBoxModel<>(availableLeaveTypeNames.toArray(new String[0])));

    }

    public void populatePayslipTable() throws SQLException {
        try {
            payslipRecords = user.getMyPayslips(); // Fetch records

            // Define column names for the Payslip table model
            String[] columnNamesPayslip = {
                "Payslip No.", "Employee ID", "Employee Name", "Period Start Date",
                "Period End Date", "Employee Position", "Employee Department",
                "Monthly Rate", "Daily Rate", "Hourly Rate", "Overtime Rate Multiplier",
                "Distinct Days Worked", "Hours Worked", "Overtime Hours", "Gross Income",
                "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Total Benefits",
                "SSS Contribution", "PhilHealth Contribution", "Pag-IBIG Contribution",
                "Taxable Income", "Withholding Tax", "Total Deductions", "Net Pay"
            };

            // Create a table model using the TableModelUtil and a lambda to map Payslip objects to row data
            DefaultTableModel tableModelPayslip = TableModelUtil.createReadOnlyTableModel(payslipRecords,
                    columnNamesPayslip,
                    record -> new Object[]{
                        record.getPayslipId(),
                        record.getEmployeeId(),
                        record.getEmployeeName(),
                        record.getPeriodStartDate(),
                        record.getPeriodEndDate(),
                        record.getEmployeePosition(),
                        record.getEmployeeDepartment(),
                        record.getMonthlyRate(),
                        record.getDailyRate(),
                        record.getHourlyRate(),
                        record.getOvertimeRateMultiplier(),
                        record.getDistinctDaysWorked(),
                        record.getHoursWorked(),
                        record.getOvertimeHours(),
                        record.getGrossIncome(),
                        record.getRiceSubsidy(),
                        record.getPhoneAllowance(),
                        record.getClothingAllowance(),
                        record.getTotalBenefits(),
                        record.getSssContribution(),
                        record.getPhilhealthContribution(),
                        record.getPagibigContribution(),
                        record.getTaxableIncome(),
                        record.getWithholdingTax(),
                        record.getTotalDeductions(),
                        record.getNetIncome()
                    }
            );

            // Set the table model to the JTable
            tblPayslip.setModel(tableModelPayslip);

            // Define preferred widths for each column in the Payslip table
            // These widths correspond to the order of columns defined in tableModelPayslip
            TableColumnModel columnModelPayslip = tblPayslip.getColumnModel();

            int[] columnWidths = {
                80, // "Payslip ID"
                80, // "Employee ID"
                150, // "Employee Name"
                120, // "Period Start Date"
                120, // "Period End Date"
                150, // "Employee Position"
                200, // "Employee Department"
                100, // "Monthly Rate"
                80, // "Daily Rate"
                80, // "Hourly Rate"
                120, // "Overtime Rate Multiplier"
                120, // "Distinct Days Worked"
                100, // "Hours Worked"
                100, // "Overtime Hours"
                100, // "Gross Income"
                100, // "Rice Subsidy"
                100, // "Phone Allowance"
                120, // "Clothing Allowance"
                100, // "Total Benefits"
                120, // "SSS Contribution"
                150, // "PhilHealth Contribution"
                150, // "Pag-IBIG Contribution"
                100, // "Taxable Income"
                100, // "Withholding Tax"
                100, // "Total Deductions"
                100 // "Net Income"
            };

            // Loop through the columns and set their preferred widths
            for (int i = 0; i < columnWidths.length; i++) {
                if (i < columnModelPayslip.getColumnCount()) { // Safety check in case widths array is shorter
                    TableColumn column = columnModelPayslip.getColumn(i);
                    column.setPreferredWidth(columnWidths[i]);
                }
            }

            // Apply the custom cell renderer to all columns
            tblPayslip.setDefaultRenderer(Object.class, new ZebraStripingCellRenderer());

            // Customize the table header background
            tblPayslip.getTableHeader().setBackground(ColorTheme.BLUE);
            tblPayslip.getTableHeader().setForeground(Color.WHITE);

        } catch (Exception e) {
            // Handle any exceptions (e.g., database errors, parsing errors)
            JOptionPane.showMessageDialog(this, "Error fetching payslip records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void populateAttendanceTable(LocalDate startDate, LocalDate endDate) throws SQLException {
        try {
            // Check if month and year are provided for filtering
            if (startDate != null && endDate != null) {
                attendanceRecords = user.getMyAttendanceFromPayPeriod(startDate, endDate); // Fetch filtered records
            } else {
                attendanceRecords = user.getMyAttendance(); // Fetch all records
            }

            // Debug: Print the fetched records
            System.out.println("Fetched Records: " + attendanceRecords);

            // Create a table model with the required columns
            String[] columnNamesAttendance = {"Record ID", "Time In", "Time Out", "Hours Worked"};
            DefaultTableModel tableModelAttendance = TableModelUtil.createReadOnlyTableModel(
                    attendanceRecords,
                    columnNamesAttendance,
                    record -> new Object[]{
                        record.getClockHourId(),
                        record.getClockInDateTime(),
                        record.getClockOutDateTime(),
                        record.getHoursWorked()
                    }
            );

            // Set the table model to the JTable
            tableAttendance.setModel(tableModelAttendance);

            // Apply the custom cell renderer to all columns
            tableAttendance.setDefaultRenderer(Object.class, new ZebraStripingCellRenderer());

            // Customize the table header background
            tableAttendance.getTableHeader().setBackground(ColorTheme.BLUE);
            tableAttendance.getTableHeader().setForeground(Color.WHITE);

        } catch (Exception e) {
            // Handle any exceptions (e.g., database errors, parsing errors)
            JOptionPane.showMessageDialog(this, "Error fetching attendance records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initCustomComponents() {

        tblLeave.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//
//        tblLeaveEntitlement.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblPayslip.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Add a ListSelectionListener to the Payslip table
        tblPayslip.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) { // Only respond when the selection is final
                int selectedRow = tblPayslip.getSelectedRow();
                if (selectedRow != -1) { // Check if a row is selected
                    // Get the selected record from the original list
                    Payslip selectedPayslipRecord = payslipRecords.get(selectedRow);

                    // Update text fields with the selected payslip record's data
                    fieldPayslipId.setText(selectedPayslipRecord.getPayslipId() != null ? selectedPayslipRecord.getPayslipId().toString() : "");
                    fieldEmployeeId.setText(selectedPayslipRecord.getEmployeeId() != null ? selectedPayslipRecord.getEmployeeId().toString() : "");
                    fieldPeriodStartDate.setText(selectedPayslipRecord.getPeriodStartDate() != null ? selectedPayslipRecord.getPeriodStartDate().toString() : "");
                    fieldPeriodEndDate.setText(selectedPayslipRecord.getPeriodEndDate() != null ? selectedPayslipRecord.getPeriodEndDate().toString() : "");
                    fieldHoursWorked.setText(selectedPayslipRecord.getHoursWorked() != null ? selectedPayslipRecord.getHoursWorked().toString() : "");
                    fieldOvertimeHours.setText(selectedPayslipRecord.getOvertimeHours() != null ? selectedPayslipRecord.getOvertimeHours().toString() : "");
                    fieldGrossIncome.setText(selectedPayslipRecord.getGrossIncome() != null ? selectedPayslipRecord.getGrossIncome().toString() : "");
                    fieldTotalBenefits.setText(selectedPayslipRecord.getTotalBenefits() != null ? selectedPayslipRecord.getTotalBenefits().toString() : "");
                    fieldTotalDeductions.setText(selectedPayslipRecord.getTotalDeductions() != null ? selectedPayslipRecord.getTotalDeductions().toString() : "");
                    fieldNetPay.setText(selectedPayslipRecord.getNetIncome() != null ? selectedPayslipRecord.getNetIncome().toString() : "");
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlBackground = new javax.swing.JPanel();
        pnlSidebar = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        tglHome = new javax.swing.JToggleButton();
        tglProfile = new javax.swing.JToggleButton();
        tglAttendance = new javax.swing.JToggleButton();
        tglLeave = new javax.swing.JToggleButton();
        tglPayslip = new javax.swing.JToggleButton();
        btnAdminLogin = new javax.swing.JButton();
        pnlTopbar = new javax.swing.JPanel();
        lblTopBarName = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblMotorPh = new javax.swing.JLabel();
        pnlCardLayout = new javax.swing.JPanel();
        pnlHome = new javax.swing.JPanel();
        lblGreeting = new javax.swing.JLabel();
        pnlClock = new javax.swing.JPanel();
        lblClock = new javax.swing.JLabel();
        btnClockOut = new javax.swing.JButton();
        btnClockIn = new javax.swing.JButton();
        pnlTimeLog = new javax.swing.JPanel();
        pnlProfile = new javax.swing.JPanel();
        lblGreeting1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblLargePosition = new javax.swing.JLabel();
        lblLargeName = new javax.swing.JLabel();
        lblLargeStatus = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblBirthday = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblBuildingStreet = new javax.swing.JLabel();
        lblBarangay = new javax.swing.JLabel();
        lblCityMunicipality = new javax.swing.JLabel();
        lblProvince = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblSss = new javax.swing.JLabel();
        lblPagibig = new javax.swing.JLabel();
        lblPhilhealth = new javax.swing.JLabel();
        lblTin = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblPosition = new javax.swing.JLabel();
        lblDepartment = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblBasicSalary = new javax.swing.JLabel();
        lblHourlyRate = new javax.swing.JLabel();
        lblSupervisor = new javax.swing.JLabel();
        lblClothingAllowance = new javax.swing.JLabel();
        lblSemiRate = new javax.swing.JLabel();
        lblRiceSubsidy = new javax.swing.JLabel();
        lblPhoneAllowance = new javax.swing.JLabel();
        pnlAttendance = new javax.swing.JPanel();
        lblGreeting3 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableAttendance = new javax.swing.JTable();
        panelSearch = new javax.swing.JPanel();
        btnSearchAttendance = new javax.swing.JButton();
        fieldSearchPayPeriodEnd = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        fieldSearchPayPeriodStart = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        btnShowAllAttendance = new javax.swing.JButton();
        pnlLeave = new javax.swing.JPanel();
        lblGreeting2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblLeaveEntitlement = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        comboLeaveType = new javax.swing.JComboBox<>();
        txtLeaveEnd = new javax.swing.JFormattedTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtReason = new javax.swing.JTextArea();
        txtLeaveStart = new javax.swing.JFormattedTextField();
        btnSubmit = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblLeave = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        pnlPayslip = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPayslip = new javax.swing.JTable();
        lblGreeting4 = new javax.swing.JLabel();
        panelFields = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fieldPayslipId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        fieldPeriodStartDate = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        fieldHoursWorked = new javax.swing.JTextField();
        fieldOvertimeHours = new javax.swing.JTextField();
        fieldTotalBenefits = new javax.swing.JTextField();
        fieldEmployeeId = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        fieldPeriodEndDate = new javax.swing.JTextField();
        fieldTotalDeductions = new javax.swing.JTextField();
        fieldNetPay = new javax.swing.JTextField();
        fieldGrossIncome = new javax.swing.JTextField();
        btnPrint = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlBackground.setBackground(java.awt.SystemColor.menu);
        pnlBackground.setPreferredSize(new java.awt.Dimension(1300, 850));
        pnlBackground.setLayout(new java.awt.BorderLayout());

        pnlSidebar.setBackground(new java.awt.Color(0, 51, 153));
        pnlSidebar.setPreferredSize(new java.awt.Dimension(220, 982));

        jPanel2.setOpaque(false);
        java.awt.GridBagLayout jPanel2Layout = new java.awt.GridBagLayout();
        jPanel2Layout.columnWidths = new int[] {0, 20, 0, 20, 0};
        jPanel2Layout.rowHeights = new int[] {0, 20, 0, 20, 0, 20, 0, 20, 0, 20, 0};
        jPanel2.setLayout(jPanel2Layout);
        btnGroupSidebar = new ButtonGroup();

        tglHome.setText("Home");
        tglHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglHomeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel2.add(tglHome, gridBagConstraints);
        btnGroupSidebar.add(tglHome);

        tglProfile.setText("Profile");
        tglProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglProfileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel2.add(tglProfile, gridBagConstraints);
        btnGroupSidebar.add(tglProfile);

        tglAttendance.setText("Attendance");
        tglAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglAttendanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel2.add(tglAttendance, gridBagConstraints);
        btnGroupSidebar.add(tglAttendance);

        tglLeave.setText("Leave");
        tglLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglLeaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel2.add(tglLeave, gridBagConstraints);
        btnGroupSidebar.add(tglLeave);

        tglPayslip.setText("Payslip");
        tglPayslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglPayslipActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel2.add(tglPayslip, gridBagConstraints);
        btnGroupSidebar.add(tglPayslip);

        btnAdminLogin.setBackground(new java.awt.Color(102, 153, 255));
        btnAdminLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAdminLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnAdminLogin.setText("Admin Login");
        btnAdminLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSidebarLayout = new javax.swing.GroupLayout(pnlSidebar);
        pnlSidebar.setLayout(pnlSidebarLayout);
        pnlSidebarLayout.setHorizontalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(btnAdminLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSidebarLayout.setVerticalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(220, 220, 220)
                .addComponent(btnAdminLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(239, Short.MAX_VALUE))
        );

        pnlBackground.add(pnlSidebar, java.awt.BorderLayout.WEST);

        pnlTopbar.setBackground(new java.awt.Color(0, 51, 153));
        pnlTopbar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 153), new java.awt.Color(0, 51, 153), new java.awt.Color(0, 0, 51), new java.awt.Color(0, 51, 153)));
        pnlTopbar.setPreferredSize(new java.awt.Dimension(1920, 70));

        lblTopBarName.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblTopBarName.setForeground(new java.awt.Color(255, 255, 255));
        lblTopBarName.setText("Name");

        jButton1.setText("Log out");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblMotorPh.setFont(new java.awt.Font("Verdana", 1, 20)); // NOI18N
        lblMotorPh.setForeground(new java.awt.Color(102, 153, 255));
        lblMotorPh.setText("MotorPH");

        javax.swing.GroupLayout pnlTopbarLayout = new javax.swing.GroupLayout(pnlTopbar);
        pnlTopbar.setLayout(pnlTopbarLayout);
        pnlTopbarLayout.setHorizontalGroup(
            pnlTopbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTopbarLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(lblMotorPh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2545, Short.MAX_VALUE)
                .addComponent(lblTopBarName)
                .addGap(103, 103, 103)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        pnlTopbarLayout.setVerticalGroup(
            pnlTopbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTopbarLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(pnlTopbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTopBarName)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMotorPh))
                .addGap(13, 13, 13))
        );

        pnlBackground.add(pnlTopbar, java.awt.BorderLayout.NORTH);

        pnlCardLayout.setBackground(java.awt.SystemColor.menu);
        pnlCardLayout.setPreferredSize(new java.awt.Dimension(1500, 800));
        pnlCardLayout.setLayout(new java.awt.CardLayout());

        lblGreeting.setFont(new java.awt.Font("Verdana", 1, 40)); // NOI18N
        lblGreeting.setText("Good day, Name");

        pnlClock.setBackground(new java.awt.Color(255, 255, 255));

        lblClock.setFont(new java.awt.Font("Verdana", 1, 64)); // NOI18N
        lblClock.setText("00 : 00 AM");

        btnClockOut.setBackground(new java.awt.Color(0, 0, 51));
        btnClockOut.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnClockOut.setForeground(new java.awt.Color(255, 255, 255));
        btnClockOut.setText("Clock-out");
        btnClockOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClockOutActionPerformed(evt);
            }
        });

        btnClockIn.setBackground(new java.awt.Color(0, 0, 51));
        btnClockIn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnClockIn.setForeground(new java.awt.Color(255, 255, 255));
        btnClockIn.setText("Clock-in");
        btnClockIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClockInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlClockLayout = new javax.swing.GroupLayout(pnlClock);
        pnlClock.setLayout(pnlClockLayout);
        pnlClockLayout.setHorizontalGroup(
            pnlClockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClockLayout.createSequentialGroup()
                .addGroup(pnlClockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlClockLayout.createSequentialGroup()
                        .addGap(354, 354, 354)
                        .addComponent(lblClock))
                    .addGroup(pnlClockLayout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(btnClockIn, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(btnClockOut, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(212, Short.MAX_VALUE))
        );
        pnlClockLayout.setVerticalGroup(
            pnlClockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClockLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(lblClock)
                .addGap(87, 87, 87)
                .addGroup(pnlClockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClockIn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClockOut, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(134, Short.MAX_VALUE))
        );

        pnlTimeLog.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlTimeLogLayout = new javax.swing.GroupLayout(pnlTimeLog);
        pnlTimeLog.setLayout(pnlTimeLogLayout);
        pnlTimeLogLayout.setHorizontalGroup(
            pnlTimeLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );
        pnlTimeLogLayout.setVerticalGroup(
            pnlTimeLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGreeting)
                    .addGroup(pnlHomeLayout.createSequentialGroup()
                        .addComponent(pnlClock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(pnlTimeLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(lblGreeting)
                .addGap(18, 18, 18)
                .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTimeLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlClock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(252, Short.MAX_VALUE))
        );

        pnlCardLayout.add(pnlHome, "card2");

        lblGreeting1.setFont(new java.awt.Font("Verdana", 1, 40)); // NOI18N
        lblGreeting1.setText("Profile");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 197, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 181, Short.MAX_VALUE)
        );

        lblLargePosition.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblLargePosition.setText("Position");

        lblLargeName.setFont(new java.awt.Font("Verdana", 1, 36)); // NOI18N
        lblLargeName.setText("Name");

        lblLargeStatus.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblLargeStatus.setText("Employment Status");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLargePosition)
                    .addComponent(lblLargeStatus)
                    .addComponent(lblLargeName))
                .addContainerGap(485, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(lblLargeStatus)
                        .addGap(18, 18, 18)
                        .addComponent(lblLargeName)
                        .addGap(18, 18, 18)
                        .addComponent(lblLargePosition))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1516, 293));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(1500, 252));

        lblName.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblName.setText("Name: ");

        lblBirthday.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblBirthday.setText("Birthday: ");

        lblAddress.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblAddress.setText("\"\"");

        lblPhone.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblPhone.setText("Phone No. :");

        lblGender.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblGender.setText("Gender: ");

        lblBuildingStreet.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblBuildingStreet.setText("Building/Street :");

        lblBarangay.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblBarangay.setText("Barangay :");

        lblCityMunicipality.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblCityMunicipality.setText("City/Municipality :");

        lblProvince.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblProvince.setText("Province :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName)
                    .addComponent(lblGender)
                    .addComponent(lblBirthday)
                    .addComponent(lblPhone))
                .addGap(428, 428, 428)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBuildingStreet)
                    .addComponent(lblAddress)
                    .addComponent(lblBarangay)
                    .addComponent(lblCityMunicipality)
                    .addComponent(lblProvince))
                .addGap(1155, 1155, 1155))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(lblAddress))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBirthday)
                    .addComponent(lblBuildingStreet))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGender)
                    .addComponent(lblBarangay))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCityMunicipality)
                    .addComponent(lblPhone))
                .addGap(18, 18, 18)
                .addComponent(lblProvince)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Personal Info", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        lblSss.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblSss.setText("Social Security System (SSS) No. :");

        lblPagibig.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblPagibig.setText("Pag-IBIG No. :");

        lblPhilhealth.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblPhilhealth.setText("PhilHealth No. :");

        lblTin.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblTin.setText("Taxpayer Identification Number (TIN):");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTin)
                    .addComponent(lblPhilhealth)
                    .addComponent(lblSss)
                    .addComponent(lblPagibig))
                .addContainerGap(567, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lblSss)
                .addGap(18, 18, 18)
                .addComponent(lblPhilhealth)
                .addGap(27, 27, 27)
                .addComponent(lblTin)
                .addGap(18, 18, 18)
                .addComponent(lblPagibig)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Identification Info", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        lblPosition.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblPosition.setText("Position: ");

        lblDepartment.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblDepartment.setText("Department: ");

        lblStatus.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblStatus.setText("Status: ");

        lblBasicSalary.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblBasicSalary.setText("Basic Salary: ");

        lblHourlyRate.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblHourlyRate.setText("Hourly Rate:");

        lblSupervisor.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblSupervisor.setText("Immediate Supervisor: ");

        lblClothingAllowance.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblClothingAllowance.setText("Clothing Allowance:");

        lblSemiRate.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblSemiRate.setText("Gross Semi-monthly Rate: ");

        lblRiceSubsidy.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblRiceSubsidy.setText("Rice Subsidy:");

        lblPhoneAllowance.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        lblPhoneAllowance.setText("Phone Allowance: ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDepartment)
                    .addComponent(lblStatus)
                    .addComponent(lblPosition)
                    .addComponent(lblSupervisor)
                    .addComponent(lblBasicSalary))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 289, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblClothingAllowance)
                    .addComponent(lblHourlyRate)
                    .addComponent(lblSemiRate)
                    .addComponent(lblRiceSubsidy)
                    .addComponent(lblPhoneAllowance))
                .addGap(154, 154, 154))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblDepartment)
                        .addGap(18, 18, 18)
                        .addComponent(lblStatus)
                        .addGap(18, 18, 18)
                        .addComponent(lblPosition))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblHourlyRate)
                        .addGap(18, 18, 18)
                        .addComponent(lblSemiRate)
                        .addGap(18, 18, 18)
                        .addComponent(lblRiceSubsidy)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblPhoneAllowance)
                        .addGap(18, 18, 18)
                        .addComponent(lblClothingAllowance))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblSupervisor)
                        .addGap(18, 18, 18)
                        .addComponent(lblBasicSalary)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Employment Info", jPanel5);

        javax.swing.GroupLayout pnlProfileLayout = new javax.swing.GroupLayout(pnlProfile);
        pnlProfile.setLayout(pnlProfileLayout);
        pnlProfileLayout.setHorizontalGroup(
            pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProfileLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblGreeting1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(1690, Short.MAX_VALUE))
        );
        pnlProfileLayout.setVerticalGroup(
            pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProfileLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblGreeting1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(159, 159, 159))
        );

        pnlCardLayout.add(pnlProfile, "card3");

        lblGreeting3.setFont(new java.awt.Font("Verdana", 1, 40)); // NOI18N
        lblGreeting3.setText("Attendance");

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        tableAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(tableAttendance);

        panelSearch.setBackground(new java.awt.Color(255, 255, 255));
        panelSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 1, 12))); // NOI18N

        btnSearchAttendance.setBackground(new java.awt.Color(64, 103, 218));
        btnSearchAttendance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnSearchAttendance.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchAttendance.setText("Search");
        btnSearchAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchAttendanceActionPerformed(evt);
            }
        });

        fieldSearchPayPeriodEnd.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldSearchPayPeriodEnd.setToolTipText("YYYY-MM-DD");
        fieldSearchPayPeriodEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldSearchPayPeriodEndActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel31.setText("End Date :");

        fieldSearchPayPeriodStart.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldSearchPayPeriodStart.setToolTipText("YYYY-MM-DD");
        fieldSearchPayPeriodStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldSearchPayPeriodStartActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel33.setText("Start Date :");

        javax.swing.GroupLayout panelSearchLayout = new javax.swing.GroupLayout(panelSearch);
        panelSearch.setLayout(panelSearchLayout);
        panelSearchLayout.setHorizontalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSearchPayPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSearchPayPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                .addComponent(btnSearchAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addComponent(btnSearchAttendance, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelSearchLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldSearchPayPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)
                            .addComponent(jLabel31)
                            .addComponent(fieldSearchPayPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11))
        );

        btnShowAllAttendance.setBackground(new java.awt.Color(64, 103, 218));
        btnShowAllAttendance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnShowAllAttendance.setForeground(new java.awt.Color(255, 255, 255));
        btnShowAllAttendance.setText("Show All");
        btnShowAllAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowAllAttendanceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 1141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnShowAllAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(426, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnShowAllAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlAttendanceLayout = new javax.swing.GroupLayout(pnlAttendance);
        pnlAttendance.setLayout(pnlAttendanceLayout);
        pnlAttendanceLayout.setHorizontalGroup(
            pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAttendanceLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGreeting3)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAttendanceLayout.setVerticalGroup(
            pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAttendanceLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(lblGreeting3)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(200, Short.MAX_VALUE))
        );

        pnlCardLayout.add(pnlAttendance, "card4");

        lblGreeting2.setFont(new java.awt.Font("Verdana", 1, 40)); // NOI18N
        lblGreeting2.setText("Leaves");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel25.setText("Leave Balances");

        tblLeaveEntitlement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tblLeaveEntitlement);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel26.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel26.setText("End Date:");

        jLabel27.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel27.setText("Leave Type: ");

        jLabel28.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel28.setText("Reason for Leave: ");

        jLabel29.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel29.setText("Start Date:");

        comboLeaveType.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        comboLeaveType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SICK", "VACATION", "BEREAVEMENT" }));
        comboLeaveType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboLeaveTypeActionPerformed(evt);
            }
        });

        txtLeaveEnd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
        txtLeaveEnd.setText("yyyy-MM-dd");
        txtLeaveEnd.setToolTipText("yyyy-MM-dd");
        txtLeaveEnd.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        txtLeaveEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLeaveEndActionPerformed(evt);
            }
        });

        txtReason.setColumns(20);
        txtReason.setRows(5);
        jScrollPane6.setViewportView(txtReason);

        txtLeaveStart.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
        txtLeaveStart.setText("yyyy-MM-dd");
        txtLeaveStart.setToolTipText("yyyy-MM-dd");
        txtLeaveStart.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        txtLeaveStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLeaveStartActionPerformed(evt);
            }
        });

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel32.setText("Request Leave");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(38, 38, 38)
                        .addComponent(txtLeaveEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel28)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboLeaveType, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addGap(25, 25, 25)
                                .addComponent(txtLeaveStart, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(132, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboLeaveType, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(45, 45, 45)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtLeaveStart, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtLeaveEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(jLabel32)
                    .addContainerGap(516, Short.MAX_VALUE)))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tblLeave.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tblLeave);

        jLabel24.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel24.setText("Requests Sent");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlLeaveLayout = new javax.swing.GroupLayout(pnlLeave);
        pnlLeave.setLayout(pnlLeaveLayout);
        pnlLeaveLayout.setHorizontalGroup(
            pnlLeaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLeaveLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(pnlLeaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGreeting2)
                    .addGroup(pnlLeaveLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(pnlLeaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(1509, Short.MAX_VALUE))
        );
        pnlLeaveLayout.setVerticalGroup(
            pnlLeaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLeaveLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(lblGreeting2)
                .addGap(18, 18, 18)
                .addGroup(pnlLeaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlLeaveLayout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(197, Short.MAX_VALUE))
        );

        pnlCardLayout.add(pnlLeave, "card5");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tblPayslip.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblPayslip);

        lblGreeting4.setFont(new java.awt.Font("Verdana", 1, 40)); // NOI18N
        lblGreeting4.setText("Payslips");

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Payslip Summary"));
        panelFields.setPreferredSize(new java.awt.Dimension(1200, 590));
        panelFields.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 1, 14)); // NOI18N
        jLabel1.setText("Employee ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 14)); // NOI18N
        jLabel2.setText("Payslip No :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel2, gridBagConstraints);

        fieldPayslipId.setEditable(false);
        fieldPayslipId.setBackground(new java.awt.Color(255, 255, 255));
        fieldPayslipId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPayslipId.setPreferredSize(new java.awt.Dimension(150, 22));
        fieldPayslipId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldPayslipIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldPayslipId, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel4.setText("Period Start Date  :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelFields.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel5.setText("Period End Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel5, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel8.setText("Total Benefits :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel9.setText("Hours Worked :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(19, 0, 0, 0);
        panelFields.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel10.setText("Gross Income :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelFields.add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel11.setText("Overtime Hours :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel11, gridBagConstraints);

        fieldPeriodStartDate.setEditable(false);
        fieldPeriodStartDate.setBackground(new java.awt.Color(255, 255, 255));
        fieldPeriodStartDate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPeriodStartDate.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelFields.add(fieldPeriodStartDate, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel20.setText("Net Pay :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(20, 7, 0, 0);
        panelFields.add(jLabel20, gridBagConstraints);

        fieldHoursWorked.setEditable(false);
        fieldHoursWorked.setBackground(new java.awt.Color(255, 255, 255));
        fieldHoursWorked.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldHoursWorked.setPreferredSize(new java.awt.Dimension(150, 22));
        fieldHoursWorked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldHoursWorkedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(19, 0, 0, 0);
        panelFields.add(fieldHoursWorked, gridBagConstraints);

        fieldOvertimeHours.setEditable(false);
        fieldOvertimeHours.setBackground(new java.awt.Color(255, 255, 255));
        fieldOvertimeHours.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldOvertimeHours.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldOvertimeHours, gridBagConstraints);

        fieldTotalBenefits.setEditable(false);
        fieldTotalBenefits.setBackground(new java.awt.Color(255, 255, 255));
        fieldTotalBenefits.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldTotalBenefits.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldTotalBenefits, gridBagConstraints);

        fieldEmployeeId.setEditable(false);
        fieldEmployeeId.setBackground(new java.awt.Color(255, 255, 255));
        fieldEmployeeId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldEmployeeId.setPreferredSize(new java.awt.Dimension(150, 22));
        fieldEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldEmployeeIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 11);
        panelFields.add(fieldEmployeeId, gridBagConstraints);

        jLabel36.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel36.setText("Total Deductions :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panelFields.add(jLabel36, gridBagConstraints);

        fieldPeriodEndDate.setEditable(false);
        fieldPeriodEndDate.setBackground(new java.awt.Color(255, 255, 255));
        fieldPeriodEndDate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPeriodEndDate.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldPeriodEndDate, gridBagConstraints);

        fieldTotalDeductions.setEditable(false);
        fieldTotalDeductions.setBackground(new java.awt.Color(255, 255, 255));
        fieldTotalDeductions.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldTotalDeductions.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldTotalDeductions, gridBagConstraints);

        fieldNetPay.setEditable(false);
        fieldNetPay.setBackground(new java.awt.Color(255, 255, 255));
        fieldNetPay.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldNetPay.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelFields.add(fieldNetPay, gridBagConstraints);

        fieldGrossIncome.setEditable(false);
        fieldGrossIncome.setBackground(new java.awt.Color(255, 255, 255));
        fieldGrossIncome.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldGrossIncome.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelFields.add(fieldGrossIncome, gridBagConstraints);

        btnPrint.setBackground(new java.awt.Color(64, 103, 218));
        btnPrint.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setText("Print Payslip");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPayslipLayout = new javax.swing.GroupLayout(pnlPayslip);
        pnlPayslip.setLayout(pnlPayslipLayout);
        pnlPayslipLayout.setHorizontalGroup(
            pnlPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPayslipLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(pnlPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGreeting4)
                    .addGroup(pnlPayslipLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnlPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPayslipLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPayslipLayout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(1539, Short.MAX_VALUE))
        );
        pnlPayslipLayout.setVerticalGroup(
            pnlPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPayslipLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(lblGreeting4)
                .addGap(18, 18, 18)
                .addGroup(pnlPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPayslipLayout.createSequentialGroup()
                        .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(196, Short.MAX_VALUE))
        );

        pnlCardLayout.add(pnlPayslip, "card6");

        pnlBackground.add(pnlCardLayout, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlBackground, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        // Proceed with logout if the user confirms
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Close the current window
                this.dispose(); // Dispose the current JFrame

                // Open the login window (or another appropriate screen)
                LoginView login = new LoginView(); // Replace with your login form class
                login.setVisible(true);

                // Clear session data (if applicable)
                // Example: SessionManager.clearSession();
                // Show success message
                JOptionPane.showMessageDialog(null, "Logged out successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // Handle exceptions
                JOptionPane.showMessageDialog(null, "Error during logout: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tglAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglAttendanceActionPerformed
        CardLayout cardLayout = (CardLayout) pnlCardLayout.getLayout();
        cardLayout.show(pnlCardLayout, "card4");
    }//GEN-LAST:event_tglAttendanceActionPerformed

    private void btnClockOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClockOutActionPerformed
        try {
            // Attempt to clock out
            user.clockOut();

            // Disable the clock-out button after successful clock-out
            btnClockOut.setEnabled(false);

            // Show success message
            JOptionPane.showMessageDialog(this, "Clocked out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            // Handle SQL exceptions (e.g., database errors)
            JOptionPane.showMessageDialog(this, "Failed to clock out: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            // Refresh the attendance table
            populateAttendanceTable(null, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(EmployeeDashboardView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnClockOutActionPerformed

    private void btnClockInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClockInActionPerformed

        try {
            // Attempt to clock in
            user.clockIn();

            // Disable the clock-in button after successful clock-in
            btnClockIn.setEnabled(false);

            // Show success message
            JOptionPane.showMessageDialog(this, "Clocked in successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh the attendance table
            populateAttendanceTable(null, null);
        } catch (SQLException e) {
            // Handle SQL exceptions (e.g., database errors)
            JOptionPane.showMessageDialog(this, "Failed to clock in: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnClockInActionPerformed

    private void tglHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglHomeActionPerformed
        CardLayout cardLayout = (CardLayout) pnlCardLayout.getLayout();
        cardLayout.show(pnlCardLayout, "card2");

    }//GEN-LAST:event_tglHomeActionPerformed

    private void tglProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglProfileActionPerformed
        CardLayout cardLayout = (CardLayout) pnlCardLayout.getLayout();
        cardLayout.show(pnlCardLayout, "card3");
    }//GEN-LAST:event_tglProfileActionPerformed

    private void tglLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglLeaveActionPerformed
        CardLayout cardLayout = (CardLayout) pnlCardLayout.getLayout();
        cardLayout.show(pnlCardLayout, "card5");
    }//GEN-LAST:event_tglLeaveActionPerformed

    private void txtLeaveEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLeaveEndActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLeaveEndActionPerformed

    private void txtLeaveStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLeaveStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLeaveStartActionPerformed

    private void tglPayslipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglPayslipActionPerformed
        CardLayout cardLayout = (CardLayout) pnlCardLayout.getLayout();
        cardLayout.show(pnlCardLayout, "card6");
    }//GEN-LAST:event_tglPayslipActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        try {
            // Retrieve the Leave Type
            String chosenLeaveTypeName = (String) comboLeaveType.getSelectedItem();
            if (chosenLeaveTypeName == null || chosenLeaveTypeName.trim().isEmpty()) {
                throw new IllegalArgumentException("Please select a leave type.");
            }

            int chosenLeaveTypeId = 0;
            for (LeaveEntitlement each : leaveEntitlements) {
                if (each.getLeaveType().getName().equals(chosenLeaveTypeName)) {
                    chosenLeaveTypeId = each.getLeaveType().getLeaveTypeId();
                    break;
                }
            }

            if (chosenLeaveTypeId == 0) {
                throw new IllegalArgumentException("Selected leave type is not available.");
            }

            // Retrieve the start and end dates
            Date startDate = (Date) txtLeaveStart.getValue();
            Date endDate = (Date) txtLeaveEnd.getValue();
            LocalDate start = startDate != null ? startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            LocalDate end = endDate != null ? endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

            // Check if dates are valid
            if (start == null || end == null) {
                throw new IllegalArgumentException("Please enter valid start and end dates.");
            }

            // Basic date validation
            if (start.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Start date cannot be in the past.");
            }

            // Retrieve the reason for leave
            String reason = txtReason.getText();
            if (reason == null || reason.trim().isEmpty()) {
                throw new IllegalArgumentException("Please enter a reason for leave.");
            }

            // File leave - this now returns boolean properly
            boolean success = user.fileLeave(chosenLeaveTypeId, start, end, reason.trim());

            if (success) {
                // Only refresh and show success if actually successful
                populateLeaveTables();
                JOptionPane.showMessageDialog(this, "Leave application submitted successfully!");
            } else {
                throw new RuntimeException("Failed to submit leave application.");
            }

        } catch (IllegalArgumentException e) {
            // Notify the user of the specific input error
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            // Handle database and business logic errors
            JOptionPane.showMessageDialog(this, e.getMessage(), "Application Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(EmployeeDashboardView.class.getName()).log(Level.SEVERE, "Unexpected error in leave submission", e);
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void comboLeaveTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboLeaveTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboLeaveTypeActionPerformed

    private void btnAdminLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminLoginActionPerformed
        new AdminDashboardView(user).setVisible(true); // Pass the EmployeeService object
        this.dispose();
    }//GEN-LAST:event_btnAdminLoginActionPerformed

 private void btnSearchAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchAttendanceActionPerformed

     try {
         populateAttendanceTable(LocalDate.parse(fieldSearchPayPeriodStart.getText()), LocalDate.parse(fieldSearchPayPeriodEnd.getText()));
     } catch (DateTimeParseException ex) {
         JOptionPane.showMessageDialog(this, "Error: Invalid date format. Please enter dates in YYYY-MM-DD format." + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
     } catch (SQLException ex) {
         JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
         Logger.getLogger(EmployeeDashboardView.class.getName()).log(Level.SEVERE, null, ex);
     }


    }//GEN-LAST:event_btnSearchAttendanceActionPerformed


    private void fieldSearchPayPeriodEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchPayPeriodEndActionPerformed

        // TODO add your handling code here:

    }//GEN-LAST:event_fieldSearchPayPeriodEndActionPerformed


    private void fieldSearchPayPeriodStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchPayPeriodStartActionPerformed

        // TODO add your handling code here:

    }//GEN-LAST:event_fieldSearchPayPeriodStartActionPerformed


    private void btnShowAllAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAllAttendanceActionPerformed

        try {
            populateAttendanceTable(null, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(EmployeeDashboardView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnShowAllAttendanceActionPerformed

    private void fieldPayslipIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldPayslipIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldPayslipIdActionPerformed

    private void fieldHoursWorkedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldHoursWorkedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldHoursWorkedActionPerformed

    private void fieldEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldEmployeeIdActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        String employeeIdStr = fieldEmployeeId.getText();
        String startDateStr = fieldPeriodStartDate.getText();

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
    }//GEN-LAST:event_btnPrintActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmployeeDashboardView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeDashboardView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeDashboardView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeDashboardView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                EmployeeView login;
//                login = new EmployeeView(1);
//                login.setVisible(true);
//            }
//        });
        //</editor-fold>
        //</editor-fold>
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                EmployeeView login;
//                login = new EmployeeView(1);
//                login.setVisible(true);
//            }
//        });
        //</editor-fold>
        //</editor-fold>
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                EmployeeView login;
//                login = new EmployeeView(1);
//                login.setVisible(true);
//            }
//        });
        //</editor-fold>
        //</editor-fold>
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                EmployeeView login;
//                login = new EmployeeView(1);
//                login.setVisible(true);
//            }
//        });
        //</editor-fold>
        //</editor-fold>
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                EmployeeView login;
//                login = new EmployeeView(1);
//                login.setVisible(true);
//            }
//        });
        //</editor-fold>
        //</editor-fold>
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                EmployeeView login;
//                login = new EmployeeView(1);
//                login.setVisible(true);
//            }
//        });
        //</editor-fold>
        //</editor-fold>
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                EmployeeView login;
//                login = new EmployeeView(1);
//                login.setVisible(true);
//            }
//        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdminLogin;
    private javax.swing.JButton btnClockIn;
    private javax.swing.JButton btnClockOut;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSearchAttendance;
    private javax.swing.JButton btnShowAllAttendance;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> comboLeaveType;
    private javax.swing.JTextField fieldEmployeeId;
    private javax.swing.JTextField fieldGrossIncome;
    private javax.swing.JTextField fieldHoursWorked;
    private javax.swing.JTextField fieldNetPay;
    private javax.swing.JTextField fieldOvertimeHours;
    private javax.swing.JTextField fieldPayslipId;
    private javax.swing.JTextField fieldPeriodEndDate;
    private javax.swing.JTextField fieldPeriodStartDate;
    private javax.swing.JTextField fieldSearchPayPeriodEnd;
    private javax.swing.JTextField fieldSearchPayPeriodStart;
    private javax.swing.JTextField fieldTotalBenefits;
    private javax.swing.JTextField fieldTotalDeductions;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblBarangay;
    private javax.swing.JLabel lblBasicSalary;
    private javax.swing.JLabel lblBirthday;
    private javax.swing.JLabel lblBuildingStreet;
    private javax.swing.JLabel lblCityMunicipality;
    private javax.swing.JLabel lblClock;
    private javax.swing.JLabel lblClothingAllowance;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblGreeting;
    private javax.swing.JLabel lblGreeting1;
    private javax.swing.JLabel lblGreeting2;
    private javax.swing.JLabel lblGreeting3;
    private javax.swing.JLabel lblGreeting4;
    private javax.swing.JLabel lblHourlyRate;
    private javax.swing.JLabel lblLargeName;
    private javax.swing.JLabel lblLargePosition;
    private javax.swing.JLabel lblLargeStatus;
    private javax.swing.JLabel lblMotorPh;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPagibig;
    private javax.swing.JLabel lblPhilhealth;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblPhoneAllowance;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JLabel lblProvince;
    private javax.swing.JLabel lblRiceSubsidy;
    private javax.swing.JLabel lblSemiRate;
    private javax.swing.JLabel lblSss;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSupervisor;
    private javax.swing.JLabel lblTin;
    private javax.swing.JLabel lblTopBarName;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel pnlAttendance;
    private javax.swing.JPanel pnlBackground;
    private javax.swing.JPanel pnlCardLayout;
    private javax.swing.JPanel pnlClock;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlLeave;
    private javax.swing.JPanel pnlPayslip;
    private javax.swing.JPanel pnlProfile;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlTimeLog;
    private javax.swing.JPanel pnlTopbar;
    private javax.swing.JTable tableAttendance;
    private javax.swing.JTable tblLeave;
    private javax.swing.JTable tblLeaveEntitlement;
    private javax.swing.JTable tblPayslip;
    private javax.swing.JToggleButton tglAttendance;
    private javax.swing.JToggleButton tglHome;
    private javax.swing.JToggleButton tglLeave;
    private javax.swing.JToggleButton tglPayslip;
    private javax.swing.JToggleButton tglProfile;
    private javax.swing.JFormattedTextField txtLeaveEnd;
    private javax.swing.JFormattedTextField txtLeaveStart;
    private javax.swing.JTextArea txtReason;
    // End of variables declaration//GEN-END:variables
    private ButtonGroup btnGroupSidebar;
    private SimpleDateFormat dateFormat;
    private DateFormatter dateFormatter;
//    private javax.swing.JPanel panelSearch;
//    private javax.swing.JLabel jLabel33;
//    private javax.swing.JLabel jLabel31;
//    private javax.swing.JTextField fieldSearchPayPeriodEnd;
//    private javax.swing.JTextField fieldSearchPayPeriodStart;
//    private javax.swing.JButton btnSearchAttendance;
//    private javax.swing.JButton btnShowAllAttendance;

}
