package Service;

import DAO.*;
import Model.ClockHour;
import Model.Department;
import Model.Employee;
import Model.EmployeeAddress;
import Model.EmploymentStatus;
import Model.Gender;
import Model.LeaveEntitlement;
import Model.LeaveRecord;
import Model.LeaveType;
import Model.PayPeriod;
import Model.Payslip;
import Model.PositionSalaryHistory;
import Model.UserRole;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Represents an Empoyee user with methods for it's use-cases
public class EmployeeService extends Employee implements EmployeeUseCases {

    private EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();
    private AttendanceMgmtImpl attendanceDAO = new AttendanceMgmtImpl();
    private LeaveMgmtImpl leaveDAO = new LeaveMgmtImpl();
    PayrollMgmtImpl payrollDAO = new PayrollMgmtImpl();

    public EmployeeService(String firstName,
            String lastName,
            Gender gender,
            LocalDate birthday,
            String phoneNumber,
            String sssNumber,
            String philhealthNumber,
            String tinNumber,
            String pagibigNumber,
            EmploymentStatus empStatus,
            Department department,
            Integer supervisorId,
            PositionSalaryHistory positionSalaryHistory,
            EmployeeAddress employeeAddress,
            int empId,
            String password,
            UserRole userRole) {
        super(firstName, lastName, gender, birthday, phoneNumber,
                sssNumber, philhealthNumber, tinNumber, pagibigNumber,
                empStatus, department, supervisorId, positionSalaryHistory,
                employeeAddress,
                empId, password, userRole);
    }

    public EmployeeService(Employee employee) {
        super(employee.getFirstName(),
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
                employee.getUserRole());
    }

    @Override
    public boolean clockIn() throws SQLException {
        ClockHour clockHourRecord = new ClockHour(this.getEmpId(), LocalDateTime.now(), null);
        attendanceDAO.createClockHourRecord(clockHourRecord);
        return true; // Return true only if the operation is successful
    }

    @Override
    public boolean clockOut() throws SQLException {
        ClockHour unclosedClockHourRecord = attendanceDAO.findUnclosedClockHourRecordByEmployee(this.getEmpId());

        if (unclosedClockHourRecord == null) {
            throw new SQLException("Please clock in first");
        }
        attendanceDAO.closeClockHourRecord(unclosedClockHourRecord);
        return true; // Return true only if the operation is successful

    }

    @Override
    public ClockHour findUnclosedClockHour() throws SQLException {
        ClockHour unclosedClockHourRecord = attendanceDAO.findUnclosedClockHourRecordByEmployee(this.getEmpId());
        return unclosedClockHourRecord;
    }

    @Override
    public List<ClockHour> getMyAttendance() throws SQLException {
        return attendanceDAO.findClockHoursByEmployee(this.getEmpId());
    }

    @Override
    public List<ClockHour> getMyAttendanceFromPayPeriod(LocalDate startDate, LocalDate endDate) throws SQLException {
        return attendanceDAO.findClockHoursByEmployeeIdAndPayPeriod(this.getEmpId(), new PayPeriod(startDate, endDate));
    }

    @Override
    public List<LeaveEntitlement> getMyLeaveEntitlements() throws SQLException {
        PayPeriod currentPayPeriod = new PayPeriod(LocalDate.now(), LocalDate.now());
        return leaveDAO.findLeaveEntitlementsByEmployeeIdAndYear(this.getEmpId(), currentPayPeriod);
    }

    public boolean fileLeave(int leaveTypeId,
            LocalDate appliedStartDate,
            LocalDate appliedEndDate,
            String reason) {
        try {
            LeaveRecord leaveRecord = new LeaveRecord(this.getEmpId(),
                    leaveTypeId,
                    LocalDate.now(),
                    appliedStartDate,
                    appliedEndDate,
                    (double) ChronoUnit.DAYS.between(appliedStartDate, appliedEndDate) + 1, // +1 to include end date
                    reason);

            return leaveDAO.createLeaveRecord(leaveRecord);

        } catch (SQLException e) {
            // Convert SQLException to RuntimeException with meaningful message
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Database error filing leave", e);
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<LeaveRecord> getMyLeaveRecords() throws SQLException {
        return leaveDAO.findLeaveRecordsByEmployeeId(this.getEmpId());

    }

    @Override
    public List<Payslip> getMyPayslips() throws SQLException {
        try {
            return payrollDAO.findAllPayslipsByEmployee(this.getEmpId());
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, "Error retrieving all payslips by employee", ex);
            return null; // Return null to indicate an error
        }
    }

}
