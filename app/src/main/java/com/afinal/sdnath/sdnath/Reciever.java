package com.afinal.sdnath.sdnath;

public class Reciever {

    public String name, phone, address,blood,time;

    public Reciever() {


    }

    public Reciever(String name, String phone, String address, String blood, String time) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.blood = blood;
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

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
