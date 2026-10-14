/*



 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license


 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template


 */
package View;

import Config.ApplicationThemeUtility;
import Model.ClockHour;
import Model.Employee;
import Model.Payslip;
import Model.ReportType;
import Service.EmployeeService;
import Service.FinanceService;
import Service.HrService;
import Util.DatabaseConnectionUtil;
import Util.ReportGeneratorUtil;
import Util.TableModelUtil;
import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 *
 *
 *
 *
 * @author Gracielle Salvador
 *
 *
 */
public class PayslipMgmtView extends javax.swing.JFrame {

    private List<Payslip> payslipRecords;
    private FinanceService userFinance;


    public PayslipMgmtView(EmployeeService user) {
        try {
            this.userFinance = new FinanceService(user);
            initComponents();
            initCustomComponents();
            populatePayslipTable(null);

        } catch (SQLException ex) {
            Logger.getLogger(PayslipMgmtView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public final void populatePayslipTable(Integer searchFieldEmployeeId) throws SQLException {
        try {
            // Check if parameters are provided for filtering
            if (searchFieldEmployeeId != null) {
                payslipRecords = userFinance.getAllPayslipsByEmployee(searchFieldEmployeeId); // Fetch filtered records
            } else {
                payslipRecords = userFinance.getAllPayslips(); // Fetch all records
            }

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

    private void initCustomComponents() {
       tblPayslip.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Add a ListSelectionListener to the table
        tblPayslip.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) { // Only respond when the selection is final
                int selectedRow = tblPayslip.getSelectedRow();
                if (selectedRow != -1) { // Check if a row is selected
                    // Get the selected record from the original list
                    Payslip selectedPayslipRecord = payslipRecords.get(selectedRow);

                    // Update text fields with the selected payslip record's data
                    fieldPayslipId.setText(selectedPayslipRecord.getPayslipId() != null ? selectedPayslipRecord.getPayslipId().toString() : "");
                    fieldEmployeeId.setText(selectedPayslipRecord.getEmployeeId() != null ? selectedPayslipRecord.getEmployeeId().toString() : "");
                    fieldEmployeeName.setText(selectedPayslipRecord.getEmployeeName() != null ? selectedPayslipRecord.getEmployeeName() : "");
                    fieldPeriodStartDate.setText(selectedPayslipRecord.getPeriodStartDate() != null ? selectedPayslipRecord.getPeriodStartDate().toString() : "");
                    fieldPeriodEndDate.setText(selectedPayslipRecord.getPeriodEndDate() != null ? selectedPayslipRecord.getPeriodEndDate().toString() : "");
                    fieldEmployeePosition.setText(selectedPayslipRecord.getEmployeePosition() != null ? selectedPayslipRecord.getEmployeePosition() : "");
                    fieldEmployeeDepartment.setText(selectedPayslipRecord.getEmployeeDepartment() != null ? selectedPayslipRecord.getEmployeeDepartment() : "");
                    fieldMonthlyRate.setText(selectedPayslipRecord.getMonthlyRate() != null ? selectedPayslipRecord.getMonthlyRate().toString() : "");
                    fieldDailyRate.setText(selectedPayslipRecord.getDailyRate() != null ? selectedPayslipRecord.getDailyRate().toString() : "");
                    fieldHourlyRate.setText(selectedPayslipRecord.getHourlyRate() != null ? selectedPayslipRecord.getHourlyRate().toString() : "");
                    fieldOvertimeRateMultiplier.setText(selectedPayslipRecord.getOvertimeRateMultiplier() != null ? selectedPayslipRecord.getOvertimeRateMultiplier().toString() : "");
                    fieldDaysWorked.setText(selectedPayslipRecord.getDistinctDaysWorked() != null ? selectedPayslipRecord.getDistinctDaysWorked().toString() : "");
                    fieldHoursWorked.setText(selectedPayslipRecord.getHoursWorked() != null ? selectedPayslipRecord.getHoursWorked().toString() : "");
                    fieldOvertimeHours.setText(selectedPayslipRecord.getOvertimeHours() != null ? selectedPayslipRecord.getOvertimeHours().toString() : "");
                    fieldGrossIncome.setText(selectedPayslipRecord.getGrossIncome() != null ? selectedPayslipRecord.getGrossIncome().toString() : "");
                    fieldRiceSubsidy.setText(selectedPayslipRecord.getRiceSubsidy() != null ? selectedPayslipRecord.getRiceSubsidy().toString() : "");
                    fieldPhoneAllowance.setText(selectedPayslipRecord.getPhoneAllowance() != null ? selectedPayslipRecord.getPhoneAllowance().toString() : "");
                    fieldClothingAllowance.setText(selectedPayslipRecord.getClothingAllowance() != null ? selectedPayslipRecord.getClothingAllowance().toString() : "");
                    fieldTotalBenefits.setText(selectedPayslipRecord.getTotalBenefits() != null ? selectedPayslipRecord.getTotalBenefits().toString() : "");
                    fieldSssContribution.setText(selectedPayslipRecord.getSssContribution() != null ? selectedPayslipRecord.getSssContribution().toString() : "");
                    fieldPhilhealthContribution.setText(selectedPayslipRecord.getPhilhealthContribution() != null ? selectedPayslipRecord.getPhilhealthContribution().toString() : "");
                    fieldPagibigContribution.setText(selectedPayslipRecord.getPagibigContribution() != null ? selectedPayslipRecord.getPagibigContribution().toString() : "");
                    fieldTaxableIncome.setText(selectedPayslipRecord.getTaxableIncome() != null ? selectedPayslipRecord.getTaxableIncome().toString() : "");
                    fieldWithholdingTax.setText(selectedPayslipRecord.getWithholdingTax() != null ? selectedPayslipRecord.getWithholdingTax().toString() : "");
                    fieldTotalDeductions.setText(selectedPayslipRecord.getTotalDeductions() != null ? selectedPayslipRecord.getTotalDeductions().toString() : "");
                    fieldNetPay.setText(selectedPayslipRecord.getNetIncome() != null ? selectedPayslipRecord.getNetIncome().toString() : "");
                }
            }
        });
    }

    /**
     *
     *
     * This method is called from within the constructor to initialize the form.
     *
     *
     * WARNING: Do NOT modify this code. The content of this method is always
     *
     *
     * regenerated by the Form Editor.
     *
     *
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnPrint = new javax.swing.JButton();
        panelFields = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fieldPayslipId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        fieldEmployeeName = new javax.swing.JTextField();
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
        fieldDailyRate = new javax.swing.JTextField();
        fieldPeriodStartDate = new javax.swing.JTextField();
        fieldEmployeePosition = new javax.swing.JTextField();
        fieldEmployeeDepartment = new javax.swing.JTextField();
        fieldMonthlyRate = new javax.swing.JTextField();
        fieldHourlyRate = new javax.swing.JTextField();
        fieldOvertimeRateMultiplier = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        fieldHoursWorked = new javax.swing.JTextField();
        fieldTotalBenefits = new javax.swing.JTextField();
        fieldPhilhealthContribution = new javax.swing.JTextField();
        fieldOvertimeHours = new javax.swing.JTextField();
        fieldGrossIncome = new javax.swing.JTextField();
        fieldTaxableIncome = new javax.swing.JTextField();
        fieldWithholdingTax = new javax.swing.JTextField();
        fieldRiceSubsidy = new javax.swing.JTextField();
        fieldDaysWorked = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        fieldClothingAllowance = new javax.swing.JTextField();
        fieldEmployeeId = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        fieldTotalDeductions = new javax.swing.JTextField();
        fieldSssContribution = new javax.swing.JTextField();
        fieldPeriodEndDate = new javax.swing.JTextField();
        fieldPhoneAllowance = new javax.swing.JTextField();
        fieldPagibigContribution = new javax.swing.JTextField();
        fieldNetPay = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelSearch = new javax.swing.JPanel();
        fieldSearchEmployeeId = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnAll = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPayslip = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTitle.setBackground(new java.awt.Color(0, 51, 153));

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Verdana", 0, 29)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Payroll Management");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 529, Short.MAX_VALUE)
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

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 517));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnPrint.setBackground(new java.awt.Color(64, 103, 218));
        btnPrint.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setText("Print Payslip");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(330, Short.MAX_VALUE)
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(218, 218, 218))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5, java.awt.BorderLayout.PAGE_END);

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
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

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel3.setText("Employee Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel3, gridBagConstraints);

        fieldEmployeeName.setEditable(false);
        fieldEmployeeName.setBackground(new java.awt.Color(255, 255, 255));
        fieldEmployeeName.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldEmployeeName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        fieldEmployeeName.setPreferredSize(new java.awt.Dimension(150, 22));
        fieldEmployeeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldEmployeeNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldEmployeeName, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel4.setText("Period Start Date  :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel5.setText("Period End Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel6.setText("Employee Position :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel7.setText("Distinct Days Worked :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel8.setText("Total Benefits :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
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
        panelFields.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel10.setText("Gross Income :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
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

        jLabel12.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel12.setText("Rice Subsidy :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel12, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel13.setText("Phone Allowance :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel13, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel14.setText("Clothing Allowance :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel14, gridBagConstraints);

        fieldDailyRate.setEditable(false);
        fieldDailyRate.setBackground(new java.awt.Color(255, 255, 255));
        fieldDailyRate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldDailyRate.setPreferredSize(new java.awt.Dimension(150, 22));
        fieldDailyRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDailyRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldDailyRate, gridBagConstraints);

        fieldPeriodStartDate.setEditable(false);
        fieldPeriodStartDate.setBackground(new java.awt.Color(255, 255, 255));
        fieldPeriodStartDate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPeriodStartDate.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldPeriodStartDate, gridBagConstraints);

        fieldEmployeePosition.setEditable(false);
        fieldEmployeePosition.setBackground(new java.awt.Color(255, 255, 255));
        fieldEmployeePosition.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldEmployeePosition.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        fieldEmployeePosition.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldEmployeePosition, gridBagConstraints);

        fieldEmployeeDepartment.setEditable(false);
        fieldEmployeeDepartment.setBackground(new java.awt.Color(255, 255, 255));
        fieldEmployeeDepartment.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldEmployeeDepartment.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        fieldEmployeeDepartment.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldEmployeeDepartment, gridBagConstraints);

        fieldMonthlyRate.setEditable(false);
        fieldMonthlyRate.setBackground(new java.awt.Color(255, 255, 255));
        fieldMonthlyRate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldMonthlyRate.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldMonthlyRate, gridBagConstraints);

        fieldHourlyRate.setEditable(false);
        fieldHourlyRate.setBackground(new java.awt.Color(255, 255, 255));
        fieldHourlyRate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldHourlyRate.setPreferredSize(new java.awt.Dimension(150, 22));
        fieldHourlyRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldHourlyRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldHourlyRate, gridBagConstraints);

        fieldOvertimeRateMultiplier.setEditable(false);
        fieldOvertimeRateMultiplier.setBackground(new java.awt.Color(255, 255, 255));
        fieldOvertimeRateMultiplier.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldOvertimeRateMultiplier.setPreferredSize(new java.awt.Dimension(150, 22));
        fieldOvertimeRateMultiplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldOvertimeRateMultiplierActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldOvertimeRateMultiplier, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel15.setText("Monthly Rate :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
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
        jLabel17.setText("SSS Contribution :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel17, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel18.setText("Taxable Income :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel18, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel19.setText("Withholding Tax :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel19, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel20.setText("Net Pay :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panelFields.add(jLabel20, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel21.setText("Employee Department :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel21, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel22.setText("Daily Rate :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel22, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel23.setText("Hourly Rate :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel23, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel24.setText("Overtime Rate Multiplier :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panelFields.add(jLabel24, gridBagConstraints);

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
        panelFields.add(fieldHoursWorked, gridBagConstraints);

        fieldTotalBenefits.setEditable(false);
        fieldTotalBenefits.setBackground(new java.awt.Color(255, 255, 255));
        fieldTotalBenefits.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldTotalBenefits.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldTotalBenefits, gridBagConstraints);

        fieldPhilhealthContribution.setEditable(false);
        fieldPhilhealthContribution.setBackground(new java.awt.Color(255, 255, 255));
        fieldPhilhealthContribution.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPhilhealthContribution.setPreferredSize(new java.awt.Dimension(120, 22));
        fieldPhilhealthContribution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldPhilhealthContributionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelFields.add(fieldPhilhealthContribution, gridBagConstraints);

        fieldOvertimeHours.setEditable(false);
        fieldOvertimeHours.setBackground(new java.awt.Color(255, 255, 255));
        fieldOvertimeHours.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldOvertimeHours.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldOvertimeHours, gridBagConstraints);

        fieldGrossIncome.setEditable(false);
        fieldGrossIncome.setBackground(new java.awt.Color(255, 255, 255));
        fieldGrossIncome.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldGrossIncome.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldGrossIncome, gridBagConstraints);

        fieldTaxableIncome.setEditable(false);
        fieldTaxableIncome.setBackground(new java.awt.Color(255, 255, 255));
        fieldTaxableIncome.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldTaxableIncome.setPreferredSize(new java.awt.Dimension(120, 22));
        fieldTaxableIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldTaxableIncomeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldTaxableIncome, gridBagConstraints);

        fieldWithholdingTax.setEditable(false);
        fieldWithholdingTax.setBackground(new java.awt.Color(255, 255, 255));
        fieldWithholdingTax.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldWithholdingTax.setPreferredSize(new java.awt.Dimension(120, 22));
        fieldWithholdingTax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldWithholdingTaxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldWithholdingTax, gridBagConstraints);

        fieldRiceSubsidy.setEditable(false);
        fieldRiceSubsidy.setBackground(new java.awt.Color(255, 255, 255));
        fieldRiceSubsidy.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldRiceSubsidy.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldRiceSubsidy, gridBagConstraints);

        fieldDaysWorked.setEditable(false);
        fieldDaysWorked.setBackground(new java.awt.Color(255, 255, 255));
        fieldDaysWorked.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldDaysWorked.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldDaysWorked, gridBagConstraints);

        jLabel25.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel25.setText("PhilHealth Contribution :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 19, 0, 0);
        panelFields.add(jLabel25, gridBagConstraints);

        jLabel30.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel30.setText("Pag-IBIG Contribution :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel30, gridBagConstraints);

        fieldClothingAllowance.setEditable(false);
        fieldClothingAllowance.setBackground(new java.awt.Color(255, 255, 255));
        fieldClothingAllowance.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldClothingAllowance.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldClothingAllowance, gridBagConstraints);

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

        jLabel31.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel31.setText("Total Deductions :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panelFields.add(jLabel31, gridBagConstraints);

        fieldTotalDeductions.setEditable(false);
        fieldTotalDeductions.setBackground(new java.awt.Color(255, 255, 255));
        fieldTotalDeductions.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldTotalDeductions.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldTotalDeductions, gridBagConstraints);

        fieldSssContribution.setEditable(false);
        fieldSssContribution.setBackground(new java.awt.Color(255, 255, 255));
        fieldSssContribution.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldSssContribution.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelFields.add(fieldSssContribution, gridBagConstraints);

        fieldPeriodEndDate.setEditable(false);
        fieldPeriodEndDate.setBackground(new java.awt.Color(255, 255, 255));
        fieldPeriodEndDate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPeriodEndDate.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldPeriodEndDate, gridBagConstraints);

        fieldPhoneAllowance.setEditable(false);
        fieldPhoneAllowance.setBackground(new java.awt.Color(255, 255, 255));
        fieldPhoneAllowance.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPhoneAllowance.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldPhoneAllowance, gridBagConstraints);

        fieldPagibigContribution.setEditable(false);
        fieldPagibigContribution.setBackground(new java.awt.Color(255, 255, 255));
        fieldPagibigContribution.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldPagibigContribution.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldPagibigContribution, gridBagConstraints);

        fieldNetPay.setEditable(false);
        fieldNetPay.setBackground(new java.awt.Color(255, 255, 255));
        fieldNetPay.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldNetPay.setPreferredSize(new java.awt.Dimension(120, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panelFields.add(fieldNetPay, gridBagConstraints);

        jPanel2.add(panelFields, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel4.setLayout(new java.awt.BorderLayout());

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
                .addGap(22, 22, 22)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSearchEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldSearchEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(0, 13, Short.MAX_VALUE))
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnAll.setBackground(new java.awt.Color(64, 103, 218));
        btnAll.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnAll.setForeground(new java.awt.Color(255, 255, 255));
        btnAll.setText("Show All");
        btnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllActionPerformed(evt);
            }
        });

        btnDelete.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Red"));
        btnDelete.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");

        btnAdd.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Green"));
        btnAdd.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(308, 308, 308)
                        .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel3, java.awt.BorderLayout.PAGE_START);

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        panelBody.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        AdminDashboardView view = new AdminDashboardView(userFinance);
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

    private void fieldPayslipIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldPayslipIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldPayslipIdActionPerformed

    private void fieldEmployeeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEmployeeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldEmployeeNameActionPerformed

    private void fieldDailyRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDailyRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDailyRateActionPerformed

    private void fieldHourlyRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldHourlyRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldHourlyRateActionPerformed

    private void fieldOvertimeRateMultiplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldOvertimeRateMultiplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldOvertimeRateMultiplierActionPerformed

    private void fieldHoursWorkedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldHoursWorkedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldHoursWorkedActionPerformed

    private void fieldPhilhealthContributionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldPhilhealthContributionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldPhilhealthContributionActionPerformed

    private void fieldTaxableIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldTaxableIncomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldTaxableIncomeActionPerformed

    private void fieldWithholdingTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldWithholdingTaxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldWithholdingTaxActionPerformed

    private void fieldEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldEmployeeIdActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed

    }//GEN-LAST:event_btnPrintActionPerformed

    private void fieldSearchEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchEmployeeIdActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_fieldSearchEmployeeIdActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
        try {
            populatePayslipTable(null);
            // TODO add your handling code here:
        } catch (SQLException ex) {
            showMessage("Error" + ex.getMessage());
        }
    }//GEN-LAST:event_btnAllActionPerformed

    /**
     *
     *
     * @param args the command line arguments
     *
     *
     */
    public static void main(String args[]) {

        ApplicationThemeUtility.setIntelliJ();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

//                new AttendanceMgmtView().setVisible(true);
            }

        });

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSearch;
    private javax.swing.JTextField fieldClothingAllowance;
    private javax.swing.JTextField fieldDailyRate;
    private javax.swing.JTextField fieldDaysWorked;
    private javax.swing.JTextField fieldEmployeeDepartment;
    private javax.swing.JTextField fieldEmployeeId;
    private javax.swing.JTextField fieldEmployeeName;
    private javax.swing.JTextField fieldEmployeePosition;
    private javax.swing.JTextField fieldGrossIncome;
    private javax.swing.JTextField fieldHourlyRate;
    private javax.swing.JTextField fieldHoursWorked;
    private javax.swing.JTextField fieldMonthlyRate;
    private javax.swing.JTextField fieldNetPay;
    private javax.swing.JTextField fieldOvertimeHours;
    private javax.swing.JTextField fieldOvertimeRateMultiplier;
    private javax.swing.JTextField fieldPagibigContribution;
    private javax.swing.JTextField fieldPayslipId;
    private javax.swing.JTextField fieldPeriodEndDate;
    private javax.swing.JTextField fieldPeriodStartDate;
    private javax.swing.JTextField fieldPhilhealthContribution;
    private javax.swing.JTextField fieldPhoneAllowance;
    private javax.swing.JTextField fieldRiceSubsidy;
    private javax.swing.JTextField fieldSearchEmployeeId;
    private javax.swing.JTextField fieldSssContribution;
    private javax.swing.JTextField fieldTaxableIncome;
    private javax.swing.JTextField fieldTotalBenefits;
    private javax.swing.JTextField fieldTotalDeductions;
    private javax.swing.JTextField fieldWithholdingTax;
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
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JTable tblPayslip;
    // End of variables declaration//GEN-END:variables

