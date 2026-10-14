//package Test.Service;
//
//import Model.*;
//import Service.EmployeeService;
//import Service.HrService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import java.time.LocalDate;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Integration tests for HrService functionality
// */
//public class HrServiceIT {
//    static HrService hrService;
//    private static final int TEST_EMPLOYEE_ID = 10001; // Existing test employee ID
//
//    /**
//     * Initializes test environment before all tests
//     * Creates HrService with a test employee
//     */
//    @BeforeAll
//    public static void setUp() throws Exception {
//        System.out.println("\n=== Initializing HrService Test Environment ===");
//        
//        // Create a test employee service
//        EmployeeService testEmployeeService = createTestEmployeeService();
//        
//        // Initialize HrService with the test employee
//        hrService = new HrService(testEmployeeService);
//        System.out.println("HrService initialized successfully with test employee");
//    }
//
//    /**
//     * Creates a test EmployeeService instance with sample data
//     */
//    private static EmployeeService createTestEmployeeService() {
//        System.out.println("Creating test employee data...");
//        
//        // Create test position
//        JobPosition position = new JobPosition(1, "INFORMATION TECHNOLOGY");
//        
//        // Create test salary history
//        PositionSalaryHistory salaryHistory = new PositionSalaryHistory(
//            1, position, 50000.0, 2000.0, 1000.0, 1500.0, 
//            LocalDate.now(), null
//        );
//        
//        // Create test address
//        Address address = new Address(
//            1, "123 Test St", "Test Barangay", 
//            "Test City", "Test Province", "1234"
//        );
//        
//        // Create test employee
//        Employee employee = new Employee(
//            "Test", "User", Gender.MALE, LocalDate.of(1990, 1, 1),
//            "1234567890", "123456789", "123456789012", "123456789", 
//            "123456789012", EmploymentStatus.PROBATIONARY, Department.INFORMATION_TECHNOLOGY,
//            null, salaryHistory, new EmployeeAddress(1, address, AddressType.HOME),
//            TEST_EMPLOYEE_ID, "testpass", UserRole.HR
//        );
//        
//        return new EmployeeService(
//            employee.getFirstName(), employee.getLastName(),
//            employee.getGender(), employee.getBirthday(),
//            employee.getPhoneNumber(), employee.getSssNumber(),
//            employee.getPhilhealthNumber(), employee.getTinNumber(),
//            employee.getPagibigNumber(), employee.getEmpStatus(),
//            employee.getDepartment(), employee.getSupervisorId(),
//            employee.getPositionSalaryHistory(), employee.getEmployeeAddress(),
//            employee.getEmpId(), employee.getPassword(), employee.getUserRole()
//        );
//    }
//
//    /**
//     * Tests retrieval of employee by ID
//     * Verifies:
//     * 1. Employee object is returned (not null)
//     * 2. Retrieved employee has matching ID
//     */
//    @Test
//    public void testGetEmployeeById() {
//        System.out.println("Retrieving employee by ID...");
//        System.out.println("Looking for employee ID: " + TEST_EMPLOYEE_ID);
//        
//        Employee employee = hrService.getEmployeeById(TEST_EMPLOYEE_ID);
//        
//        System.out.println("Found employee: " + 
//            (employee != null ? employee.getFirstName() + " " + employee.getLastName() : "null"));
//        
//        assertNotNull(employee, "Employee should not be null");
//        assertEquals(TEST_EMPLOYEE_ID, employee.getEmpId(), 
//            "Employee ID should match requested ID");
//        System.out.println("Test passed - Employee retrieved successfully");
//    }
//
//    /**
//     * Tests retrieval of leave records for an employee
//     * Verifies:
//     * 1. List is returned (not null)
//     * 2. Contains valid leave records
//     */
//    @Test
//    public void testGetLeaveRecordsByEmployeeId() {
//        System.out.println("Retrieving leave records...");
//        System.out.println("For employee ID: " + TEST_EMPLOYEE_ID);
//        
//        List<LeaveRecord> leaveRecords = hrService.getLeaveRecordsByEmployeeId(TEST_EMPLOYEE_ID);
//        
//        System.out.println("Found " + leaveRecords.size() + " leave records");
//        if (!leaveRecords.isEmpty()) {
//            System.out.println("Sample leave record: " + 
//                leaveRecords.get(0).getAppliedStartDate() + " to " + 
//                leaveRecords.get(0).getAppliedEndDate());
//        }
//        
//        assertNotNull(leaveRecords, "Leave records list should not be null");
//        System.out.println("Test passed - Leave records retrieved");
//    }
//
//    /**
//     * Tests retrieval of attendance records for pay period
//     * Verifies:
//     * 1. List is returned (not null)
//     * 2. Contains valid attendance records
//     */
//    @Test
//    public void testGetAttendanceByEmployeeAndPayPeriod() {
//        System.out.println("Retrieving attendance records...");
//        LocalDate start = LocalDate.now().withDayOfMonth(1);
//        LocalDate end = start.plusDays(14);
//        System.out.println("Pay period: " + start + " to " + end);
//        
//        List<ClockHour> attendanceList = hrService.getAttendanceByEmployeeAndPayPeriod(
//            TEST_EMPLOYEE_ID, start, end);
//        
//        System.out.println("Found " + attendanceList.size() + " attendance records");
//        if (!attendanceList.isEmpty()) {
//            System.out.println("Sample record - Clock In: " + 
//                attendanceList.get(0).getClockInDateTime());
//        }
//        
//        assertNotNull(attendanceList, "Attendance list should not be null");
//        System.out.println("Test passed - Attendance records retrieved");
//    }
//
//    /**
//     * Tests retrieval of all employees
//     * Verifies:
//     * 1. List is returned (not null)
//     * 2. Contains at least one employee
//     */
//    @Test
//    public void testGetAllEmployees() {
//        System.out.println("Retrieving all employees...");
//        
//        List<Employee> employees = hrService.getAllEmployees();
//        
//        System.out.println("Total employees found: " + employees.size());
//        if (!employees.isEmpty()) {
//            System.out.println("First employee: " + 
//                employees.get(0).getFirstName() + " " + 
//                employees.get(0).getLastName());
//        }
//        
//        assertNotNull(employees, "Employee list should not be null");
//        assertFalse(employees.isEmpty(), "There should be at least one employee");
//        System.out.println("Test passed - Employee list retrieved");
//    }
//
//    /**
//     * Tests retrieval of leave entitlements
//     * Verifies:
//     * 1. List is returned (not null)
//     * 2. Contains valid entitlements
//     */
//    @Test
//    public void testGetLeaveEntitlementsByEmployeeId() {
//        System.out.println("Retrieving leave entitlements...");
//        System.out.println("For employee ID: " + TEST_EMPLOYEE_ID);
//        
//        List<LeaveEntitlement> entitlements = hrService.getLeaveEntitlementsByEmployeeId(TEST_EMPLOYEE_ID);
//        
//        System.out.println("Found " + entitlements.size() + " leave entitlements");
//        if (!entitlements.isEmpty()) {
//            System.out.println("Sample entitlement: " + 
//                entitlements.get(0).getLeaveType().getLeaveTypeId()+ 
//                " - " + entitlements.get(0).getRemainingDays() + " days remaining");
//        }
//        
//        assertNotNull(entitlements, "Leave entitlements list should not be null");
//        System.out.println("Test passed - Leave entitlements retrieved");
//    }
//}
//
