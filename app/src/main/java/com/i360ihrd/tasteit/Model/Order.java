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



     public Order(String productId, String productName, String quantity, String price, String discount) {

        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;

    }



   public String getID() {
        return ID;
    }


    public void setID(String ID) {
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
