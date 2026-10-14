/*



 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license


 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template


 */
package View;

import Config.ApplicationThemeUtility;
import Model.MonthlyPayrollSummary;
import Service.EmployeeService;
import Service.FinanceService;
import Util.TableModelUtil;
import java.awt.Color;
import java.sql.SQLException;
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
public class MonthlyPayrollSummaryRecordsView extends javax.swing.JFrame {

    private List<MonthlyPayrollSummary> payrollSummaryRecords;
    private FinanceService userFinance;

    /**
     *
     *
     * Creates new form MonthlyPayrollSummaryRecordsView
     *
     *
     */
    public MonthlyPayrollSummaryRecordsView(EmployeeService user) {
        try {
            this.userFinance = new FinanceService(user);
            initComponents();
            initCustomComponents();
            populatePayrollSummaryTable();

        } catch (SQLException ex) {
            Logger.getLogger(MonthlyPayrollSummaryRecordsView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public final void populatePayrollSummaryTable() throws SQLException {
        try {
            payrollSummaryRecords = userFinance.getAllMonthlyPayrollSummary();
            System.out.println("DEBUG:"
                    + "payroll summary records: " + payrollSummaryRecords.toString());


            // Define column names for the table model
            String[] columnNamesPayrollSummary = {
                "Payroll Month", 
                "Total Gross Income",
                "Total SSS Contribution", 
                "Total PhilHealth Contribution",
                "Total Pag-IBIG Contribution", 
                "Total Withholding Tax",  
                "Total Net Pay"
            };

            // Create a table model using the TableModelUtil and a lambda to map objects to row data
            DefaultTableModel tableModelPayrollSummary = TableModelUtil.createReadOnlyTableModel(payrollSummaryRecords,
                    columnNamesPayrollSummary,
                    record -> new Object[]{
                        record.getPayrollMonth(),
                        record.getTotalGrossIncome(),
                        record.getTotalSssContribution(),
                        record.getTotalPhilhealthContribution(),
                        record.getTotalPagibigContribution(),
                        record.getTotalWithholdingTax(),
                        record.getTotalNetPay()
                    }
            );

            // Set the table model to the JTable
            tblPayrollSummary.setModel(tableModelPayrollSummary);

//            // Define preferred widths for each column in the Payslip table
//            // These widths correspond to the order of columns defined in tableModelPayslip
//            TableColumnModel columnModelPayrollSummary = tblPayrollSummary.getColumnModel();
//
//            int[] columnWidths = {
//                100, // "Payroll Month", 
//                150, // "Total Gross Income",
//                200, // "Total SSS Contribution", 
//                200, // "Total PhilHealth Contribution",
//                200, // "Total Pag-IBIG Contribution", 
//                150, // "Total Withholding Tax",  
//                200, // "Total Net Pay"
//            };
//
//            // Loop through the columns and set their preferred widths
//            for (int i = 0; i < columnWidths.length; i++) {
//                if (i < columnModelPayrollSummary.getColumnCount()) { // Safety check in case widths array is shorter
//                    TableColumn column = columnModelPayrollSummary.getColumn(i);
//                    column.setPreferredWidth(columnWidths[i]);
//                }
//            }

            // Apply the custom cell renderer to all columns
            tblPayrollSummary.setDefaultRenderer(Object.class, new ZebraStripingCellRenderer());

            // Customize the table header background
            tblPayrollSummary.getTableHeader().setBackground(ColorTheme.BLUE);
            tblPayrollSummary.getTableHeader().setForeground(Color.WHITE);

        } catch (Exception e) {
            // Handle any exceptions (e.g., database errors, parsing errors)
            JOptionPane.showMessageDialog(this, "Error fetching payslip records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void initCustomComponents() {
//        tblPayrollSummary.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPayrollSummary = new javax.swing.JTable();
        lblGreeting = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTitle.setBackground(new java.awt.Color(0, 51, 153));

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Verdana", 0, 29)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Monthly Payroll Summary Records");

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

        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tblPayrollSummary.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblPayrollSummary);

        lblGreeting.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        lblGreeting.setText("Summary Records");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1377, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGreeting))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(lblGreeting)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(277, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1494, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
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
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnLogout;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGreeting;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JTable tblPayrollSummary;
    // End of variables declaration//GEN-END:variables


    public FinanceService getUserFinance() {
        return userFinance;
    }

    public void setUserFinance(FinanceService userFinance) {
        this.userFinance = userFinance;
    }


}
