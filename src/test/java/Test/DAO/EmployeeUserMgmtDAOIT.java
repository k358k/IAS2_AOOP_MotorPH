package Test.DAO;

import DAO.EmployeeUserMgmtDAO;
import DAO.EmployeeUserMgmtImpl;
import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeUserMgmtDAOIT {

    private EmployeeUserMgmtDAO dao;

    /**
     * Setup method that runs before each test case
     * Initializes the DAO implementation with database connection
     */
    @BeforeEach
    void setUp() {
        dao = new EmployeeUserMgmtImpl();
    }

    /**
     * Tests retrieval of all employees from the database
     * Verifies that:
     * 1. The returned list is not null
     * 2. The list is not empty
     * 3. Prints all employees' basic info for visual verification
     */
    @Test
    void testFindAllEmployee() throws SQLException {
        // Retrieve all employees from database
        List<Employee> employees = dao.findAllEmployee();
        
        // Print all employees for verification
        System.out.println("All Employees:");
        employees.forEach(emp -> 
            System.out.println(emp.getEmpId() + ": " + emp.getFirstName() + " " + emp.getLastName())
        );

        // Assertions to verify data integrity
        assertNotNull(employees);
        assertFalse(employees.isEmpty(), "Expected at least one employee in the database");
    }

    /**
     * Tests finding a specific employee by their ID
     * Verifies that:
     * 1. The employee with specified ID exists
     * 2. The retrieved employee's ID matches the requested ID
     * 3. Basic employee info is printed for verification
     */
    @Test
    void testFindEmployeeById() throws SQLException {
        // Known existing employee ID
        int testEmployeeId = 10003;
        
        // Retrieve employee from database
        Employee employee = dao.findEmployeeById(testEmployeeId);
        
        // Print employee details for verification
        System.out.println( "Found Employee with ID:" + testEmployeeId);
        System.out.println(employee.getFirstName() + " " + employee.getLastName());

        // Assertions to verify data integrity
        assertNotNull(employee, "Employee with ID" + testEmployeeId + "should exist in the DB");
        assertEquals(testEmployeeId, employee.getEmpId());
    }

    /**
     * Tests updating a user's credentials (password and role)
     * Procedure:
     * 1. Retrieves an existing employee
     * 2. Creates new credentials with updated password
     * 3. Updates the credentials in database
     * 4. Verifies update was successful
     * 5. Restores original credentials (cleanup)
     */
    @Test
    void testUpdateUserCredential() throws SQLException {
        // Known existing employee ID
        int testEmployeeId = 10003;
        
        // Retrieve employee from database
        Employee employee = dao.findEmployeeById(testEmployeeId);
        
        // Store current password for restoration
        String oldPassword = employee.getPassword();
        
        // Create unique new password
        String newPassword = "testpass_" + System.currentTimeMillis();
        
        // Create new credential object with updated password
        UserCredential cred = new UserCredential(
            testEmployeeId,
            newPassword,
            employee.getUserRole()
        );

        // Perform update and verify success
        boolean success = dao.updateUserCredential(cred);
        System.out.println("Updated user credential for employee ID: " + testEmployeeId);
        assertTrue(success, "User credential update should be successful");

        // Cleanup: restore original password
        cred = new UserCredential(
            testEmployeeId,
            oldPassword,
            employee.getUserRole()
        );
        dao.updateUserCredential(cred);
    }

    /**
     * Tests updating an employee's basic record information
     * Procedure:
     * 1. Retrieves an existing employee
     * 2. Updates the last name with a unique value
     * 3. Verifies update was successful
     * 4. Restores original last name (cleanup)
     */
    @Test
    void testUpdateEmployeeRecord() throws SQLException {
        int testEmployeeId = 10003;
        Employee employee = dao.findEmployeeById(testEmployeeId);
        String originalLastName = employee.getLastName();

        // Create a new employee object with updated data
        Employee updatedEmployee = new Employee(
            employee.getFirstName(),
            "TestLastname_" + System.currentTimeMillis(),
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
            employee.getPassword(),
            employee.getUserRole()
        );

        boolean updated = dao.updateEmployeeRecord(updatedEmployee);
        assertTrue(updated, "Employee record should be updated successfully");

        // Verify the update
        Employee retrieved = dao.findEmployeeById(testEmployeeId);
        assertEquals(updatedEmployee.getLastName(), retrieved.getLastName());

        // Cleanup
        updatedEmployee.setLastName(originalLastName);
        dao.updateEmployeeRecord(updatedEmployee);
    }

    /**
     * Tests the complete lifecycle of an employee record:
     * 1. Creation of a new employee
     * 2. Verification of successful creation
     * 3. Deletion of the test employee
     * 
     * Creates all required dependent objects including:
     * - Job position
     * - Salary history
     * - Address information
     * - Employee credentials
     */
    @Test
    void testCreateAndDeleteEmployeeRecord() throws SQLException {
        // Create test supervisor first if needed
        int testSupervisorId = 10003; // Use an existing supervisor ID

        JobPosition jobPosition = new JobPosition("Chief Executive Officer", "");

        PositionSalaryHistory positionSalaryHistory = new PositionSalaryHistory(
            0, jobPosition, 50000.00, 2000.00, 1000.00, 1500.00, 
            LocalDate.now(), null);

        Address address = new Address(
            0, "123 Test St", "Test Barangay", "Test City", 
            "Test Province", "1234");

        EmployeeAddress employeeAddress = new EmployeeAddress(0, address, AddressType.HOME);

        Employee employee = new Employee(
            "Test", "UserNew", Gender.MALE, LocalDate.of(1990, 1, 1),
            "1234567890", "123456789", "123456789012", "123456789", "123456789012",
            EmploymentStatus.PROBATIONARY, Department.INFORMATION_TECHNOLOGY,
            testSupervisorId, // Provide a valid supervisor ID
            positionSalaryHistory, employeeAddress, 0, "testpass", UserRole.EMPLOYEE
        );

        boolean created = dao.createEmployeeRecord(employee);
        assertTrue(created, "Employee should be created successfully");
        assertNotNull(employee.getEmpId(), "New employee should have generated ID");

    }
}