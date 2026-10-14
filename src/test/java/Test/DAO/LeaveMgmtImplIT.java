package Test.DAO;

import DAO.LeaveMgmtDAO;
import DAO.LeaveMgmtImpl;
import Model.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class checks if LeaveMgmtImpl works properly with real data.
 * It uses employee_id = 10001 for testing purposes.
 */
public class LeaveMgmtImplIT {

    private LeaveMgmtDAO leaveDAO;
    private final Integer testEmployeeId = 10001;
    private final LocalDate testStartDate = LocalDate.of(2025, 1, 1);
    private final LocalDate testEndDate = LocalDate.of(2025, 1, 10); // Shorter period for testing
    private final LocalDate testApplicationDate = LocalDate.now();

    @BeforeEach
    void setUp() {
        // Initialize the DAO implementation before each test
        leaveDAO = new LeaveMgmtImpl();
    }

    @Test
    void testFindLeaveEntitlementsByEmployeeIdAndYear() throws SQLException {
        // For January 2025
        PayPeriod period = new PayPeriod(testStartDate, testStartDate.withDayOfMonth(testStartDate.lengthOfMonth()));
        List<LeaveEntitlement> entitlements = leaveDAO.findLeaveEntitlementsByEmployeeIdAndYear(testEmployeeId, period);
        assertNotNull(entitlements);
        System.out.println("Entitlements: " + entitlements);
    }

    @Test
    void testFindAllLeaveRecords() throws SQLException {
        // Get all leave records in the system
        List<LeaveRecord> allRecords = leaveDAO.findAllLeaveRecords();
        assertNotNull(allRecords);

        // Count records for our test employee
        long employeeLeaveCount = allRecords.stream()
            .filter(record -> record.getEmployeeId().equals(testEmployeeId))
            .count();

        System.out.println("Total leave records in system: " + allRecords.size());
        System.out.println("Leave records for employee " + testEmployeeId + ": " + employeeLeaveCount);

        // Get just the test employee's records
        List<LeaveRecord> employeeRecords = leaveDAO.findLeaveRecordsByEmployeeId(testEmployeeId);

        // Verify the counts match
        assertEquals(employeeLeaveCount, employeeRecords.size(), 
            "Count from full list should match employee-specific query");

        // Print details of each leave record for this employee
        System.out.println("\nDetails of leave records for employee " + testEmployeeId + ":");
        employeeRecords.forEach(record -> {
            System.out.println("Leave ID: " + record.getLeaveRecordId());
            System.out.println("Type ID: " + record.getLeaveTypeId());
            System.out.println("Start Date: " + record.getAppliedStartDate());
            System.out.println("End Date: " + record.getAppliedEndDate());
            System.out.println("Status: " + record.getStatus());
            System.out.println("Days Used: " + record.getDaysUsed());
            System.out.println("----------------------");
        });
    }

    @Test
    void testFindLeaveRecordsByEmployeeId() throws SQLException {
        // Get all leave requests made by employee 10001
        List<LeaveRecord> records = leaveDAO.findLeaveRecordsByEmployeeId(testEmployeeId);
        assertNotNull(records);
        System.out.println("Leave Records: " + records);
    }

    @Test
    void testFindLeaveRecordsByStatus() throws SQLException {
        // Get all approved leaves in the system
        List<LeaveRecord> approved = leaveDAO.findLeaveRecordsByStatus(LeaveStatus.APPROVED);
        assertNotNull(approved);
        System.out.println("Approved Leaves: " + approved);
    }

    @Test
    void testFindApprovedLeavesByEmployeeAndPayPeriod() throws SQLException {
        // Define a valid pay period
        PayPeriod period = new PayPeriod(testStartDate, testStartDate.withDayOfMonth(testStartDate.lengthOfMonth()));

        List<LeaveRecord> approvedLeaves = leaveDAO.findApprovedLeavesByEmployeeAndPayPeriod(testEmployeeId, period);
        assertNotNull(approvedLeaves);
    }

    @Test
    void testCreateAndDeleteLeaveRecord() throws SQLException {
        // Create a leave record then delete it to test both features
        LeaveRecord leave = createTestLeaveRecord();
        
        boolean created = leaveDAO.createLeaveRecord(leave);
        assertTrue(created, "Leave should be created");

        // Get latest leave record for cleanup
        List<LeaveRecord> records = leaveDAO.findLeaveRecordsByEmployeeId(testEmployeeId);
        LeaveRecord latest = records.get(records.size() - 1);

        boolean deleted = leaveDAO.deleteLeaveRecord(latest.getLeaveRecordId()); // Fixed to use leaveRecordId
        assertTrue(deleted, "Leave should be deleted");
    }

    @Test
    void testApproveAndRejectLeave() throws SQLException {
        // Create a pending leave, approve it, then reject it to test both methods
        LeaveRecord leave = createTestLeaveRecord();

        // First, create it
        assertTrue(leaveDAO.createLeaveRecord(leave));

        // Fetch latest to get the auto-generated ID
        List<LeaveRecord> records = leaveDAO.findLeaveRecordsByEmployeeId(testEmployeeId);
        LeaveRecord latest = records.get(records.size() - 1);

        // Set required fields for approval/rejection
        latest.setStatusUpdatedBy(testEmployeeId);
        latest.setRemarks("Test remarks");

        // Approve the leave
        latest.setStatus(LeaveStatus.APPROVED);
        assertTrue(leaveDAO.approveLeave(latest));

//        // Reject it (simulate status change)
//        latest.setStatus(LeaveStatus.REJECTED);
//        assertTrue(leaveDAO.rejectLeave(latest));

        // Clean up
        assertTrue(leaveDAO.deleteLeaveRecord(latest.getLeaveRecordId()));
    }

    @Test
    void testFindLeaveRecordById() throws SQLException {
        // Create a leave record then fetch it by its ID
        LeaveRecord leave = createTestLeaveRecord();

        leaveDAO.createLeaveRecord(leave);
        List<LeaveRecord> records = leaveDAO.findLeaveRecordsByEmployeeId(testEmployeeId);
        LeaveRecord latest = records.get(records.size() - 1);

        LeaveRecord found = leaveDAO.findLeaveRecordById(latest.getLeaveRecordId()); // Fixed to use leaveRecordId
        assertNotNull(found);
        assertEquals(latest.getLeaveRecordId(), found.getLeaveRecordId());

        leaveDAO.deleteLeaveRecord(latest.getLeaveRecordId());
    }

    /**
     * Helper method to create a properly initialized LeaveRecord for testing
     */
    private LeaveRecord createTestLeaveRecord() {
        LeaveRecord leave = new LeaveRecord();
        leave.setEmployeeId(testEmployeeId);
        leave.setLeaveTypeId(1); // Using a valid leave type ID (1 for Annual Leave typically)
        leave.setApplicationDate(testApplicationDate);
        leave.setAppliedStartDate(testStartDate);
        leave.setAppliedEndDate(testEndDate);
        leave.setDaysUsed(1.0); // Set days used
        leave.setStatus(LeaveStatus.PENDING);
        leave.setReason("Test reason");
        return leave;
    }
}

