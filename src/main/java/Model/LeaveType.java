// LeaveType.java
package Model;

public class LeaveType {
    private int leaveTypeId;
    private String name;
    private String description;
    private Integer maxDaysAllowed;
    private boolean isActive;
    
    // Default constructor
    public LeaveType() {}
    
    /**
     * Constructs a LeaveType instance.
     *
     * @param name the name of the leave type (e.g., "Maternal Leave")
     * @param description a short description of the leave type
     * @param maxDaysAllowed the maximum number of days allowed for this leave type per year
     * @param isActive indicates whether the leave type is currently active
     */
    public LeaveType(String name, String description, Integer maxDaysAllowed, boolean isActive) {
        this.name = name;
        this.description = description;
        this.maxDaysAllowed = maxDaysAllowed;
        this.isActive = isActive;
    }
    
    // Full constructor with ID
    public LeaveType(int leaveTypeId, String name, String description, Integer maxDaysAllowed, boolean isActive) {
        this.leaveTypeId = leaveTypeId;
        this.name = name;
        this.description = description;
        this.maxDaysAllowed = maxDaysAllowed;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public int getLeaveTypeId() {
        return leaveTypeId;
    }
    
    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getMaxDaysAllowed() {
        return maxDaysAllowed;
    }
    
    public void setMaxDaysAllowed(Integer maxDaysAllowed) {
        this.maxDaysAllowed = maxDaysAllowed;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
}
    