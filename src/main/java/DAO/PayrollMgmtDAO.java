package DAO;

import Model.*;
import Model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author Gracielle Salvador
 */

/**
 * Data Access Object for managing Payslip and Monthly Payroll Summary entities.
 */
public interface PayrollMgmtDAO {
    
    List<Payslip> findAllPayslipsByEmployee(Integer empId) throws SQLException;
    List<Payslip> findAllPayslips() throws SQLException;
    
    List<MonthlyPayrollSummary> findAllMonthlyPayrollSummary() throws SQLException;   
    
    boolean createPayslip(Integer empId, LocalDate periodStart, LocalDate periodEnd) throws SQLException;
    boolean deletePayslip(Integer payslipId) throws SQLException; 
}