package com.rupeek.hotelbooking.domain;

public class Location {

    private String city;
    private String locality;

    public Location() {
    }

    public Location(String city, String locality) {
        this.city = city;
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}
