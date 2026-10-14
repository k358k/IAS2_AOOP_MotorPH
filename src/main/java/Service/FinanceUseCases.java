package Service;

import Model.*;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

public interface FinanceUseCases {

    List<Employee> getAllEmployees() throws SQLException;

    List<Employee> getEmployeeById(int employeeId) throws SQLException;

    List<LeaveRecord> getApprovedLeavesByEmployeeAndPayPeriod(int empId, PayPeriod payPeriod) throws SQLException;

    List<ClockHour> getAttendanceByEmployeeAndPayPeriod(int employeeId, PayPeriod payPeriod) throws SQLException;

    List<LeaveEntitlement> getLeaveEntitlementsByEmployeeIdAndYear(int empId, PayPeriod payPeriod) throws SQLException;

    List<Payslip> getAllPayslipsByEmployee(Integer empId) throws SQLException;

    List<Payslip> getAllPayslips() throws SQLException;

    List<MonthlyPayrollSummary> getAllMonthlyPayrollSummary() throws SQLException;

    boolean createPayslip(Integer empId, Integer month, Integer year) throws SQLException;

    boolean deletePayslip(Integer payslipId) throws SQLException;

}
