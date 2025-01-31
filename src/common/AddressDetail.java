package common;

/**
 * Represents an address detail with a single address field.
 */
public class AddressDetail {

    private String address; // The address

    public AddressDetail(String address) {
        this.address = address;
    }

    // Getter and setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
