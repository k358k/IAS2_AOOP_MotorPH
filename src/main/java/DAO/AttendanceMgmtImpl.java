package DAO;

import DAO.EmployeeUserMgmtDAO;
import Model.*;
import Util.DatabaseConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceMgmtImpl implements AttendanceMgmtDAO {
    private Connection connection;

    public AttendanceMgmtImpl() {
        this.connection = DatabaseConnectionUtil.getConnection();
    }

    @Override
    public List<ClockHour> findAllClockHourRecords() throws SQLException {

        List<ClockHour> clockHours = new ArrayList<>();
        String sql = "{CALL FindAllClockHourRecords()}";
        
        try (CallableStatement stmt = connection.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ClockHour clockHour = new ClockHour();
                clockHour.setClockHourId(rs.getInt("clock_hour_id"));
                clockHour.setEmpId(rs.getInt("employee_id"));
                
                Timestamp clockInTimestamp = rs.getTimestamp("clock_in_time");
                if (clockInTimestamp != null) {
                    clockHour.setClockInDateTime(clockInTimestamp.toLocalDateTime());
                }
                
                Timestamp clockOutTimestamp = rs.getTimestamp("clock_out_time");
                if (clockOutTimestamp != null) {
                    clockHour.setClockOutDateTime(clockOutTimestamp.toLocalDateTime());
                }
                
                clockHour.setHoursWorked(rs.getDouble("hours_worked"));
                clockHours.add(clockHour);
            }
        }
        
        return clockHours;
    }
    
    @Override
    public List<ClockHour> findClockHoursByEmployee(Integer empId) throws SQLException {

        List<ClockHour> clockHours = new ArrayList<>();
        String sql = "{CALL FindClockHoursByEmployee(?)}";
        
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, empId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ClockHour clockHour = new ClockHour();
                    clockHour.setClockHourId(rs.getInt("clock_hour_id"));
                    clockHour.setEmpId(rs.getInt("employee_id"));
                    
                    Timestamp clockInTimestamp = rs.getTimestamp("clock_in_time");
                    if (clockInTimestamp != null) {
                        clockHour.setClockInDateTime(clockInTimestamp.toLocalDateTime());
                    }
                    
                    Timestamp clockOutTimestamp = rs.getTimestamp("clock_out_time");
                    if (clockOutTimestamp != null) {
                        clockHour.setClockOutDateTime(clockOutTimestamp.toLocalDateTime());
                    }
                    
                    clockHour.setHoursWorked(rs.getDouble("hours_worked"));
                    clockHours.add(clockHour);
                }
            }
        }
        
        return clockHours;
    }
    
    @Override
    public ClockHour findUnclosedClockHourRecordByEmployee(Integer empId) throws SQLException {

        String sql = "{CALL FindUnclosedClockHourByEmployee(?)}";
        
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, empId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ClockHour clockHour = new ClockHour();
                    clockHour.setClockHourId(rs.getInt("clock_hour_id"));
                    clockHour.setEmpId(rs.getInt("employee_id"));
                    
                    Timestamp clockInTimestamp = rs.getTimestamp("clock_in_time");
                    if (clockInTimestamp != null) {
                        clockHour.setClockInDateTime(clockInTimestamp.toLocalDateTime());
                    }
                    
                    // clock_out_time will be null for unclosed records
                    Timestamp clockOutTimestamp = rs.getTimestamp("clock_out_time");
                    if (clockOutTimestamp != null) {
                        clockHour.setClockOutDateTime(clockOutTimestamp.toLocalDateTime());
                    }
                    
                    clockHour.setHoursWorked(rs.getDouble("hours_worked"));
                    return clockHour;
                }
            }
        }
        
        return null; // No unclosed record found
    }
    
    @Override
    public List<ClockHour> findClockHoursByEmployeeIdAndPayPeriod(Integer empId, PayPeriod payPeriod) throws SQLException {

        List<ClockHour> clockHours = new ArrayList<>();
        String sql = "{CALL FindClockHoursByEmployeeIdAndPayPeriod(?, ?, ?)}";
        
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, empId);
            stmt.setDate(2, Date.valueOf(payPeriod.getStartDate()));
            stmt.setDate(3, Date.valueOf(payPeriod.getEndDate()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ClockHour clockHour = new ClockHour();
                    clockHour.setClockHourId(rs.getInt("clock_hour_id"));
                    clockHour.setEmpId(rs.getInt("employee_id"));
                    
                    Timestamp clockInTimestamp = rs.getTimestamp("clock_in_time");
                    if (clockInTimestamp != null) {
                        clockHour.setClockInDateTime(clockInTimestamp.toLocalDateTime());
                    }
                    
                    Timestamp clockOutTimestamp = rs.getTimestamp("clock_out_time");
                    if (clockOutTimestamp != null) {
                        clockHour.setClockOutDateTime(clockOutTimestamp.toLocalDateTime());
                    }
                    
                    clockHour.setHoursWorked(rs.getDouble("hours_worked"));
                    clockHours.add(clockHour);
                }
            }
        }
        
        return clockHours;
    }
    
    @Override
    public boolean createClockHourRecord(ClockHour clockHour) throws SQLException {

        String sql = "{CALL CreateClockHourRecord(?, ?, ?)}";
        
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, clockHour.getEmpId());
            
            // Convert LocalDateTime to Timestamp for DATETIME fields
            if (clockHour.getClockInDateTime() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(clockHour.getClockInDateTime()));
            } else {
                stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Use current time if null
            }
            
            if (clockHour.getClockOutDateTime() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(clockHour.getClockOutDateTime()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP); // Allow null for open records
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Get the generated ID and set it back to the object
                    int generatedId = rs.getInt("clock_hour_id");
                    clockHour.setClockHourId(generatedId);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public boolean closeClockHourRecord(ClockHour clockHour) throws SQLException {

        String sql = "{CALL CloseClockHourRecordByEmployee(?)}";
        
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, clockHour.getEmpId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Update the clockHour object with the closed record data
                    clockHour.setClockHourId(rs.getInt("clock_hour_id"));
                    
                    Timestamp clockOutTimestamp = rs.getTimestamp("clock_out_time");
                    if (clockOutTimestamp != null) {
                        clockHour.setClockOutDateTime(clockOutTimestamp.toLocalDateTime());
                    }
                    
                    clockHour.setHoursWorked(rs.getDouble("hours_worked"));
                    return true;
                }
            }
        } catch (SQLException e) {
            // Handle specific error messages from stored procedure
            if (e.getMessage().contains("No unclosed clock hour record found")) {
                return false; // Or throw a custom exception
            }
            throw e; // Re-throw other SQL exceptions
        }
        
        return false;
    }
    
    @Override
    public boolean deleteClockHourRecord(Integer clockHourRecordId) throws SQLException {

        String sql = "{CALL DeleteClockHourRecord(?)}";
        
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, clockHourRecordId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if deletion was successful
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Clock hour record not found")) {
                return false;
            }
            throw e;
        }
    }
}