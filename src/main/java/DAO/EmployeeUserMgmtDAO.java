package DAO;

import Model.Employee;
import Model.*;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author Gracielle Salvador
 */

/**
 * Data Access Object for managing Employee, UserCredential, EmployeeAddress, Address, PositionSalaryHistory, Job Position entities.
 */
public interface EmployeeUserMgmtDAO {
    
    List<Employee> findAllEmployee() throws SQLException;
    Employee findEmployeeById(Integer empId)  throws SQLException;
    
    boolean createEmployeeRecord(Employee employee) throws SQLException;
    boolean updateEmployeeRecord(Employee newEmployee) throws SQLException;
    boolean updateUserCredential(UserCredential newUserCredential) throws SQLException;
    boolean deleteEmployeeRecord(int employeeId) throws SQLException;

}
