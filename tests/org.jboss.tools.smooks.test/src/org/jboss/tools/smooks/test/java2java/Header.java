package org.jboss.tools.smooks.test.java2java;



/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class Header {
    private Long customerNumber = 1234L;
    private String customerName = "Buzz Lightyear";

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("customerNumber: " + customerNumber + ", ");
        stringBuilder.append("customerName: " + customerName);

        return stringBuilder.toString();
    }
}