package Service;

import Model.*;
import DAO.*;
import Util.AuditLogger;
import Util.PasswordUtil;
import Util.DatabaseConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AuthenticationService {

    private EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();

    public boolean authenticate(int empId, String password) {
        
        try (Connection conn = DatabaseConnectionUtil.getConnection()) {
            
            // Fetch the user's password, failed attempts, and lock status from database
            String sql = "SELECT password, failed_attempts, account_locked FROM user_credential WHERE employee_id = ?"; 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                // AUDIT LOG (INVALID USER)
                AuditLogger.logActivity(
                    String.valueOf(empId),
                    "LOGIN_FAILED",
                    String.valueOf(empId),
                    "Login attempt failed: User ID does not exist"
                );

                JOptionPane.showMessageDialog(null, 
                    "Invalid credentials. Please check your ID and password.", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            boolean isLocked = rs.getBoolean("account_locked");
            int currentAttempts = rs.getInt("failed_attempts");

            // Control 1: Stops here if account is already locked
            if (isLocked) {
                // AUDIT LOG (LOCKED ACCOUNT TRY)
                AuditLogger.logActivity(
                    String.valueOf(empId),
                    "LOGIN_BLOCKED",
                    String.valueOf(empId),
                    "Login rejected: Account is currently locked"
                );

                JOptionPane.showMessageDialog(null, 
                    "Account is locked due to 3 failed login attempts. Please contact IT.", 
                    "Account Locked", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String hashedDbPassword = rs.getString("password");
            
            // Check if password matches
            if (PasswordUtil.checkPassword(password, hashedDbPassword)) {
                
                // Control 1: Correct password, reset counter back to 0
                PreparedStatement resetStmt = conn.prepareStatement(
                    "UPDATE user_credential SET failed_attempts = 0 WHERE employee_id = ?"
                );
                resetStmt.setInt(1, empId);
                resetStmt.executeUpdate();
                
                // AUDIT LOG (SUCCESSFUL LOGIN)
                AuditLogger.logActivity(
                    String.valueOf(empId),
                    "LOGIN_SUCCESS",
                    String.valueOf(empId),
                    "User successfully authenticated"
                );

                return true;
            } else {
                // Wrong password: Add 1 to failed attempts
                currentAttempts++;

                if (currentAttempts >= 3) {
                    // Control 1: Reached 3 tries -> lock the account in database
                    PreparedStatement lockStmt = conn.prepareStatement(
                        "UPDATE user_credential SET failed_attempts = ?, account_locked = TRUE WHERE employee_id = ?"
                    );
                    lockStmt.setInt(1, currentAttempts);
                    lockStmt.setInt(2, empId);
                    lockStmt.executeUpdate();

                    // AUDIT LOG (ACCOUNT LOCKED)
                    AuditLogger.logActivity(
                        String.valueOf(empId),
                        "ACCOUNT_LOCKED",
                        String.valueOf(empId),
                        "Account locked: Exceeded 3 failed login attempts"
                    );

                    JOptionPane.showMessageDialog(null, 
                        "Maximum login attempts exceeded. Account is now LOCKED.", 
                        "Account Locked Warning", 
                        JOptionPane.WARNING_MESSAGE);
                } else {
                    // Control 1: Save updated attempt count in database
                    PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE user_credential SET failed_attempts = ? WHERE employee_id = ?"
                    );
                    updateStmt.setInt(1, currentAttempts);
                    updateStmt.setInt(2, empId);
                    updateStmt.executeUpdate();

                    // AUDIT LOG (FAILED PASSWORD TRY)
                    AuditLogger.logActivity(
                        String.valueOf(empId),
                        "LOGIN_FAILED",
                        String.valueOf(empId),
                        "Incorrect password entry. Attempt " + currentAttempts + " of 3"
                    );

                    // UPDATED: Now uses generic security terminology
                    JOptionPane.showMessageDialog(null, 
                        "Invalid credentials. Attempts remaining: " + (3 - currentAttempts), 
                        "Login Failed", 
                        JOptionPane.WARNING_MESSAGE);
                }
                return false;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Employee loadUserDatafromDatabase(int empId) {
        try {
            return employeeDAO.findEmployeeById(empId);
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}