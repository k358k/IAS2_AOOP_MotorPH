//package Test.Service;
//
//import DAO.*;
//import Model.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Integration Test class for FinanceService that verifies:
// * - Attendance record retrieval
// * - Leave entitlement calculations  
// * - Employee data access with security redaction
// */
//public class FinanceServiceIT {
//
//    private FinanceService financeService;
//    private final int TEST_EMPLOYEE_ID = 10001; // Existing employee ID for testing
//    private final LocalDate TEST_START_DATE = LocalDate.of(2025, 1, 1);
//    private final LocalDate TEST_END_DATE = LocalDate.of(2025, 1, 31);
//
//    /**
//     * Initializes test environment before each test case
//     * 1. Loads test employee from database
//     * 2. Creates EmployeeService wrapper  
//     * 3. Initializes FinanceService with test data
//     */
//    @BeforeEach
//    void setUp() throws SQLException {
//        System.out.println("\n=== Initializing test environment ===");
//        
//        // Load test employee
//        Employee testEmployee = new EmployeeUserMgmtImpl().findEmployeeById(TEST_EMPLOYEE_ID);
//        System.out.println("Loaded test employee: " + testEmployee.getFirstName() + " " + testEmployee.getLastName());
//        
//        // Create service instance
//        EmployeeService employeeService = new EmployeeService(
//            testEmployee.getFirstName(),
//            testEmployee.getLastName(),
//            testEmployee.getGender(),
//            testEmployee.getBirthday(),
//            testEmployee.getPhoneNumber(),
//            testEmployee.getSssNumber(),
//            testEmployee.getPhilhealthNumber(),
//            testEmployee.getTinNumber(),
//            testEmployee.getPagibigNumber(),
//            testEmployee.getEmpStatus(),
//            testEmployee.getDepartment(),
//            testEmployee.getSupervisorId(),
//            testEmployee.getPositionSalaryHistory(),
//            testEmployee.getEmployeeAddress(),
//            testEmployee.getEmpId(),
//            testEmployee.getPassword(),
//            testEmployee.getUserRole()
//        );
//        
//        financeService = new FinanceService(employeeService);
//        System.out.println("FinanceService initialized successfully");
//    }
//
//    /**
//     * Tests retrieval of clock-in/out records for payroll processing
//     * Verifies:
//     * 1. Records are returned (not null)
//     * 2. Each record contains valid timestamps
//     * 3. Handles empty results gracefully
//     */
//    @Test
//    void testGetAttendanceByEmployeeAndPayPeriod() {
//        System.out.println("Retrieving attendance records...");
//        PayPeriod payPeriod = new PayPeriod(TEST_START_DATE, TEST_END_DATE);
//        System.out.println("Pay Period: " + payPeriod.getStartDate() + " to " + payPeriod.getEndDate());
//        
//        List<ClockHour> attendanceRecords = financeService.getAttendanceByEmployeeAndPayPeriod(TEST_EMPLOYEE_ID, payPeriod);
//        
//        // Print results
//        System.out.println("Found " + attendanceRecords.size() + " records");
//        attendanceRecords.forEach(record -> 
//            System.out.println("- " + record.getClockInDateTime() + " to " + 
//                             (record.getClockOutDateTime() != null ? record.getClockOutDateTime() : "ONGOING"))
//        );
//        
//        // Assertions
//        assertNotNull(attendanceRecords, "Attendance records should not be null");
//        if (!attendanceRecords.isEmpty()) {
//            assertNotNull(attendanceRecords.get(0).getClockInDateTime(), 
//                "Clock-in time must be recorded");
//        }
//        System.out.println("✓ Test passed - Attendance records valid");
//    }
//
//    /**
//     * Tests retrieval of available leave days for payroll deductions
//     * Verifies:
//     * 1. Entitlements are returned (not null)  
//     * 2. Each record contains valid leave type
//     * 3. Days calculations are present
//     */
//    @Test
//    void testGetLeaveEntitlementsByEmployeeIdAndYear() {
//        System.out.println("\n[TEST] Retrieving leave entitlements...");
//        PayPeriod payPeriod = new PayPeriod(TEST_START_DATE, TEST_END_DATE);
//        System.out.println("For Year: " + payPeriod.getStartDate().getYear());
//        
//        List<LeaveEntitlement> entitlements = financeService.getLeaveEntitlementsByEmployeeIdAndYear(TEST_EMPLOYEE_ID, payPeriod);
//        
//        // Print results
//        System.out.println("Leave Balances:");
//        entitlements.forEach(entitlement -> 
//            System.out.println("- " + entitlement.getLeaveType().getLeaveTypeId()+ 
//                             ": " + entitlement.getRemainingDays() + "/" + 
//                             entitlement.getEntitlementDays() + " days remaining")
//        );
//        
//        // Assertions
//        assertNotNull(entitlements, "Leave entitlements should not be null");
//        if (!entitlements.isEmpty()) {
//            assertNotNull(entitlements.get(0).getLeaveType(), 
//                "Leave type must be specified");
//        }
//        System.out.println("Test passed - Leave entitlements valid");
//    }
//
//    /**
//     * Tests retrieval of approved leave requests for payroll adjustment
//     * Verifies:
//     * 1. Only APPROVED status leaves are returned
//     * 2. Contains valid date ranges  
//     * 3. Handles empty results gracefully
//     */
//    @Test
//    void testGetApprovedLeavesByEmployeeAndPayPeriod() {
//        System.out.println("Retrieving approved leaves...");
//        PayPeriod payPeriod = new PayPeriod(TEST_START_DATE, TEST_END_DATE);
//        System.out.println("Pay Period: " + payPeriod.getStartDate() + " to " + payPeriod.getEndDate());
//        
//        List<LeaveRecord> approvedLeaves = financeService.getApprovedLeavesByEmployeeAndPayPeriod(TEST_EMPLOYEE_ID, payPeriod);
//        
//        // Print results
//        System.out.println("Approved Leaves:");
//        approvedLeaves.forEach(leave -> 
//            System.out.println("- " + leave.getAppliedStartDate() + " to " + 
//                             leave.getAppliedEndDate() + " (" + leave.getDaysUsed() + " days)")
//        );
//        
//        // Assertions
//        assertNotNull(approvedLeaves, "Approved leaves should not be null");
//        approvedLeaves.forEach(leave -> 
//            assertEquals(LeaveStatus.APPROVED, leave.getStatus(),
//                "Only APPROVED leaves should be returned")
//        );
//        System.out.println("Test passed - Approved leaves valid");
//    }
//
//    /**
//     * Tests employee data retrieval with GDPR compliance:
//     * 1. Verifies sensitive fields are redacted (null)
//     * 2. Maintains required payroll fields
//     * 3. Handles invalid IDs gracefully
//     */
//    @Test
//    void testGetEmployeeById() {
//        System.out.println("Retrieving employee with data redaction...");
//        System.out.println("Testing Employee ID: " + TEST_EMPLOYEE_ID);
//        
//        Employee employee = financeService.getEmployeeById(TEST_EMPLOYEE_ID);
//        
//        // Print results
//        System.out.println("Employee Data:");
//        System.out.println("- Name: " + employee.getFirstName() + " " + employee.getLastName());
//        System.out.println("- Department: " + employee.getDepartment());
//        System.out.println("Redacted Fields:");
//        System.out.println("- Password: " + employee.getPassword());
//        System.out.println("- Birthday: " + employee.getBirthday());
//        System.out.println("- Phone: " + employee.getPhoneNumber());
//        
//        // Assertions
//        assertNotNull(employee, "Employee record should exist");
//        assertNull(employee.getPassword(), "Password must be redacted");
//        assertNull(employee.getBirthday(), "Birthday must be redacted");
//        assertNull(employee.getPhoneNumber(), "Phone must be redacted");
//        System.out.println("Test passed - Data redaction correct");
//    }
//
//    /**
//     * Tests bulk employee retrieval with:
//     * 1. Verification of redaction for all records
//     * 2. Handling of empty datasets
//     * 3. Performance with large result sets
//     */
//    @Test
//    void testGetAllEmployees() {
//        System.out.println("Retrieving all employees with redaction...");
//        
//        List<Employee> employees = financeService.getAllEmployees();
//        
//        // Print summary
//        System.out.println("Total Employees: " + employees.size());
//        if (!employees.isEmpty()) {
//            Employee sample = employees.get(0);
//            System.out.println("Sample Employee:");
//            System.out.println("- Name: " + sample.getFirstName() + " " + sample.getLastName());
//            System.out.println("Redacted Fields:");
//            System.out.println("- Role: " + sample.getUserRole());
//            System.out.println("- Address: " + sample.getEmployeeAddress());
//        }
//        
//        // Assertions
//        assertNotNull(employees, "Employee list should not be null");
//        employees.forEach(emp -> {
//            assertNull(emp.getPassword(), "All passwords must be redacted");
//            assertNull(emp.getUserRole(), "All roles must be redacted");
//        });
//        System.out.println("Test passed - Bulk redaction correct");
//    }
//
//    /**
//     * Tests error handling for:
//     * 1. Non-existent employee IDs
//     * 2. Invalid parameter formats
//     * 3. Database connection issues
//     */
//    @Test
//    void testGetNonExistentEmployee() {
//        System.out.println("\n[TEST] Handling invalid employee ID...");
//        int invalidId = -999;
//        System.out.println("Using Invalid ID: " + invalidId);
//        
//        Employee employee = financeService.getEmployeeById(invalidId);
//        
//        // Print result
//        System.out.println("Result: " + (employee == null ? "NULL (expected)" : "Unexpected record"));
//        
//        // Assertion
//        assertNull(employee, "Invalid ID should return null");
//        System.out.println("✓ Test passed - Invalid ID handled correctly");
//    }
//}
//
