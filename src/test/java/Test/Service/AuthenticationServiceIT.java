package Test.Service;

import Service.AuthenticationService;
import Model.Employee;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceIT {

    // This object is responsible for handling login/authentication logic
    AuthenticationService authService;

    /**
     * This method runs before each test.
     * It's used to initialize the AuthenticationService so it's ready to test.
     */
    @BeforeEach
    void setUp() {
        System.out.println("Setting up AuthenticationService...");
        authService = new AuthenticationService();
    }

    /**
     * This test checks if a real user can successfully log in
     * using the correct employee ID and password.
     */
    @Test
    void testAuthenticationWithValidCredentials() {
        System.out.println("Running test: testAuthenticationWithValidCredentials");

        int testEmpId = 10002;          // Example: Real employee ID
        String testPassword = "def";    // Actual password stored in the database

        // Try to log in with valid credentials
        boolean isAuthenticated = authService.authenticate(testEmpId, testPassword);

        // Print the result to the console
        System.out.println("Authentication result for valid credentials: " + isAuthenticated);

        // Check if the result is true (login successful)
        assertTrue(isAuthenticated, "Authentication should succeed with correct credentials.");
    }

    /**
     * This test checks that login fails when the correct ID is used
     * but the password is wrong.
     */
    @Test
    void testAuthenticationWithInvalidPassword() {
        System.out.println("Running test: testAuthenticationWithInvalidPassword");

        int testEmpId = 10002;               // Still using a real user ID
        String wrongPassword = "wrongPassword"; // Wrong password

        // Try to log in with a wrong password
        boolean isAuthenticated = authService.authenticate(testEmpId, wrongPassword);

        // Show result
        System.out.println("Authentication result for invalid password: " + isAuthenticated);

        // Test should expect false (login should fail)
        assertFalse(isAuthenticated, "Authentication should fail with incorrect password.");
    }

    /**
     * This test checks that login fails when using a fake or non-existent user ID.
     */
    @Test
    void testAuthenticationWithNonExistentUser() {
        System.out.println("Running test: testAuthenticationWithNonExistentUser");

        int fakeEmpId = 99999;               // This employee ID does not exist
        String anyPassword = "password123";  // Any password

        // Try to log in with non-existent user
        boolean isAuthenticated = authService.authenticate(fakeEmpId, anyPassword);

        // Print result
        System.out.println("Authentication result for non-existent user: " + isAuthenticated);

        // Should be false since the employee doesn't exist
        assertFalse(isAuthenticated, "Authentication should fail for non-existent user.");
    }

    /**
     * This test checks if user data (like name, ID, password) can be
     * properly loaded from the database using the employee ID.
     */
    @Test
    void testLoadUserDataFromDatabase() {
        System.out.println("Running test: testLoadUserDataFromDatabase");

        int testEmpId = 10002;   // Real employee ID you want to test

        // Load user info from the database
        Employee emp = authService.loadUserDatafromDatabase(testEmpId);

        // Print all the important data retrieved for confirmation
        System.out.println("Loaded Employee Data:");
        System.out.println("Employee ID: " + emp.getEmpId());
        System.out.println("First Name: " + emp.getFirstName());
        System.out.println("Last Name: " + emp.getLastName());
        System.out.println("Password: " + emp.getPassword());

        // Make sure the user data is not null
        assertNotNull(emp, "Employee should be found in the database.");
        assertEquals(testEmpId, emp.getEmpId(), "Employee ID should match.");
        assertNotNull(emp.getPassword(), "Employee should have a password.");
    }
}

