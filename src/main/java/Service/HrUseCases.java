package Service;

import Model.*;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

public interface HrUseCases {

    //Employee Management
    List<Employee> getAllEmployees() throws SQLException;

    List<Employee> getEmployeeById(int employeeId) throws SQLException;

    boolean addEmployee(Employee employee) throws SQLException;

    boolean updateEmployee(Employee employee) throws SQLException;

    boolean deleteEmployee(int employeeId) throws SQLException;

    //Attendance Management
    List<ClockHour> getAllAttendance() throws SQLException;

    List<ClockHour> getAttendanceByEmployeeAndPayPeriod(int employeeId, LocalDate startDate, LocalDate endDate) throws SQLException;

    boolean addAttendance(ClockHour clockHour) throws SQLException;

    boolean deleteAttendance(int attendanceId) throws SQLException;

    //Leave Management
    List<LeaveEntitlement> getLeaveEntitlementsByEmployeeIdAndYear(int empId, int year) throws SQLException;

    LeaveRecord getLeaveRecordById(int leaveRecordId) throws SQLException;

    List<LeaveRecord> getAllLeaveRecords() throws SQLException;

    List<LeaveRecord> getLeaveRecordsByEmployeeId(int employeeId) throws SQLException;

    List<LeaveRecord> getLeaveRecordsByStatus(LeaveStatus leaveStatus) throws SQLException;

    boolean approveLeave(int leaveRecordId, String remarks) throws SQLException;

    boolean rejectLeave(int leaveRecordId, String remarks) throws SQLException;

    boolean deleteLeave(int leaveRecordId) throws SQLException;

    //generateEmployeeReport
}
