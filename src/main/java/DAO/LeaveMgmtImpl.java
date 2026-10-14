package DAO;

import Model.LeaveEntitlement;
import Model.LeaveRecord;
import Model.LeaveStatus;
import Model.LeaveType;
import Model.PayPeriod;
import Util.DatabaseConnectionUtil;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeaveMgmtImpl implements LeaveMgmtDAO {
    private Connection connection;

    public LeaveMgmtImpl() {
        this.connection = DatabaseConnectionUtil.getConnection();
    }

    @Override
    public List<LeaveEntitlement> findLeaveEntitlementsByEmployeeIdAndYear(Integer empId, PayPeriod payPeriod) throws SQLException {

        List<LeaveEntitlement> entitlements = new ArrayList<>();
        String sql = "CALL FindLeaveEntitlementsByEmployeeIdAndYear(?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.setInt(2, payPeriod.getStartDate().getYear());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Create LeaveType object
                    LeaveType leaveType = new LeaveType(
                        rs.getInt("leave_type_id"),
                        rs.getString("leave_type_name"),
                        null, // description not in SP result
                        null, // maxDaysAllowed not in SP result
                        true  // set to active so only active leave types are counted
                    );
                    
                    // Create LeaveEntitlement object
                    LeaveEntitlement entitlement = new LeaveEntitlement(
                        rs.getInt("leave_entitlement_id"),
                        rs.getInt("employee_id"),
                        leaveType,
                        rs.getInt("entitlement_days"),
                        rs.getInt("year"),
                        rs.getInt("days_used"),
                        rs.getInt("remaining_days")
                    );
                    
                    entitlements.add(entitlement);
                }
            }
        }
        
        return entitlements;
    }
    
    @Override
    public LeaveRecord findLeaveRecordById(Integer leaveRecordId) throws SQLException {

        String sql = "CALL FindLeaveRecordById(?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leaveRecordId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLeaveRecord(rs);
                }
            }
        }
        
        return null;
    }
    
    @Override
    public List<LeaveRecord> findAllLeaveRecords() throws SQLException {

        List<LeaveRecord> records = new ArrayList<>();
        String sql = "CALL FindAllLeaveRecords()";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                records.add(mapResultSetToLeaveRecord(rs));
            }
        }
        
        return records;
    }
    
    @Override
    public List<LeaveRecord> findLeaveRecordsByEmployeeId(Integer employeeId) throws SQLException {

        List<LeaveRecord> records = new ArrayList<>();
        String sql = "CALL FindLeaveRecordsByEmployeeId(?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToLeaveRecord(rs));
                }
            }
        }
        
        return records;
    }
    
    @Override
    public List<LeaveRecord> findLeaveRecordsByStatus(LeaveStatus leaveStatus) throws SQLException {

        List<LeaveRecord> records = new ArrayList<>();
        String sql = "CALL FindLeaveRecordsByStatus(?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, leaveStatus.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToLeaveRecord(rs));
                }
            }
        }
        
        return records;
    }
    
    @Override
    public List<LeaveRecord> findApprovedLeavesByEmployeeAndPayPeriod(Integer empId, PayPeriod payPeriod) throws SQLException {

        List<LeaveRecord> records = new ArrayList<>();
        String sql = "CALL FindApprovedLeavesByEmployeeByPayPeriod(?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.setDate(2, Date.valueOf(payPeriod.getStartDate()));
            stmt.setDate(3, Date.valueOf(payPeriod.getEndDate()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToLeaveRecord(rs));
                }
            }
        }
        
        return records;
    }
    
    @Override
    public boolean approveLeave(LeaveRecord leaveRecord) throws SQLException {

        String sql = "CALL ApproveLeave(?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leaveRecord.getLeaveRecordId());
            stmt.setInt(2, leaveRecord.getStatusUpdatedBy());
            stmt.setString(3, leaveRecord.getRemarks());
            
            try (ResultSet rs = stmt.executeQuery()) {
                // If stored procedure returns a result set, it means approval was successful
                return rs.next();
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Leave record not found") || 
                e.getMessage().contains("already processed") ||
                e.getMessage().contains("Insufficient leave balance")) {
                throw e;
            }
            return false;
        }
    }
    
    @Override
    public boolean rejectLeave(LeaveRecord leaveRecord) throws SQLException {

        String sql = "CALL RejectLeave(?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leaveRecord.getLeaveRecordId());
            stmt.setInt(2, leaveRecord.getStatusUpdatedBy());
            stmt.setString(3, leaveRecord.getRemarks());
            
            try (ResultSet rs = stmt.executeQuery()) {
                // If stored procedure returns a result set, it means rejection was successful
                return rs.next();
            }
        } catch (SQLException e) {

            if (e.getMessage().contains("Leave record not found") || 
                e.getMessage().contains("already processed")) {
                throw e;
            }
            return false;
        }
    }
    
    @Override
    public boolean createLeaveRecord(LeaveRecord leaveRecord) throws SQLException {

        String sql = "CALL CreateLeaveRecord(?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, leaveRecord.getEmployeeId());
                stmt.setInt(2, leaveRecord.getLeaveTypeId());
                stmt.setDate(3, Date.valueOf(leaveRecord.getApplicationDate()));
                stmt.setDate(4, Date.valueOf(leaveRecord.getAppliedStartDate()));
                stmt.setDate(5, Date.valueOf(leaveRecord.getAppliedEndDate()));
                stmt.setDouble(6, leaveRecord.getDaysUsed());
                stmt.setString(7, leaveRecord.getReason());

                try (ResultSet rs = stmt.executeQuery()) {
                    // If stored procedure returns a result set, it means creation was successful
                    return rs.next();
                }
            } catch (SQLException e) {
                // Log the error for debugging
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to create leave record", e);

                // Re-throw all SQLExceptions with proper error messages
                String message = e.getMessage();
                if (message != null) {
                    if (message.contains("End date must be after or equal to start date")) {
                        throw new SQLException("End date must be after or equal to start date");
                    }
                    if (message.contains("Invalid or inactive leave type")) {
                        throw new SQLException("Invalid or inactive leave type");
                    }
                    // Add overlap check if your database has it
                    if (message.contains("overlapping") || message.contains("duplicate") || message.contains("conflict")) {
                        throw new SQLException("You have overlapping leave requests for the selected dates");
                    }
                }

                // Re-throw the original exception if no specific handling
                throw e;
            }
    }
    
    @Override
    public boolean deleteLeaveRecord(Integer leaveRecordId) throws SQLException {

        String sql = "CALL DeleteLeaveRecord(?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leaveRecordId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int rowsAffected = rs.getInt("rows_affected");
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Leave record not found")) {
                throw e;
            }
            return false;
        }
        
        return false;
    }
    
    /**
     * Helper method to create LeaveRecord object from ResultSet
     */
    private LeaveRecord mapResultSetToLeaveRecord(ResultSet rs) throws SQLException {
        // Convert SQL Date to LocalDate
        LocalDate applicationDate = rs.getDate("application_date") != null ? 
            rs.getDate("application_date").toLocalDate() : null;
        LocalDate appliedStartDate = rs.getDate("applied_start_date") != null ? 
            rs.getDate("applied_start_date").toLocalDate() : null;
        LocalDate appliedEndDate = rs.getDate("applied_end_date") != null ? 
            rs.getDate("applied_end_date").toLocalDate() : null;
        
        // Convert SQL Timestamp to LocalDateTime
        LocalDateTime statusUpdatedAt = rs.getTimestamp("status_updated_at") != null ? 
            rs.getTimestamp("status_updated_at").toLocalDateTime() : null;
        
        // Convert string status to enum
        LeaveStatus status = LeaveStatus.valueOf(rs.getString("status"));
        
        // Handle nullable integers
        Integer statusUpdatedBy = rs.getObject("status_updated_by") != null ? 
            rs.getInt("status_updated_by") : null;
        
        LeaveRecord record = new LeaveRecord(
            rs.getInt("leave_record_id"),
            rs.getInt("employee_id"),
            rs.getInt("leave_type_id"),
            applicationDate,
            appliedStartDate,
            appliedEndDate,
            rs.getDouble("days_used"),
            rs.getString("reason"),
            status,
            statusUpdatedBy,
            statusUpdatedAt,
            rs.getString("remarks")
        );
        
        return record;
    }
}
