package com.i360ihrd.tasteit.Model;

/**
 * Created by Jamshaid on 08-02-2018.
 */

public class Order {

    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;



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

     public Order(String productId, String productName, String quantity, String price, String discount) {

>>>>>>> dd2ef52352a29bc6c8f81008a35d95ead8b47944
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;

    }

<<<<<<< HEAD
=======

>>>>>>> dd2ef52352a29bc6c8f81008a35d95ead8b47944

   public String getID() {
        return ID;
    }

<<<<<<< HEAD
    public void setID(String ID) {
=======
    public
    void setID(String ID) {

>>>>>>> dd2ef52352a29bc6c8f81008a35d95ead8b47944
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
