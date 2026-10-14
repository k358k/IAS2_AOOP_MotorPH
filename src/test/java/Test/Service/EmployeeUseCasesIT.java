package Test.Service;

import DAO.EmployeeUserMgmtImpl;
import DAO.AttendanceMgmtImpl;
import DAO.LeaveMgmtImpl;
import Model.*;
import Service.EmployeeService;
import Service.EmployeeUseCases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeUseCasesIT {

    EmployeeUseCases employeeService;
    static final int EMPLOYEE_ID = 10001;

    @BeforeEach
    public void setUp() throws Exception {
        // Get real employee data from the database
        EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();
        Employee employee = employeeDAO.findEmployeeById(EMPLOYEE_ID);

        assertNotNull(employee, "Employee with ID 10001 should exist.");

        // Create an instance of EmployeeService using real data
        employeeService = new EmployeeService(
            employee.getFirstName(),
            employee.getLastName(),
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

        // Inject the actual DAO implementations via reflection
        setPrivateField(employeeService, "employeeDAO", employeeDAO);
        setPrivateField(employeeService, "attendanceDAO", new AttendanceMgmtImpl());
        setPrivateField(employeeService, "leaveDAO", new LeaveMgmtImpl());
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // Test clockIn() - Records current time as start of work
    @Test
    public void testClockIn() throws SQLException{
        boolean result = employeeService.clockIn();
        System.out.println("Clock-in result for employee 10001: " + result);
        assertTrue(result, "Clock-in should return true if successful.");
    }

    // Test clockOut() - Records current time as end of work
    @Test
    public void testClockOut() throws SQLException{
        boolean result = employeeService.clockOut();
        System.out.println("Clock-out result for employee 10001: " + result);
        assertTrue(result, "Clock-out should return true if successful.");
    }

    // Test findUnclosedClockHour() - Check if there is a clock-in with no clock-out
    @Test
    public void testFindUnclosedClockHour() throws SQLException{
        ClockHour result = employeeService.findUnclosedClockHour();
        if (result == null) {
            System.out.println("No unclosed clock hour found for employee 10001.");
        } else {
            System.out.println("Unclosed clock-in found: " + result.getClockInDateTime());
            assertNotNull(result.getClockInDateTime(), "Clock-in time should not be null.");
        }
    }

    // Test getMyAttendance() - List of all clock-in/out records
    @Test
    public void testGetMyAttendance() throws SQLException{
        List<ClockHour> result = employeeService.getMyAttendance();
        assertNotNull(result, "Attendance list should not be null.");
        System.out.println("Attendance records for employee 10001:");
        for (ClockHour hour : result) {
            System.out.println("In: " + hour.getClockInDateTime() + " | Out: " + hour.getClockOutDateTime());
        }
    }

    // Test getMyLeaveEntitlements() - How many leave days are available
    @Test
    public void testGetMyLeaveEntitlements() throws SQLException{
        List<LeaveEntitlement> entitlements = employeeService.getMyLeaveEntitlements();
        assertNotNull(entitlements, "Leave entitlements list should not be null.");
        System.out.println("Leave entitlements for employee 10001:");
        for (LeaveEntitlement ent : entitlements) {
            System.out.println(" | Entitled Days: " + ent.getEntitlementDays());
        }
    }

    // Test fileLeave() - Submit a leave request for future dates
    @Test
    public void testFileLeave() throws SQLException{
        int leaveTypeId = 1; // Example: Vacation Leave
        LocalDate start = LocalDate.now().plusDays(3);
        LocalDate end = LocalDate.now().plusDays(5);
        String reason = "Test Leave Filing";

        boolean result = employeeService.fileLeave(leaveTypeId, start, end, reason);
        System.out.println("File leave result: " + result);
        assertTrue(result, "Leave filing should return true if successful.");
    }

    // Test getMyLeaveRecords() - List of past filed leaves
    @Test
    public void testGetMyLeaveRecords() throws SQLException{
        List<LeaveRecord> records = employeeService.getMyLeaveRecords();
        assertNotNull(records, "Leave records list should not be null.");
        System.out.println("Leave records for employee 10001:");
    }
}

