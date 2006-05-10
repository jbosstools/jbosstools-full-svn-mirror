package org.hibernate.netbeans.console.model;

/**
 * @author leon
 */
public class ProductOwnerAddress {
    
    private Long id;
    
    private String street;
    
    private String number;
    
    public ProductOwnerAddress() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    
}
