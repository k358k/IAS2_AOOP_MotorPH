package Service;

import DAO.*;
import Model.*;
import Util.PasswordUtil;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationService {

    EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();

    public boolean authenticate(int empId, String password) {
        // Fetch the user credentials from the database
        Employee emp = loadUserDatafromDatabase(empId);

        // Check if exists
        if (emp == null) {
            System.out.println("No user found with empId: " + empId);
            return false; // User does not exist
        }

        // Get hashed password from database to check
        String hashedDbPassword = emp.getPassword();
        System.out.println("hashed password from database: " + hashedDbPassword);
        System.out.println("hashed password from input: " + PasswordUtil.hashPassword(password));

        if (PasswordUtil.checkPassword(password, hashedDbPassword)) {
            return true; // Authentication successful
        }

        return false; // Authentication failed
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
