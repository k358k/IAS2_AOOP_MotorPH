package Model;

/**
 *
 * @author Gracielle Salvador
 */

/**
 * Represents an address with various components such as street, barangay, city, province, and zipcode.
 */
public class Address {
    private Integer addressId;
    private String buildingStreet;
    private String barangay;
    private String cityMunicipality;
    private String province;
    private String zipcode;

    // Default constructor
    public Address() {
    }

    // Parameterized constructor
    public Address(Integer addressId, String buildingStreet, String barangay, 
                   String cityMunicipality, String province, String zipcode) {
        this.addressId = addressId;
        this.buildingStreet = buildingStreet;
        this.barangay = barangay;
        this.cityMunicipality = cityMunicipality;
        this.province = province;
        this.zipcode = zipcode;
    }

    // Getters and Setters
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getBuildingStreet() {
        return buildingStreet;
    }

    public void setBuildingStreet(String buildingStreet) {
        this.buildingStreet = buildingStreet;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getCityMunicipality() {
        return cityMunicipality;
    }

    public void setCityMunicipality(String cityMunicipality) {
        this.cityMunicipality = cityMunicipality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", buildingStreet='" + buildingStreet + '\'' +
                ", barangay='" + barangay + '\'' +
                ", cityMunicipality='" + cityMunicipality + '\'' +
                ", province='" + province + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
