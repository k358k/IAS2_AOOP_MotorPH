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
public class LeaveMgmtDAOIT {

    private LeaveMgmtDAO leaveDAO;
    private final Integer testEmployeeId = 10001;

    @BeforeEach
    void setUp() {
        // Initialize the DAO implementation before each test
        leaveDAO = new LeaveMgmtImpl();
    }

    @Test
    void testFindLeaveEntitlementsByEmployeeIdAndYear() throws SQLException {
        System.out.println("Running testFindLeaveEntitlementsByEmployeeIdAndYear...");
        // Check if employee 10001 has leave entitlements for a given pay year
        PayPeriod period = new PayPeriod(LocalDate.now(), LocalDate.now());
        List<LeaveEntitlement> entitlements = leaveDAO.findLeaveEntitlementsByEmployeeIdAndYear(testEmployeeId, period);
        assertNotNull(entitlements);
        System.out.println("Entitlements for Employee ID: " + testEmployeeId + "for the Year: " +  period.getStartDate().getYear() + " : " + entitlements);
    }

    @Test
    void testFindAllLeaveRecords() throws SQLException {
        // Get all leave records in the system
        List<LeaveRecord> records = leaveDAO.findAllLeaveRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
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
        // Get approved leaves of employee 10001 in a certain pay period
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        PayPeriod period = new PayPeriod(startDate, endDate);
        List<LeaveRecord> approvedLeaves = leaveDAO.findApprovedLeavesByEmployeeAndPayPeriod(testEmployeeId, period);
        assertNotNull(approvedLeaves);
    }

}

