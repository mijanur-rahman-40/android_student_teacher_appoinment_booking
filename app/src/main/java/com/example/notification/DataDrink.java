package com.example.notification;

public class DataDrink {
    String drinkName;
    String price;

    public DataDrink() {

    }

    public DataDrink(String drinkName, String price) {
        this.drinkName = drinkName;
        this.price = price;
    }

    public String getdrinkName() {
        return drinkName;
    }

    public void setdrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
