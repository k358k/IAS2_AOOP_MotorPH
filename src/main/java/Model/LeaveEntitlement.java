package Model;
/**
 *
 * @author Gracielle Salvador
 */

/**
 * Represents the leave entitlement for an employee in a given year and type.
 * Added additional variables for display purposes.
 */
public class LeaveEntitlement {
    private Integer leaveEntitlementId;
    private Integer employeeId;
    private LeaveType leaveType;
    private Integer entitlementDays;
    private Integer year;
    private Integer daysUsed;
    private Integer remainingDays;
    
    // Default constructor
    public LeaveEntitlement() {}
    
    /**
     * Constructor for LeaveEntitlement
     */
    public LeaveEntitlement(Integer employeeId, LeaveType leaveType, Integer entitlementDays, 
                           Integer year, Integer daysUsed, Integer remainingDays) {
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.entitlementDays = entitlementDays;
        this.year = year;
        this.daysUsed = daysUsed;
        this.remainingDays = remainingDays;
    }
    
    // Full constructor with ID
    public LeaveEntitlement(Integer leaveEntitlementId, Integer employeeId, LeaveType leaveType, 
                           Integer entitlementDays, Integer year, Integer daysUsed, Integer remainingDays) {
        this.leaveEntitlementId = leaveEntitlementId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.entitlementDays = entitlementDays;
        this.year = year;
        this.daysUsed = daysUsed;
        this.remainingDays = remainingDays;
    }
    
    // Constructor without calculated fields (for creating new entitlements)
    public LeaveEntitlement(Integer employeeId, LeaveType leaveType, Integer entitlementDays, Integer year) {
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.entitlementDays = entitlementDays;
        this.year = year;
        this.daysUsed = 0;
        this.remainingDays = entitlementDays;
    }
    
    // Getters and Setters
    public Integer getLeaveEntitlementId() {
        return leaveEntitlementId;
    }
    
    public void setLeaveEntitlementId(Integer leaveEntitlementId) {
        this.leaveEntitlementId = leaveEntitlementId;
    }
    
    public Integer getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public LeaveType getLeaveType() {
        return leaveType;
    }
    
    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }
    
    public Integer getEntitlementDays() {
        return entitlementDays;
    }
    
    public void setEntitlementDays(Integer entitlementDays) {
        this.entitlementDays = entitlementDays;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public Integer getDaysUsed() {
        return daysUsed;
    }
    
    public void setDaysUsed(Integer daysUsed) {
        this.daysUsed = daysUsed;
    }
    
    public Integer getRemainingDays() {
        return remainingDays;
    }
    
    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
    }
    
    // Utility method to recalculate remaining days
    public void calculateRemainingDays() {
        if (entitlementDays != null && daysUsed != null) {
            this.remainingDays = entitlementDays - daysUsed;
        }
    }
}