    public List<Payslip> getPayslipRecords() {
        return payslipRecords;
    }

    public void setPayslipRecords(List<Payslip> payslipRecords) {
        this.payslipRecords = payslipRecords;
    }

    public FinanceService getUserFinance() {
        return userFinance;
    }

    public void setUserFinance(FinanceService userFinance) {
        this.userFinance = userFinance;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(JButton btnAdd) {
        this.btnAdd = btnAdd;
    }

    public JButton getBtnAll() {
        return btnAll;
    }

    public void setBtnAll(JButton btnAll) {
        this.btnAll = btnAll;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(JButton btnDelete) {
        this.btnDelete = btnDelete;
    }

    public JButton getBtnPrint() {
        return btnPrint;
    }

    public void setBtnPrint(JButton btnPrint) {
        this.btnPrint = btnPrint;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public void setBtnSearch(JButton btnSearch) {
        this.btnSearch = btnSearch;
    }

    public JTextField getFieldClothingAllowance() {
        return fieldClothingAllowance;
    }

    public void setFieldClothingAllowance(JTextField fieldClothingAllowance) {
        this.fieldClothingAllowance = fieldClothingAllowance;
    }

    public JTextField getFieldDailyRate() {
        return fieldDailyRate;
    }

    public void setFieldDailyRate(JTextField fieldDailyRate) {
        this.fieldDailyRate = fieldDailyRate;
    }

    public JTextField getFieldDaysWorked() {
        return fieldDaysWorked;
    }

    public void setFieldDaysWorked(JTextField fieldDaysWorked) {
        this.fieldDaysWorked = fieldDaysWorked;
    }

    public JTextField getFieldEmployeeDepartment() {
        return fieldEmployeeDepartment;
    }

    public void setFieldEmployeeDepartment(JTextField fieldEmployeeDepartment) {
        this.fieldEmployeeDepartment = fieldEmployeeDepartment;
    }

    public JTextField getFieldEmployeeId() {
        return fieldEmployeeId;
    }

    public void setFieldEmployeeId(JTextField fieldEmployeeId) {
        this.fieldEmployeeId = fieldEmployeeId;
    }

    public JTextField getFieldEmployeeName() {
        return fieldEmployeeName;
    }

    public void setFieldEmployeeName(JTextField fieldEmployeeName) {
        this.fieldEmployeeName = fieldEmployeeName;
    }

    public JTextField getFieldEmployeePosition() {
        return fieldEmployeePosition;
    }

    public void setFieldEmployeePosition(JTextField fieldEmployeePosition) {
        this.fieldEmployeePosition = fieldEmployeePosition;
    }

    public JTextField getFieldGrossIncome() {
        return fieldGrossIncome;
    }

    public void setFieldGrossIncome(JTextField fieldGrossIncome) {
        this.fieldGrossIncome = fieldGrossIncome;
    }

    public JTextField getFieldHourlyRate() {
        return fieldHourlyRate;
    }

    public void setFieldHourlyRate(JTextField fieldHourlyRate) {
        this.fieldHourlyRate = fieldHourlyRate;
    }

    public JTextField getFieldHoursWorked() {
        return fieldHoursWorked;
    }

    public void setFieldHoursWorked(JTextField fieldHoursWorked) {
        this.fieldHoursWorked = fieldHoursWorked;
    }

    public JTextField getFieldMonthlyRate() {
        return fieldMonthlyRate;
    }

    public void setFieldMonthlyRate(JTextField fieldMonthlyRate) {
        this.fieldMonthlyRate = fieldMonthlyRate;
    }

    public JTextField getFieldNetPay() {
        return fieldNetPay;
    }

    public void setFieldNetPay(JTextField fieldNetPay) {
        this.fieldNetPay = fieldNetPay;
    }

    public JTextField getFieldOvertimeHours() {
        return fieldOvertimeHours;
    }

    public void setFieldOvertimeHours(JTextField fieldOvertimeHours) {
        this.fieldOvertimeHours = fieldOvertimeHours;
    }

    public JTextField getFieldOvertimeRateMultiplier() {
        return fieldOvertimeRateMultiplier;
    }

    public void setFieldOvertimeRateMultiplier(JTextField fieldOvertimeRateMultiplier) {
        this.fieldOvertimeRateMultiplier = fieldOvertimeRateMultiplier;
    }

    public JTextField getFieldPagibigContribution() {
        return fieldPagibigContribution;
    }

    public void setFieldPagibigContribution(JTextField fieldPagibigContribution) {
        this.fieldPagibigContribution = fieldPagibigContribution;
    }

    public JTextField getFieldPayslipId() {
        return fieldPayslipId;
    }

    public void setFieldPayslipId(JTextField fieldPayslipId) {
        this.fieldPayslipId = fieldPayslipId;
    }

    public JTextField getFieldPeriodEndDate() {
        return fieldPeriodEndDate;
    }

    public void setFieldPeriodEndDate(JTextField fieldPeriodEndDate) {
        this.fieldPeriodEndDate = fieldPeriodEndDate;
    }

    public JTextField getFieldPeriodStartDate() {
        return fieldPeriodStartDate;
    }

    public void setFieldPeriodStartDate(JTextField fieldPeriodStartDate) {
        this.fieldPeriodStartDate = fieldPeriodStartDate;
    }

    public JTextField getFieldPhilhealthContribution() {
        return fieldPhilhealthContribution;
    }

    public void setFieldPhilhealthContribution(JTextField fieldPhilhealthContribution) {
        this.fieldPhilhealthContribution = fieldPhilhealthContribution;
    }

    public JTextField getFieldPhoneAllowance() {
        return fieldPhoneAllowance;
    }

    public void setFieldPhoneAllowance(JTextField fieldPhoneAllowance) {
        this.fieldPhoneAllowance = fieldPhoneAllowance;
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

    public JTextField getFieldSssContribution() {
        return fieldSssContribution;
    }

    public void setFieldSssContribution(JTextField fieldSssContribution) {
        this.fieldSssContribution = fieldSssContribution;
    }

    public JTextField getFieldTaxableIncome() {
        return fieldTaxableIncome;
    }

    public void setFieldTaxableIncome(JTextField fieldTaxableIncome) {
        this.fieldTaxableIncome = fieldTaxableIncome;
    }

    public JTextField getFieldTotalBenefits() {
        return fieldTotalBenefits;
    }

    public void setFieldTotalBenefits(JTextField fieldTotalBenefits) {
        this.fieldTotalBenefits = fieldTotalBenefits;
    }

    public JTextField getFieldTotalDeductions() {
        return fieldTotalDeductions;
    }

    public void setFieldTotalDeductions(JTextField fieldTotalDeductions) {
        this.fieldTotalDeductions = fieldTotalDeductions;
    }

    public JTextField getFieldWithholdingTax() {
        return fieldWithholdingTax;
    }

    public void setFieldWithholdingTax(JTextField fieldWithholdingTax) {
        this.fieldWithholdingTax = fieldWithholdingTax;
    }

    public JTable getTblPayslip() {
        return tblPayslip;
    }

    public void setTblPayslip(JTable tblPayslip) {
        this.tblPayslip = tblPayslip;
    }

}
