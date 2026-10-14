package Service;

import DAO.AttendanceMgmtImpl;
import DAO.EmployeeUserMgmtImpl;
import DAO.LeaveMgmtImpl;
import Model.*;
import Util.PasswordUtil;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HrService extends EmployeeService implements HrUseCases {

    EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();
    AttendanceMgmtImpl attendanceDAO = new AttendanceMgmtImpl();
    LeaveMgmtImpl leaveDAO = new LeaveMgmtImpl();

    public HrService(String firstName,
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

    public HrService(EmployeeService employeeService) {
        super(employeeService.getFirstName(),
                employeeService.getLastName(),
                employeeService.getGender(),
                employeeService.getBirthday(),
                employeeService.getPhoneNumber(),
                employeeService.getSssNumber(),
                employeeService.getPhilhealthNumber(),
                employeeService.getTinNumber(),
                employeeService.getPagibigNumber(),
                employeeService.getEmpStatus(),
                employeeService.getDepartment(),
                employeeService.getSupervisorId(),
                employeeService.getPositionSalaryHistory(),
                employeeService.getEmployeeAddress(),
                employeeService.getEmpId(),
                employeeService.getPassword(),
                employeeService.getUserRole());
    }

    @Override
    public List<Employee> getAllEmployees() throws SQLException {
        try {
            return employeeDAO.findAllEmployee();
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public List<Employee> getEmployeeById(int employeeId) throws SQLException {
        List<Employee> empList = new ArrayList<>(); // Specify the type parameter

        try {
            Employee employee = employeeDAO.findEmployeeById(employeeId);
            if (employee != null) {
                empList.add(employee); // Add the employee only if it's not null
            } else {
                System.out.println("HrService.getEmployeeById | No employee found with ID: " + employeeId); // Optional: log or handle the case
            }
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, "Error retrieving employee with ID: " + employeeId, ex);
            throw ex; // Rethrow the exception after logging
        }

        return empList; // Return the list (could be empty if no employee was found)
    }

    @Override
    public boolean addEmployee(Employee employee) throws SQLException {
        try {
            employee.setPassword(PasswordUtil.hashPassword(employee.getPassword())); // Hash the password
            employeeDAO.createEmployeeRecord(employee);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) throws SQLException {
        try {
            System.out.println("Running HrService.updateEmployee()...");
            System.out.println("Employee Object Parameter: " + employee.toString());

            employeeDAO.updateEmployeeRecord(employee);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public boolean deleteEmployee(int employeeId) throws SQLException {
        try {
            employeeDAO.deleteEmployeeRecord(employeeId);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public List<ClockHour> getAllAttendance() throws SQLException {
        try {
            return attendanceDAO.findAllClockHourRecords();
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public List<ClockHour> getAttendanceByEmployeeAndPayPeriod(int employeeId, LocalDate startDate, LocalDate endDate) throws SQLException {
        try {
            return attendanceDAO.findClockHoursByEmployeeIdAndPayPeriod(this.getEmpId(), new PayPeriod(startDate, endDate));
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public boolean addAttendance(ClockHour clockHour) throws SQLException {
        try {
            attendanceDAO.createClockHourRecord(clockHour);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public boolean deleteAttendance(int attendanceId) throws SQLException {
        try {
            attendanceDAO.deleteClockHourRecord(attendanceId);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public List<LeaveEntitlement> getLeaveEntitlementsByEmployeeIdAndYear(int empId, int year) throws SQLException {
        try {
            return leaveDAO.findLeaveEntitlementsByEmployeeIdAndYear(empId, new PayPeriod(LocalDate.of(year, 1, 1), LocalDate.of(year, 1, 1))); // Placeholder month and day
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public LeaveRecord getLeaveRecordById(int leaveRecordId) throws SQLException {
        try {
            return leaveDAO.findLeaveRecordById(leaveRecordId);
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public List<LeaveRecord> getAllLeaveRecords() throws SQLException {
        try {
            return leaveDAO.findAllLeaveRecords();
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public List<LeaveRecord> getLeaveRecordsByEmployeeId(int employeeId) throws SQLException {
        try {
            return leaveDAO.findLeaveRecordsByEmployeeId(employeeId);
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public List<LeaveRecord> getLeaveRecordsByStatus(LeaveStatus leaveStatus) throws SQLException {
        try {
            return leaveDAO.findLeaveRecordsByStatus(leaveStatus);
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public boolean approveLeave(int leaveRecordId, String remarks) throws SQLException {
        try {
            leaveDAO.approveLeave(new LeaveRecord(
                    leaveRecordId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    this.getEmpId(),
                    null,
                    remarks));
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public boolean rejectLeave(int leaveRecordId, String remarks) throws SQLException {
        try {
            leaveDAO.rejectLeave(new LeaveRecord(
                    leaveRecordId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    this.getEmpId(),
                    null,
                    remarks));
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public boolean deleteLeave(int leaveRecordId) throws SQLException {
        try {
            leaveDAO.deleteLeaveRecord(leaveRecordId);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

}
