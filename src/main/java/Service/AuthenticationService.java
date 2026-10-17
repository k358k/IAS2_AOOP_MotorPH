package Service;

import Model.*;
import DAO.*;
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
            
            String sql = "SELECT password, failed_attempts, account_locked FROM user_credential WHERE employee_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, 
                    "No user found with ID: " + empId, 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            boolean isLocked = rs.getBoolean("account_locked");
            int currentAttempts = rs.getInt("failed_attempts");

            if (isLocked) {
                JOptionPane.showMessageDialog(null, 
                    "Account is locked due to 3 failed login attempts. Please contact IT.", 
                    "Account Locked", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String hashedDbPassword = rs.getString("password");

            if (PasswordUtil.checkPassword(password, hashedDbPassword)) {
                PreparedStatement resetStmt = conn.prepareStatement(
                    "UPDATE user_credential SET failed_attempts = 0 WHERE employee_id = ?"
                );
                resetStmt.setInt(1, empId);
                resetStmt.executeUpdate();
                
                return true;
            } else {
                currentAttempts++;

                if (currentAttempts >= 3) {
                    PreparedStatement lockStmt = conn.prepareStatement(
                        "UPDATE user_credential SET failed_attempts = ?, account_locked = TRUE WHERE employee_id = ?"
                    );
                    lockStmt.setInt(1, currentAttempts);
                    lockStmt.setInt(2, empId);
                    lockStmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, 
                        "Maximum login attempts exceeded. Account is now LOCKED.", 
                        "Account Locked Warning", 
                        JOptionPane.WARNING_MESSAGE);
                } else {
                    PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE user_credential SET failed_attempts = ? WHERE employee_id = ?"
                    );
                    updateStmt.setInt(1, currentAttempts);
                    updateStmt.setInt(2, empId);
                    updateStmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, 
                        "Invalid password. Attempts remaining: " + (3 - currentAttempts), 
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