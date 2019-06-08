package com.example.findfood;

public class RestaurantLocation {
    static String name,latitude,longtitude;

    public RestaurantLocation(String name, String latitude, String longtitude) {
        this.name = name;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public static String getName() {
        return name;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static String getLongtitude() {
        return longtitude;
    }
}
