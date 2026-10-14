package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
/**
 *
 * @author Gracielle Salvador
 */

/**
 * Represents an employee that is also a system user.
 */
public class Employee extends UserCredential{
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private String phoneNumber;
    private String sssNumber;
    private String philhealthNumber;
    private String tinNumber;
    private String pagibigNumber;
    private EmploymentStatus empStatus;
    private Department department;
    private Integer supervisorId;
    private PositionSalaryHistory positionSalaryHistory;
    private EmployeeAddress employeeAddress;

    public Employee(String firstName, 
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
        super(empId, password, userRole);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
        this.empStatus = empStatus;
        this.department = department;
        this.supervisorId = supervisorId;
        this.positionSalaryHistory = positionSalaryHistory;
        this.employeeAddress = employeeAddress;
    }

    /**
     * @return the employee ID from UserCredential
     */
    public int getEmpId(){
        return super.getEmpId();
    }

    /**
     * @return the login password from UserCredential
     */
    public String getPassword(){
        return super.getPassword();
    }

    /**
     * @return the user role from UserCredential
     */
    public UserRole getUserRole(){
        return super.getUserRole();
    }
    
    /**
     * @param empId the employee ID to set
     */
    public void setEmpId(int empId){
        super.setEmpId(empId);
    }
    
    /**
     * @param password the login password to set
     */
    public void setPassword(String password){
        super.setPassword(password);
    }

    /**
     * @param userRole the user role to set
     */
    public void setUserRole(UserRole userRole){
        super.setUserRole(userRole);
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public void setSssNumber(String sssNumber) {
        this.sssNumber = sssNumber;
    }

    public String getPhilhealthNumber() {
        return philhealthNumber;
    }

    public void setPhilhealthNumber(String philhealthNumber) {
        this.philhealthNumber = philhealthNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getPagibigNumber() {
        return pagibigNumber;
    }

    public void setPagibigNumber(String pagibigNumber) {
        this.pagibigNumber = pagibigNumber;
    }

    public EmploymentStatus getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(EmploymentStatus empStatus) {
        this.empStatus = empStatus;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Integer supervisorId) {
        this.supervisorId = supervisorId;
    }

    public PositionSalaryHistory getPositionSalaryHistory() {
        return positionSalaryHistory;
    }

    public void setPositionSalaryHistory(PositionSalaryHistory positionSalaryHistory) {
        this.positionSalaryHistory = positionSalaryHistory;
    }

    public EmployeeAddress getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(EmployeeAddress employeeAddress) {
        this.employeeAddress = employeeAddress;
    }
    
    
    /**
     * Provides a string representation of the Employee object,
     * including inherited UserCredential details and all Employee-specific attributes.
     *
     * @return A formatted string containing employee details.
     */
    @Override
    public String toString() {
        return "Employee{" +
               "empId=" + getEmpId() + // Get from superclass
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", gender=" + gender +
               ", birthday=" + birthday +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", sssNumber='" + sssNumber + '\'' +
               ", philhealthNumber='" + philhealthNumber + '\'' +
               ", tinNumber='" + tinNumber + '\'' +
               ", pagibigNumber='" + pagibigNumber + '\'' +
               ", empStatus=" + empStatus +
               ", department=" + department +
               ", supervisorId=" + supervisorId +
               ", positionSalaryHistory=" + (positionSalaryHistory != null ? positionSalaryHistory.toString() : "null") +
               ", employeeAddress=" + (employeeAddress != null ? employeeAddress.toString() : "null") +
               ", password='" + getPassword() + '\'' + // Get from superclass
               ", userRole=" + getUserRole() + // Get from superclass
               '}';
    }
}