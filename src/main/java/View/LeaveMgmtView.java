/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Config.ApplicationThemeUtility;
import Model.LeaveEntitlement;
import Model.LeaveRecord;
import Model.LeaveStatus;
import Service.EmployeeService;
import Service.HrService;
import Util.TableModelUtil;
import java.awt.Color;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Gracielle Salvador
 */
public class LeaveMgmtView extends javax.swing.JFrame {

    private List<LeaveRecord> leaveRecords;
    List<LeaveEntitlement> leaveEntitlements;
    private HrService userHr;

    /**
     * Creates new form LeaveMgmtView
     */
    public LeaveMgmtView(EmployeeService user) {
        try {
            this.userHr = new HrService(user);
            initComponents();
            initCustomComponents();
            populateLeaveTable(null);
        } catch (SQLException ex) {
            showMessage("Error: " + ex.getMessage());
            Logger.getLogger(LeaveMgmtView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void populateLeaveTable(List<LeaveRecord> leaveRecordsParam) throws SQLException {
        // Leave Record
        if (leaveRecordsParam == null) {
            leaveRecords = userHr.getAllLeaveRecords(); //If null, display all leave records
        } else {
            leaveRecords = leaveRecordsParam; // Display filtered record
        }
        String[] columnNamesLeave = {
            "Leave Record ID", "Employee ID", "Leave Type ID", "Application Date",
            "Start Date", "End Date", "Days Used", "Reason", "Leave Status",
            "Status Updated By", "Status Updated At", "Remarks"

        };

        DefaultTableModel tableModelLeave = TableModelUtil.createReadOnlyTableModel(
                leaveRecords,
                columnNamesLeave,
                record -> new Object[]{
                    record.getLeaveRecordId(),
                    record.getEmployeeId(),
                    record.getLeaveTypeId(),
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

            100, // "Leave Type ID"

            100, // "Application Date"

            100, // "Start Date"

            100, // "End Date"

            100, // "Days Used"

            150, // "Reason"

            100, // "Leave Status"

            120, // "Status Updated By"

            120, // "Status Updated At"

            200, // "Remarks"
        };

        // Loop through the columns and set their preferred widths
        for (int i = 0; i < columnWidthsLeave.length; i++) {

            if (i < columnModelLeave.getColumnCount()) { // Safety check in case widths array is shorter

                TableColumn column = columnModelLeave.getColumn(i);

                column.setPreferredWidth(columnWidthsLeave[i]);

            }
        }

    }

    private void populateLeaveEntitlementTable(int empId, int year) throws SQLException {
        // LEAVE ENTITLEMENT
        leaveEntitlements = userHr.getLeaveEntitlementsByEmployeeIdAndYear(empId, year);
        String[] columnNamesEntitlement = {
            "Leave Entitlement ID", "Employee ID", "Leave Type ID", "Leave Type Name",
            "Entitlement Days", "Year", "Days Used", "Remaining Days"
        };

        DefaultTableModel tableModelEntitlement = TableModelUtil.createReadOnlyTableModel(
                leaveEntitlements,
                columnNamesEntitlement,
                record -> new Object[]{
                    record.getLeaveEntitlementId(),
                    record.getEmployeeId(),
                    record.getLeaveType().getLeaveTypeId(),
                    record.getLeaveType().getName(),
                    record.getEntitlementDays(),
                    record.getYear(),
                    record.getDaysUsed(),
                    record.getRemainingDays()
                }
        );

        tblLeaveEntitlement.setModel(tableModelEntitlement);

        // Apply the custom cell renderer to all columns
        tblLeaveEntitlement.setDefaultRenderer(Object.class, new ZebraStripingCellRenderer());

        // Customize the table header background
        tblLeaveEntitlement.getTableHeader().setBackground(ColorTheme.BLUE);

        tblLeaveEntitlement.getTableHeader().setForeground(Color.WHITE); // White text
        
        
        // Set Column Widths
        TableColumnModel columnModelLeaveEntitlement = tblLeaveEntitlement.getColumnModel();

        int[] columnWidthsLeaveEntitlement = {
            130, // "Leave Entitlement ID"

            100, // "Employee ID"

            100, // "Leave Type ID"

            130, // "Leave Type Name"

            130, // "Entitlement Days"

            100, // "Year"

            100, // "Days Used"

            130, // "Remaining Days"
        };

        // Loop through the columns and set their preferred widths
        for (int i = 0; i < columnWidthsLeaveEntitlement.length; i++) {

            if (i < columnModelLeaveEntitlement.getColumnCount()) { // Safety check in case widths array is shorter

                TableColumn column = columnModelLeaveEntitlement.getColumn(i);

                column.setPreferredWidth(columnWidthsLeaveEntitlement[i]);

            }
        }

    }

    private void initCustomComponents() {
        tblLeave.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblLeaveEntitlement.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Add a ListSelectionListener to the table
        tblLeave.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) { // Only respond when the selection is final
                int selectedRow = tblLeave.getSelectedRow();
                if (selectedRow != -1) { // Check if a row is selected
                    // Get the selected  record
                    LeaveRecord selectedRecord = leaveRecords.get(selectedRow);

                    // Update LEAVE RECORD text fields with the selected record's data
                    fieldRecordId.setText(String.valueOf(selectedRecord.getLeaveRecordId()));
                    fieldEmployeeId.setText(String.valueOf(selectedRecord.getEmployeeId()));
                    fieldLeaveTypeId.setText(selectedRecord.getLeaveTypeId().toString());
                    fieldApplicationDate.setText(selectedRecord.getApplicationDate().toString());
                    fieldAppliedStartDate.setText(selectedRecord.getAppliedStartDate().toString());
                    fieldAppliedEndDate.setText(selectedRecord.getAppliedEndDate().toString());
                    fieldDaysUsed.setText(selectedRecord.getDaysUsed().toString());
                    fieldReason.setText(selectedRecord.getReason());
                    fieldStatusUpdatedBy.setText(Optional.ofNullable(selectedRecord.getStatusUpdatedBy()).map(Object::toString).orElse(""));
                    fieldStatusUpdatedAt.setText(Optional.ofNullable(selectedRecord.getStatusUpdatedAt()).map(Object::toString).orElse(""));
                    fieldLeaveStatus.setText(selectedRecord.getStatus().toString());
                    txtAreaRemarks.setText(Optional.ofNullable(selectedRecord.getRemarks()).map(Object::toString).orElse(""));

                    try {
                        //Populate Leave Entitlement Table based on the selected record
                        populateLeaveEntitlementTable(selectedRecord.getEmployeeId(), selectedRecord.getAppliedStartDate().getYear());
                    } catch (SQLException ex) {
                        Logger.getLogger(LeaveMgmtView.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblLeaveEntitlement = new javax.swing.JTable();
        panelFields = new javax.swing.JPanel();
        fieldLeaveRecordId = new javax.swing.JLabel();
        fieldApplicationDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        fieldEmployeeId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        fieldLeaveTypeId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fieldRecordId = new javax.swing.JTextField();
        fieldLeaveStatus = new javax.swing.JTextField();
        fieldAppliedStartDate = new javax.swing.JTextField();
        fieldAppliedEndDate = new javax.swing.JTextField();
        fieldDaysUsed = new javax.swing.JTextField();
        fieldStatusUpdatedBy = new javax.swing.JTextField();
        fieldStatusUpdatedAt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        fieldReason = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaRemarks = new javax.swing.JTextArea();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        panelLeftTop = new javax.swing.JPanel();
        panelSearch = new javax.swing.JPanel();
        fieldSearchEmployeeId = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSearchPending = new javax.swing.JButton();
        btnSearchApproved = new javax.swing.JButton();
        btnSearchRejected = new javax.swing.JButton();
        btnSearchAll = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLeave = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTitle.setBackground(new java.awt.Color(0, 51, 153));

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Verdana", 0, 29)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Leave Management");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 538, Short.MAX_VALUE)
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
        panelFieldsButton.setPreferredSize(new java.awt.Dimension(300, 200));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Leave Entitlements of this Employee on the Year Applied", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 14))); // NOI18N
        jPanel3.setLayout(new java.awt.BorderLayout());

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
        jScrollPane4.setViewportView(tblLeaveEntitlement);

        jPanel3.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout panelFieldsButtonLayout = new javax.swing.GroupLayout(panelFieldsButton);
        panelFieldsButton.setLayout(panelFieldsButtonLayout);
        panelFieldsButtonLayout.setHorizontalGroup(
            panelFieldsButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFieldsButtonLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFieldsButtonLayout.setVerticalGroup(
            panelFieldsButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFieldsButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        panelBodyRight.add(panelFieldsButton, java.awt.BorderLayout.PAGE_END);

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setPreferredSize(new java.awt.Dimension(800, 550));
        panelFields.setLayout(new java.awt.GridBagLayout());

        fieldLeaveRecordId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldLeaveRecordId.setText("Leave Record ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(fieldLeaveRecordId, gridBagConstraints);

        fieldApplicationDate.setEditable(false);
        fieldApplicationDate.setBackground(new java.awt.Color(255, 255, 255));
        fieldApplicationDate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldApplicationDate.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldApplicationDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldApplicationDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldApplicationDate, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel2.setText("Employee ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel2, gridBagConstraints);

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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldEmployeeId, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel3.setText("Leave Type ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel3, gridBagConstraints);

        fieldLeaveTypeId.setEditable(false);
        fieldLeaveTypeId.setBackground(new java.awt.Color(255, 255, 255));
        fieldLeaveTypeId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldLeaveTypeId.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldLeaveTypeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldLeaveTypeIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldLeaveTypeId, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel4.setText("Application Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel5.setText("Applied Start Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel6.setText("Applied End Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel7.setText("Remarks :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 0, 0);
        panelFields.add(jLabel7, gridBagConstraints);

        fieldRecordId.setEditable(false);
        fieldRecordId.setBackground(new java.awt.Color(255, 255, 255));
        fieldRecordId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldRecordId.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 11);
        panelFields.add(fieldRecordId, gridBagConstraints);

        fieldLeaveStatus.setEditable(false);
        fieldLeaveStatus.setBackground(new java.awt.Color(255, 255, 255));
        fieldLeaveStatus.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldLeaveStatus.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldLeaveStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldLeaveStatusActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 16;
        panelFields.add(fieldLeaveStatus, gridBagConstraints);

        fieldAppliedStartDate.setEditable(false);
        fieldAppliedStartDate.setBackground(new java.awt.Color(255, 255, 255));
        fieldAppliedStartDate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldAppliedStartDate.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldAppliedStartDate, gridBagConstraints);

        fieldAppliedEndDate.setEditable(false);
        fieldAppliedEndDate.setBackground(new java.awt.Color(255, 255, 255));
        fieldAppliedEndDate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldAppliedEndDate.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldAppliedEndDate, gridBagConstraints);

        fieldDaysUsed.setEditable(false);
        fieldDaysUsed.setBackground(new java.awt.Color(255, 255, 255));
        fieldDaysUsed.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldDaysUsed.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldDaysUsed, gridBagConstraints);

        fieldStatusUpdatedBy.setEditable(false);
        fieldStatusUpdatedBy.setBackground(new java.awt.Color(255, 255, 255));
        fieldStatusUpdatedBy.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldStatusUpdatedBy.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldStatusUpdatedBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldStatusUpdatedByActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldStatusUpdatedBy, gridBagConstraints);

        fieldStatusUpdatedAt.setEditable(false);
        fieldStatusUpdatedAt.setBackground(new java.awt.Color(255, 255, 255));
        fieldStatusUpdatedAt.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldStatusUpdatedAt.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldStatusUpdatedAt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldStatusUpdatedAtActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panelFields.add(fieldStatusUpdatedAt, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel15.setText("Reason :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 0, 0);
        panelFields.add(jLabel15, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel21.setText("Days Used :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel21, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel22.setText("Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel22, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel23.setText("Status Updated By :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        panelFields.add(jLabel23, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel24.setText("Status Updated At :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelFields.add(jLabel24, gridBagConstraints);

        fieldReason.setEditable(false);
        fieldReason.setBackground(new java.awt.Color(255, 255, 255));
        fieldReason.setColumns(20);
        fieldReason.setRows(5);
        fieldReason.setPreferredSize(new java.awt.Dimension(200, 84));
        jScrollPane2.setViewportView(fieldReason);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelFields.add(jScrollPane2, gridBagConstraints);

        txtAreaRemarks.setColumns(20);
        txtAreaRemarks.setRows(5);
        jScrollPane3.setViewportView(txtAreaRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 0, 0);
        panelFields.add(jScrollPane3, gridBagConstraints);

        btnApprove.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Green"));
        btnApprove.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnApprove.setForeground(new java.awt.Color(255, 255, 255));
        btnApprove.setText("Approve");
        btnApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApproveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 7);
        panelFields.add(btnApprove, gridBagConstraints);

        btnReject.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Red"));
        btnReject.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnReject.setForeground(new java.awt.Color(255, 255, 255));
        btnReject.setText("Reject");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 7);
        panelFields.add(btnReject, gridBagConstraints);

        panelBodyRight.add(panelFields, java.awt.BorderLayout.LINE_START);

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnDelete.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Red"));
        btnDelete.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSearchPending.setBackground(new java.awt.Color(64, 103, 218));
        btnSearchPending.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnSearchPending.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchPending.setText("Pending");
        btnSearchPending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPendingActionPerformed(evt);
            }
        });

        btnSearchApproved.setBackground(new java.awt.Color(64, 103, 218));
        btnSearchApproved.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnSearchApproved.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchApproved.setText("Approved");
        btnSearchApproved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchApprovedActionPerformed(evt);
            }
        });

        btnSearchRejected.setBackground(new java.awt.Color(64, 103, 218));
        btnSearchRejected.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnSearchRejected.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchRejected.setText("Rejected");
        btnSearchRejected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchRejectedActionPerformed(evt);
            }
        });

        btnSearchAll.setBackground(new java.awt.Color(64, 103, 218));
        btnSearchAll.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnSearchAll.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchAll.setText("All");
        btnSearchAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLeftTopLayout = new javax.swing.GroupLayout(panelLeftTop);
        panelLeftTop.setLayout(panelLeftTopLayout);
        panelLeftTopLayout.setHorizontalGroup(
            panelLeftTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeftTopLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(panelLeftTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelLeftTopLayout.createSequentialGroup()
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSearchAll, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearchPending, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearchApproved, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearchRejected, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(panelLeftTopLayout.createSequentialGroup()
                        .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        panelLeftTopLayout.setVerticalGroup(
            panelLeftTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLeftTopLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelLeftTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchPending, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchApproved, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchRejected, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchAll, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(panelLeftTop, java.awt.BorderLayout.PAGE_START);

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
        jScrollPane1.setViewportView(tblLeave);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        panelBody.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldLeaveTypeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldLeaveTypeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldLeaveTypeIdActionPerformed

    private void fieldApplicationDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldApplicationDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldApplicationDateActionPerformed

    private void fieldEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldEmployeeIdActionPerformed

    private void fieldStatusUpdatedAtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldStatusUpdatedAtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldStatusUpdatedAtActionPerformed

    private void fieldLeaveStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldLeaveStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldLeaveStatusActionPerformed

    private void fieldStatusUpdatedByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldStatusUpdatedByActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldStatusUpdatedByActionPerformed

    private void fieldSearchEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldSearchEmployeeIdActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try {
            String searchIdText = fieldSearchEmployeeId.getText();

            // If search input is empty, display all employees
            if (searchIdText == null || searchIdText.isBlank()) {
                showMessage("Please enter an Employee ID to search for");
            } else {
                // If search input is not empty, parse ID and search for a specific employee
                Integer empId = Integer.valueOf(searchIdText); // Parse the employee ID

                // Attempt to find the employee by ID. userHr.getEmployeeById returns a List<Employee>.
                var foundEmployees = userHr.getEmployeeById(empId);

                // Check if the list is not empty (meaning an employee was found)
                if (!foundEmployees.isEmpty()) {
                    // Returns a list with at most one employee for a specific ID
                    System.out.println("Search Employee : " + foundEmployees.get(0).toString());

                    leaveRecords = userHr.getLeaveRecordsByEmployeeId(Integer.parseInt(fieldSearchEmployeeId.getText()));
                    populateLeaveTable(leaveRecords); // Populate table with just that employee's records
                } else {
                    showMessage("No employee found with ID: " + empId);
                }
            }
        } catch (NumberFormatException e) {
            showMessage("Invalid Employee ID format. Please enter a valid number.");
        } catch (SQLException e) {
            String errorMessage = "Database Error: " + e.getMessage();
            if (e.getSQLState() != null) {
                errorMessage += " (SQL State: " + e.getSQLState() + ")";
            }
            showMessage(errorMessage);
        } catch (Exception e) {
            showMessage("An unexpected error occurred during search: " + e.getMessage());
            Logger.getLogger(LeaveMgmtView.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnSearchPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchPendingActionPerformed
        try {
            leaveRecords = userHr.getLeaveRecordsByStatus(LeaveStatus.PENDING);
            populateLeaveTable(leaveRecords);
        } catch (SQLException ex) {
            Logger.getLogger(LeaveMgmtView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchPendingActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSearchApprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchApprovedActionPerformed
        try {
            leaveRecords = userHr.getLeaveRecordsByStatus(LeaveStatus.APPROVED);
            populateLeaveTable(leaveRecords);
        } catch (SQLException ex) {
            Logger.getLogger(LeaveMgmtView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchApprovedActionPerformed

    private void btnSearchRejectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchRejectedActionPerformed
        try {
            leaveRecords = userHr.getLeaveRecordsByStatus(LeaveStatus.REJECTED);
            populateLeaveTable(leaveRecords);
        } catch (SQLException ex) {
            Logger.getLogger(LeaveMgmtView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchRejectedActionPerformed

    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnApproveActionPerformed

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

    private void btnSearchAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchAllActionPerformed
        try {
            populateLeaveTable(null);
        } catch (SQLException ex) {
            Logger.getLogger(LeaveMgmtView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchAllActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        ApplicationThemeUtility.setIntelliJ();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new LeaveMgmtView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnReject;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchAll;
    private javax.swing.JButton btnSearchApproved;
    private javax.swing.JButton btnSearchPending;
    private javax.swing.JButton btnSearchRejected;
    private javax.swing.JTextField fieldApplicationDate;
    private javax.swing.JTextField fieldAppliedEndDate;
    private javax.swing.JTextField fieldAppliedStartDate;
    private javax.swing.JTextField fieldDaysUsed;
    private javax.swing.JTextField fieldEmployeeId;
    private javax.swing.JLabel fieldLeaveRecordId;
    private javax.swing.JTextField fieldLeaveStatus;
    private javax.swing.JTextField fieldLeaveTypeId;
    private javax.swing.JTextArea fieldReason;
    private javax.swing.JTextField fieldRecordId;
    private javax.swing.JTextField fieldSearchEmployeeId;
    private javax.swing.JTextField fieldStatusUpdatedAt;
    private javax.swing.JTextField fieldStatusUpdatedBy;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelBodyRight;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelFieldsButton;
    private javax.swing.JPanel panelLeftTop;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JTable tblLeave;
    private javax.swing.JTable tblLeaveEntitlement;
    private javax.swing.JTextArea txtAreaRemarks;
    // End of variables declaration//GEN-END:variables

    public List<LeaveRecord> getLeaveRecords() {
        return leaveRecords;
    }

    public void setLeaveRecords(List<LeaveRecord> leaveRecords) {
        this.leaveRecords = leaveRecords;
    }

    public HrService getUserHr() {
        return userHr;
    }

    public void setUserHr(HrService userHr) {
        this.userHr = userHr;
    }

    public JButton getBtnApprove() {
        return btnApprove;
    }

    public void setBtnApprove(JButton btnApprove) {
        this.btnApprove = btnApprove;
    }

    public JButton getBtnReject() {
        return btnReject;
    }

    public void setBtnReject(JButton btnReject) {
        this.btnReject = btnReject;
    }

    public JTextField getFieldApplicationDate() {
        return fieldApplicationDate;
    }

    public void setFieldApplicationDate(JTextField fieldApplicationDate) {
        this.fieldApplicationDate = fieldApplicationDate;
    }

    public JTextField getFieldAppliedEndDate() {
        return fieldAppliedEndDate;
    }

    public void setFieldAppliedEndDate(JTextField fieldAppliedEndDate) {
        this.fieldAppliedEndDate = fieldAppliedEndDate;
    }

    public JTextField getFieldAppliedStartDate() {
        return fieldAppliedStartDate;
    }

    public void setFieldAppliedStartDate(JTextField fieldAppliedStartDate) {
        this.fieldAppliedStartDate = fieldAppliedStartDate;
    }

    public JTextField getFieldDaysUsed() {
        return fieldDaysUsed;
    }

    public void setFieldDaysUsed(JTextField fieldDaysUsed) {
        this.fieldDaysUsed = fieldDaysUsed;
    }

    public JTextField getFieldEmployeeId() {
        return fieldEmployeeId;
    }

    public void setFieldEmployeeId(JTextField fieldEmployeeId) {
        this.fieldEmployeeId = fieldEmployeeId;
    }

    public JLabel getFieldLeaveRecordId() {
        return fieldLeaveRecordId;
    }

    public void setFieldLeaveRecordId(JLabel fieldLeaveRecordId) {
        this.fieldLeaveRecordId = fieldLeaveRecordId;
    }

    public JTextField getFieldLeaveStatus() {
        return fieldLeaveStatus;
    }

    public void setFieldLeaveStatus(JTextField fieldLeaveStatus) {
        this.fieldLeaveStatus = fieldLeaveStatus;
    }

    public JTextField getFieldLeaveTypeId() {
        return fieldLeaveTypeId;
    }

    public void setFieldLeaveTypeId(JTextField fieldLeaveTypeId) {
        this.fieldLeaveTypeId = fieldLeaveTypeId;
    }

    public JTextArea getFieldReason() {
        return fieldReason;
    }

    public void setFieldReason(JTextArea fieldReason) {
        this.fieldReason = fieldReason;
    }

    public JTextField getFieldRecordId() {
        return fieldRecordId;
    }

    public void setFieldRecordId(JTextField fieldRecordId) {
        this.fieldRecordId = fieldRecordId;
    }

    public JTextArea getTxtAreaRemarks() {
        return txtAreaRemarks;
    }

    public void setTxtAreaRemarks(JTextArea txtAreaRemarks) {
        this.txtAreaRemarks = txtAreaRemarks;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(JButton btnDelete) {
        this.btnDelete = btnDelete;
    }

    public JTable getTblLeave() {
        return tblLeave;
    }

    public void setTblLeave(JTable tblLeave) {
        this.tblLeave = tblLeave;
    }   


}
