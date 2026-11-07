package DAO;

import DAO.EmployeeUserMgmtDAO;
import Model.*;
import Util.AuditLogger;
import Util.DatabaseConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeUserMgmtImpl implements EmployeeUserMgmtDAO {
    private Connection connection;

    public EmployeeUserMgmtImpl() {
        this.connection = DatabaseConnectionUtil.getConnection();
    }
    
    // Helper method to capture current active user ID (defaults to HR_ADMIN)
    private String getCurrentUserId() {
        return "HR_ADMIN"; 
    }
    
    @Override
    public Employee findEmployeeById(Integer empId) throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        Employee employee = null;

        try {
            callableStatement = connection.prepareCall("{CALL FindEmployeeById(?)}");
            callableStatement.setInt(1, empId);
            resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                employee = mapResultSetToEmployee(resultSet);
            }

            // AUDIT LOG (READ SINGLE RECORD)
            AuditLogger.logActivity(
                getCurrentUserId(),
                "READ_EMPLOYEE",
                String.valueOf(empId),
                employee != null ? "Successfully retrieved employee record" : "Employee record not found"
            );

        } catch (SQLException e) {
            AuditLogger.logActivity(getCurrentUserId(), "READ_EMPLOYEE_FAILED", String.valueOf(empId), e.getMessage());
            throw e;
        } finally {
            if (resultSet != null) resultSet.close();
            if (callableStatement != null) callableStatement.close();
        }

        return employee;
    }
   
    @Override
    public List<Employee> findAllEmployee() throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        List<Employee> employees = new ArrayList<>();

        try {
            callableStatement = connection.prepareCall("{CALL FindAllEmployees()}");
            resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = mapResultSetToEmployee(resultSet);
                employees.add(employee);
            }

            // AUDIT LOG (READ ALL RECORDS)
            AuditLogger.logActivity(
                getCurrentUserId(),
                "READ_ALL_EMPLOYEES",
                "N/A",
                "Retrieved list of " + employees.size() + " employee records"
            );

        } catch (SQLException e) {
            AuditLogger.logActivity(getCurrentUserId(), "READ_ALL_EMPLOYEES_FAILED", "N/A", e.getMessage());
            throw e;
        } finally {
            if (resultSet != null) resultSet.close();
            if (callableStatement != null) callableStatement.close();
        }

        return employees;
    }
    
    @Override
    public boolean createEmployeeRecord(Employee employee) throws SQLException {
        CallableStatement callableStatement = null;      
        Statement stmt = null;
        ResultSet rsKey = null;
        boolean isSuccess = false;
        int generatedEmpId = 0;

        try {
            callableStatement = connection.prepareCall("{CALL CreateEmployeeRecord(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            callableStatement.setString(1, employee.getFirstName());
            callableStatement.setString(2, employee.getLastName());
            callableStatement.setString(3, employee.getGender().toString());
            callableStatement.setDate(4, Date.valueOf(employee.getBirthday()));
            callableStatement.setString(5, employee.getPhoneNumber());
            callableStatement.setString(6, employee.getSssNumber());
            callableStatement.setString(7, employee.getPhilhealthNumber());
            callableStatement.setString(8, employee.getTinNumber());
            callableStatement.setString(9, employee.getPagibigNumber());
            callableStatement.setString(10, employee.getEmpStatus().toString());
            callableStatement.setString(11, employee.getDepartment().toString());
            callableStatement.setInt(12, employee.getSupervisorId());
            
            callableStatement.setString(13, employee.getPositionSalaryHistory().getJobPosition().getTitle());
            callableStatement.setString(14, employee.getPositionSalaryHistory().getJobPosition().getDescription());          
            callableStatement.setBigDecimal(15, new BigDecimal(Double.toString(employee.getPositionSalaryHistory().getBasicSalary())));
            callableStatement.setBigDecimal(16, new BigDecimal(Double.toString(employee.getPositionSalaryHistory().getRiceSubsidy())));
            callableStatement.setBigDecimal(17, new BigDecimal(Double.toString(employee.getPositionSalaryHistory().getPhoneAllowance())));
            callableStatement.setBigDecimal(18, new BigDecimal(Double.toString(employee.getPositionSalaryHistory().getClothingAllowance())));
            
            callableStatement.setString(19, employee.getEmployeeAddress().getAddressRecord().getBuildingStreet());
            callableStatement.setString(20, employee.getEmployeeAddress().getAddressRecord().getBarangay());
            callableStatement.setString(21, employee.getEmployeeAddress().getAddressRecord().getCityMunicipality());
            callableStatement.setString(22, employee.getEmployeeAddress().getAddressRecord().getProvince());
            callableStatement.setString(23, employee.getEmployeeAddress().getAddressRecord().getZipcode());
            callableStatement.setString(24, employee.getEmployeeAddress().getAddressType().toString());
            
            callableStatement.setString(25, employee.getPassword());
            callableStatement.setString(26, employee.getUserRole().toString());

            // 1. Execute database insertion FIRST
            callableStatement.execute();
            isSuccess = true;

            // 2. Retrieve LAST_INSERT_ID() generated by MySQL
            stmt = connection.createStatement();
            rsKey = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rsKey.next()) {
                generatedEmpId = rsKey.getInt(1);
            }

            // 3. AUDIT LOG (CREATE EVENT) - Uses the real retrieved auto-increment ID!
            AuditLogger.logActivity(
                getCurrentUserId(),
                "CREATE_EMPLOYEE",
                String.valueOf(generatedEmpId > 0 ? generatedEmpId : "NEW"),
                "Created employee: " + employee.getFirstName() + " " + employee.getLastName()
            );
            
        } catch (SQLException e) {
            AuditLogger.logActivity(getCurrentUserId(), "CREATE_EMPLOYEE_FAILED", "N/A", e.getMessage());
            throw e;
        } finally {
            if (rsKey != null) rsKey.close();
            if (stmt != null) stmt.close();
            if (callableStatement != null) callableStatement.close();
        }
        
        return isSuccess;
    }
                
    @Override
    public boolean updateEmployeeRecord(Employee newEmployee) throws SQLException {
        CallableStatement callableStatement = null;      
        boolean isSuccess = false;

        try {
            callableStatement = connection.prepareCall("{CALL UpdateEmployeeRecordSimplified(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            callableStatement.setInt(1, newEmployee.getEmpId());
            callableStatement.setString(2, newEmployee.getFirstName());
            callableStatement.setString(3, newEmployee.getLastName());
            callableStatement.setString(4, newEmployee.getGender().toString());
            callableStatement.setDate(5, Date.valueOf(newEmployee.getBirthday()));
            callableStatement.setString(6, newEmployee.getPhoneNumber());
            callableStatement.setString(7, newEmployee.getSssNumber());
            callableStatement.setString(8, newEmployee.getPhilhealthNumber());
            callableStatement.setString(9, newEmployee.getTinNumber());
            callableStatement.setString(10, newEmployee.getPagibigNumber());
            callableStatement.setString(11, newEmployee.getEmpStatus().toString());
            callableStatement.setString(12, newEmployee.getDepartment().toString());
            callableStatement.setObject(13, newEmployee.getSupervisorId());
            
            callableStatement.setObject(14, newEmployee.getPositionSalaryHistory().getJobPosition().getTitle());
            callableStatement.setString(15, newEmployee.getPositionSalaryHistory().getJobPosition().getDescription());
            callableStatement.setBigDecimal(16, new BigDecimal(Double.toString(newEmployee.getPositionSalaryHistory().getBasicSalary())));
            callableStatement.setBigDecimal(17, new BigDecimal(Double.toString(newEmployee.getPositionSalaryHistory().getRiceSubsidy())));
            callableStatement.setBigDecimal(18, new BigDecimal(Double.toString(newEmployee.getPositionSalaryHistory().getPhoneAllowance())));
            callableStatement.setBigDecimal(19, new BigDecimal(Double.toString(newEmployee.getPositionSalaryHistory().getClothingAllowance())));
            
            callableStatement.setString(20, newEmployee.getEmployeeAddress().getAddressRecord().getBuildingStreet());
            callableStatement.setString(21, newEmployee.getEmployeeAddress().getAddressRecord().getBarangay());
            callableStatement.setString(22, newEmployee.getEmployeeAddress().getAddressRecord().getCityMunicipality());
            callableStatement.setString(23, newEmployee.getEmployeeAddress().getAddressRecord().getProvince());
            callableStatement.setString(24, newEmployee.getEmployeeAddress().getAddressRecord().getZipcode());
            callableStatement.setString(25, newEmployee.getEmployeeAddress().getAddressType().toString());

            callableStatement.execute();
            isSuccess = true;

            // AUDIT LOG (UPDATE EVENT)
            AuditLogger.logActivity(
                getCurrentUserId(),
                "UPDATE_EMPLOYEE",
                String.valueOf(newEmployee.getEmpId()),
                "Updated details for: " + newEmployee.getFirstName() + " " + newEmployee.getLastName()
            );

        } catch (SQLException e) {
            AuditLogger.logActivity(getCurrentUserId(), "UPDATE_EMPLOYEE_FAILED", String.valueOf(newEmployee.getEmpId()), e.getMessage());
            throw e;
        } finally {
            if (callableStatement != null) callableStatement.close();
        }

        return isSuccess;
    }

    @Override
    public boolean updateUserCredential(UserCredential newUserCredential) throws SQLException {
        CallableStatement callableStatement = null;      
        boolean isSuccess = false;

        try {
            callableStatement = connection.prepareCall("{CALL UpdateUserCredential(?, ?, ?)}");

            callableStatement.setInt(1, newUserCredential.getEmpId());
            callableStatement.setString(2, newUserCredential.getPassword());
            callableStatement.setString(3, newUserCredential.getUserRole().toString());

            callableStatement.execute();
            isSuccess = true;

            // AUDIT LOG (CREDENTIAL UPDATE EVENT)
            AuditLogger.logActivity(
                getCurrentUserId(),
                "UPDATE_CREDENTIALS",
                String.valueOf(newUserCredential.getEmpId()),
                "Updated password and role to: " + newUserCredential.getUserRole().toString()
            );

        } catch (SQLException e) {
            AuditLogger.logActivity(getCurrentUserId(), "UPDATE_CREDENTIALS_FAILED", String.valueOf(newUserCredential.getEmpId()), e.getMessage());
            throw e;
        } finally {
            if (callableStatement != null) callableStatement.close();
        }

        return isSuccess;
    }

    @Override
    public boolean deleteEmployeeRecord(int employeeId) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            int rowsAffected = pstmt.executeUpdate();
            boolean deleted = rowsAffected > 0;

            if (deleted) {
                // AUDIT LOG (DELETE EVENT)
                AuditLogger.logActivity(
                    getCurrentUserId(),
                    "DELETE_EMPLOYEE",
                    String.valueOf(employeeId),
                    "Permanently deleted employee record from database"
                );
            }

            return deleted;
        } catch (SQLException e) {
            AuditLogger.logActivity(getCurrentUserId(), "DELETE_EMPLOYEE_FAILED", String.valueOf(employeeId), e.getMessage());
            return false;
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        int empId = rs.getInt("employee_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        Gender gender = Gender.valueOf(rs.getString("gender"));
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        String phoneNumber = rs.getString("phone_number");
        String sssNumber = rs.getString("sss_number");
        String philhealthNumber = rs.getString("philhealth_number");
        String tinNumber = rs.getString("tin_number");
        String pagibigNumber = rs.getString("pagibig_number");
        EmploymentStatus empStatus = EmploymentStatus.valueOf(rs.getString("employment_status"));
        Department department = Department.valueOf(rs.getString("department"));
        Integer supervisorId = rs.getInt("supervisor_id");
        String password = rs.getString("password");
        UserRole userRole = UserRole.valueOf(rs.getString("user_role"));

        JobPosition jobPosition = new JobPosition(
            rs.getString("job_title"),
            rs.getString("job_description")
        );
        jobPosition.setPositionId(rs.getInt("position_id"));

        PositionSalaryHistory positionSalaryHistory = new PositionSalaryHistory(
            empId,
            jobPosition,
            rs.getBigDecimal("basic_salary").doubleValue(),
            rs.getBigDecimal("rice_subsidy").doubleValue(),
            rs.getBigDecimal("phone_allowance").doubleValue(),
            rs.getBigDecimal("clothing_allowance").doubleValue(),
            rs.getDate("effective_start_date").toLocalDate(),
            rs.getDate("effective_end_date") != null ? rs.getDate("effective_end_date").toLocalDate() : null
        );
        positionSalaryHistory.setPositionSalaryHistoryId(rs.getInt("position_salary_history_id"));

        Address address = new Address(
            rs.getInt("address_id"),
            rs.getString("building_street"),
            rs.getString("barangay"),
            rs.getString("city_municipality"),
            rs.getString("province"),
            rs.getString("zipcode")
        );

        EmployeeAddress employeeAddress = new EmployeeAddress(
            empId,
            address,
            AddressType.valueOf(rs.getString("address_type"))
        );

        return new Employee(
            firstName,
            lastName,
            gender,
            birthday,
            phoneNumber,
            sssNumber,
            philhealthNumber,
            tinNumber,
            pagibigNumber,
            empStatus,
            department,
            supervisorId,
            positionSalaryHistory,
            employeeAddress,
            empId,
            password,
            userRole
        );
    }
}