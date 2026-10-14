/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Config.ApplicationThemeUtility;
import Model.Address;
import Model.ClockHour;
import Model.Department;
import Model.Employee;
import Model.EmploymentStatus;
import Model.Gender;
import Service.EmployeeService;
import Service.HrService;
import Util.TableModelUtil;
import java.awt.Color;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Gracielle Salvador
 */
public class EmployeeMgmtView extends javax.swing.JFrame {

    private List<Employee> employeeRecords;
    private HrService userHr;

    /**
     * Creates new form EmployeeMgmtView
     */
    public EmployeeMgmtView(EmployeeService user) {
        this.userHr = new HrService(user);
        initComponents();
        initCustomComponents();
        populateEmployeeTable(null);

    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public final void populateEmployeeTable(Integer searchFieldEmployeeId) {

        try {

            // Check if employee ID is provided for filtering
            if (searchFieldEmployeeId != null) {
                employeeRecords = userHr.getEmployeeById(searchFieldEmployeeId); // Fetch filtered records
            } else {
                employeeRecords = userHr.getAllEmployees(); // Fetch all records
            }

            for (Employee record : employeeRecords) {
                System.out.println(record.toString());
            }

            String[] columnNamesEmployee = {
                "Employee ID", "First Name", "Last Name", "Gender", "Birthday", "Phone No.",
                "Building/Street", "Barangay", "City/Municipality", "Province", "ZIP code",
                "SSS No.", "PhilHealth No.", "TIN", "Pag-IBIG No.",
                "Employment Status", "Department", "Supervisor's ID",
                "Job Position", "Basic Salary", "Hourly Rate", "Gross Semi-Monthly Rate", "Rice Subsidy", "Phone Allowance", "Clothing Allowance"
            };

            DefaultTableModel tableModelEmployee = TableModelUtil.createReadOnlyTableModel(
                    employeeRecords,
                    columnNamesEmployee,
                    record -> new Object[]{
                        record.getEmpId(),
                        record.getFirstName(),
                        record.getLastName(),
                        record.getGender().toString(),
                        record.getBirthday(),
                        record.getPhoneNumber(),
                        record.getEmployeeAddress().getAddressRecord().getBuildingStreet(),
                        record.getEmployeeAddress().getAddressRecord().getBarangay(),
                        record.getEmployeeAddress().getAddressRecord().getCityMunicipality(),
                        record.getEmployeeAddress().getAddressRecord().getProvince(),
                        record.getEmployeeAddress().getAddressRecord().getZipcode(),
                        record.getSssNumber(),
                        record.getPhilhealthNumber(),
                        record.getTinNumber(),
                        record.getPagibigNumber(),
                        record.getEmpStatus().toString(),
                        record.getDepartment().toString(),
                        record.getSupervisorId(),
                        record.getPositionSalaryHistory().getJobPosition().getTitle(),
                        record.getPositionSalaryHistory().getBasicSalary(),
                        record.getPositionSalaryHistory().getHourlyRate(),
                        record.getPositionSalaryHistory().getGrossSemiMonthlyRate(),
                        record.getPositionSalaryHistory().getRiceSubsidy(),
                        record.getPositionSalaryHistory().getPhoneAllowance(),
                        record.getPositionSalaryHistory().getClothingAllowance(),}
            );
            tblEmployee.setModel(tableModelEmployee);

            //Set Column Widths
            TableColumnModel columnModelEmployee = tblEmployee.getColumnModel();

            int[] columnWidths = {
                80, // "Employee ID"
                120, // "First Name"
                120, // "Last Name"
                100, // "Gender"
                100, // "Birthday"
                120, // "Phone Number"
                150, // "Building/Street"
                100, // "Barangay"
                100, // "City/Municipality"
                100, // "Province"
                80, // "Zipcode"
                100, // "SSS Number"
                120, // "Philhealth Number"
                100, // "TIN Number"
                120, // "Pag-IBIG Number"
                120, // "Employment Status"
                120, // "Department"
                100, // "Supervisor ID"
                150, // "Position"
                100, // "Basic Salary"
                80, // "Hourly Rate"
                120, // "Gross Semi-Monthly Rate"
                80, // "Rice Subsidy"
                100, // "Phone Allowance"
                100 // "Clothing Allowance"
            };

            // Loop through the columns and set their preferred widths
            for (int i = 0; i < columnWidths.length; i++) {
                if (i < columnModelEmployee.getColumnCount()) { // Safety check in case widths array is shorter
                    TableColumn column = columnModelEmployee.getColumn(i);
                    column.setPreferredWidth(columnWidths[i]);
                }
            }

            // Apply the custom cell renderer to all columns
            tblEmployee.setDefaultRenderer(Object.class, new ZebraStripingCellRenderer());

            // Customize the table header background
            tblEmployee.getTableHeader().setBackground(ColorTheme.DARK); // Darker background
            tblEmployee.getTableHeader().setForeground(Color.WHITE); // White text

            fieldEmployeeId.setEditable(false);

        } catch (Exception ex) {
            Logger.getLogger(EmployeeDashboardView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initCustomComponents() {
        tblEmployee.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Initialize Combo boxes
        // Create the EmploymentStatus model
        List<EmploymentStatus> allEmploymentStatuses = Arrays.asList(EmploymentStatus.values());
        DefaultComboBoxModel<EmploymentStatus> employmentStatusModel = new DefaultComboBoxModel<>(allEmploymentStatuses.toArray(new EmploymentStatus[0]));
        // Set the model for comboBoxEmploymentStatus
        comboBoxEmploymentStatus.setModel(employmentStatusModel);

        // Create the Gender model
        List<Gender> allGender = Arrays.asList(Gender.values());
        DefaultComboBoxModel<Gender> genderModel = new DefaultComboBoxModel<>(allGender.toArray(new Gender[0]));
        // Set the model for comboBoxGender
        comboBoxGender.setModel(genderModel);

        // Create the Department model
        List<Department> allDepartments = Arrays.asList(Department.values());
        DefaultComboBoxModel<Department> departmentModel = new DefaultComboBoxModel<>(allDepartments.toArray(new Department[0]));
        // Set the model for comboBoxDepartment
        comboBoxDepartment.setModel(departmentModel);

        // Add a ListSelectionListener to the table
        tblEmployee.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) { // Only respond when the selection is final
                int selectedRow = tblEmployee.getSelectedRow();
                if (selectedRow != -1) { // Check if a row is selected
                    // Get the selected employee record
                    Employee selectedEmployee = employeeRecords.get(selectedRow); // No null check here as per request

                    // Update text fields with the selected employee's data
                    // Personal Details
                    fieldEmployeeId.setText(String.valueOf(selectedEmployee.getEmpId()));
                    fieldFirstName.setText(selectedEmployee.getFirstName());
                    fieldLastName.setText(selectedEmployee.getLastName());
                    comboBoxGender.setSelectedItem(selectedEmployee.getGender());
                    fieldBirthday.setText(selectedEmployee.getBirthday().toString());
                    fieldPhoneNumber.setText(selectedEmployee.getPhoneNumber());

                    // Address Details - Handles Null values
                    Address addressRecord = selectedEmployee.getEmployeeAddress().getAddressRecord();
                    Function<String, String> getOrNa = s -> (s == null || s.trim().isEmpty()) ? "N/A" : s;
                    fieldBuildingStreet.setText(addressRecord != null ? getOrNa.apply(addressRecord.getBuildingStreet()) : "N/A");
                    fieldBarangay.setText(addressRecord != null ? getOrNa.apply(addressRecord.getBarangay()) : "N/A");
                    fieldCityMunicipality.setText(addressRecord != null ? getOrNa.apply(addressRecord.getCityMunicipality()) : "N/A");
                    fieldProvince.setText(addressRecord != null ? getOrNa.apply(addressRecord.getProvince()) : "N/A");
                    fieldZipcode.setText(addressRecord != null ? getOrNa.apply(addressRecord.getZipcode()) : "N/A");

                    // SSS, Philhealth, TIN, Pag-IBIG Numbers
                    fieldSssNumber.setText(selectedEmployee.getSssNumber());
                    fieldPhilhealthNumber.setText(selectedEmployee.getPhilhealthNumber());
                    fieldTin.setText(selectedEmployee.getTinNumber());
                    fieldPagibigNumber.setText(selectedEmployee.getPagibigNumber());

                    // Employment Details
                    comboBoxEmploymentStatus.setSelectedItem(selectedEmployee.getEmpStatus());
                    comboBoxDepartment.setSelectedItem(selectedEmployee.getDepartment());
                    Integer supervisorId = selectedEmployee.getSupervisorId();
                    fieldSupervisorId.setText(supervisorId != null && supervisorId != 0 ? String.valueOf(supervisorId) : "N/A");

                    fieldJobTitle.setText(selectedEmployee.getPositionSalaryHistory().getJobPosition().getTitle());

                    fieldBasicSalary.setText(String.valueOf(selectedEmployee.getPositionSalaryHistory().getBasicSalary()));
                    fieldHourlyRate.setText(String.valueOf(selectedEmployee.getPositionSalaryHistory().getHourlyRate()));
                    fieldGrossSemiMonthlyRate.setText(String.valueOf(selectedEmployee.getPositionSalaryHistory().getGrossSemiMonthlyRate()));
                    fieldRiceSubsidy.setText(String.valueOf(selectedEmployee.getPositionSalaryHistory().getRiceSubsidy()));
                    fieldPhoneAllowance.setText(String.valueOf(selectedEmployee.getPositionSalaryHistory().getPhoneAllowance()));
                    fieldClothingAllowance.setText(String.valueOf(selectedEmployee.getPositionSalaryHistory().getClothingAllowance()));

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

        panelTitle = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        panelBody = new javax.swing.JPanel();
        panelBodyRight = new javax.swing.JPanel();
        panelFieldsButton = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        panelFields = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fieldFirstName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        fieldLastName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        fieldCityMunicipality = new javax.swing.JTextField();
        fieldBirthday = new javax.swing.JTextField();
        fieldPhoneNumber = new javax.swing.JTextField();
        fieldBuildingStreet = new javax.swing.JTextField();
        fieldBarangay = new javax.swing.JTextField();
        fieldProvince = new javax.swing.JTextField();
        fieldZipcode = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        comboBoxEmploymentStatus = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        fieldPhilhealthNumber = new javax.swing.JTextField();
        fieldJobTitle = new javax.swing.JTextField();
        fieldHourlyRate = new javax.swing.JTextField();
        fieldTin = new javax.swing.JTextField();
        fieldPagibigNumber = new javax.swing.JTextField();
        fieldBasicSalary = new javax.swing.JTextField();
        comboBoxDepartment = new javax.swing.JComboBox<>();
        fieldRiceSubsidy = new javax.swing.JTextField();
        fieldPhoneAllowance = new javax.swing.JTextField();
        fieldClothingAllowance = new javax.swing.JTextField();
        fieldSssNumber = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        fieldGrossSemiMonthlyRate = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        fieldSupervisorId = new javax.swing.JTextField();
        fieldEmployeeId = new javax.swing.JTextField();
        comboBoxGender = new javax.swing.JComboBox<>();
        fieldEmployeeId1 = new javax.swing.JTextField();
        fieldEmployeeId2 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        panelLeftTop = new javax.swing.JPanel();
        panelSearch = new javax.swing.JPanel();
        fieldSearchEmployeeId = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmployee = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTitle.setBackground(new java.awt.Color(0, 51, 153));

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Verdana", 0, 29)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Employee Information Management");

        btnLogout.setBackground(new java.awt.Color(0, 51, 153));
        btnLogout.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnBack.setBackground(new java.awt.Color(0, 51, 153));
        btnBack.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTitleLayout = new javax.swing.GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 545, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(panelTitle, java.awt.BorderLayout.PAGE_START);

        panelBody.setLayout(new java.awt.BorderLayout());

        panelBodyRight.setPreferredSize(new java.awt.Dimension(800, 690));
        panelBodyRight.setLayout(new java.awt.BorderLayout());

        panelFieldsButton.setBackground(new java.awt.Color(255, 255, 255));

        btnSave.setBackground(new java.awt.Color(64, 103, 218));
        btnSave.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFieldsButtonLayout = new javax.swing.GroupLayout(panelFieldsButton);
        panelFieldsButton.setLayout(panelFieldsButtonLayout);
        panelFieldsButtonLayout.setHorizontalGroup(
            panelFieldsButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFieldsButtonLayout.createSequentialGroup()
                .addContainerGap(511, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );
        panelFieldsButtonLayout.setVerticalGroup(
            panelFieldsButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFieldsButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        panelBodyRight.add(panelFieldsButton, java.awt.BorderLayout.PAGE_END);

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setPreferredSize(new java.awt.Dimension(1200, 590));
        panelFields.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel1.setText("Employee ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel2.setText("First Name : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel2, gridBagConstraints);

        fieldFirstName.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldFirstName.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldFirstNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldFirstName, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel3.setText("Last Name : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel3, gridBagConstraints);

        fieldLastName.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldLastName.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldLastNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldLastName, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel4.setText("Gender :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel5.setText("Birthday :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel6.setText("Phone No. :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(23, 0, 0, 0);
        panelFields.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel7.setText("Social Security System No. :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 0, 0);
        panelFields.add(jLabel7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel8.setText("Job Title :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel9.setText("PhilHealth No. :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel10.setText("Pag-IBIG No. :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel11.setText("TIN :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel11, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel12.setText("Employment Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel12, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel13.setText("Department :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel13, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel14.setText("Supervisor's ID : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel14, gridBagConstraints);

        fieldCityMunicipality.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldCityMunicipality.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldCityMunicipality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldCityMunicipalityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldCityMunicipality, gridBagConstraints);

        fieldBirthday.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldBirthday.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldBirthday, gridBagConstraints);

        fieldPhoneNumber.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPhoneNumber.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 0, 0);
        panelFields.add(fieldPhoneNumber, gridBagConstraints);

        fieldBuildingStreet.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldBuildingStreet.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldBuildingStreet, gridBagConstraints);

        fieldBarangay.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldBarangay.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldBarangay, gridBagConstraints);

        fieldProvince.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldProvince.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldProvince.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldProvinceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldProvince, gridBagConstraints);

        fieldZipcode.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldZipcode.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldZipcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldZipcodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldZipcode, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel15.setText("Barangay :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel15, gridBagConstraints);

        jLabel16.setText("'");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel16, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel17.setText("Basic Salary :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel17, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel18.setText("Rice Subsidy :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel18, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel19.setText("Phone Allowance :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel19, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel20.setText("Clothing Allowance :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panelFields.add(jLabel20, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel21.setText("Building/Street :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel21, gridBagConstraints);

        comboBoxEmploymentStatus.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        comboBoxEmploymentStatus.setPreferredSize(new java.awt.Dimension(180, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(comboBoxEmploymentStatus, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel22.setText("City/Municipality :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel22, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel23.setText("Province :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel23, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel24.setText("ZIP Code :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel24, gridBagConstraints);

        fieldPhilhealthNumber.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPhilhealthNumber.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldPhilhealthNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldPhilhealthNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldPhilhealthNumber, gridBagConstraints);

        fieldJobTitle.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldJobTitle.setPreferredSize(new java.awt.Dimension(180, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldJobTitle, gridBagConstraints);

        fieldHourlyRate.setEditable(false);
        fieldHourlyRate.setBackground(new java.awt.Color(255, 255, 255));
        fieldHourlyRate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldHourlyRate.setPreferredSize(new java.awt.Dimension(180, 22));
        fieldHourlyRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldHourlyRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelFields.add(fieldHourlyRate, gridBagConstraints);

        fieldTin.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldTin.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldTin, gridBagConstraints);

        fieldPagibigNumber.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPagibigNumber.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldPagibigNumber, gridBagConstraints);

        fieldBasicSalary.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldBasicSalary.setPreferredSize(new java.awt.Dimension(180, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelFields.add(fieldBasicSalary, gridBagConstraints);

        comboBoxDepartment.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        comboBoxDepartment.setPreferredSize(new java.awt.Dimension(180, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(comboBoxDepartment, gridBagConstraints);

        fieldRiceSubsidy.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldRiceSubsidy.setPreferredSize(new java.awt.Dimension(180, 22));
        fieldRiceSubsidy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldRiceSubsidyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldRiceSubsidy, gridBagConstraints);

        fieldPhoneAllowance.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPhoneAllowance.setPreferredSize(new java.awt.Dimension(180, 22));
        fieldPhoneAllowance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldPhoneAllowanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldPhoneAllowance, gridBagConstraints);

        fieldClothingAllowance.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldClothingAllowance.setPreferredSize(new java.awt.Dimension(180, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldClothingAllowance, gridBagConstraints);

        fieldSssNumber.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldSssNumber.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 0, 0);
        panelFields.add(fieldSssNumber, gridBagConstraints);

        jLabel25.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel25.setText("Hourly Rate :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel25, gridBagConstraints);

        fieldGrossSemiMonthlyRate.setEditable(false);
        fieldGrossSemiMonthlyRate.setBackground(new java.awt.Color(255, 255, 255));
        fieldGrossSemiMonthlyRate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldGrossSemiMonthlyRate.setPreferredSize(new java.awt.Dimension(180, 22));
        fieldGrossSemiMonthlyRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldGrossSemiMonthlyRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldGrossSemiMonthlyRate, gridBagConstraints);

        jLabel27.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel27.setText("Gross Semi-Monthly Rate :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        panelFields.add(jLabel27, gridBagConstraints);

        fieldSupervisorId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldSupervisorId.setPreferredSize(new java.awt.Dimension(180, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldSupervisorId, gridBagConstraints);

        fieldEmployeeId.setEditable(false);
        fieldEmployeeId.setBackground(new java.awt.Color(255, 255, 255));
        fieldEmployeeId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldEmployeeId.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldEmployeeIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 11);
        panelFields.add(fieldEmployeeId, gridBagConstraints);

        comboBoxGender.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        comboBoxGender.setPreferredSize(new java.awt.Dimension(180, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 11);
        panelFields.add(comboBoxGender, gridBagConstraints);

        fieldEmployeeId1.setEditable(false);
        fieldEmployeeId1.setBackground(new java.awt.Color(255, 255, 255));
        fieldEmployeeId1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldEmployeeId1.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldEmployeeId1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldEmployeeId1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 11);
        panelFields.add(fieldEmployeeId1, gridBagConstraints);

        fieldEmployeeId2.setEditable(false);
        fieldEmployeeId2.setBackground(new java.awt.Color(255, 255, 255));
        fieldEmployeeId2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldEmployeeId2.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldEmployeeId2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldEmployeeId2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 11);
        panelFields.add(fieldEmployeeId2, gridBagConstraints);

        panelBodyRight.add(panelFields, java.awt.BorderLayout.CENTER);

        panelBody.add(panelBodyRight, java.awt.BorderLayout.LINE_END);

        jPanel1.setLayout(new java.awt.BorderLayout());

        panelSearch.setBackground(new java.awt.Color(255, 255, 255));
        panelSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 1, 12))); // NOI18N

        fieldSearchEmployeeId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldSearchEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldSearchEmployeeIdActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel26.setText("Employee ID :");

        btnSearch.setBackground(new java.awt.Color(64, 103, 218));
        btnSearch.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSearchLayout = new javax.swing.GroupLayout(panelSearch);
        panelSearch.setLayout(panelSearchLayout);
        panelSearchLayout.setHorizontalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSearchEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fieldSearchEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26))
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        btnDelete.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Red"));
        btnDelete.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");

        btnAdd.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Green"));
        btnAdd.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLeftTopLayout = new javax.swing.GroupLayout(panelLeftTop);
        panelLeftTop.setLayout(panelLeftTopLayout);
        panelLeftTopLayout.setHorizontalGroup(
            panelLeftTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeftTopLayout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(panelLeftTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelLeftTopLayout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        panelLeftTopLayout.setVerticalGroup(
            panelLeftTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLeftTopLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelLeftTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel1.add(panelLeftTop, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblEmployee);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        panelBody.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldLastNameActionPerformed

    private void fieldFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldFirstNameActionPerformed

    private void fieldZipcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldZipcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldZipcodeActionPerformed

    private void fieldCityMunicipalityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldCityMunicipalityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldCityMunicipalityActionPerformed

    private void fieldProvinceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldProvinceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldProvinceActionPerformed

    private void fieldPhilhealthNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldPhilhealthNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldPhilhealthNumberActionPerformed

    private void fieldRiceSubsidyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldRiceSubsidyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldRiceSubsidyActionPerformed

    private void fieldPhoneAllowanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldPhoneAllowanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldPhoneAllowanceActionPerformed

    private void fieldSearchEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldSearchEmployeeIdActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void fieldEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldEmployeeIdActionPerformed

    private void fieldEmployeeId1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEmployeeId1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldEmployeeId1ActionPerformed

    private void fieldEmployeeId2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEmployeeId2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldEmployeeId2ActionPerformed

    private void fieldGrossSemiMonthlyRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldGrossSemiMonthlyRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldGrossSemiMonthlyRateActionPerformed

    private void fieldHourlyRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldHourlyRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldHourlyRateActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        AdminDashboardView view = new AdminDashboardView(userHr);
        view.setVisible(true);

        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
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
    }//GEN-LAST:event_btnLogoutActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        ApplicationThemeUtility.setIntelliJ();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new EmployeeMgmtView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<Model.Department> comboBoxDepartment;
    private javax.swing.JComboBox<Model.EmploymentStatus> comboBoxEmploymentStatus;
    private javax.swing.JComboBox<Model.Gender> comboBoxGender;
    private javax.swing.JTextField fieldBarangay;
    private javax.swing.JTextField fieldBasicSalary;
    private javax.swing.JTextField fieldBirthday;
    private javax.swing.JTextField fieldBuildingStreet;
    private javax.swing.JTextField fieldCityMunicipality;
    private javax.swing.JTextField fieldClothingAllowance;
    private javax.swing.JTextField fieldEmployeeId;
    private javax.swing.JTextField fieldEmployeeId1;
    private javax.swing.JTextField fieldEmployeeId2;
    private javax.swing.JTextField fieldFirstName;
    private javax.swing.JTextField fieldGrossSemiMonthlyRate;
    private javax.swing.JTextField fieldHourlyRate;
    private javax.swing.JTextField fieldJobTitle;
    private javax.swing.JTextField fieldLastName;
    private javax.swing.JTextField fieldPagibigNumber;
    private javax.swing.JTextField fieldPhilhealthNumber;
    private javax.swing.JTextField fieldPhoneAllowance;
    private javax.swing.JTextField fieldPhoneNumber;
    private javax.swing.JTextField fieldProvince;
    private javax.swing.JTextField fieldRiceSubsidy;
    private javax.swing.JTextField fieldSearchEmployeeId;
    private javax.swing.JTextField fieldSssNumber;
    private javax.swing.JTextField fieldSupervisorId;
    private javax.swing.JTextField fieldTin;
    private javax.swing.JTextField fieldZipcode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelBodyRight;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelFieldsButton;
    private javax.swing.JPanel panelLeftTop;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JTable tblEmployee;
    // End of variables declaration//GEN-END:variables

    public HrService getUserHr() {
        return userHr;
    }

    public void setUserHr(HrService userHr) {
        this.userHr = userHr;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(JButton btnAdd) {
        this.btnAdd = btnAdd;
    }

    public JButton getBtnBack() {
        return btnBack;
    }

    public void setBtnBack(JButton btnBack) {
        this.btnBack = btnBack;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(JButton btnDelete) {
        this.btnDelete = btnDelete;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public void setBtnLogout(JButton btnLogout) {
        this.btnLogout = btnLogout;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public void setBtnSave(JButton btnSave) {
        this.btnSave = btnSave;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public void setBtnSearch(JButton btnSearch) {
        this.btnSearch = btnSearch;
    }

    public JComboBox<Department> getComboBoxDepartment() {
        return comboBoxDepartment;
    }

    public void setComboBoxDepartment(JComboBox<Department> comboBoxDepartment) {
        this.comboBoxDepartment = comboBoxDepartment;
    }

    public JComboBox<EmploymentStatus> getComboBoxEmploymentStatus() {
        return comboBoxEmploymentStatus;
    }

    public void setComboBoxEmploymentStatus(JComboBox<EmploymentStatus> comboBoxEmploymentStatus) {
        this.comboBoxEmploymentStatus = comboBoxEmploymentStatus;
    }

    public JTextField getFieldBarangay() {
        return fieldBarangay;
    }

    public void setFieldBarangay(JTextField fieldBarangay) {
        this.fieldBarangay = fieldBarangay;
    }

    public JTextField getFieldBasicSalary() {
        return fieldBasicSalary;
    }

    public void setFieldBasicSalary(JTextField fieldBasicSalary) {
        this.fieldBasicSalary = fieldBasicSalary;
    }

    public JTextField getFieldBirthday() {
        return fieldBirthday;
    }

    public void setFieldBirthday(JTextField fieldBirthday) {
        this.fieldBirthday = fieldBirthday;
    }

    public JTextField getFieldBuildingStreet() {
        return fieldBuildingStreet;
    }

    public void setFieldBuildingStreet(JTextField fieldBuildingStreet) {
        this.fieldBuildingStreet = fieldBuildingStreet;
    }

    public JTextField getFieldCityMunicipality() {
        return fieldCityMunicipality;
    }

    public void setFieldCityMunicipality(JTextField fieldCityMunicipality) {
        this.fieldCityMunicipality = fieldCityMunicipality;
    }

    public JTextField getFieldClothingAllowance() {
        return fieldClothingAllowance;
    }

    public void setFieldClothingAllowance(JTextField fieldClothingAllowance) {
        this.fieldClothingAllowance = fieldClothingAllowance;
    }

    public JTextField getFieldEmployeeId() {
        return fieldEmployeeId;
    }

    public void setFieldEmployeeId(JTextField fieldEmployeeId) {
        this.fieldEmployeeId = fieldEmployeeId;
    }

    public JTextField getFieldFirstName() {
        return fieldFirstName;
    }

    public void setFieldFirstName(JTextField fieldFirstName) {
        this.fieldFirstName = fieldFirstName;
    }

    public List<Employee> getEmployeeRecords() {
        return employeeRecords;
    }

    public void setEmployeeRecords(List<Employee> employeeRecords) {
        this.employeeRecords = employeeRecords;
    }

    public JComboBox<Gender> getComboBoxGender() {
        return comboBoxGender;
    }

    public void setComboBoxGender(JComboBox<Gender> comboBoxGender) {
        this.comboBoxGender = comboBoxGender;
    }

    public JTextField getFieldGrossSemiMonthlyRate() {
        return fieldGrossSemiMonthlyRate;
    }

    public void setFieldGrossSemiMonthlyRate(JTextField fieldGrossSemiMonthlyRate) {
        this.fieldGrossSemiMonthlyRate = fieldGrossSemiMonthlyRate;
    }

    public JTextField getFieldHourlyRate() {
        return fieldHourlyRate;
    }

    public void setFieldHourlyRate(JTextField fieldHourlyRate) {
        this.fieldHourlyRate = fieldHourlyRate;
    }

    public JTextField getFieldJobTitle() {
        return fieldJobTitle;
    }

    public void setFieldJobTitle(JTextField fieldJobTitle) {
        this.fieldJobTitle = fieldJobTitle;
    }

    public JTextField getFieldLastName() {
        return fieldLastName;
    }

    public void setFieldLastName(JTextField fieldLastName) {
        this.fieldLastName = fieldLastName;
    }

    public JTextField getFieldPagibigNumber() {
        return fieldPagibigNumber;
    }

    public void setFieldPagibigNumber(JTextField fieldPagibigNumber) {
        this.fieldPagibigNumber = fieldPagibigNumber;
    }

    public JTextField getFieldPhilhealthNumber() {
        return fieldPhilhealthNumber;
    }

    public void setFieldPhilhealthNumber(JTextField fieldPhilhealthNumber) {
        this.fieldPhilhealthNumber = fieldPhilhealthNumber;
    }

    public JTextField getFieldPhoneAllowance() {
        return fieldPhoneAllowance;
    }

    public void setFieldPhoneAllowance(JTextField fieldPhoneAllowance) {
        this.fieldPhoneAllowance = fieldPhoneAllowance;
    }

    public JTextField getFieldPhoneNumber() {
        return fieldPhoneNumber;
    }

    public void setFieldPhoneNumber(JTextField fieldPhoneNumber) {
        this.fieldPhoneNumber = fieldPhoneNumber;
    }

    public JTextField getFieldProvince() {
        return fieldProvince;
    }

    public void setFieldProvince(JTextField fieldProvince) {
        this.fieldProvince = fieldProvince;
    }

    public JTextField getFieldRiceSubsidy() {
        return fieldRiceSubsidy;
    }

    public void setFieldRiceSubsidy(JTextField fieldRiceSubsidy) {
        this.fieldRiceSubsidy = fieldRiceSubsidy;
    }

    public JTextField getFieldSearchEmployeeId() {
        return fieldSearchEmployeeId;
    }

    public void setFieldSearchEmployeeId(JTextField fieldSearchEmployeeId) {
        this.fieldSearchEmployeeId = fieldSearchEmployeeId;
    }

    public JTextField getFieldSssNumber() {
        return fieldSssNumber;
    }

    public void setFieldSssNumber(JTextField fieldSssNumber) {
        this.fieldSssNumber = fieldSssNumber;
    }

    public JTextField getFieldSupervisorId() {
        return fieldSupervisorId;
    }

    public void setFieldSupervisorId(JTextField fieldSupervisorId) {
        this.fieldSupervisorId = fieldSupervisorId;
    }

    public JTextField getFieldTin() {
        return fieldTin;
    }

    public void setFieldTin(JTextField fieldTin) {
        this.fieldTin = fieldTin;
    }

    public JTextField getFieldZipcode() {
        return fieldZipcode;
    }

    public void setFieldZipcode(JTextField fieldZipcode) {
        this.fieldZipcode = fieldZipcode;
    }

    public JTable getTblEmployee() {
        return tblEmployee;
    }

    public void setTblEmployee(JTable tblEmployee) {
        this.tblEmployee = tblEmployee;
    }

}
