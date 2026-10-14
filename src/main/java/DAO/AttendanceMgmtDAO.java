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
 * Data Access Object for managing Clock Hour entity.
 */
public interface AttendanceMgmtDAO {
   
    List<ClockHour> findAllClockHourRecords()throws SQLException;
    List<ClockHour> findClockHoursByEmployee(Integer empId) throws SQLException;
    ClockHour findUnclosedClockHourRecordByEmployee(Integer empId) throws SQLException;
    List<ClockHour> findClockHoursByEmployeeIdAndPayPeriod(Integer empId, PayPeriod payPeriod) throws SQLException;

    boolean createClockHourRecord(ClockHour clockHour) throws SQLException;
    boolean closeClockHourRecord(ClockHour clockHour) throws SQLException;
    boolean deleteClockHourRecord(Integer clockHourRecordId) throws SQLException;
    
}