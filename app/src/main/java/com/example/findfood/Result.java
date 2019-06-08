package com.example.findfood;

public class Result {
    String name,building,state,rating,longtitude,latitude;

    public Result(String name, String building, String state, String rating, String longtitude, String latitude) {
        this.name = name;
        this.building = building;
        this.state = state;
        this.rating = rating;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public String getBuilding() {
        return building;
    }

    public String getState() {
        return state;
    }

    public String getRating() {
        return rating;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

}
