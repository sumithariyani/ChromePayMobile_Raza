package com.example.chromepaymobile.Models;

public class UnBlockedDidModel {

    String name;
    String photo;
    String phone;
    String id;

    public UnBlockedDidModel() {
        this.name = name;
        this.photo = photo;
        this.phone = phone;
        this.id = id;
    }

    public String getId(){return id;}

    public void setId(String id){this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
