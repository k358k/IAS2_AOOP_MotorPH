//package Test.Service;
//
//import Model.*;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import java.time.*;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class HrUseCasesIT {
//    private static HrUseCases hrService;
//    private static final int TEST_EMPLOYEE_ID = 10001;
//
//    @BeforeAll
//    static void setUp() {
//        // Create a test employee to initialize the service
//        Employee testEmployee = createTestEmployee();
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
//        hrService = new HrService(employeeService);
//    }
//
//    private static Employee createTestEmployee() {
//        // Create test position
//        JobPosition position = new JobPosition(1, "HR Manager");
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
//        return new Employee(
//            "Test", "User", Gender.MALE, LocalDate.of(1990, 1, 1),
//            "1234567890", "123456789", "123456789012", "123456789", 
//            "123456789012", EmploymentStatus.PROBATIONARY, Department.HUMAN_RESOURCES,
//            null, salaryHistory, new EmployeeAddress(1, address, AddressType.HOME),
//            TEST_EMPLOYEE_ID, "testpass", UserRole.HR
//        );
//    }
//
//    @Test
//    void testGetEmployee10001() {
//        System.out.println("\n=== Testing employee retrieval ===");
//        Employee employee = hrService.getEmployeeById(TEST_EMPLOYEE_ID);
//        
//        System.out.println("Employee Details:");
//        System.out.println("ID: " + employee.getEmpId());
//        System.out.println("Name: " + employee.getFirstName() + " " + employee.getLastName());
//        System.out.println("Department: " + employee.getDepartment());
//        
//        assertNotNull(employee, "Employee should not be null");
//        assertEquals(TEST_EMPLOYEE_ID, employee.getEmpId(), "Employee ID should match");
//        assertNotNull(employee.getFirstName(), "Employee name should not be null");
//    }
//
//    @Test
//    void testGetAttendanceForEmployee10001() {
//        System.out.println("\n=== Testing attendance records ===");
//        LocalDate now = LocalDate.now();
//        LocalDate startDate = now.withDayOfMonth(1);
//        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
//        
//        System.out.println("Date range: " + startDate + " to " + endDate);
//        
//        List<ClockHour> attendance = hrService.getAttendanceByEmployeeAndPayPeriod(
//            TEST_EMPLOYEE_ID, startDate, endDate);
//            
//        System.out.println("Found " + attendance.size() + " records");
//        if (!attendance.isEmpty()) {
//            System.out.println("First record - Clock In: " + 
//                attendance.get(0).getClockInDateTime());
//        }
//        
//        assertNotNull(attendance, "Attendance list should not be null");
//    }
//
//    @Test
//    void testGetLeaveEntitlementsForEmployee10001() {
//        System.out.println("\n=== Testing leave entitlements ===");
//        List<LeaveEntitlement> entitlements = hrService.getLeaveEntitlementsByEmployeeId(TEST_EMPLOYEE_ID);
//        
//        System.out.println("Found " + entitlements.size() + " leave types");
//        entitlements.forEach(e -> System.out.println(
//            e.getLeaveType().getLeaveTypeId() + ": " + 
//            e.getRemainingDays() + "/" + e.getEntitlementDays() + " days"));
//        
//        assertNotNull(entitlements, "Leave entitlements should not be null");
//    }
//
//    @Test
//    void testGetLeaveRecordsForEmployee10001() {
//        System.out.println("\n=== Testing leave records ===");
//        List<LeaveRecord> leaveRecords = hrService.getLeaveRecordsByEmployeeId(TEST_EMPLOYEE_ID);
//        
//        System.out.println("Found " + leaveRecords.size() + " leave records");
//        if (!leaveRecords.isEmpty()) {
//            LeaveRecord first = leaveRecords.get(0);
//            System.out.println("First record: " + first.getAppliedStartDate() + 
//                " to " + first.getAppliedEndDate() + " (" + first.getStatus() + ")");
//        }
//        
//        assertNotNull(leaveRecords, "Leave records should not be null");
//    }
//
//    @Test
//    void testAddAndDeleteAttendance() {
//        System.out.println("\n=== Testing attendance creation ===");
//        // Create test record for current time
//        ClockHour testRecord = new ClockHour(
//            TEST_EMPLOYEE_ID,
//            LocalDateTime.now().minusHours(8),
//            LocalDateTime.now()
//        );
//        
//        // Add record
//        boolean added = hrService.addAttendance(testRecord);
//        System.out.println("Add operation: " + (added ? "SUCCESS" : "FAILED"));
//        assertTrue(added, "Should add attendance record");
//        
//        // In a real implementation, you would need to get the generated ID here
//        // For testing purposes, we'll skip deletion since we don't have the ID
//        System.out.println("Note: Delete test skipped - need generated record ID");
//    }
//}
//
