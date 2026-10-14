package Test.DAO;

import DAO.EmployeeUserMgmtImpl;
import Model.*;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeUserMgmtImplIT {

    // DAO instance to be tested
    private static EmployeeUserMgmtImpl dao;

    /**
     * Setup method runs once before all tests
     * Initializes the DAO instance with database connection
     */
    @BeforeAll
    static void setup() {
        dao = new EmployeeUserMgmtImpl();
        System.out.println("DAO initialized.");
    }

    /**
     * Tests finding an employee by their ID
     * Verifies that all employee data fields are properly retrieved
     * and not null where expected
     */
    @Test
    void testFindEmployeeById() throws SQLException { // ***** failed ******
        // Test with known employee ID
        int id = 10001;
        
        // Retrieve employee from database
        Employee emp = dao.findEmployeeById(id);
        
        // Print employee details for verification
        System.out.println("Employee Data:");
        System.out.println("ID: " + emp.getEmpId());
        System.out.println("Name: " + emp.getFirstName() + " " + emp.getLastName());
        System.out.println("Gender: " + emp.getGender());
        System.out.println("Birth Date: " + emp.getBirthday());
        System.out.println("Contact: " + emp.getPhoneNumber());
        System.out.println("SSS: " + emp.getSssNumber());
        System.out.println("PhilHealth: " + emp.getPhilhealthNumber());
        System.out.println("TIN: " + emp.getTinNumber());
        System.out.println("HDMF: " + emp.getPagibigNumber());
        System.out.println("Status: " + emp.getEmpStatus());
        System.out.println("Department: " + emp.getDepartment());
        System.out.println("User Role: " + emp.getUserRole()); 
        System.out.println("Password: " + emp.getPassword());

        // Verify position/salary history exists and print details
        PositionSalaryHistory posHist = emp.getPositionSalaryHistory();
        assertNotNull(posHist);
        System.out.println("Salary: " + posHist.getBasicSalary());

        // Verify address exists and print details
        EmployeeAddress address = emp.getEmployeeAddress();
        assertNotNull(address);
        System.out.println("Address: " + 
            address.getAddressRecord().getBuildingStreet() + ", " + 
            address.getAddressRecord().getCityMunicipality() + ", " + 
            address.getAddressRecord().getProvince());

        // Assertions to verify data integrity
        assertEquals(id, emp.getEmpId());
        assertNotNull(emp.getFirstName());
        assertNotNull(emp.getPassword());
    }

    /**
     * Tests retrieving all employees from the database
     * Verifies the list is not null and not empty
     * Prints basic info for each employee
     */
    @Test
    void testFindAllEmployee() throws SQLException {
        // Retrieve all employees
        List<Employee> list = dao.findAllEmployee();
        
        // Print count and basic info
        System.out.println("Total employees found: " + list.size());
        for (Employee e : list) {
            System.out.println(e.getEmpId() + " - " + e.getFirstName() + " " + e.getLastName());
        }

        // Verify we got results
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    /**
     * Tests updating an employee's last name
     * 1. Gets original last name
     * 2. Updates with temporary value
     * 3. Verifies update was successful
     * 4. Restores original value (cleanup)
     */
    @Test
    void testUpdateEmployeeLastName() throws SQLException {
        // Use known employee ID
        int id = 10001;
        
        // Get current employee record
        Employee emp = dao.findEmployeeById(id);
        assertNotNull(emp);

        // Store original last name for restoration
        String oldLast = emp.getLastName();
        
        // Create unique temporary last name
        String tempLast = "Updated_" + System.currentTimeMillis();
        emp.setLastName(tempLast);

        // Perform update and verify success
        boolean success = dao.updateEmployeeRecord(emp);
        System.out.println("Updated Last Name to: " + tempLast);
        assertTrue(success);

        // Cleanup: restore original last name
        emp.setLastName(oldLast);
        dao.updateEmployeeRecord(emp);
    }

    /**
     * Tests updating a user's password
     * 1. Gets current password
     * 2. Updates with temporary password
     * 3. Verifies update was successful
     * 4. Restores original password (cleanup)
     */
    @Test
    void testUpdateUserPassword() throws SQLException {
        // Use known employee ID
        int id = 10001;
        
        // Get current employee record
        Employee emp = dao.findEmployeeById(id);
        assertNotNull(emp);

        // Store original password for restoration
        String oldPass = emp.getPassword();
        
        // Create unique temporary password
        String newPass = "pw_" + System.currentTimeMillis();

        // Create UserCredential object with required parameters
        UserCredential uc = new UserCredential(
            id,                // empId
            newPass,           // password
            emp.getUserRole()  // userRole
        );

        // Perform update and verify success
        boolean updated = dao.updateUserCredential(uc);
        System.out.println("Updated password for ID " + id);
        assertTrue(updated);

        // Cleanup: restore original password
        uc = new UserCredential(
            id,
            oldPass,
            emp.getUserRole()
        );
        dao.updateUserCredential(uc);
    }
}

