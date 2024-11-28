package com.example.lab8_ph51025_and103.model;

// File: Order.java (nên nằm cùng package hoặc trong package riêng)
public class Order {
    private String location;
    private String province;
    private String district;
    private String ward;

    // Constructor
    public Order(String location, String province, String district, String ward) {
        this.location = location;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

    // Getters and setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}

