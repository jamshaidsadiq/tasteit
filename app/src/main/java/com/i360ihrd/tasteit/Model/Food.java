package com.i360ihrd.tasteit.Model;

/**
 * Created by Jamshaid on 09-01-2018.
 */

public class Food {
   private String Description,Discount,Image,MenuId,Name,Price;

    public Food() {
    }

    public Food(String description, String discount, String image, String menuid, String name, String price) {
        Description = description;
        Discount = discount;
        Image = image;
        MenuId = menuid;
        Name = name;
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMenuid() {
        return MenuId;
    }

    public void setMenuid(String menuid) {
        MenuId = menuid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
