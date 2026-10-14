package Service;

import DAO.AttendanceMgmtImpl;
import DAO.EmployeeUserMgmtImpl;
import DAO.LeaveMgmtImpl;
import Model.*;
import Util.PasswordUtil;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItService extends EmployeeService implements ItUseCases {

    EmployeeUserMgmtImpl employeeDAO = new EmployeeUserMgmtImpl();

    public ItService(String firstName,
            String lastName,
            Gender gender,
            LocalDate birthday,
            String phoneNumber,
            String sssNumber,
            String philhealthNumber,
            String tinNumber,
            String pagibigNumber,
            EmploymentStatus empStatus,
            Department department,
            Integer supervisorId,
            PositionSalaryHistory positionSalaryHistory,
            EmployeeAddress employeeAddress,
            int empId,
            String password,
            UserRole userRole) {
        super(firstName, lastName, gender, birthday, phoneNumber,
                sssNumber, philhealthNumber, tinNumber, pagibigNumber,
                empStatus, department, supervisorId, positionSalaryHistory,
                employeeAddress,
                empId, password, userRole);
    }

    public ItService(EmployeeService employeeService) {
        super(employeeService.getFirstName(),
                employeeService.getLastName(),
                employeeService.getGender(),
                employeeService.getBirthday(),
                employeeService.getPhoneNumber(),
                employeeService.getSssNumber(),
                employeeService.getPhilhealthNumber(),
                employeeService.getTinNumber(),
                employeeService.getPagibigNumber(),
                employeeService.getEmpStatus(),
                employeeService.getDepartment(),
                employeeService.getSupervisorId(),
                employeeService.getPositionSalaryHistory(),
                employeeService.getEmployeeAddress(),
                employeeService.getEmpId(),
                employeeService.getPassword(),
                employeeService.getUserRole());
    }

    @Override
    public List<Employee> getAllEmployees() {
        try {
            List<Employee> employeeRecords = employeeDAO.findAllEmployee();

            // Check if the list is not null and not empty before processing
            if (employeeRecords != null && !employeeRecords.isEmpty()) {
                for (Employee employee : employeeRecords) {
                    // Clear sensitive fields for each individual employee object
                    employee.setGender(null);
                    employee.setPagibigNumber(null);
                    employee.setPhilhealthNumber(null);

                    employee.getPositionSalaryHistory().setBasicSalary(null);
                    employee.getPositionSalaryHistory().setClothingAllowance(null);
                    employee.getPositionSalaryHistory().setPhoneAllowance(null);
                    employee.getPositionSalaryHistory().setRiceSubsidy(null);

                    employee.setSssNumber(null);
                    employee.setSupervisorId(null);
                    employee.setTinNumber(null);
                    employee.setBirthday(null);
                    employee.setEmployeeAddress(null);
                    employee.setPhoneNumber(null);
                    employee.setSupervisorId(null);

                    //employee.setPassword(null);
                }
            }
            return employeeRecords; // Returns the modified list (can be empty or null if findAllEmployee returns null)
        } catch (SQLException ex) {
            Logger.getLogger(ItService.class.getName()).log(Level.SEVERE, "Error retrieving all employees", ex);
            return null; // Return null to indicate an error
        }
    }

    @Override
    public List<Employee> getEmployeeById(int employeeId) throws SQLException {
        List<Employee> empList = new ArrayList<>(); // Specify the type parameter

        try {
            Employee employee = employeeDAO.findEmployeeById(employeeId);
            if (employee != null) {
                empList.add(employee); // Add the employee only if it's not null
            } else {
                System.out.println("HrService.getEmployeeById | No employee found with ID: " + employeeId); // Optional: log or handle the case
            }
        } catch (SQLException ex) {
            Logger.getLogger(HrService.class.getName()).log(Level.SEVERE, "Error retrieving employee with ID: " + employeeId, ex);
            throw ex; // Rethrow the exception after logging
        }

        return empList; // Return the list (could be empty if no employee was found)
    }

    @Override
    public boolean updatePassword(int empIdToUpdate, String newPassword) {
        try {
            //HASHING LOGIC
            String hashedPassword = PasswordUtil.hashPassword(newPassword); // Hash the password first

            //DEBUG
            System.out.println("Running ItService.updatePassword()...");
            System.out.println("Plaintext password parameter: " + newPassword);
            System.out.println("Password after Hashing: " + hashedPassword);

            employeeDAO.updateUserCredential(new UserCredential(empIdToUpdate, hashedPassword, employeeDAO.findEmployeeById(empIdToUpdate).getUserRole()));
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean updateUserRole(int empIdToUpdate, UserRole newUserRole) {
        try {
            employeeDAO.updateUserCredential(new UserCredential(empIdToUpdate, employeeDAO.findEmployeeById(empIdToUpdate).getPassword(), newUserRole));
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
