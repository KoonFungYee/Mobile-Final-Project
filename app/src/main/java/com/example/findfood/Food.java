package com.example.findfood;

public class Food {
    String foodname,price,restname,state;

    public Food(String foodname, String price, String restname, String state) {
        this.foodname = foodname;
        this.price = price;
        this.restname = restname;
        this.state = state;
    }

    public String getFoodname() {
        return foodname;
    }

    public String getPrice() {
        return price;
    }

    public String getRestname() {
        return restname;
    }

    public String getState() {
        return state;
    }
}
