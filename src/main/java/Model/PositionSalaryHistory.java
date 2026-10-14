package Model;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * @author Gracielle Salvador
 */


/**
 * Represents the salary history of a position held by an employee, including various allowances and effective dates.
 */
public class PositionSalaryHistory {
    private Integer positionSalaryHistoryId;
    private Integer empId;
    private JobPosition jobPosition;
    private Double basicSalary;
    private Double hourlyRate; // Set by database trigger
    private Double grossSemiMonthlyRate; // Set by database trigger
    private Double riceSubsidy;
    private Double phoneAllowance;
    private Double clothingAllowance;
    private LocalDate effectiveStartDate;
    private LocalDate effectiveEndDate;

    // Default constructor
    public PositionSalaryHistory() {
    }

    // Parameterized constructor
    public PositionSalaryHistory(Integer empId, JobPosition jobPosition, Double basicSalary,
                                 Double riceSubsidy, Double phoneAllowance, Double clothingAllowance,
                                 LocalDate effectiveStartDate, LocalDate effectiveEndDate) {
        this.empId = empId;
        this.jobPosition = jobPosition;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.effectiveStartDate = effectiveStartDate;
        this.effectiveEndDate = effectiveEndDate;
    }

    // Getters and Setters
    public Integer getPositionSalaryHistoryId() {
        return positionSalaryHistoryId;
    }

    public void setPositionSalaryHistoryId(Integer positionSalaryHistoryId) {
        this.positionSalaryHistoryId = positionSalaryHistoryId;
    }

    public Integer getEmployeeId() {
        return empId;
    }

    public void setEmployeeId(Integer empId) {
        this.empId = empId;
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition getJobPosition) {
        this.jobPosition = jobPosition;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Double getHourlyRate() {
    hourlyRate = basicSalary / 168;
    return new BigDecimal(hourlyRate).setScale(2, RoundingMode.DOWN).doubleValue(); //Formats into two decimal places without rounding up
    }


    public Double getGrossSemiMonthlyRate() {
        return basicSalary/2;
    }

    public Double getRiceSubsidy() {
        return riceSubsidy;
    }

    public void setRiceSubsidy(Double riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public Double getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(Double phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public Double getClothingAllowance() {
        return clothingAllowance;
    }

    public void setClothingAllowance(Double clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public LocalDate getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(LocalDate effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public LocalDate getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(LocalDate effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    @Override
    public String toString() {
        return "PositionSalaryHistory{" +
                "positionSalaryHistoryId=" + positionSalaryHistoryId +
                ", empId=" + empId +
                ", jobPosition=" + jobPosition.toString() +
                ", basicSalary=" + basicSalary +
                ", riceSubsidy=" + riceSubsidy +
                ", phoneAllowance=" + phoneAllowance +
                ", clothingAllowance=" + clothingAllowance +
                ", effectiveStartDate=" + effectiveStartDate +
                ", effectiveEndDate=" + effectiveEndDate +
                '}';
    }
}
   
