package Test;

import DAO.EmployeeUserMgmtImpl;
import Model.Employee;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeFetchTest {

    @Test
    void testPrintAllEmployeeInfoById() throws SQLException {
        int EMPLOYEE_ID = 10001;

        // Initialize the DAO
        EmployeeUserMgmtImpl dao = new EmployeeUserMgmtImpl();

        // Fetch employee from the database
        Employee employee = dao.findEmployeeById(EMPLOYEE_ID);

        // Check if employee exists
        assertNotNull(employee, "Employee with ID " + EMPLOYEE_ID + " should exist in the database");

        // Print all employee fields to verify the data
        System.out.println("=== Employee Details ===");
        System.out.println("employee_id: " + employee.getEmpId());
        System.out.println("first_name: " + employee.getFirstName());
        System.out.println("last_name: " + employee.getLastName());
        System.out.println("gender: " + employee.getGender());
        System.out.println("birthday: " + employee.getBirthday());
        System.out.println("phone_number: " + employee.getPhoneNumber());
        System.out.println("sss_number: " + employee.getSssNumber());
        System.out.println("philhealth_number: " + employee.getPhilhealthNumber());
        System.out.println("tin_number: " + employee.getTinNumber());
        System.out.println("pagibig_number: " + employee.getPagibigNumber());
        System.out.println("employment_status: " + employee.getEmpStatus());
        System.out.println("department: " + employee.getDepartment());
        System.out.println("supervisor_id: " + employee.getSupervisorId());
        System.out.println("position_salary_history_id: " + employee.getPositionSalaryHistory());

//        // Optional: add basic checks (not required but helps validate expected data)
//        assertEquals(EMPLOYEE_ID, employee.getEmpId());
//        assertEquals("Manuel III", employee.getFirstName());
//        assertEquals("Garcia", employee.getLastName());
    }
}

