package Model;

/**
 *
 * @author Gracielle Salvador
 */

/**
 * Represents the association between an employee and their address, including the type of address.
 */
public class EmployeeAddress {
    private Integer empId;
    private Address addressRecord;
    private AddressType addressType;

    // Default constructor
    public EmployeeAddress() {
    }

    // Parameterized constructor
    public EmployeeAddress(Integer empId, Address addressRecord, AddressType addressType) {
        this.empId = empId;
        this.addressRecord = addressRecord;
        this.addressType = addressType;
    }

    // Getters and Setters
    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Address getAddressRecord() {
        return addressRecord;
    }

    public void setAddressRecord(Address addressRecord) {
        this.addressRecord = addressRecord;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return "EmployeeAddress{" +
                "empId=" + empId +
                ", addressRecord=" + addressRecord.toString() +
                ", addressType=" + addressType +
                '}';
    }
}





