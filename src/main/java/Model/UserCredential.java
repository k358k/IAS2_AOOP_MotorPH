/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
/**
 *
 * @author Gracielle Salvador
 */

/**
 * Represents user authentication credentials and access privileges.
 * This class handles sensitive login information and should be used with proper security measures.
 */
public class UserCredential {
    private int empId;
    private String password;
    private UserRole userRole;

    /**
     * Creates new user credentials.
     * @param empId Employee ID
     * @param password password 
     * @param userRole System access level/privileges
     */
    public UserCredential(int empId, String password, UserRole userRole){
        this.empId = empId;
        this.password = password;
        this.userRole = userRole;
    }

    /**
     * Standard getters for credential information:
     * <ul>
     *   <li>getEmpId() - Returns associated employee ID</li>
     *   <li>getPassword() - Returns password (handle with care)</li>
     *   <li>getUserRole() - Returns access level</li>
     * </ul>
     */
    public int getEmpId() {
        return empId;
    }
    public String getPassword() {
        return password;
    }
    public UserRole getUserRole() {
        return userRole;
    }
   
     /**
     * Standard setters for credential information:
     * <ul>
     *   <li>setEmpId(int) - Updates employee ID</li>
     *   <li>setPassword(String) - Updates password (should be hashed)</li>
     *   <li>setUserRole(UserRole) - Updates access privileges</li>
     * </ul>
     */
    public void setEmpId(int empId){
        this.empId = empId;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    
    /**
     * Returns a secure string representation that obscures the password.
     * @return Formatted string with masked password
     */
    @Override
    public String toString(){
        return "Employee{" +
                "id='" + empId + '\'' +
                ", password='" + password + '\'' +
                ", user role='" + userRole + '\'' +
                '}';
    }

}