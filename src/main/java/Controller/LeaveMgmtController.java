/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Service.HrService;
import java.sql.SQLException;
import javax.swing.*;
import View.LeaveMgmtView;

/**
 * Controller class for managing leave operations in the application.
 * Handles user interactions between the {@link LeaveMgmtView} and
 * service layers {@link HrService}).

 */
public class LeaveMgmtController {
    private LeaveMgmtView view;

    private HrService userHr;

    /**
     * Constructs a LeaveManagementController with the specified view.
     * Initializes services and sets up event listeners.
     * 
     * @param view the LeaveManagementView to control
     */
    public LeaveMgmtController(LeaveMgmtView view) {
        this.view = view;
        this.userHr = new HrService(view.getUserHr());

        SwingUtilities.invokeLater(this::initController); // Ensure this runs on the EDT
    }
    
    /**
     * Initializes controller event listeners.
     */
    private void initController() {
        view.getBtnDelete().addActionListener(e -> {
            System.out.println("Delete Button Clicked");
            deleteLeave();
        });
        
        view.getBtnApprove().addActionListener(e -> {
            System.out.println("Approve Button Clicked");
            approveLeave();
        });
        
        view.getBtnReject().addActionListener(e -> {
            System.out.println("Reject Button Clicked");
            rejectLeave();
        });

    }

    /**
     * Handles leave approval process.
     */
    private void approveLeave() {
        String leaveIdText = view.getFieldRecordId().getText();
        String remarks = view.getTxtAreaRemarks().getText();
        
        if (leaveIdText.isEmpty()) {
            view.showMessage("Please select a leave record to approve.");
            return;
        } else if (remarks.isEmpty()) {
            view.showMessage("Please fill in the remarks.");
            return;
        }

        int response = JOptionPane.showConfirmDialog(
            view, 
            "Do you want to approve this leave request?", 
            "Confirm Approval", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (response == JOptionPane.YES_OPTION) {
            try {
                int leaveId = Integer.parseInt(leaveIdText);
                boolean success = userHr.approveLeave(leaveId,remarks);
                
                if (success) {
                    view.showMessage("Leave request approved successfully!");
                    view.populateLeaveTable(null);
                    
                } else {
                    view.showMessage("Failed to approve leave request.");
                }
            } catch (NumberFormatException e) {
                view.showMessage("Invalid leave ID format.");
            } catch (SQLException e) {
                view.showMessage("Error: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                view.showMessage("An error occurred while approving the leave request.");
            }
        }
    }

    /**
     * Handles leave rejection process.
     */
    private void rejectLeave() {
        String leaveIdText = view.getFieldRecordId().getText();
        String remarks = view.getTxtAreaRemarks().getText();
        
        if (leaveIdText.isEmpty()) {
            view.showMessage("Please select a leave record to reject.");
            return;
        } else if (remarks.isEmpty()) {
            view.showMessage("Please fill in the remarks.");
            return;
        }

        int response = JOptionPane.showConfirmDialog(
            view, 
            "Do you want to reject this leave request?", 
            "Confirm Approval", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (response == JOptionPane.YES_OPTION) {
            try {
                int leaveId = Integer.parseInt(leaveIdText);
                boolean success = userHr.rejectLeave(leaveId,remarks);
                
                if (success) {
                    view.showMessage("Leave request rejected successfully!");
                    view.populateLeaveTable(null);
                } else {
                    view.showMessage("Failed to reject leave request.");
                }
            } catch (NumberFormatException e) {
                view.showMessage("Invalid leave ID format.");
            } catch (SQLException e) {
                view.showMessage("Error: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                view.showMessage("An error occurred while rejecting the leave request.");
            }
        }
    }

    /**
     * Handles leave deletion process.
     */
    private void deleteLeave() {
        int selectedRow = view.getTblLeave().getSelectedRow();
        int recordId = 0;
        
        if (selectedRow != -1) { // Check if a row is selected
            recordId = (Integer) view.getTblLeave().getValueAt(selectedRow, view.getTblLeave().getColumn("Leave Record ID").getModelIndex());
        } else {
            System.out.println("Please select a record to delete.");
        }
        
        int response = JOptionPane.showConfirmDialog(
            view,
            "Are you sure you want to delete this leave record?\nThis action cannot be undone.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        ); 
        
        if (response == JOptionPane.YES_OPTION) {
            try {
                boolean success = userHr.deleteLeave(recordId);
                if (success) {
                    view.populateLeaveTable(null);
                    view.showMessage("Leave record deleted successfully");
                } else {
                    view.showMessage("Failed to delete leave record");
                }
            } catch (SQLException e) {
                view.showMessage("Database Error: " + e.getMessage());
            } catch (Exception e) {
                view.showMessage("Error deleting record: " + e.getMessage());
            }
        }
    }
    
}