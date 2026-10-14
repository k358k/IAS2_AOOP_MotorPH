/*



 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license


 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template


 */
package View;

import Config.ApplicationThemeUtility;
import Model.ClockHour;
import Model.Employee;
import Service.EmployeeService;
import Service.HrService;
import Util.TableModelUtil;
import java.awt.Color;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

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
public class AttendanceMgmtView extends javax.swing.JFrame {
    private List<ClockHour> attendanceRecords;
    private HrService userHr;


    public AttendanceMgmtView(EmployeeService user) {
        try {
            this.userHr = new HrService(user);
            initComponents();
            initCustomComponents();
            populateAttendanceTable(null,null,null);

        } catch (SQLException ex) {
            Logger.getLogger(AttendanceMgmtView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void showMessage(String message){ JOptionPane.showMessageDialog(this, message); }  
    
    public final void populateAttendanceTable(Integer searchFieldEmployeeId, LocalDate startDate, LocalDate endDate)throws SQLException{
            try {
            // Check if parameters are provided for filtering
            if (searchFieldEmployeeId != null && startDate != null && endDate != null) {
                attendanceRecords = userHr.getAttendanceByEmployeeAndPayPeriod(searchFieldEmployeeId, startDate, endDate); // Fetch filtered records
            } else {
                attendanceRecords = userHr.getAllAttendance(); // Fetch all records
            }

            // Create a table model with the required columns
            String[] columnNamesAttendance = {"Record ID", "Employee ID", "Date", "Clock In Time", "Clock Out Time", "Hours Worked"};
            DefaultTableModel tableModelAttendance = TableModelUtil.createReadOnlyTableModel(
                    attendanceRecords,
                    columnNamesAttendance,
                    record -> new Object[]{
                        record.getClockHourId(),
                        record.getEmpId(),
                        record.getClockInDateTime() != null ? record.getClockInDateTime().toLocalDate() : null, // Date
                        record.getClockInDateTime() != null ? record.getClockInDateTime().toLocalTime() : null, // Clock In Time
                        record.getClockOutDateTime() != null ? record.getClockOutDateTime().toLocalTime() : null, // Clock Out Time
                        record.getHoursWorked() != null ? record.getHoursWorked() : 0 // Hours Worked (default to 0 if null)
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
//       tableAttendance.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       
       // Add a ListSelectionListener to the table
       tableAttendance.getSelectionModel().addListSelectionListener(event -> {
           if (!event.getValueIsAdjusting()) { // Only respond when the selection is final
               int selectedRow = tableAttendance.getSelectedRow();
               if (selectedRow != -1) { // Check if a row is selected
                   // Get the selected record
                   ClockHour selectedAttendanceRecord = attendanceRecords.get(selectedRow);

                   // Update text fields with the selected records's data
            fieldEmployeeId.setText(selectedAttendanceRecord.getEmpId() != null ? selectedAttendanceRecord.getEmpId().toString() : ""); // Employee ID
            fieldDate.setText(selectedAttendanceRecord.getClockInDateTime() != null ? selectedAttendanceRecord.getClockInDateTime().toLocalDate().toString() : ""); // Date
            fieldClockInTime.setText(selectedAttendanceRecord.getClockInDateTime() != null ? selectedAttendanceRecord.getClockInDateTime().toLocalTime().toString() : ""); // Clock In Time
            fieldClockOutTime.setText(selectedAttendanceRecord.getClockOutDateTime() != null ? selectedAttendanceRecord.getClockOutDateTime().toLocalTime().toString() : ""); // Clock Out Time
            fieldHoursWorked.setText(selectedAttendanceRecord.getHoursWorked() != null ? selectedAttendanceRecord.getHoursWorked().toString() : "0"); // Hours Worked (default to 0 if null)

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

        panelTitle = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        panelBody = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelSearch = new javax.swing.JPanel();
        fieldSearchEmployeeId = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        fieldSearchPayPeriodEnd = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        fieldSearchPayPeriodStart = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        btnAll = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fieldDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        fieldEmployeeId = new javax.swing.JTextField();
        fieldClockOutTime = new javax.swing.JTextField();
        fieldClockInTime = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fieldHoursWorked = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        scrollPaneTblAttendance = new javax.swing.JScrollPane();
        tableAttendance = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTitle.setBackground(new java.awt.Color(0, 51, 153));

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Verdana", 0, 29)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Attendance Management");

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

        fieldSearchPayPeriodEnd.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldSearchPayPeriodEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldSearchPayPeriodEndActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel29.setText("Pay Period End :");

        fieldSearchPayPeriodStart.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        fieldSearchPayPeriodStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldSearchPayPeriodStartActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel27.setText("Pay Period Start :");

        javax.swing.GroupLayout panelSearchLayout = new javax.swing.GroupLayout(panelSearch);
        panelSearch.setLayout(panelSearchLayout);
        panelSearchLayout.setHorizontalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSearchEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSearchPayPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSearchPayPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(180, 180, 180)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fieldSearchPayPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel27)
                        .addComponent(fieldSearchPayPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29))
                    .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fieldSearchEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26)))
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
                .addGap(103, 103, 103)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(452, 452, 452))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        panelBody.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Employee ID :");

        fieldDate.setEditable(false);
        fieldDate.setBackground(new java.awt.Color(255, 255, 255));
        fieldDate.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        fieldDate.setPreferredSize(new java.awt.Dimension(200, 22));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Date :");

        fieldEmployeeId.setEditable(false);
        fieldEmployeeId.setBackground(new java.awt.Color(255, 255, 255));
        fieldEmployeeId.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        fieldEmployeeId.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldEmployeeIdActionPerformed(evt);
            }
        });

        fieldClockOutTime.setEditable(false);
        fieldClockOutTime.setBackground(new java.awt.Color(255, 255, 255));
        fieldClockOutTime.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        fieldClockOutTime.setPreferredSize(new java.awt.Dimension(200, 20));

        fieldClockInTime.setEditable(false);
        fieldClockInTime.setBackground(new java.awt.Color(255, 255, 255));
        fieldClockInTime.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        fieldClockInTime.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldClockInTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldClockInTimeActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Clock Out Time :");

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Clock In Time :");

        fieldHoursWorked.setEditable(false);
        fieldHoursWorked.setBackground(new java.awt.Color(255, 255, 255));
        fieldHoursWorked.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        fieldHoursWorked.setPreferredSize(new java.awt.Dimension(200, 22));
        fieldHoursWorked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldHoursWorkedActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Hours Worked :");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 74, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fieldEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldClockInTime, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fieldHoursWorked, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldClockOutTime, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(fieldEmployeeId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(45, 45, 45)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldClockInTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fieldClockOutTime, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(40, 40, 40)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fieldHoursWorked, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        scrollPaneTblAttendance.setViewportView(tableAttendance);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(scrollPaneTblAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollPaneTblAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBody.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void fieldSearchEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchEmployeeIdActionPerformed

        // TODO add your handling code here:

    }//GEN-LAST:event_fieldSearchEmployeeIdActionPerformed


    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed

