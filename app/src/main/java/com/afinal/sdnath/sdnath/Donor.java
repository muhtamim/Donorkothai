package com.afinal.sdnath.sdnath;

public class Donor {

    public String name, phone, address, hospital, bag, uid,time;

    public Donor() {

    }

    public Donor(String name, String phone, String address, String hospital, String bag, String uid, String time) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.hospital = hospital;
        this.bag = bag;
        this.uid = uid;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getBag() {
        return bag;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
