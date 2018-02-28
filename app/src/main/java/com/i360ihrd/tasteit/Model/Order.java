package com.i360ihrd.tasteit.Model;

/**
 * Created by Jamshaid on 08-02-2018.
 */

public class Order {
    private String ID;
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;
    private String ID;


    public Order() {
    }

    public Order(String ID, String productId, String productName, String quantity, String price, String discount) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

<<<<<<< HEAD
     public Order(String productId, String productName, String quantity, String price, String discount) {

=======
    public Order(String Id,String productId, String productName, String quantity, String price, String discount) {
>>>>>>> 7ad9287c7a40f5da5a9d51adf49abd0239e136b7
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
<<<<<<< HEAD
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
=======
        ID = Id;
    }

    public
    String getID() {
        return ID;
    }

    public
    void setID(String ID) {
>>>>>>> 7ad9287c7a40f5da5a9d51adf49abd0239e136b7
        this.ID = ID;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
