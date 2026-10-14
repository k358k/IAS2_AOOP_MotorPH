package DAO;

import DAO.PayrollMgmtDAO;
import Model.LeaveEntitlement;
import Model.LeaveRecord;
import Model.LeaveStatus;
import Model.LeaveType;
import Model.MonthlyPayrollSummary;
import Model.PayPeriod;
import Model.Payslip;
import Util.DatabaseConnectionUtil;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PayrollMgmtImpl implements PayrollMgmtDAO {

    private Connection connection;

    public PayrollMgmtImpl() {
        this.connection = DatabaseConnectionUtil.getConnection();
    }

    @Override
    public List<Payslip> findAllPayslipsByEmployee(Integer empId) throws SQLException {

        List<Payslip> records = new ArrayList<>();
        String sql = "CALL GetAllPayslipOfEmployee(?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToPayslip(rs));
                }
            }
        }
        return records;
    }

    @Override
    public List<Payslip> findAllPayslips() throws SQLException {
        List<Payslip> records = new ArrayList<>();
        String sql = "CALL GetPayslipDetails";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                records.add(mapResultSetToPayslip(rs));
            }
        }
        return records;
    }

    @Override
    public List<MonthlyPayrollSummary> findAllMonthlyPayrollSummary() throws SQLException {
        List<MonthlyPayrollSummary> records = new ArrayList<>();
        String sql = "SELECT * FROM monthly_payroll_summary_totals";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                records.add(mapResultSetToMonthlyPayrollSummary(rs));
            }
        }
        return records;
    }

    @Override
    public boolean createPayslip(Integer empId, LocalDate periodStart, LocalDate periodEnd) throws SQLException {
        String sql = "CALL GenerateAndRecordPayslip(?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.setDate(2, Date.valueOf(periodStart));
            stmt.setDate(3, Date.valueOf(periodEnd));

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                // Duplicate entry - payslip already exists
                throw new SQLException("Payslip already exists for this employee and period", e);
            }
            // Re-throw other SQL exceptions
            throw e;
        }
    }

    @Override
    public boolean deletePayslip(Integer payslipId) throws SQLException {
        String sql = "DELETE FROM payslip WHERE payslip_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, payslipId); // Set the payslip ID in the prepared statement
            int rowsAffected = pstmt.executeUpdate(); // Execute the delete operation
            return rowsAffected > 0; // Return true if a record was deleted
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
            return false; // Return false if an error occurred
        }
    }

    /**
     * Maps the current row of a ResultSet to a MonthlyPayrollSummary object. It
     * expects the ResultSet to contain columns with the exact aliases as
     * defined in the stored procedures.
     *
     * @param rs The ResultSet containing MonthlyPayrollSummary data.
     * @return A MonthlyPayrollSummary object populated with data from the
     * ResultSet.
     * @throws SQLException If a database access error occurs or the column
     * labels are invalid.
     */
    private static MonthlyPayrollSummary mapResultSetToMonthlyPayrollSummary(ResultSet rs) throws SQLException {
        String payrollMonth = rs.getString("Payroll Month");
        Double totalGrossIncome = rs.getObject("Total Gross Income", Double.class);
        Double totalSssContribution = rs.getObject("Total SSS Contribution", Double.class);
        Double totalPhilhealthContribution = rs.getObject("Total PhilHealth Contribution", Double.class);
        Double totalPagibigContribution = rs.getObject("Total Pag-IBIG Contribution", Double.class);
        Double totalWithholdingTax = rs.getObject("Total Withholding Tax", Double.class);
        Double totalNetPay = rs.getObject("Total Net Pay", Double.class);

        return new MonthlyPayrollSummary(
                payrollMonth, totalGrossIncome, totalSssContribution,
                totalPhilhealthContribution, totalPagibigContribution,
                totalWithholdingTax, totalNetPay
        );
    }

    private static Payslip mapResultSetToPayslip(ResultSet rs) throws SQLException {
        Integer payslipId = rs.getObject("Payslip No.", Integer.class); // Use getObject with class for null-safe primitive wrappers
        Integer employeeId = rs.getObject("Employee ID", Integer.class);
        String employeeName = rs.getString("Employee Name");
        LocalDate periodStartDate = rs.getDate("Period Start Date") != null ? rs.getDate("Period Start Date").toLocalDate() : null;
        LocalDate periodEndDate = rs.getDate("Period End Date") != null ? rs.getDate("Period End Date").toLocalDate() : null;
        String employeePosition = rs.getString("Employee Position");
        String employeeDepartment = rs.getString("Employee Department");
        Double monthlyRate = rs.getObject("Monthly Rate", Double.class);
        Double dailyRate = rs.getObject("Daily Rate", Double.class);
        Double hourlyRate = rs.getObject("Hourly Rate", Double.class);
        Double overtimeRateMultiplier = rs.getObject("Overtime Rate Multiplier", Double.class);
        Integer distinctDaysWorked = rs.getObject("Distinct Days Worked", Integer.class);
        Double hoursWorked = rs.getObject("Hours Worked", Double.class);
        Double overtimeHours = rs.getObject("Overtime Hours", Double.class);
        Double grossIncome = rs.getObject("Gross Income", Double.class);
        Double riceSubsidy = rs.getObject("Rice Subsidy", Double.class);
        Double phoneAllowance = rs.getObject("Phone Allowance", Double.class);
        Double clothingAllowance = rs.getObject("Clothing Allowance", Double.class);
        Double totalBenefits = rs.getObject("Total Benefits", Double.class);
        Double sssContribution = rs.getObject("Social Security System Contribution", Double.class);
        Double philhealthContribution = rs.getObject("PhilHealth Contribution", Double.class);
        Double pagibigContribution = rs.getObject("Pag-IBIG Contribution", Double.class);
        Double taxableIncome = rs.getObject("Taxable Income", Double.class);
        Double withholdingTax = rs.getObject("Withholding Tax", Double.class);
        Double totalDeductions = rs.getObject("Total Deductions", Double.class);
        Double netIncome = rs.getObject("Net Income / Take Home Pay", Double.class);

        return new Payslip(
                payslipId, employeeId, employeeName, periodStartDate, periodEndDate,
                employeePosition, employeeDepartment, monthlyRate, dailyRate,
                hourlyRate, overtimeRateMultiplier, distinctDaysWorked, hoursWorked,
                overtimeHours, grossIncome, riceSubsidy, phoneAllowance,
                clothingAllowance, totalBenefits, sssContribution, philhealthContribution,
                pagibigContribution, taxableIncome, withholdingTax, totalDeductions,
                netIncome
        );
    }
}