        // TODO add your handling code here:

    }//GEN-LAST:event_btnSearchActionPerformed


    private void fieldSearchPayPeriodStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchPayPeriodStartActionPerformed

        // TODO add your handling code here:

    }//GEN-LAST:event_fieldSearchPayPeriodStartActionPerformed


    private void fieldSearchPayPeriodEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchPayPeriodEndActionPerformed

        // TODO add your handling code here:

    }//GEN-LAST:event_fieldSearchPayPeriodEndActionPerformed


    private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
        try {
            populateAttendanceTable(null,null,null);
            // TODO add your handling code here:
        } catch (SQLException ex) {
            showMessage("Error" + ex.getMessage());
        }

    }//GEN-LAST:event_btnAllActionPerformed


    private void fieldEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEmployeeIdActionPerformed

        // TODO add your handling code here:

    }//GEN-LAST:event_fieldEmployeeIdActionPerformed


    private void fieldClockInTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldClockInTimeActionPerformed

        // TODO add your handling code here:

    }//GEN-LAST:event_fieldClockInTimeActionPerformed

    private void fieldHoursWorkedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldHoursWorkedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldHoursWorkedActionPerformed

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
    private javax.swing.JButton btnSearch;
    private javax.swing.JTextField fieldClockInTime;
    private javax.swing.JTextField fieldClockOutTime;
    private javax.swing.JTextField fieldDate;
    private javax.swing.JTextField fieldEmployeeId;
    private javax.swing.JTextField fieldHoursWorked;
    private javax.swing.JTextField fieldSearchEmployeeId;
    private javax.swing.JTextField fieldSearchPayPeriodEnd;
    private javax.swing.JTextField fieldSearchPayPeriodStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JScrollPane scrollPaneTblAttendance;
    private javax.swing.JTable tableAttendance;
    // End of variables declaration//GEN-END:variables

    public List<ClockHour> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(List<ClockHour> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

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

    public JButton getBtnAll() {
        return btnAll;
    }

    public void setBtnAll(JButton btnAll) {
        this.btnAll = btnAll;
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

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public void setBtnSearch(JButton btnSearch) {
        this.btnSearch = btnSearch;
    }

    public JTextField getFieldClockInTime() {
        return fieldClockInTime;
    }

    public void setFieldClockInTime(JTextField fieldClockInTime) {
        this.fieldClockInTime = fieldClockInTime;
    }

    public JTextField getFieldClockOutTime() {
        return fieldClockOutTime;
    }

    public void setFieldClockOutTime(JTextField fieldClockOutTime) {
        this.fieldClockOutTime = fieldClockOutTime;
    }

    public JTextField getFieldDate() {
        return fieldDate;
    }

    public void setFieldDate(JTextField fieldDate) {
        this.fieldDate = fieldDate;
    }

    public JTextField getFieldEmployeeId() {
        return fieldEmployeeId;
    }

    public void setFieldEmployeeId(JTextField fieldEmployeeId) {
        this.fieldEmployeeId = fieldEmployeeId;
    }

    public JTextField getFieldHoursWorked() {
        return fieldHoursWorked;
    }

    public void setFieldHoursWorked(JTextField fieldHoursWorked) {
        this.fieldHoursWorked = fieldHoursWorked;
    }

    public JTextField getFieldSearchEmployeeId() {
        return fieldSearchEmployeeId;
    }

    public void setFieldSearchEmployeeId(JTextField fieldSearchEmployeeId) {
        this.fieldSearchEmployeeId = fieldSearchEmployeeId;
    }

    public JTextField getFieldSearchPayPeriodEnd() {
        return fieldSearchPayPeriodEnd;
    }

    public void setFieldSearchPayPeriodEnd(JTextField fieldSearchPayPeriodEnd) {
        this.fieldSearchPayPeriodEnd = fieldSearchPayPeriodEnd;
    }

    public JTextField getFieldSearchPayPeriodStart() {
        return fieldSearchPayPeriodStart;
    }

    public void setFieldSearchPayPeriodStart(JTextField fieldSearchPayPeriodStart) {
        this.fieldSearchPayPeriodStart = fieldSearchPayPeriodStart;
    }

    public JTable getTableAttendance() {
        return tableAttendance;
    }

    public void setTableAttendance(JTable tableAttendance) {
        this.tableAttendance = tableAttendance;
    }



}
