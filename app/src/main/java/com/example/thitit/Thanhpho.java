package com.example.thitit;

public class Thanhpho {

    int id;
    String city;
    String country;
    String temp;

    public Thanhpho(String city, String country, String temp) {
        this.city = city;
        this.country = country;
        this.temp = temp;
    }
    public Thanhpho(int id, String city) {
        this.id = id;
        this.city = city;

    }
}
