package org.jboss.tools.smooks.test.java2java;


/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class OrderItem {
    private long productId;
    private Integer quantity;
    private double price;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{productId: " + productId + " | ");
        stringBuilder.append("quantity: " + quantity + " | ");
        stringBuilder.append("price: " + price + "}");

        return stringBuilder.toString();
    }
}