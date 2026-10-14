package Service;

import DAO.AttendanceMgmtImpl;
import DAO.EmployeeUserMgmtImpl;
import DAO.LeaveMgmtImpl;
import DAO.PayrollMgmtImpl;
import Model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinanceService extends EmployeeService implements FinanceUseCases {

    EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();
    AttendanceMgmtImpl attendanceDAO = new AttendanceMgmtImpl();
    LeaveMgmtImpl leaveDAO = new LeaveMgmtImpl();
    PayrollMgmtImpl payrollDAO = new PayrollMgmtImpl();

    public FinanceService(String firstName,
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

    public FinanceService(EmployeeService employeeService) {
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
    public List<ClockHour> getAttendanceByEmployeeAndPayPeriod(int employeeId, PayPeriod payPeriod) throws SQLException {
        try {
            return attendanceDAO.findClockHoursByEmployeeIdAndPayPeriod(this.getEmpId(), payPeriod);
        } catch (SQLException ex) {
            Logger.getLogger(FinanceService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<LeaveEntitlement> getLeaveEntitlementsByEmployeeIdAndYear(int empId, PayPeriod payPeriod) throws SQLException {
        try {
            return leaveDAO.findLeaveEntitlementsByEmployeeIdAndYear(empId, payPeriod);
        } catch (SQLException ex) {
            Logger.getLogger(FinanceService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<LeaveRecord> getApprovedLeavesByEmployeeAndPayPeriod(int empId, PayPeriod payPeriod) throws SQLException {
        try {
            return leaveDAO.findApprovedLeavesByEmployeeAndPayPeriod(empId, payPeriod);
        } catch (SQLException ex) {
            Logger.getLogger(FinanceService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Employee> getEmployeeById(int employeeId) throws SQLException {
        List<Employee> empList = new ArrayList<>(); // Specify the type parameter

        try {
            Employee employee = employeeDAO.findEmployeeById(employeeId);
            if (employee != null) {
                employee.setPassword(null); //Hide sensitive data
                employee.setUserRole(null);
                employee.setBirthday(null);
                employee.setEmployeeAddress(null);
                employee.setPhoneNumber(null);
                empList.add(employee); // Add the employee only if it's not null
            } else {
                System.out.println("FinanceService.getEmployeeById | No employee found with ID: " + employeeId);
            }
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, "Error retrieving employee with ID: " + employeeId, ex);
            throw ex; // Rethrow the exception after logging
        }

        return empList; // Return the list (could be empty if no employee was found)
    }

    @Override
    public List<Employee> getAllEmployees() throws SQLException {
        try {
            List<Employee> employeeRecords = employeeDAO.findAllEmployee();

            // Check if the list is not null and not empty before processing
            if (employeeRecords != null && !employeeRecords.isEmpty()) {
                for (Employee employee : employeeRecords) {
                    // Clear sensitive fields for each individual employee object
                    employee.setPassword(null);
                    employee.setUserRole(null);
                    employee.setBirthday(null);
                    employee.setEmployeeAddress(null);
                    employee.setPhoneNumber(null);
                }
            }
            return employeeRecords; // Returns the modified list (can be empty or null if findAllEmployee returns null)
        } catch (SQLException ex) {
            Logger.getLogger(FinanceService.class.getName()).log(Level.SEVERE, "Error retrieving all employees", ex);
            return null; // Return null to indicate an error
        }
    }

    @Override
    public List<Payslip> getAllPayslipsByEmployee(Integer empId) throws SQLException {
        try {
            return payrollDAO.findAllPayslipsByEmployee(empId);
        } catch (SQLException ex) {
            Logger.getLogger(FinanceService.class.getName()).log(Level.SEVERE, "Error retrieving all payslips by employee", ex);
            return null; // Return null to indicate an error
        }
    }

    @Override
    public List<Payslip> getAllPayslips() throws SQLException {
        try {
            return payrollDAO.findAllPayslips();
        } catch (SQLException ex) {
            Logger.getLogger(FinanceService.class.getName()).log(Level.SEVERE, "Error retrieving all payslips", ex);
            return null; // Return null to indicate an error
        }
    }

    @Override
    public List<MonthlyPayrollSummary> getAllMonthlyPayrollSummary() throws SQLException {
        try {
            return payrollDAO.findAllMonthlyPayrollSummary();
        } catch (SQLException ex) {
            Logger.getLogger(FinanceService.class.getName()).log(Level.SEVERE, "Error retrieving all monthly payroll summary", ex);
            return null; // Return null to indicate an error
        }
    }

    /*
    Currently implements a whole month as parameters for now, but database takes specific dates.
     */
    @Override
    public boolean createPayslip(Integer empId, Integer month, Integer year) throws SQLException {
        try {
            // Get the first day of the month
            LocalDate firstDay = LocalDate.of(year, month, 1);
            // Get the last day of the month
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate lastDay = yearMonth.atEndOfMonth();

            payrollDAO.createPayslip(empId, firstDay, lastDay);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public boolean deletePayslip(Integer payslipId) throws SQLException {
        try {
            payrollDAO.deletePayslip(payslipId);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

}
