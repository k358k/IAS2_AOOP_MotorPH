package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 *
 * @author Gracielle Salvador
 */

/**
 * Represents an employee that is also a system user.
 */
public class ClockHour {
    private Integer clockHourId;
    private Integer empId;
    private LocalDateTime clockInDateTime;
    private LocalDateTime clockOutDateTime;
    private Double hoursWorked;

    // Default constructor
    public ClockHour() {
    }
    
    public ClockHour(Integer empId, LocalDateTime clockInDateTime, LocalDateTime clockOutDateTime) {
        this.empId = empId;
        this.clockInDateTime = clockInDateTime;
        this.clockOutDateTime = clockOutDateTime;
    }

    public Integer getClockHourId() {
        return clockHourId;
    }

    public void setClockHourId(Integer clockHourId) {
        this.clockHourId = clockHourId;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public LocalDateTime getClockInDateTime() {
        return clockInDateTime;
    }

    public void setClockInDateTime(LocalDateTime clockInDateTime) {
        this.clockInDateTime = clockInDateTime;
    }

    public LocalDateTime getClockOutDateTime() {
        return clockOutDateTime;
    }

    public void setClockOutDateTime(LocalDateTime clockOutDateTime) {
        this.clockOutDateTime = clockOutDateTime;
    }
    

    public Double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

}