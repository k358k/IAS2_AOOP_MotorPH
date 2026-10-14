package Model;

import java.time.LocalDate;

/**
 *
 * @author Gracielle Salvador
 */
public class MonthlyPayrollSummary {

    private String payrollMonth;
    private Double totalGrossIncome;
    private Double totalSssContribution;
    private Double totalPhilhealthContribution;
    private Double totalPagibigContribution;
    private Double totalWithholdingTax;
    private Double totalNetPay;

    public MonthlyPayrollSummary(String payrollMonth, Double totalGrossIncome, Double totalSssContribution, Double totalPhilhealthContribution, Double totalPagibigContribution, Double totalWithholdingTax, Double totalNetPay) {
        this.payrollMonth = payrollMonth;
        this.totalGrossIncome = totalGrossIncome;
        this.totalSssContribution = totalSssContribution;
        this.totalPhilhealthContribution = totalPhilhealthContribution;
        this.totalPagibigContribution = totalPagibigContribution;
        this.totalWithholdingTax = totalWithholdingTax;
        this.totalNetPay = totalNetPay;
    }

    public String getPayrollMonth() {
        return payrollMonth;
    }

    public void setPayrollMonth(String payrollMonth) {
        this.payrollMonth = payrollMonth;
    }

    public Double getTotalGrossIncome() {
        return totalGrossIncome;
    }

    public void setTotalGrossIncome(Double totalGrossIncome) {
        this.totalGrossIncome = totalGrossIncome;
    }

    public Double getTotalSssContribution() {
        return totalSssContribution;
    }

    public void setTotalSssContribution(Double totalSssContribution) {
        this.totalSssContribution = totalSssContribution;
    }

    public Double getTotalPhilhealthContribution() {
        return totalPhilhealthContribution;
    }

    public void setTotalPhilhealthContribution(Double totalPhilhealthContribution) {
        this.totalPhilhealthContribution = totalPhilhealthContribution;
    }

    public Double getTotalPagibigContribution() {
        return totalPagibigContribution;
    }

    public void setTotalPagibigContribution(Double totalPagibigContribution) {
        this.totalPagibigContribution = totalPagibigContribution;
    }

    public Double getTotalWithholdingTax() {
        return totalWithholdingTax;
    }

    public void setTotalWithholdingTax(Double totalWithholdingTax) {
        this.totalWithholdingTax = totalWithholdingTax;
    }

    public Double getTotalNetPay() {
        return totalNetPay;
    }

    public void setTotalNetPay(Double totalNetPay) {
        this.totalNetPay = totalNetPay;
    }



}