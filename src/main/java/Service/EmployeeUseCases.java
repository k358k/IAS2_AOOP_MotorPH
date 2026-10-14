package Service;

import Model.*;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

public interface EmployeeUseCases {

    boolean clockIn() throws SQLException;

    boolean clockOut() throws SQLException;

    ClockHour findUnclosedClockHour() throws SQLException;

    List<ClockHour> getMyAttendance() throws SQLException;

    List<ClockHour> getMyAttendanceFromPayPeriod(LocalDate startDate, LocalDate endDate) throws SQLException;

    List<LeaveEntitlement> getMyLeaveEntitlements() throws SQLException;

    boolean fileLeave(int leaveTypeId, LocalDate appliedStartDate, LocalDate appliedEndDate, String reason) throws SQLException;

    List<LeaveRecord> getMyLeaveRecords() throws SQLException;

    List<Payslip> getMyPayslips() throws SQLException;
}
