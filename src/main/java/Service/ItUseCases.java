package Service;

import Model.*;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

public interface ItUseCases {

    List<Employee> getAllEmployees() throws SQLException;

    List<Employee> getEmployeeById(int employeeId) throws SQLException;

    boolean updatePassword(int empIdToUpdate, String newPassword) throws SQLException;

    boolean updateUserRole(int empIdToUpdate, UserRole newUserRole) throws SQLException;
}
