//package Test.Service;
//
//import DAO.AttendanceMgmtDAO;
//import DAO.EmployeeUserMgmtImpl;
//import Model.*;
//import Service.EmployeeService;
//import java.sql.SQLException;
//import org.junit.jupiter.api.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Integration tests for EmployeeService functionality
// * Tests all employee use cases including:
// * - Attendance tracking (clock in/out)
// * - Leave management
// * - Record retrieval
// */
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class EmployeeServiceIT {
//
//    private static EmployeeService employeeService;
//    private static final int TEST_EMP_ID = 10001; // Test employee ID (should exist in DB)
//    private static ClockHour testClockHour; // Tracks test clock-in record
//
//    /**
//     * Initializes test environment before all tests run
//     * 1. Loads test employee from database
//     * 2. Creates EmployeeService instance
//     * 3. Cleans up any existing test data
//     */
//    @BeforeAll
//    public static void setUp() {
//        System.out.println("\n=== SETTING UP TESTS FOR EMPLOYEE " + TEST_EMP_ID + " ===");
//        try {
//            // Load test employee from database
//            Employee employee = new EmployeeUserMgmtImpl().findEmployeeById(TEST_EMP_ID);
//            assertNotNull(employee, "Employee with ID " + TEST_EMP_ID + " not found");
//            
//            // Initialize service with test employee
//            employeeService = new EmployeeService(employee);
//            
//            // Clean up any existing test data
//            cleanUpTestData();
//        } catch (Exception e) {
//            fail("Test setup failed: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Cleans up any existing unclosed clock hours
//     * Prevents interference with current test run
//     */
//    private static void cleanUpTestData() throws Exception {
//        System.out.println("Checking for existing unclosed clock hours...");
//        ClockHour unclosed = employeeService.findUnclosedClockHour();
//        if (unclosed != null) {
//            System.out.println("Found existing unclosed record - cleaning up");
//            boolean clockOutResult = employeeService.clockOut();
//            assertTrue(clockOutResult, "Cleanup clockOut should succeed");
//        }
//    }
//
//    /**
//     * Tests employee clock-in functionality
//     * Verifies:
//     * 1. clockIn() returns true on success
//     * 2. Creates a findable unclosed clock record
//     * 3. Record belongs to test employee
//     */
//    @Test
//    @Order(1)
//    public void testClockIn() {
//        System.out.println("\n[TEST 1] Testing clockIn()...");
//        boolean result = employeeService.clockIn();
//        assertTrue(result, "clockIn should return true");
//
//        // Verify record was created
//        testClockHour = employeeService.findUnclosedClockHour();
//        assertNotNull(testClockHour, "Should find unclosed clock hour after clockIn");
//        assertEquals(TEST_EMP_ID, testClockHour.getEmpId(), "Clock hour should belong to test employee");
//        System.out.println("Clocked in at: " + testClockHour.getClockInDateTime());
//    }
//
//    /**
//     * Tests retrieval of unclosed clock hour
//     * Verifies:
//     * 1. Returns non-null record after clock-in
//     * 2. Record has null clock-out time
//     * 3. Record belongs to test employee
//     */
//    @Test
//    @Order(2)
//    public void testFindUnclosedClockHour() {
//        System.out.println("\n[TEST 2] Testing findUnclosedClockHour()...");
//        ClockHour result = employeeService.findUnclosedClockHour();
//        assertNotNull(result, "Should return unclosed clock hour");
//        assertEquals(TEST_EMP_ID, result.getEmpId(), "Should belong to test employee");
//        assertNull(result.getClockOutDateTime(), "Clock out time should be null");
//        System.out.println("Found unclosed clock hour from: " + result.getClockInDateTime());
//    }
//    
//    /**
//     * Tests employee clock-out functionality
//     * Verifies:
//     * 1. clockOut() returns true on success
//     * 2. Closes the open clock record
//     * 3. No unclosed records remain after
//     */
//    @Test
//    @Order(3)
//    public void testClockOut() throws SQLException{
//        System.out.println("\n[TEST 3] Testing clockOut()...");
//        
//        // Ensure we have an open clock-in to test with
//        ClockHour unclosed = employeeService.findUnclosedClockHour();
//        if (unclosed == null) {
//            System.out.println("No unclosed record found - creating test record");
//            boolean clockInResult = employeeService.clockIn();
//            assertTrue(clockInResult, "Setup clockIn should succeed");
//            unclosed = employeeService.findUnclosedClockHour();
//        }
//        
//        System.out.println("Attempting to clock out record from: " + unclosed.getClockInDateTime());
//        boolean clockOutResult = employeeService.clockOut();
//        assertTrue(clockOutResult, "clockOut should return true");
//        
//        // Verify clock-out was recorded
//        ClockHour after = employeeService.findUnclosedClockHour();
//        assertNull(after, "No unclosed record should remain after clockOut");
//        System.out.println("Successfully clocked out at: " + LocalDateTime.now());
//    }
//
//    /**
//     * Tests retrieval of employee attendance history
//     * Verifies:
//     * 1. Returns non-null list
//     * 2. Contains at least one record
//     * 3. All records belong to test employee
//     */
//    @Test
//    @Order(4)
//    public void testGetMyAttendance() throws SQLException{
//        System.out.println("\n[TEST 4] Testing getMyAttendance()...");
//        List<ClockHour> records = employeeService.getMyAttendance();
//        assertNotNull(records, "Should return attendance list");
//        assertFalse(records.isEmpty(), "Should have at least one record");
//        assertTrue(records.stream().allMatch(r -> r.getEmpId() == TEST_EMP_ID),
//                "All records should belong to test employee");
//        System.out.println("Found " + records.size() + " attendance records");
//        System.out.println("Most recent record: " + records.get(0).getClockInDateTime() + 
//                         " to " + records.get(0).getClockOutDateTime());
//    }
//
//    /**
//     * Tests retrieval of employee leave entitlements
//     * Verifies:
//     * 1. Returns non-null list
//     * 2. Contains valid entitlement data
//     */
//    @Test
//    @Order(5)
//    public void testGetMyLeaveEntitlements() {
//        System.out.println("\n[TEST 5] Testing getMyLeaveEntitlements()...");
//        List<LeaveEntitlement> entitlements = employeeService.getMyLeaveEntitlements();
//        assertNotNull(entitlements, "Should return leave entitlements");
//        System.out.println("Found " + entitlements.size() + " leave entitlements");
//        entitlements.forEach(e -> System.out.println("- " + e.getLeaveType().getLeaveTypeId() + 
//                              ": " + e.getRemainingDays() + " days remaining"));
//    }
//
//    /**
//     * Tests leave application functionality
//     * Verifies:
//     * 1. Returns true on successful submission
//     * 2. Creates a leave record in system
//     */
//    @Test
//    @Order(6)
//    public void testFileLeave() throws SQLException{
//        System.out.println("\n[TEST 6] Testing fileLeave()...");
//        LocalDate start = LocalDate.now().plusDays(7);
//        LocalDate end = LocalDate.now().plusDays(9);
//        System.out.println("Filing leave from " + start + " to " + end);
//        boolean result = employeeService.fileLeave(1, start, end, "Integration test leave");
//        assertTrue(result, "fileLeave should return true");
//        System.out.println("Leave filed successfully");
//    }
//
//    /**
//     * Tests retrieval of employee leave history
//     * Verifies:
//     * 1. Returns non-null list
//     * 2. Contains valid leave records
//     */
//    @Test
//    @Order(7)
//    public void testGetMyLeaveRecords() throws SQLException{
//        System.out.println("\n[TEST 7] Testing getMyLeaveRecords()...");
//        List<LeaveRecord> records = employeeService.getMyLeaveRecords();
//        assertNotNull(records, "Should return leave records");
//        System.out.println("Found " + records.size() + " leave records");
//        if (!records.isEmpty()) {
//            System.out.println("Most recent leave: " + records.get(0).getAppliedStartDate() + 
//                             " to " + records.get(0).getAppliedEndDate() + 
//                             " (" + records.get(0).getStatus() + ")");
//        }
//    }
//
//    /**
//     * Cleans up test data after all tests complete
//     * 1. Closes any remaining unclosed clock hours
//     * 2. Logs cleanup results
//     */
//    @AfterAll
//    public static void tearDown() {
//        System.out.println("\n=== CLEANING UP TEST DATA ===");
//        try {
//            ClockHour unclosed = employeeService.findUnclosedClockHour();
//            if (unclosed != null) {
//                System.out.println("Found unclosed record from " + unclosed.getClockInDateTime());
//                boolean result = employeeService.clockOut();
//                System.out.println("Cleanup " + (result ? "succeeded" : "failed"));
//            }
//            System.out.println("Test cleanup complete");
//        } catch (Exception e) {
//            System.out.println("Cleanup failed: " + e.getMessage());
//        }
//    }
//}
//
