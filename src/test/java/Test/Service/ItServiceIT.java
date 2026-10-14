package Service;

import DAO.EmployeeUserMgmtImpl;
import Model.Employee;
import Model.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItServiceIT {

    private static ItService itService;
    private static Employee realEmployee;

    // This runs once before all tests
    @BeforeAll
    static void setUp() throws Exception {
        // Use DAO to get real employee from DB (e.g., employee 10001)
        EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();
        realEmployee = employeeDAO.findEmployeeById(10001);

        // Make sure real employee was found
        assertNotNull(realEmployee, "Employee with ID 10001 should exist in the database");

        // Create ItService using real employee info from DB
        itService = new ItService(
            realEmployee.getFirstName(),
            realEmployee.getLastName(),
            realEmployee.getGender(),
            realEmployee.getBirthday(),
            realEmployee.getPhoneNumber(),
            realEmployee.getSssNumber(),
            realEmployee.getPhilhealthNumber(),
            realEmployee.getTinNumber(),
            realEmployee.getPagibigNumber(),
            realEmployee.getEmpStatus(),
            realEmployee.getDepartment(),
            realEmployee.getSupervisorId(),
            realEmployee.getPositionSalaryHistory(),
            realEmployee.getEmployeeAddress(),
            realEmployee.getEmpId(),
            realEmployee.getPassword(),
            realEmployee.getUserRole()
        );
    }

    // ✅ Test 1: Get all employees from DB
    @Test
    void testGetAllEmployees() {
        // This method returns all employees from the database
        List<Employee> employees = itService.getAllEmployees();

        // Make sure the list is not null
        assertNotNull(employees, "Employee list should not be null");

        // Print the number of employees found
        System.out.println("Employees found: " + employees.size());

        // Check the first employee if list is not empty
        if (!employees.isEmpty()) {
            Employee e = employees.get(0);
            System.out.println("Sample Employee: " + e.getFirstName() + " " + e.getLastName());
        }
    }

    // ✅ Test 2: Update password of real employee 10001
    @Test
    void testUpdatePassword() {
        String newPassword = "TestPass123"; // In production, hash this password

        // Try to update the password
        boolean success = itService.updatePassword(realEmployee.getEmpId(), newPassword);

        // Verify it was updated
        assertTrue(success, "Password should be updated for employee 10001");

        System.out.println("Password update success for employee ID 10001");
    }

    // ✅ Test 3: Update user role of real employee 10001
    @Test
    void testUpdateUserRole() {
        UserRole newRole = UserRole.HR;

        // Try to update the user role
        boolean success = itService.updateUserRole(realEmployee.getEmpId(), newRole);

        // Confirm success
        assertTrue(success, "User role should be updated for employee 10001");

        System.out.println("User role changed to " + newRole + " for employee ID 10001");
    }
}

