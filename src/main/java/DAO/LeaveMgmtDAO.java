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
 * Data Access Object for managing Leave Record and Leave Entitlements entities.
 */
public interface LeaveMgmtDAO {
    
    List<LeaveEntitlement> findLeaveEntitlementsByEmployeeIdAndYear(Integer empId, PayPeriod payPeriod) throws SQLException;
    
    LeaveRecord findLeaveRecordById(Integer leaveRecordId) throws SQLException; 
    List<LeaveRecord> findAllLeaveRecords()throws SQLException; 
    List<LeaveRecord> findLeaveRecordsByEmployeeId(Integer employeeId) throws SQLException;   
    List<LeaveRecord> findLeaveRecordsByStatus(LeaveStatus leaveStatus) throws SQLException; 
    List<LeaveRecord> findApprovedLeavesByEmployeeAndPayPeriod(Integer empId, PayPeriod payPeriod) throws SQLException;

    
    boolean approveLeave(LeaveRecord leaveRecord) throws SQLException; 
    boolean rejectLeave(LeaveRecord leaveRecord) throws SQLException; 
    
    boolean createLeaveRecord(LeaveRecord leaveRecord) throws SQLException;
    boolean deleteLeaveRecord(Integer leaveRecordId) throws SQLException;
 
}