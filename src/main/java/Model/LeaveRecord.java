package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRecord {
    private int leaveRecordId;
    private Integer employeeId;
    private Integer leaveTypeId;
    private LocalDate applicationDate;
    private LocalDate appliedStartDate;
    private LocalDate appliedEndDate;
    private Double daysUsed;
    private String reason;
    private LeaveStatus status;
    private Integer statusUpdatedBy;
    private LocalDateTime statusUpdatedAt;
    private String remarks;
    
    // Default constructor
    public LeaveRecord() {}
    
    /**
     * Constructs a LeaveRecord instance.
     *
     * @param employeeId the employee's ID
     * @param leaveTypeId the leave type ID
     * @param applicationDate the date the leave was filed
     * @param appliedStartDate the start date of the leave
     * @param appliedEndDate the end date of the leave
     * @param daysUsed the number of leave days used
     * @param reason the reason for the leave
     * @param status the current status of the leave
     * @param statusUpdatedBy the ID of the approver/reviewer
     * @param statusUpdatedAt the timestamp when the status was updated
     * @param remarks any additional remarks
     */
    public LeaveRecord(Integer employeeId,
                       Integer leaveTypeId,
                       LocalDate applicationDate,
                       LocalDate appliedStartDate,
                       LocalDate appliedEndDate,
                       Double daysUsed,
                       String reason,
                       LeaveStatus status,
                       Integer statusUpdatedBy,
                       LocalDateTime statusUpdatedAt,
                       String remarks) {
        this.employeeId = employeeId;
        this.leaveTypeId = leaveTypeId;
        this.applicationDate = applicationDate;
        this.appliedStartDate = appliedStartDate;
        this.appliedEndDate = appliedEndDate;
        this.daysUsed = daysUsed;
        this.reason = reason;
        this.status = status;
        this.statusUpdatedBy = statusUpdatedBy;
        this.statusUpdatedAt = statusUpdatedAt;
        this.remarks = remarks;
    }
    
    // Full constructor with ID
    public LeaveRecord(int leaveRecordId,
                       Integer employeeId,
                       Integer leaveTypeId,
                       LocalDate applicationDate,
                       LocalDate appliedStartDate,
                       LocalDate appliedEndDate,
                       Double daysUsed,
                       String reason,
                       LeaveStatus status,
                       Integer statusUpdatedBy,
                       LocalDateTime statusUpdatedAt,
                       String remarks) {
        this.leaveRecordId = leaveRecordId;
        this.employeeId = employeeId;
        this.leaveTypeId = leaveTypeId;
        this.applicationDate = applicationDate;
        this.appliedStartDate = appliedStartDate;
        this.appliedEndDate = appliedEndDate;
        this.daysUsed = daysUsed;
        this.reason = reason;
        this.status = status;
        this.statusUpdatedBy = statusUpdatedBy;
        this.statusUpdatedAt = statusUpdatedAt;
        this.remarks = remarks;
    }
    
    // Constructor for creating new leave records (without ID and status fields)
    public LeaveRecord(Integer employeeId,
                       Integer leaveTypeId,
                       LocalDate applicationDate,
                       LocalDate appliedStartDate,
                       LocalDate appliedEndDate,
                       Double daysUsed,
                       String reason) {
        this.employeeId = employeeId;
        this.leaveTypeId = leaveTypeId;
        this.applicationDate = applicationDate;
        this.appliedStartDate = appliedStartDate;
        this.appliedEndDate = appliedEndDate;
        this.daysUsed = daysUsed;
        this.reason = reason;
        this.status = LeaveStatus.PENDING; // Default status
    }
    
    // Getters and Setters
    public int getLeaveRecordId() {
        return leaveRecordId;
    }
    
    public void setLeaveRecordId(int leaveRecordId) {
        this.leaveRecordId = leaveRecordId;
    }
    
    public Integer getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public Integer getLeaveTypeId() {
        return leaveTypeId;
    }
    
    public void setLeaveTypeId(Integer leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }
    
    public LocalDate getApplicationDate() {
        return applicationDate;
    }
    
    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }
    
    public LocalDate getAppliedStartDate() {
        return appliedStartDate;
    }
    
    public void setAppliedStartDate(LocalDate appliedStartDate) {
        this.appliedStartDate = appliedStartDate;
    }
    
    public LocalDate getAppliedEndDate() {
        return appliedEndDate;
    }
    
    public void setAppliedEndDate(LocalDate appliedEndDate) {
        this.appliedEndDate = appliedEndDate;
    }
    
    public Double getDaysUsed() {
        return daysUsed;
    }
    
    public void setDaysUsed(Double daysUsed) {
        this.daysUsed = daysUsed;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public LeaveStatus getStatus() {
        return status;
    }
    
    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
    
    public Integer getStatusUpdatedBy() {
        return statusUpdatedBy;
    }
    
    public void setStatusUpdatedBy(Integer statusUpdatedBy) {
        this.statusUpdatedBy = statusUpdatedBy;
    }
    
    public LocalDateTime getStatusUpdatedAt() {
        return statusUpdatedAt;
    }
    
    public void setStatusUpdatedAt(LocalDateTime statusUpdatedAt) {
        this.statusUpdatedAt = statusUpdatedAt;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}