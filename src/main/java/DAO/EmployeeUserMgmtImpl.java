package DAO;

import DAO.EmployeeUserMgmtDAO;
import Model.*;
import Util.DatabaseConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeUserMgmtImpl implements EmployeeUserMgmtDAO {
    private Connection connection;

    public EmployeeUserMgmtImpl() {
        this.connection = DatabaseConnectionUtil.getConnection();
    }
    
    
    @Override
   public Employee findEmployeeById(Integer empId) throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        Employee employee = null;

        try {
            // Prepare the stored procedure call
            callableStatement = connection.prepareCall("{CALL FindEmployeeById(?)}");

            // Set input parameter
            callableStatement.setInt(1, empId);

            // Execute the stored procedure
            resultSet = callableStatement.executeQuery();

            // Process the result set
            if (resultSet.next()) {
                employee = mapResultSetToEmployee(resultSet);
            }

        } catch (SQLException e) {
            // Handle SQL exceptions
            throw e;
        } finally {
            // Clean up resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (callableStatement != null) {
                callableStatement.close();
            }
        }

        return employee;
    }
   
   @Override
    public List<Employee> findAllEmployee() throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        List<Employee> employees = new ArrayList<>();

        try {
            // Prepare the stored procedure call
            callableStatement = connection.prepareCall("{CALL FindAllEmployees()}");

            // Execute the stored procedure
            resultSet = callableStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                Employee employee = mapResultSetToEmployee(resultSet);
                employees.add(employee);
            }

        } catch (SQLException e) {
            // Handle SQL exceptions
            throw e;
        } finally {
            // Clean up resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (callableStatement != null) {
                callableStatement.close();
            }
        }

        return employees;
    }
    
    
    
    @Override
    public boolean createEmployeeRecord(Employee employee) throws SQLException {
        CallableStatement callableStatement = null;      
        boolean isSuccess = false;

        try {
            // Prepare the stored procedure call
            callableStatement = connection.prepareCall("{CALL CreateEmployeeRecord(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            // Set input parameters
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
            callableStatement.setBigDecimal(15, new BigDecimal(employee.getPositionSalaryHistory().getBasicSalary())); // Converted into String first to avoid precision issues.
            callableStatement.setBigDecimal(16, new BigDecimal(employee.getPositionSalaryHistory().getRiceSubsidy()));
            callableStatement.setBigDecimal(17, new BigDecimal(employee.getPositionSalaryHistory().getPhoneAllowance()));
            callableStatement.setBigDecimal(18, new BigDecimal(employee.getPositionSalaryHistory().getClothingAllowance()));
            
            callableStatement.setString(19, employee.getEmployeeAddress().getAddressRecord().getBuildingStreet());
            callableStatement.setString(20, employee.getEmployeeAddress().getAddressRecord().getBarangay());
            callableStatement.setString(21, employee.getEmployeeAddress().getAddressRecord().getCityMunicipality());
            callableStatement.setString(22, employee.getEmployeeAddress().getAddressRecord().getProvince());
            callableStatement.setString(23, employee.getEmployeeAddress().getAddressRecord().getZipcode());
            callableStatement.setString(24, employee.getEmployeeAddress().getAddressType().toString());
            
            callableStatement.setString(25, employee.getPassword());
            callableStatement.setString(26, employee.getUserRole().toString());

            // Execute the stored procedure
            callableStatement.execute();
            isSuccess = true; // Set success flag
            
        } catch (SQLException e) {
            // Handle SQL exceptions
            throw e;
        } finally {
            // Clean up resources
            if (callableStatement != null) {
                callableStatement.close();
            }
        }
        
        return isSuccess; // Return success status
    }
                
    @Override
    public boolean updateEmployeeRecord(Employee newEmployee) throws SQLException{
        CallableStatement callableStatement = null;      
        boolean isSuccess = false;
        
        System.out.println("Running EmployeeUserMgmtImpl.updateEmployeeRecord()...");
        System.out.println("Employee Object Parameter: " + newEmployee.toString());

        try {
            // Prepare the stored procedure call
            callableStatement = connection.prepareCall("{CALL UpdateEmployeeRecordSimplified(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            // Set input parameters
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
            callableStatement.setBigDecimal(16, new BigDecimal(Double.toString(newEmployee.getPositionSalaryHistory().getBasicSalary()))); // Converted into String first to avoid precision issues.
            callableStatement.setBigDecimal(17, new BigDecimal(Double.toString(newEmployee.getPositionSalaryHistory().getRiceSubsidy())));
            callableStatement.setBigDecimal(18, new BigDecimal(Double.toString(newEmployee.getPositionSalaryHistory().getPhoneAllowance())));
            callableStatement.setBigDecimal(19, new BigDecimal(Double.toString(newEmployee.getPositionSalaryHistory().getClothingAllowance())));
            
            callableStatement.setString(20, newEmployee.getEmployeeAddress().getAddressRecord().getBuildingStreet());
            callableStatement.setString(21, newEmployee.getEmployeeAddress().getAddressRecord().getBarangay());
            callableStatement.setString(22, newEmployee.getEmployeeAddress().getAddressRecord().getCityMunicipality());
            callableStatement.setString(23, newEmployee.getEmployeeAddress().getAddressRecord().getProvince());
            callableStatement.setString(24, newEmployee.getEmployeeAddress().getAddressRecord().getZipcode());
            callableStatement.setString(25, newEmployee.getEmployeeAddress().getAddressType().toString());

            // Execute the stored procedure
            callableStatement.execute();
            isSuccess = true; // Set success flag
        } catch (SQLException e) {
            e.printStackTrace(); // For debugging purposes
            throw e; // Rethrow or handle as needed
        } finally {
            // Clean up resources
            if (callableStatement != null){
                callableStatement.close();
        }
            
        return isSuccess; // Return success status
        }
        
        
    }

    @Override
    public boolean updateUserCredential(UserCredential newUserCredential) throws SQLException {
    CallableStatement callableStatement = null;      
        boolean isSuccess = false;

        try {
            // Prepare the stored procedure call
            callableStatement = connection.prepareCall("{CALL UpdateUserCredential(?, ?, ?)}");

            // Set input parameters
            callableStatement.setInt(1, newUserCredential.getEmpId());
            callableStatement.setString(2, newUserCredential.getPassword());
            callableStatement.setString(3, newUserCredential.getUserRole().toString());

            // Execute the stored procedure
            callableStatement.execute();
            isSuccess = true; // Set success flag
        } catch (SQLException e) {
            e.printStackTrace(); // For debugging purposes
            throw e; // Rethrow or handle as needed
        } finally {
            // Clean up resources
            if (callableStatement != null){
                callableStatement.close();
        }
            
        return isSuccess; // Return success status
        }
        
        
    }

    @Override
    public boolean deleteEmployeeRecord(int employeeId) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId); // Set the employee ID in the prepared statement
            int rowsAffected = pstmt.executeUpdate(); // Execute the delete operation
            return rowsAffected > 0; // Return true if a record was deleted
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
            return false; // Return false if an error occurred
        }
    }

   // Helper method to map ResultSet to Employee object
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        // Get data from ResultSet
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

        // Create JobPosition with full information
        JobPosition jobPosition = new JobPosition(
            rs.getString("job_title"),
            rs.getString("job_description")
        );
        jobPosition.setPositionId(rs.getInt("position_id"));

        // Create PositionSalaryHistory
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

        // Create Address
        Address address = new Address(
            rs.getInt("address_id"),
            rs.getString("building_street"),
            rs.getString("barangay"),
            rs.getString("city_municipality"),
            rs.getString("province"),
            rs.getString("zipcode")
        );

        // Create EmployeeAddress
        EmployeeAddress employeeAddress = new EmployeeAddress(
            empId,
            address,
            AddressType.valueOf(rs.getString("address_type"))
        );

        // Create Employee using the parameterized constructor
        Employee employee = new Employee(
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

        return employee;
    }

}
