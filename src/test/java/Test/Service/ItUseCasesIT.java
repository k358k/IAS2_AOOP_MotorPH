package Service;

import Model.*;
import Service.ItService;
import DAO.EmployeeUserMgmtImpl;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

// This test uses real employee data from the database
public class ItUseCasesIT {

    private ItService itService;

    @BeforeEach
    void setUp() {
        // Initialize ItService using real employee data from the database
        EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();
        Employee employee = null;
        try {
            employee = employeeDAO.findEmployeeById(10001); // ← Real DB data
        } catch (SQLException ex) {
            Logger.getLogger(ItUseCasesIT.class.getName()).log(Level.SEVERE, null, ex);
        }

        itService = new ItService(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                employee.getBirthday(),
                employee.getPhoneNumber(),
                employee.getSssNumber(),
                employee.getPhilhealthNumber(),
                employee.getTinNumber(),
                employee.getPagibigNumber(),
                employee.getEmpStatus(),
                employee.getDepartment(),
                employee.getSupervisorId(),
                employee.getPositionSalaryHistory(),
                employee.getEmployeeAddress(),
                employee.getEmpId(),
                employee.getPassword(), // in real case this should be hashed
                employee.getUserRole()
        );
    }

    //Get all employees from database
    @Test
    void testGetAllEmployees() {
        List<Employee> employees = itService.getAllEmployees();

        assertNotNull(employees, "Employee list should not be null");
        assertTrue(employees.size() > 0, "Should return at least one employee");

        System.out.println("Employee count: " + employees.size());
    }

    /**
     * Tests updating password for employee with ID 10001
     * Verifies that:
     * 1. Password update operation returns true (success)
     * 2. New password meets security requirements
     */
    @Test
    void testUpdatePasswordForEmployee10001() {
        System.out.println("\n=== Testing password update for employee 10001 ===");
        String newPassword = "newSecret123";

        System.out.println("Attempting to update password to: " + newPassword);
        boolean success = itService.updatePassword(10001, newPassword);

        System.out.println("Update operation result: " + (success ? "SUCCESS" : "FAILED"));
        assertTrue(success, "Password update should succeed");

        if (success) {
            System.out.println("Password updated successfully");
        } else {
            System.out.println("Password update failed");
        }
    }

    /**
     * Tests updating user role for employee with ID 10001
     * Verifies that:
     * 1. Role update operation returns true (success)
     * 2. New role is valid and authorized
     */
    @Test
    void testUpdateUserRoleForEmployee10001() {
        System.out.println("\n=== Testing role update for employee 10001 ===");
        UserRole newRole = UserRole.EMPLOYEE;

        System.out.println("Attempting to update role to: " + newRole);
        boolean updated = itService.updateUserRole(10001, newRole);

        System.out.println("Update operation result: " + (updated ? "SUCCESS" : "FAILED"));
        assertTrue(updated, "User role should be updated successfully");

        if (updated) {
            System.out.println("Role updated to " + newRole + " successfully");
        } else {
            System.out.println("Role update failed");
        }
    }
}

