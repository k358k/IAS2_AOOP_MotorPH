package Model;

import java.time.LocalDate;

/**
 *
 * @author Trisha Quismundo
 */
public class Payslip {

    private Integer payslipId;
    private Integer employeeId;
    private String employeeName;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private String employeePosition;
    private String employeeDepartment;
    private Double monthlyRate;
    private Double dailyRate;
    private Double hourlyRate;
    private Double overtimeRateMultiplier;
    private Integer distinctDaysWorked;
    private Double hoursWorked;
    private Double overtimeHours;
    private Double grossIncome;
    private Double riceSubsidy;
    private Double phoneAllowance;
    private Double clothingAllowance;
    private Double totalBenefits;
    private Double sssContribution;
    private Double philhealthContribution;
    private Double pagibigContribution;
    private Double taxableIncome;
    private Double withholdingTax;
    private Double totalDeductions;
    private Double netIncome;

    public Payslip(Integer payslipId, Integer employeeId, String employeeName, LocalDate periodStartDate, LocalDate periodEndDate, String employeePosition, String employeeDepartment, Double monthlyRate, Double dailyRate, Double hourlyRate, Double overtimeRateMutiplier, Integer distinctDaysWorked, Double hoursWorked, Double overtimeHours, Double grossIncome, Double riceSubsidy, Double phoneAllowance, Double clothiingAllowance, Double totalBenefits, Double sssContribution, Double philhealthContribution, Double pagibigContribution, Double taxableIncome, Double withholdingTax, Double totalDeductions, Double netIncome) {
        this.payslipId = payslipId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
        this.employeePosition = employeePosition;
        this.employeeDepartment = employeeDepartment;
        this.monthlyRate = monthlyRate;
        this.dailyRate = dailyRate;
        this.hourlyRate = hourlyRate;
        this.overtimeRateMultiplier = overtimeRateMutiplier;
        this.distinctDaysWorked = distinctDaysWorked;
        this.hoursWorked = hoursWorked;
        this.overtimeHours = overtimeHours;
        this.grossIncome = grossIncome;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothiingAllowance;
        this.totalBenefits = totalBenefits;
        this.sssContribution = sssContribution;
        this.philhealthContribution = philhealthContribution;
        this.pagibigContribution = pagibigContribution;
        this.taxableIncome = taxableIncome;
        this.withholdingTax = withholdingTax;
        this.totalDeductions = totalDeductions;
        this.netIncome = netIncome;
    }

    public Integer getPayslipId() {
        return payslipId;
    }

    public void setPayslipId(Integer payslipId) {
        this.payslipId = payslipId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public Double getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(Double monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public Double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Double getOvertimeRateMultiplier() {
        return overtimeRateMultiplier;
    }

    public void setOvertimeRateMultiplier(Double overtimeRateMutiplier) {
        this.overtimeRateMultiplier = overtimeRateMutiplier;
    }

    public Integer getDistinctDaysWorked() {
        return distinctDaysWorked;
    }

    public void setDistinctDaysWorked(Integer distinctDaysWorked) {
        this.distinctDaysWorked = distinctDaysWorked;
    }

    public Double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Double getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(Double grossIncome) {
        this.grossIncome = grossIncome;
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

    public void setClothingAllowance(Double clothiingAllowance) {
        this.clothingAllowance = clothiingAllowance;
    }

    public Double getTotalBenefits() {
        return totalBenefits;
    }

    public void setTotalBenefits(Double totalBenefits) {
        this.totalBenefits = totalBenefits;
    }

    public Double getSssContribution() {
        return sssContribution;
    }

    public void setSssContribution(Double sssContribution) {
        this.sssContribution = sssContribution;
    }

    public Double getPhilhealthContribution() {
        return philhealthContribution;
    }

    public void setPhilhealthContribution(Double philhealthContribution) {
        this.philhealthContribution = philhealthContribution;
    }

    public Double getPagibigContribution() {
        return pagibigContribution;
    }

    public void setPagibigContribution(Double pagibigContribution) {
        this.pagibigContribution = pagibigContribution;
    }

    public Double getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(Double taxableIncome) {
        this.taxableIncome = taxableIncome;
    }

    public Double getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(Double withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public Double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(Double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public Double getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(Double netIncome) {
        this.netIncome = netIncome;
    }

    @Override
    public String toString() {
        return "Payslip{"
                + "payslipId=" + payslipId
                + ", employeeId=" + employeeId
                + ", employeeName=" + employeeName
                + ", periodStartDate=" + periodStartDate
                + ", periodEndDate=" + periodEndDate
                + ", employeePosition=" + employeePosition
                + ", employeeDepartment=" + employeeDepartment
                + ", monthlyRate=" + monthlyRate
                + ", dailyRate=" + dailyRate
                + ", hourlyRate=" + hourlyRate
                + ", overtimeRateMultiplier=" + overtimeRateMultiplier
                + ", distinctDaysWorked=" + distinctDaysWorked
                + ", hoursWorked=" + hoursWorked
                + ", overtimeHours=" + overtimeHours
                + ", grossIncome=" + grossIncome
                + ", riceSubsidy=" + riceSubsidy
                + ", phoneAllowance=" + phoneAllowance
                + ", clothingAllowance=" + clothingAllowance
                + ", totalBenefits=" + totalBenefits
                + ", sssContribution=" + sssContribution
                + ", philhealthContribution=" + philhealthContribution
                + ", pagibigContribution=" + pagibigContribution
                + ", taxableIncome=" + taxableIncome
                + ", withholdingTax=" + withholdingTax
                + ", totalDeductions=" + totalDeductions
                + ", netIncome=" + netIncome
                + '}';
    }

}
