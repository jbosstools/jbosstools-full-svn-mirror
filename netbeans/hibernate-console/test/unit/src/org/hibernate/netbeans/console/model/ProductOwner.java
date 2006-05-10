package org.hibernate.netbeans.console.model;

/**
 * @author leon
 */
public class ProductOwner {

    private String firstName;

    private String lastName;

    private ProductOwnerAddress address;
    
    private StoreCity account;
    
    public ProductOwner() {
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

    public void setAddress(ProductOwnerAddress address) {
        this.address = address;
    }

    public ProductOwnerAddress getAddress() {
        return address;
    }

    public StoreCity getAccount() {
        return account;
    }

    public void setAccount(StoreCity account) {
        this.account = account;
    }


}
